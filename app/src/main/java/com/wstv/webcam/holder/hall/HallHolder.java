package com.wstv.webcam.holder.hall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.CamDetailActivity;
import com.wstv.webcam.fragment.HallFragment;
import com.wstv.webcam.http.HttpService;
import com.wstv.webcam.http.callback.BaseCallback;
import com.wstv.webcam.http.model.audience.AudienceStateResult;
import com.wstv.webcam.http.model.room.Room;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;

/**
 * <p>Description: </p>
 * HallHolder
 *
 * @author lilibin
 * @createDate 2019/3/12 16:39
 */

public class HallHolder extends CustomHolder<Room> {

    private HallFragment hallFragment;

    private String[] testImage = {"http://img1.imgtn.bdimg.com/it/u=413529390,3074554009&fm=26&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1674309791,146310262&fm=26&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3217311928,1852822454&fm=11&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3306322383,1145858665&fm=26&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=1393771583,1541386921&fm=26&gp=0.jpg"};

    public HallHolder(Context context, List<Room> lists, int itemID, HallFragment hallFragment) {
        super(context, lists, itemID);
        this.hallFragment = hallFragment;
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
                enterRoom(datas.get(position));
            }
        });

        if (null != room) {
//            holderHelper.setText(R.id.item_hall_name, datas.get(position).pushersMap.get(datas.get(position).roomCreator).userName);
            holderHelper.setText(R.id.item_hall_name, datas.get(position).roomInfo);
            holderHelper.setText(R.id.item_hall_views, String.valueOf(datas.get(position).audiencesCnt));

            String avatarUrl = datas.get(position).pushersMap.get(datas.get(position).roomCreator).userAvatar;
            Glide.with(context).load(!TextUtils.isEmpty(avatarUrl) ? avatarUrl : testImage[position % testImage.length]).into((ImageView) holderHelper.getView(R.id.item_hall_image));
        }
        int padding = context.getResources().getDimensionPixelSize(R.dimen.qb_px_15);
        itemView.setPadding(padding * (position % 2 == 0 ? 3 : 1), 0, padding * (position % 2 == 1 ? 3 : 1), padding * 2);
    }

    private void enterRoom(final Room room) {
        HttpService.audienceState(hallFragment, room.roomID, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), new BaseCallback<AudienceStateResult>(context, hallFragment) {
            @Override
            public void onSuccess(AudienceStateResult audienceStateResult, int id) {
                if (audienceStateResult.detail.state != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CamDetailActivity.BUNDLE_KEY_FLV_URL, room.mixedPlayURL);
                    bundle.putString(CamDetailActivity.BUNDLE_KEY_LINK_URL, room.pushersMap.get(room.roomCreator).accelerateURL);
                    bundle.putString(CamDetailActivity.BUNDLE_KEY_ROOM_ID, room.roomID);
                    bundle.putString(CamDetailActivity.BUNDLE_KEY_PERFORMER_ID, room.roomCreator);
                    bundle.putSerializable(CamDetailActivity.BUNDLE_KEY_PERFORMER, room.pushersMap.get(room.roomCreator));
                    bundle.putSerializable(CamDetailActivity.BUNDLE_KEY_AUDIENCE, room.audiences);
                    bundle.putInt(CamDetailActivity.BUNDLE_KEY_MANAGE, audienceStateResult.detail.manager);
                    bundle.putLong(CamDetailActivity.BUNDLE_KEY_SHUT_UP, audienceStateResult.detail.endTime);
                    bundle.putLong(CamDetailActivity.BUNDLE_KEY_SHUT_UP, audienceStateResult.detail.endTime);
//                    LiveRoomUtil.getInstance((BaseActivity) context).getLiveRoom().addRoom(room);
                    Intent intent = new Intent(context, CamDetailActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
    }
}