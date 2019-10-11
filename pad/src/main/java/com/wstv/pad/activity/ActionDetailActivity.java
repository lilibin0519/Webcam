package com.wstv.pad.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.wstv.pad.AppConstant;
import com.wstv.pad.R;
import com.wstv.pad.activity.base.BaseRecyclerActivity;
import com.wstv.pad.holder.action.ActionHeader;
import com.wstv.pad.holder.action.CommentViewListener;
import com.wstv.pad.http.HttpService;
import com.wstv.pad.http.callback.BaseCallback;
import com.wstv.pad.http.model.EmptyResult;
import com.wstv.pad.http.model.action.ActionBean;
import com.wstv.pad.http.model.action.Comment;
import com.wstv.pad.http.model.action.CommentListResult;
import com.wstv.pad.tencent.roomutil.misc.TextMsgInputDialog;
import com.wstv.pad.util.AndroidBug5497Workaround;

import em.sang.com.allrecycleview.cutline.RecycleViewDivider;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * <p>Description: </p>
 * ActionDetailActivity
 *
 * @author lilibin
 * @createDate 2019/5/24 10:20
 */

public class ActionDetailActivity extends BaseRecyclerActivity<Comment> implements OnToolsItemClickListener<Comment> {

    private int currentPage = 1;

    private ActionBean actionBean;

    private ActionHeader header;

    private TextMsgInputDialog mTextMsgInputDialog;

    private boolean isInit = true;

    private boolean showInputFirst;

    private String callUserId;

    @Override
    public void initView() {
        AndroidBug5497Workaround.assistActivity(findViewById(android.R.id.content));
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_action, content.getRecyclerView(), false);
        header = new ActionHeader(this, headerView, actionBean);
        adapter.addHead(header);
        content.getRecyclerView().addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));
//        requestComments();
        content.setRefreshEnable(true);
        content.setRefreshing(true);
    }

    private void requestComments() {
        HttpService.getComments(this, actionBean.id, currentPage, AppConstant.DEFAULT_PAGE_SIZE, new BaseCallback<CommentListResult>(this, this) {
            @Override
            public void onSuccess(CommentListResult commentListResult, int id) {
                if (currentPage == 1) {
                    data.clear();
                }
                content.setLoadMoreEnable(null != commentListResult.detail && commentListResult.detail.size() == AppConstant.DEFAULT_PAGE_SIZE);
                content.setHasBottom(null != commentListResult.detail && commentListResult.detail.size() == AppConstant.DEFAULT_PAGE_SIZE);
                data.addAll(commentListResult.detail);
                adapter.notifyDataSetChanged();
                if (isInit) {
                    isInit = false;
                    initInputDialog();
                    if (showInputFirst) {
                        showInputMsgDialog();
                    }
                }
                content.complete();
            }

            @Override
            protected void onFailed(String errCode, String errMsg, CommentListResult commentListResult) {
                content.complete();
            }
        });
    }

    @Override
    public void onItemClick(final int position, Comment item) {
        if (item.createUser.equals(PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID))) {
            return;
        }
        callUserId = item.createUser;
        showInputMsgDialog();
        mTextMsgInputDialog.setHint(item.user.nickname);
    }

    private void initInputDialog(){
        mTextMsgInputDialog = new TextMsgInputDialog(this, R.style.InputDialog);
        mTextMsgInputDialog.setmOnTextSendListener(new TextMsgInputDialog.OnTextSendListener() {
            @Override
            public void onTextSend(final String msg, boolean tanmuOpen) {
//                content.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (adapter.getItemCount() > 0) {
//                            content.getRecyclerView().scrollToPosition(adapter.getItemCount() - 1);
//                        }
//                    }
//                });
                postComment(msg);
            }
        });
    }

    private void postComment(String msg) {
        HttpService.postComment(this, actionBean.id, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), msg, callUserId, new BaseCallback<EmptyResult>(this, this) {
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
        HttpService.getComments(this, actionBean.id, 1, AppConstant.DEFAULT_PAGE_SIZE * currentPage, new BaseCallback<CommentListResult>(this, this) {
            @Override
            public void onSuccess(CommentListResult commentListResult, int id) {
                data.clear();
                content.setLoadMoreEnable(null != commentListResult.detail && commentListResult.detail.size() == AppConstant.DEFAULT_PAGE_SIZE * currentPage);
                content.setHasBottom(null != commentListResult.detail && commentListResult.detail.size() == AppConstant.DEFAULT_PAGE_SIZE * currentPage);
                data.addAll(commentListResult.detail);
                adapter.notifyDataSetChanged();
                content.complete();
            }

            @Override
            protected void onFailed(String errCode, String errMsg, CommentListResult commentListResult) {
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
    protected String getTitleStr() {
        return "动态详情";
    }

    @Override
    public DefaultAdapterViewListener<Comment> getViewListener() {
        return new CommentViewListener(this);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_action_comment;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        actionBean = (ActionBean) extras.getSerializable("actionBean");
        showInputFirst = extras.getBoolean("showInputFirst");
    }

    @Override
    protected void onEventComming(EventCenter center) {

    }
}
