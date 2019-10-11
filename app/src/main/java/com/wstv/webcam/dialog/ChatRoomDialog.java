package com.wstv.webcam.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ChatRoomDialog extends Dialog implements OnToolsItemClickListener<PrivateMessage> {

    private Context context;

    @Bind(R.id.activity_chat_room_content)
    public SwipeRecyclerView content;

    public DefaultAdapter<PrivateMessage> adapter;

    public List<PrivateMessage> data;

    private String roomTitle = "光光";

    private String userId = "ff8080816ac683db016ad546c635001b";

    private String avatar;

    private MLVBLiveRoomImpl liveRoom;

    @Bind(R.id.activity_chat_room_confirm_btn)
    TextView sendText;

    @Bind(R.id.activity_chat_room_send)
    LinearLayout sendBtn;

    @Bind(R.id.activity_chat_room_input)
    EditText editText;

    @Bind(R.id.title_bar_title)
    TextView title;

    @Bind(R.id.title_bar_left)
    LinearLayout left;

    private int mLastDiff = 0;

    private InputMethodManager imm;

    public ChatRoomDialog(@NonNull final Context context, String userId, String roomTitle, String avatar) {
        super(context, R.style.BottomDialog);
        this.userId = userId;
        this.roomTitle = roomTitle;
        this.avatar = avatar;
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_chat_room, null);
        setContentView(contentView);
        setCanceledOnTouchOutside(true);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        this.context = context;
        ButterKnife.bind(this);
        initSelf();

        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                dialog.dismiss();
            }
        });
    }

    public void setRoomTitle(String titleStr){
        if (null != title) {
            title.setText(titleStr);
        }
    }

    private void initSelf() {
        setRoomTitle(roomTitle);
        if (null != left) {
            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        liveRoom = LiveRoomUtil.getInstance((BaseActivity) context).getLiveRoom();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sendText.setTextColor(s.length() > 0 ? Color.parseColor("#05a764") : ContextCompat.getColor(context, R.color.label_gray));
            }
        });
        sendBtn.setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        }));
        initList();

        editText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){

            //当键盘弹出隐藏的时候会 调用此方法。
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //获取当前界面可视部分
                ((BaseActivity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight =  ((BaseActivity) context).getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;
                if (heightDifference > 0 && adapter.getItemCount() > 0) {
                    content.getRecyclerView().smoothScrollToPosition(adapter.getItemCount() - 1);
                }
            }

        });
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
        liveRoom.postPrivateMessage(userId, roomTitle, avatar, editText.getText().toString(), new MLVBLiveRoomImpl.onPrivateMessageCallback() {
            @Override
            public void onSuccess(PrivateMessage message) {
                data.get(data.indexOf(message)).success = true;
                ((BaseActivity) context).runOnUiThread(new Runnable() {
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
                ((BaseActivity) context).runOnUiThread(new Runnable() {
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
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        content.getRecyclerView().setLayoutManager(manager);
        data = new ArrayList<>();
        content.setAdapter(adapter = getAdapter());
        content.setRefreshEnable(false);
        content.setLoadMoreEnable(false);
        content.setHasBottom(false);
    }

    protected DefaultAdapter<PrivateMessage> getAdapter() {
        ChatAdapter adapter = new ChatAdapter(context, data, 0, null);
        adapter.setLeftListener(new PrivateLeftListener(this));
        adapter.setRightListener(new PrivateRightListener(this));
        return adapter;
    }

    @Override
    public void onItemClick(int position, PrivateMessage item) {

    }

    public void addMessage(PrivateMessage message){
        data.add(message);
        adapter.notifyDataSetChanged();
    }
}
