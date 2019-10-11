package com.wstv.pad.holder.search;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wstv.pad.R;
import com.wstv.pad.http.model.search.HotWord;
import com.wstv.pad.util.click.ClickProxy;

import java.util.List;

import am.widget.wraplayout.WrapLayout;
import em.sang.com.allrecycleview.holder.CustomPeakHolder;

/**
 * Created by Kindred on 2019/3/15.
 */

public class SearchHeaderHolder extends CustomPeakHolder {

    private WrapClick wrapClick;

    private List<HotWord> hot;

    public SearchHeaderHolder(Context context, View itemView, List<HotWord> hot, WrapClick wrapClick) {
        super(itemView);
        this.context = context;
        this.wrapClick = wrapClick;
        this.hot = hot;
    }

    @Override
    public void initView(int position, Context context) {
        initWrap((WrapLayout) holderHelper.getView(R.id.header_search_wrap), hot);
    }

    private void initWrap(final WrapLayout wrapLayout, List<HotWord> list) {
        if (null == list || list.size() == 0) {
            return;
        }
        for (HotWord label : list) {
            View layout = LayoutInflater.from(context).inflate(R.layout.layout_hot_search, null);
            TextView textView = layout.findViewById(R.id.layout_hot_search_label);
            textView.setText(TextUtils.isEmpty(label.content) ? "热词热词" : label.content);
            textView.setOnClickListener(new ClickProxy(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != wrapClick) {
                        wrapClick.onWrapClick(((TextView) v).getText().toString());
                    }
                }
            }));
            wrapLayout.addView(layout);
        }
    }

    public interface WrapClick{
        void onWrapClick(String condition);
    }
}
