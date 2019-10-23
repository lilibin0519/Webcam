package com.wstv.webcam.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.libin.mylibrary.widget.swiperecyclerview.SwipeRecyclerView;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.holder.performer.PerformerViewListener;
import com.wstv.webcam.holder.search.SearchHeaderHolder;
import com.wstv.webcam.http.HttpService;
import com.wstv.webcam.http.callback.BaseCallback;
import com.wstv.webcam.http.model.EmptyResult;
import com.wstv.webcam.http.model.Performer;
import com.wstv.webcam.http.model.SearchResult;
import com.wstv.webcam.http.model.search.HotWord;
import com.wstv.webcam.http.model.search.HotWordsResult;
import com.wstv.webcam.util.click.ClickProxy;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import em.sang.com.allrecycleview.adapter.DefaultAdapter;
import em.sang.com.allrecycleview.cutline.RecycleViewDivider;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * <p>Description: </p>
 * SearchActivity
 *
 * @author lilibin
 * @createDate 2019/3/11 12:11
 */

public class SearchActivity extends BaseActivity implements OnToolsItemClickListener<Performer>,SwipeRecyclerView.OnLoadListener {

    @Bind(R.id.title_bar_edit_layout)
    LinearLayout editLayout;

    @Bind(R.id.title_bar_edit)
    EditText editText;

    @Bind(R.id.activity_search_content)
    SwipeRecyclerView content;

    private String conditionStr = "";

    private List<Performer> data;

    private DefaultAdapter<Performer> adapter;

    private SearchHeaderHolder headerHolder;

    private List<HotWord> hot;

    private int currentPage;

    @Override
    protected void initViewAndData() {
        initList();
        HttpService.hotWords(this, new BaseCallback<HotWordsResult>(this, this) {
            @Override
            public void onSuccess(HotWordsResult hotWordsResult, int id) {
                hot.clear();
                hot.addAll(hotWordsResult.detail);
                adapter.notifyDataSetChanged();
            }
        });
        ButterKnife.findById(this, R.id.title_bar_right).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightClick(v);
            }
        }));
//        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
//            @Override
//            public void keyBoardShow(int height) {
//                if (adapter.getHeards().size() > 0) {
//                    adapter.getHeards().clear();
//                    adapter.notifyItemRemoved(0);
//                    adapter.notifyItemRangeChanged(0, data.size());
//                }
//            }
//
//            @Override
//            public void keyBoardHide(int height) {
//                if (adapter.getHeards().size() == 0) {
//                    adapter.addHead(headerHolder);
//                    adapter.notifyItemInserted(0);
//                    adapter.notifyItemRangeChanged(0, data.size() + 1);
//                }
//            }
//        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    hideHeader();
                } else {
                    showHeader();
                }
            }
        });
        editLayout.setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.requestFocus();
            }
        }));
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){//搜索按键action
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    conditionStr = editText.getText().toString();
                    if (TextUtils.isEmpty(conditionStr)){
                        return true;
                    }
                    data.clear();
                    onSearch(conditionStr);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(int position, Performer item) {
        if (item.isfans) {
            cancelFollow(position);
        } else {
            requestFollow(position);
        }
    }

    private void cancelFollow(final int position) {
        HttpService.cancelFollow(this, data.get(position).id, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID),
                new BaseCallback<EmptyResult>(this, this) {
            @Override
            public void onSuccess(EmptyResult result, int id) {
                data.get(position).isfans = false;
                adapter.notifyItemRangeChanged(position, 1);
            }
        });
    }

    private void requestFollow(final int position) {
        HttpService.follow(this, data.get(position).id, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID),
                new BaseCallback<EmptyResult>(this, this) {
            @Override
            public void onSuccess(EmptyResult result, int id) {
                data.get(position).isfans = true;
                adapter.notifyItemRangeChanged(position, 1);
            }
        });
    }

    @Override
    public void onRightClick(View v) {
        onLeftClick(v);
    }

    private void onSearch(final String conditionStr) {
        hideHeader();
        HttpService.search(this, conditionStr, String.valueOf(currentPage), String.valueOf(AppConstant.DEFAULT_PAGE_SIZE), PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID),
                new BaseCallback<SearchResult>(this, this) {
                    @Override
                    public void onSuccess(SearchResult searchResult, int id) {
                        if (null != searchResult.detail && searchResult.detail.size() > 0) {
                            data.addAll(searchResult.detail);
                        }
                        content.setHasBottom(null != searchResult.detail && searchResult.detail.size() == AppConstant.DEFAULT_PAGE_SIZE);
                        content.setLoadMoreEnable(null != searchResult.detail && searchResult.detail.size() == AppConstant.DEFAULT_PAGE_SIZE);
                        content.complete();
                        adapter.notifyDataSetChanged();
                    }
                });
//        content.requestFocus();
    }

    private void hideHeader() {
        if (adapter.getHeards().size() > 0) {
            adapter.getHeards().clear();
            adapter.notifyItemRemoved(0);
            adapter.notifyItemRangeChanged(0, data.size());
        }
    }

    private void showHeader() {
        if (adapter.getHeards().size() == 0) {
            adapter.addHead(headerHolder);
            adapter.notifyItemInserted(0);
            adapter.notifyItemRangeChanged(0, data.size() + 1);
            content.getRecyclerView().scrollToPosition(0);
        }
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        data.clear();
        adapter.notifyDataSetChanged();
        onSearch(conditionStr);
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        onSearch(conditionStr);
    }

    private void initList() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        content.getRecyclerView().setLayoutManager(manager);
        content.getRecyclerView().addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL,
                getResources().getDimensionPixelSize(R.dimen.qb_px_1), ContextCompat.getColor(this, R.color.line_color)));
        data = new ArrayList<>();
        hot = new ArrayList<>();

        content.setAdapter(adapter = new DefaultAdapter<>(this, data, R.layout.item_performer, new PerformerViewListener(this)));
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_search, content.getRecyclerView(), false);
        adapter.addHead(headerHolder = new SearchHeaderHolder(this, headerView, hot, new SearchHeaderHolder.WrapClick() {
            @Override
            public void onWrapClick(String condition) {
                editText.setText(condition);
                conditionStr = condition;
                onSearch(conditionStr);
            }
        }));
        content.setRefreshEnable(true);
        content.setLoadMoreEnable(false);
        content.setHasBottom(false);
        content.setOnLoadListener(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_search;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComming(EventCenter center) {

    }
}
