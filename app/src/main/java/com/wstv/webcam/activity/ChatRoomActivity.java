package com.wstv.webcam.activity;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.Trace;
import com.libin.mylibrary.widget.swiperecyclerview.SwipeRecyclerView;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.adapter.ChatAdapter;
import com.wstv.webcam.entity.PrivateMessage;
import com.wstv.webcam.holder.chat.PrivateLeftListener;
import com.wstv.webcam.holder.chat.PrivateRightListener;
import com.wstv.webcam.tencent.liveroom.MLVBLiveRoomImpl;
import com.wstv.webcam.util.LiveRoomUtil;
import com.wstv.webcam.util.click.ClickProxy;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import em.sang.com.allrecycleview.adapter.DefaultAdapter;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * <p>Description: </p>
 * ChatRoomActivity
 *
 * @author lilibin
 * @createDate 2019/5/20 11:06
 */

public class ChatRoomActivity extends BaseActivity implements SwipeRecyclerView.OnLoadListener, OnToolsItemClickListener<PrivateMessage> {

    @Bind(R.id.activity_chat_room_content)
    public SwipeRecyclerView content;
    
    public DefaultAdapter<PrivateMessage> adapter;
    
    public List<PrivateMessage> data;

    private String roomTitle = "光光";

    private String userId = "ff8080816ac683db016ad546c635001b";

    private String withAvatar = "ff8080816ac683db016ad546c635001b";

    private MLVBLiveRoomImpl liveRoom;

    @Bind(R.id.activity_chat_room_confirm_btn)
    TextView sendText;

    @Bind(R.id.activity_chat_room_send)
    LinearLayout sendBtn;

    @Bind(R.id.activity_chat_room_input)
    EditText editText;

    @Override
    protected void initViewAndData() {
        setTitleStr(roomTitle);
        liveRoom = LiveRoomUtil.getInstance(this).getLiveRoom();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sendText.setTextColor(s.length() > 0 ? Color.parseColor("#05a764") : ContextCompat.getColor(ChatRoomActivity.this, R.color.label_gray));
            }
        });
        sendBtn.setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        }));
        initList();
        initView();
        Trace.e("initViewAndData");
        liveRoom.readPrivate(userId, -1, new RealmChangeListener<RealmResults<PrivateMessage>>() {
            @Override
            public void onChange(RealmResults<PrivateMessage> element) {
                element.removeAllChangeListeners();
                data.addAll(0, element.size() > 20 ? element.subList(0, 20) : element);
                adapter.notifyDataSetChanged();
                if (adapter.getItemCount() > 0) {
                    content.getRecyclerView().smoothScrollToPosition(adapter.getItemCount() - 1);
                }
            }
        });
    }

    private void sendMessage() {
        if (editText.getText().toString().length() == 0) {
            return;
        }
        liveRoom.postPrivateMessage(userId, roomTitle, withAvatar, editText.getText().toString(), new MLVBLiveRoomImpl.onPrivateMessageCallback() {
            @Override
            public void onSuccess(PrivateMessage message) {
                data.get(data.indexOf(message)).success = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(int code, String errInfo) {

            }

            @Override
            public void onSending(PrivateMessage message) {
                data.add(message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        editText.setText("");
                        if (adapter.getItemCount() > 0) {
                            content.getRecyclerView().smoothScrollToPosition(adapter.getItemCount() - 1);
                        }
                    }
                });
            }
        });
    }

    protected void initList() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        content.getRecyclerView().setLayoutManager(manager);
        data = new ArrayList<>();
        content.setAdapter(adapter = getAdapter());
        content.setRefreshEnable(false);
        content.setLoadMoreEnable(false);
        content.setHasBottom(false);
        content.setOnLoadListener(this);
//        content.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                int firstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition(); //当前屏幕 首个 可见的 Item 的position
//
//                if (firstVisiblePosition == 1) {
//                    liveRoom.readPrivate(userId, data.get(data.size() - 1).createTime, new RealmChangeListener<RealmResults<PrivateMessage>>() {
//                        @Override
//                        public void onChange(RealmResults<PrivateMessage> element) {
//                            data.addAll(0, element.size() > 20 ? element.subList(0, 20) : element);
//                            adapter.notifyDataSetChanged();
//                        }
//                    });
//                }
//            }
//        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_chat_room;
    }

    @Override
    protected View getLoadingTargetView() {
        return content.getRecyclerView();
    }

    public DefaultAdapterViewListener<PrivateMessage> getViewListener() {
        return null;
    }

    public int getItemLayoutId() {
        return 0;
    }

    public void initView() {
        editText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){

            //当键盘弹出隐藏的时候会 调用此方法。
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //获取当前界面可视部分
                ChatRoomActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight =  ChatRoomActivity.this.getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;
                if (heightDifference > 0 && adapter.getItemCount() > 0) {
                    content.getRecyclerView().smoothScrollToPosition(adapter.getItemCount() - 1);
                }
            }

        });
    }

    protected DefaultAdapter<PrivateMessage> getAdapter() {
        ChatAdapter adapter = new ChatAdapter(this, data, getItemLayoutId(), getViewListener());
        adapter.setLeftListener(new PrivateLeftListener(this));
        adapter.setRightListener(new PrivateRightListener(this));
        return adapter;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        userId = extras.getString("userId");
        roomTitle = extras.getString("roomTitle");
        withAvatar = extras.getString("withAvatar");
    }

    @Override
    protected void onEventComming(EventCenter center) {
        if (center.getEventCode() == 2048) {
            PrivateMessage message = (PrivateMessage) center.getData();
            data.add(message);
            adapter.notifyDataSetChanged();
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
    public void onItemClick(int position, PrivateMessage item) {

    }

    @Override
    protected boolean isShouldHideSoftKeyBoard(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] l = { 0, 0 };
            View layoutView = ButterKnife.findById(this, R.id.activity_chat_room_input_layout);
            layoutView.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top +layoutView.getHeight(), right = left
                    + layoutView.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // If click the EditText event ,ignore it
                return false;
            } else {
                return true;
            }
        }
        // if the focus is EditText,ignore it;
        return false;
    }
}
