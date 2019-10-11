package com.wstv.webcam.activity;

import android.os.Bundle;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.base.BaseRecyclerActivity;
import com.wstv.webcam.entity.ChatRoom;
import com.wstv.webcam.holder.chat.ChatRoomListener;
import com.wstv.webcam.tencent.liveroom.MLVBLiveRoomImpl;
import com.wstv.webcam.util.LiveRoomUtil;

import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * <p>Description: </p>
 * ChatListActivity
 *
 * @author lilibin
 * @createDate 2019/5/21 13:08
 */

public class ChatListActivity extends BaseRecyclerActivity<ChatRoom> implements OnToolsItemClickListener<ChatRoom> {

    private MLVBLiveRoomImpl liveRoom;

    @Override
    protected String getTitleStr() {
        return "消息列表";
    }

    @Override
    public DefaultAdapterViewListener<ChatRoom> getViewListener() {
        return new ChatRoomListener(this);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_chat_list;
    }

    @Override
    public void initView() {
        liveRoom = LiveRoomUtil.getInstance(this).getLiveRoom();
        liveRoom.readChatRoom(new RealmChangeListener<RealmResults<ChatRoom>>() {
            @Override
            public void onChange(RealmResults<ChatRoom> element) {
                element.removeAllChangeListeners();
                data.addAll(element);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void onEventComming(EventCenter center) {
        if (center.getEventCode() == 2049) {
            liveRoom.readChatRoom(new RealmChangeListener<RealmResults<ChatRoom>>() {
                @Override
                public void onChange(RealmResults<ChatRoom> element) {
                    element.removeAllChangeListeners();
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onItemClick(int position, ChatRoom item) {
        Bundle bundle = new Bundle();
        bundle.putString("userId", item.userID);
        bundle.putString("roomTitle", item.userName);
        bundle.putString("withAvatar", item.userAvatar);
        readyGo(ChatRoomActivity.class, bundle);
    }
}
