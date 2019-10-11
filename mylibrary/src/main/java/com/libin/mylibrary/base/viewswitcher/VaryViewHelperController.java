/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.libin.mylibrary.base.viewswitcher;


import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.libin.mylibrary.R;
import com.libin.mylibrary.util.StringUtils;


public class VaryViewHelperController
{

    private IVaryViewHelper helper;

    private AnimationDrawable animationDrawable;
    
    private ImageView ivLoading;

    public VaryViewHelperController(View view)
    {
        this(new VaryViewHelper(view));
    }

    public VaryViewHelperController(IVaryViewHelper helper)
    {
        super();
        this.helper = helper;
    }

    public void showNetworkError(View.OnClickListener onClickListener)
    {
        /*View layout = helper.inflate(R.layout.message);

        TextView textView = (TextView) layout.findViewById(R.id.message_info);
        textView.setText(helper.getContext().getResources().getString(R.string.common_no_network_msg));

        ImageView imageView = (ImageView) layout.findViewById(R.id.message_icon);
        imageView.setImageResource(R.drawable.monkey_without);

        if (null != onClickListener)
        {
            layout.setOnClickListener(onClickListener);
        }

        helper.showLayout(layout);*/
    }

    public void showError(String errorMsg, View.OnClickListener onClickListener)
    {
        View layout = helper.inflate(R.layout.message);
        TextView textView = (TextView) layout.findViewById(R.id.message_info);
        if (!StringUtils.isEmpty(errorMsg))
        {
            textView.setText(errorMsg);
        } else
        {
            textView.setText(helper.getContext().getResources().getString(R.string.common_error_msg));
        }

//        ImageView imageView = (ImageView) layout.findViewById(R.id.message_icon);
//        imageView.setImageResource(R.drawable.monkey_without);

        if (null != onClickListener)
        {
            layout.setOnClickListener(onClickListener);
        }

        helper.showLayout(layout);
    }

    public void showEmpty(String msg, View.OnClickListener onClickListener)
    {
        showEmpty(msg, 0, onClickListener);
    }

    public void showEmpty(String emptyMsg, int imgID, View.OnClickListener onClickListener)
    {
        View layout = helper.inflate(R.layout.message);
        TextView textView = (TextView) layout.findViewById(R.id.message_info);
        if (emptyMsg != null)
        {
            textView.setText(emptyMsg);
        } else
        {
            textView.setText(helper.getContext().getResources().getString(R.string.common_empty_msg));
        }

        ImageView imageView = (ImageView) layout.findViewById(R.id.message_icon);
        if (0 != imgID)
        {
            imageView.setImageResource(imgID);
        } else
        {
//            imageView.setImageResource(R.drawable.monkey_sigh);
        }

        if (null != onClickListener)
        {
            layout.setOnClickListener(onClickListener);
        }

        helper.showLayout(layout);
    }

    public void showLoading(String msg, Activity activity)
    {
        /*View layout = helper.inflate(R.layout.loading);
        if (!StringUtils.isEmpty(msg))
        {
            TextView textView = (TextView) layout.findViewById(R.id.loading_msg);
            textView.setText(msg);
        }
        helper.showLayout(layout);
        ivLoading = (ImageView) layout.findViewById(R.id.ivLoading);
        if (animationDrawable == null)
        {
            animationDrawable = (AnimationDrawable) activity.getResources().getDrawable(R.drawable.animation_loading);
        }
        ivLoading.setImageDrawable(animationDrawable);
        animationDrawable.start();*/
//        Glide.with(activity).load(R.drawable.icon_loading).into(ivLoading);
    }

    public void showLoading(String msg, Fragment fragment)
    {
        /*View layout = helper.inflate(R.layout.loading);
        if (!StringUtils.isEmpty(msg))
        {
            TextView textView = (TextView) layout.findViewById(R.id.loading_msg);
            textView.setText(msg);
        }
        helper.showLayout(layout);
        ivLoading = (ImageView) layout.findViewById(R.id.ivLoading);

        if (animationDrawable == null)
        {
            animationDrawable = (AnimationDrawable) fragment.getResources().getDrawable(R.drawable.animation_loading);
        }
        ivLoading.setImageDrawable(animationDrawable);*/
//        Glide.with(fragment).load(R.drawable.icon_loading).into(ivLoading);
    }

    public void restore()
    {
        if(animationDrawable != null && animationDrawable.isRunning())
        {
            animationDrawable.stop();
        }
        
        if(ivLoading != null)
            ivLoading.clearAnimation();
        
        helper.restoreView();
    }
}
