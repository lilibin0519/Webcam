package com.wstv.webcam.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.libin.mylibrary.widget.swiperecyclerview.SwipeRecyclerView;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.CamDetailActivity;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.entity.ChatRoom;
import com.wstv.webcam.holder.chat.ChatRoomListener;
import com.wstv.webcam.tencent.liveroom.MLVBLiveRoomImpl;
import com.wstv.webcam.util.LiveRoomUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import em.sang.com.allrecycleview.adapter.DefaultAdapter;
import em.sang.com.allrecycleview.cutline.RecycleViewDivider;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ChatListDialog extends Dialog implements OnToolsItemClickListener<ChatRoom> {

    private Context context;

    @Bind(R.id.layout_recycler_content)
    public SwipeRecyclerView content;

    @Bind(R.id.title_bar_title)
    TextView title;

    @Bind(R.id.title_bar_left)
    LinearLayout left;

    public DefaultAdapter<ChatRoom> adapter;

    public List<ChatRoom> data;

    private MLVBLiveRoomImpl liveRoom;

    public ChatListDialog(@NonNull final Context context) {
        super(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_chat_list, null);
        setContentView(contentView);
        setCanceledOnTouchOutside(true);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        this.context = context;
        ButterKnife.bind(this);

        initList();
        title.setText("私信");
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        liveRoom = LiveRoomUtil.getInstance((BaseActivity) context).getLiveRoom();
        liveRoom.readChatRoom(new RealmChangeListener<RealmResults<ChatRoom>>() {
            @Override
            public void onChange(RealmResults<ChatRoom> element) {
                element.removeAllChangeListeners();
                data.addAll(element);
                adapter.notifyDataSetChanged();
            }
        });
    }

    protected void initList() {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        content.getRecyclerView().setLayoutManager(manager);
        data = new ArrayList<>();
        content.setAdapter(adapter = new DefaultAdapter<>(context, data, R.layout.item_chat_list, new ChatRoomListener(this)));
        content.getRecyclerView().addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.VERTICAL));
        content.setRefreshEnable(false);
        content.setLoadMoreEnable(false);
        content.setHasBottom(false);
    }

    @Override
    public void onItemClick(int position, ChatRoom item) {
        dismiss();
        ((CamDetailActivity) context).showPrivateChatDialog(item.userID, item.userName);
    }

    public void refresh() {
        liveRoom.readChatRoom(new RealmChangeListener<RealmResults<ChatRoom>>() {
            @Override
            public void onChange(RealmResults<ChatRoom> element) {
                element.removeAllChangeListeners();
                adapter.notifyDataSetChanged();
            }
        });
    }
}
