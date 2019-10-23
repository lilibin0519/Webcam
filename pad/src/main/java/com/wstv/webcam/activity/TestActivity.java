package com.wstv.webcam.activity;

import android.os.Bundle;
import android.view.View;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.opensource.svgaplayer.SVGAImageView;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by Kindred on 2019/4/12.
 */

public class TestActivity extends BaseActivity {

    @Bind(R.id.activity_cam_detail_anim_view)
    SVGAImageView animView;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_test;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void initViewAndData() {
//        animView.setCallback(new SVGACallback() {
//            @Override
//            public void onPause() {
//                Trace.e("onPause");
//            }
//
//            @Override
//            public void onFinished() {
//                Trace.e("onFinished");
//            }
//
//            @Override
//            public void onRepeat() {
//                Trace.e("onRepeat");
//            }
//
//            @Override
//            public void onStep(int i, double v) {
//                Trace.e("onStep");
//            }
//        });
//        animView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
        animView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animView.isAnimating()) {
                    return;
                }
                animView.startAnimation();
            }
        });
//        animView.post(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
    }

    private void loadAnimation() {
//        SVGAParser parser = new SVGAParser(this);
//        parser.decodeFromAssets("angel.svga", new SVGAParser.ParseCompletion() {
//            @Override
//            public void onComplete(@NotNull SVGAVideoEntity videoItem) {
//                animView.setVideoItem(videoItem);
//                animView.startAnimation();
//            }
//            @Override
//            public void onError() {
//                showToastCenter("error");
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComming(EventCenter center) {

    }
}
