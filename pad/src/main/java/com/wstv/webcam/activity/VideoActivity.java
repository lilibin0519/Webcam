package com.wstv.webcam.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.libin.mylibrary.util.GlideLoadUtils;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.http.HttpService;
import com.wstv.webcam.http.callback.BaseCallback;
import com.wstv.webcam.http.model.EmptyResult;
import com.wstv.webcam.http.model.action.VideoBean;
import com.wstv.webcam.util.click.ClickProxy;
import com.wstv.webcam.widget.CustomVideoView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Kindred on 2019/5/17.
 */

public class VideoActivity extends BaseActivity implements SurfaceHolder.Callback {

    public static final String KEY_VIDEO_URL = "key.video.url";

    public static final String KEY_VIDEO_MODEL = "key.video.model";

    public static final String KEY_THUMBNAIL_URL = "key.thumbnail.url";

    public static final String KEY_VIDEO_ID = "key.video.id";

    public static final String KEY_VIDEO_LIKE = "key.video.like";

    @Bind(R.id.activity_video_view)
    CustomVideoView videoView;

    private MediaPlayer player;

    private SurfaceHolder holder;

    @Bind(R.id.activity_video_thumbnail)
    ImageView thumbnail;

    @Bind(R.id.activity_video_like_image)
    ImageView likeImage;

    @Bind(R.id.activity_video_like_count)
    TextView likeCount;

    @Bind(R.id.activity_video_share_count)
    TextView shareCount;

    @Bind(R.id.activity_video_nickname)
    TextView nickname;

    @Bind(R.id.activity_video_content)
    TextView content;

    @Bind(R.id.activity_video_avatar)
    CircleImageView avatar;

    @Bind(R.id.activity_video_follow)
    ImageView follow;

    private VideoBean videoBean;

    private String avatarUrl;

    private boolean isFans;

    private String nicknameStr;

    @Bind(R.id.activity_video_comment_count)
    TextView commentCount;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_video;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        videoBean = (VideoBean) extras.getSerializable(KEY_VIDEO_MODEL);
        avatarUrl = extras.getString("avatar");
        isFans = extras.getBoolean("isFans");
        nicknameStr = extras.getString("nicknameStr");
    }

    @Override
    protected void initViewAndData() {
//        leftImage.setImageTintList(ContextCompat.getColorStateList(this, android.R.color.black));
        title.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        if (TextUtils.isEmpty(videoBean.url)) {
            showToastCenter("视频地址为空");
            return;
        }
        HttpService.watchVideo(this, videoBean.id);
//        Glide.with(this).load(videoBean.posterUrl).into(thumbnail);
        GlideLoadUtils.getInstance().glideLoad(this, videoBean.posterUrl,
                thumbnail);
        //设置视频控制器
        initVideoView();

        setData();
        ButterKnife.findById(this, R.id.activity_video_like).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(videoBean.isMyLove) || "0".equals(videoBean.isMyLove)) {
                    HttpService.likeVideo(VideoActivity.this, videoBean.id, new BaseCallback<EmptyResult>(VideoActivity.this, VideoActivity.this) {
                        @Override
                        public void onSuccess(EmptyResult result, int id) {
                            videoBean.isMyLove = "1";
                            videoBean.praise++;
                            setData();
                            EventBus.getDefault().post(new EventCenter(2046));
                        }
                    });
                } else {
                    HttpService.cancelLikeVideo(VideoActivity.this, videoBean.id, new BaseCallback<EmptyResult>(VideoActivity.this, VideoActivity.this) {
                        @Override
                        public void onSuccess(EmptyResult result, int id) {
                            videoBean.isMyLove = "0";
                            videoBean.praise--;
                            setData();
                            EventBus.getDefault().post(new EventCenter(2045));
                        }
                    });
                }
            }
        }));
        follow.setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFans) {
                    cancelFollow();
                } else {
                    requestFollow();
                }
            }
        }));

        ButterKnife.findById(this, R.id.activity_video_comment).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("videoId", videoBean.id);
                readyGo(VideoCommentsActivity.class, bundle);
            }
        }));
    }

    private void cancelFollow() {
        HttpService.cancelFollow(this, videoBean.createUser, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID),
                new BaseCallback<EmptyResult>(this, this) {
                    @Override
                    public void onSuccess(EmptyResult result, int id) {
                        isFans = false;
                        follow.setImageResource(R.drawable.icon_follow);
                        follow.setImageResource(R.drawable.icon_follow);
                        EventBus.getDefault().post(new EventCenter(2044));
                    }
                });
    }

    private void requestFollow() {
        HttpService.follow(this, videoBean.createUser, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID),
                new BaseCallback<EmptyResult>(this, this) {
                    @Override
                    public void onSuccess(EmptyResult result, int id) {
                        isFans = true;
                        follow.setImageResource(R.drawable.icon_follow_cancel);
                        follow.setImageResource(R.drawable.icon_follow_cancel);
                        EventBus.getDefault().post(new EventCenter(2044));
                    }
                });
    }

    private void setData() {
//        Glide.with(this).load(avatarUrl).dontAnimate().placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).into(avatar);
        GlideLoadUtils.getInstance().glideLoad(this, avatarUrl,
                avatar, R.drawable.default_avatar);
        follow.setImageResource(isFans ? R.drawable.icon_follow_cancel : R.drawable.icon_follow);
        likeImage.setImageResource("1".equals(videoBean.isMyLove) ? R.drawable.icon_video_like : R.drawable.icon_video_not_like);
        likeCount.setText(String.valueOf(videoBean.praise));
        shareCount.setText(String.valueOf(videoBean.share));
        nickname.setText(nicknameStr);
        content.setText(videoBean.title);
        commentCount.setText(videoBean.comment);
    }

    private void initVideoView() {
        Uri uri = Uri.parse(videoBean.url);

//        videoView.setMediaController(new MediaController(this));
        //播放完成回调
        videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
                if(what != MediaPlayer.MEDIA_INFO_BUFFERING_START ){
                    thumbnail.setVisibility(View.GONE);
                }
                return true;
            }
        });


        videoView.post(new Runnable() {
            @Override
            public void run() {
//                int[] widthAndHeight = getWidthAndHeight(holder.videoViewParent,dynamicsBean.getWeight(),dynamicsBean.getHeight());
                videoView.getHolder().setFixedSize(videoView.getWidth(), videoView.getHeight());
                // 重绘VideoView大小，这个方法是在重写VideoView时对外抛出方法
//                videoView.setMeasure(widthAndHeight[0], widthAndHeight[1]);
                // 请求调整
                videoView.requestLayout();
            }
        });

        //设置视频路径
        videoView.setVideoURI(uri);

        //开始播放视频
        videoView.start();
    }

    @Override
    protected int getStatusBarColor() {
        return android.R.color.transparent;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComming(EventCenter center) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        player.setDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (player.isPlaying()) {
//            player.stop();
//        }
//        player.release();
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            showToastCenter("播放完成");
        }
    }
}
