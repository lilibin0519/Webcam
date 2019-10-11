package com.wstv.pad.holder.live;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.libin.mylibrary.base.util.PreferenceUtil;
import com.libin.mylibrary.util.GlideLoadUtils;
import com.wstv.pad.AppConstant;
import com.wstv.pad.R;
import com.wstv.pad.fragment.LiveFragment;
import com.wstv.pad.http.HttpService;
import com.wstv.pad.http.callback.BaseCallback;
import com.wstv.pad.http.model.audience.AudienceStateResult;
import com.wstv.pad.http.model.room.Room;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;

/**
 * <p>Description: </p>
 * LiveHolder
 *
 * @author lilibin
 * @createDate 2019/3/12 16:39
 */

public class LiveHolder extends CustomHolder<Room> {

    private LiveFragment liveFragment;

    private String[] testImage = {"http://img1.imgtn.bdimg.com/it/u=413529390,3074554009&fm=26&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1674309791,146310262&fm=26&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3217311928,1852822454&fm=11&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3306322383,1145858665&fm=26&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=1393771583,1541386921&fm=26&gp=0.jpg"};

    public LiveHolder(Context context, List<Room> lists, int itemID, LiveFragment liveFragment) {
        super(context, lists, itemID);
        this.liveFragment = liveFragment;
    }

    @Override
    public void initView(final int position, final List<Room> datas, final Context context) {
        final Room room = datas.get(position);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == room) {
                    return;
                }
                enterRoom(room);
            }
        });

        if (null != room) {
//            holderHelper.setText(R.id.item_live_name, datas.get(position).pushersMap.get(datas.get(position).roomCreator).userName);
            holderHelper.setText(R.id.item_live_name, datas.get(position).roomInfo);
            holderHelper.setText(R.id.item_live_views, String.valueOf(datas.get(position).audiencesCnt));

            String avatarUrl = datas.get(position).pushersMap.get(datas.get(position).roomCreator).userAvatar;
//            Glide.with(context).load(!TextUtils.isEmpty(avatarUrl) ? avatarUrl : testImage[position % testImage.length]).into((ImageView) holderHelper.getView(R.id.item_live_image));
            GlideLoadUtils.getInstance().glideLoad(context, !TextUtils.isEmpty(avatarUrl) ? avatarUrl : testImage[position % testImage.length],
                    (ImageView) holderHelper.getView(R.id.item_live_image));
        }
//        int padding = context.getResources().getDimensionPixelSize(R.dimen.qb_px_15);
//        itemView.setPadding(padding * (position % 2 == 0 ? 3 : 1), 0, padding * (position % 2 == 1 ? 3 : 1), padding * 2);
    }

    private void enterRoom(final Room room) {
        HttpService.audienceState(liveFragment, room.roomID, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), new BaseCallback<AudienceStateResult>(context, liveFragment) {
            @Override
            public void onSuccess(AudienceStateResult audienceStateResult, int id) {
                if (audienceStateResult.detail.state != 0) {
                    liveFragment.enterRoom(room);
                }
            }
        });
    }
}
