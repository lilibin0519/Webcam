package com.wstv.pad.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Display;
import android.view.WindowManager;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.wstv.pad.AppConstant;
import com.wstv.pad.R;
import com.wstv.pad.activity.base.BaseRecyclerActivity;
import com.wstv.pad.holder.action.VideoCommentViewListener;
import com.wstv.pad.http.HttpService;
import com.wstv.pad.http.callback.BaseCallback;
import com.wstv.pad.http.model.EmptyResult;
import com.wstv.pad.http.model.action.VideoComment;
import com.wstv.pad.http.model.action.VideoCommentsResult;
import com.wstv.pad.tencent.roomutil.misc.TextMsgInputDialog;
import com.wstv.pad.util.AndroidBug5497Workaround;

import em.sang.com.allrecycleview.cutline.RecycleViewDivider;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * <p>Description: </p>
 * VideoCommentsActivity
 *
 * @author lilibin
 * @createDate 2019/6/5 16:09
 */

public class VideoCommentsActivity extends BaseRecyclerActivity<VideoComment> implements OnToolsItemClickListener<VideoComment> {

    private int currentPage = 1;

    private String videoId;

    private TextMsgInputDialog mTextMsgInputDialog;

    private boolean isInit = true;

    private String callUserId;

    @Override
    protected String getTitleStr() {
        return "视频评论";
    }

    @Override
    public void initView() {
        AndroidBug5497Workaround.assistActivity(findViewById(android.R.id.content));
        content.getRecyclerView().addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));
        content.setRefreshEnable(true);
        content.setRefreshing(true);
    }

    @Override
    public void onItemClick(int position, VideoComment item) {
        if (item.createUser.equals(PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID))) {
            return;
        }
        callUserId = item.createUser;
        showInputMsgDialog();
        mTextMsgInputDialog.setHint(item.user.nickname);
    }

    private void requestComments() {
        HttpService.getVideoComments(this, videoId, currentPage, AppConstant.DEFAULT_PAGE_SIZE, new BaseCallback<VideoCommentsResult>(this, this) {
            @Override
            public void onSuccess(VideoCommentsResult result, int id) {
                if (currentPage == 1) {
                    data.clear();
                }
                content.setLoadMoreEnable(null != result.detail && result.detail.size() == AppConstant.DEFAULT_PAGE_SIZE);
                content.setHasBottom(null != result.detail && result.detail.size() == AppConstant.DEFAULT_PAGE_SIZE);
                data.addAll(result.detail);
                adapter.notifyDataSetChanged();
                if (isInit) {
                    isInit = false;
                    initInputDialog();
                    showInputMsgDialog();
                }
                content.complete();
            }

            @Override
            protected void onFailed(String errCode, String errMsg, VideoCommentsResult result) {
                content.complete();
            }
        });
    }

    private void initInputDialog(){
        mTextMsgInputDialog = new TextMsgInputDialog(this, R.style.InputDialog);
        mTextMsgInputDialog.setmOnTextSendListener(new TextMsgInputDialog.OnTextSendListener() {
            @Override
            public void onTextSend(final String msg, boolean tanmuOpen) {
                postComment(msg);
            }
        });
    }

    private void postComment(String msg) {
        HttpService.postVideoComment(this, videoId, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), msg, callUserId, new BaseCallback<EmptyResult>(this, this) {
            @Override
            public void onSuccess(EmptyResult emptyResult, int id) {
                callUserId = null;
                afterPostComment();
            }
        });
    }

    private void afterPostComment() {
        int total = data.size() + 1;
        currentPage = total / 10 + (total % 10 > 7 ?  2 : 1);
        HttpService.getVideoComments(this, videoId, 1, AppConstant.DEFAULT_PAGE_SIZE * currentPage, new BaseCallback<VideoCommentsResult>(this, this) {
            @Override
            public void onSuccess(VideoCommentsResult result, int id) {
                data.clear();
                content.setLoadMoreEnable(null != result.detail && result.detail.size() == AppConstant.DEFAULT_PAGE_SIZE * currentPage);
                content.setHasBottom(null != result.detail && result.detail.size() == AppConstant.DEFAULT_PAGE_SIZE * currentPage);
                data.addAll(result.detail);
                adapter.notifyDataSetChanged();
                content.complete();
            }

            @Override
            protected void onFailed(String errCode, String errMsg, VideoCommentsResult result) {
                content.complete();
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void showInputMsgDialog() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mTextMsgInputDialog.getWindow().getAttributes();

        lp.width = (display.getWidth()); //设置宽度
        mTextMsgInputDialog.getWindow().setAttributes(lp);
        mTextMsgInputDialog.setCancelable(true);
        mTextMsgInputDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mTextMsgInputDialog.show();
//        content.getRecyclerView().smoothScrollToPosition(data.size() == 0 ? 0 : data.size());
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        requestComments();
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        requestComments();
    }

    @Override
    public DefaultAdapterViewListener<VideoComment> getViewListener() {
        return new VideoCommentViewListener(this);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_action_comment;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        videoId = extras.getString("videoId");
    }

    @Override
    protected void onEventComming(EventCenter center) {

    }
}
