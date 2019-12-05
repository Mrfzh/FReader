package com.feng.freader.test;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.feng.freader.R;
import com.feng.freader.base.BaseActivity;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.entity.bean.CatalogBean;
import com.feng.freader.http.OkhttpCall;
import com.feng.freader.http.OkhttpUtil;
import com.feng.freader.util.BlurUtil;
import com.feng.freader.view.activity.MainActivity;
import com.google.gson.Gson;

import java.security.MessageDigest;

public class TestActivity extends BaseActivity {

    private static final String TAG = "TestActivity";

    private Button mTestBtn;
    private View mTopView;
    private View mBottomView;
    private SeekBar mSeekBar;

    private boolean mIsHide = true;

    @Override
    protected void doBeforeSetContentView() {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //隐藏状态栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //隐藏标题栏
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mTopView = findViewById(R.id.v_test_top);
        mBottomView = findViewById(R.id.v_test_bottom);
        mSeekBar = findViewById(R.id.sb_test);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 拖动进度条时回调，得到当前进度
                Log.d(TAG, "onProgressChanged: progress = " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mTestBtn = findViewById(R.id.btn_test);
        mTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(30);
            }
        });
    }

    @Override
    protected void doAfterInit() {

    }

    private void anim() {
        if (mIsHide) {
            Animation topAnim = AnimationUtils.loadAnimation(
                    TestActivity.this, R.anim.read_setting_top_enter);
            Animation bottomAnim = AnimationUtils.loadAnimation(
                    TestActivity.this, R.anim.read_setting_bottom_enter);
            mTopView.startAnimation(topAnim);
            mBottomView.setAnimation(bottomAnim);
            mTopView.setVisibility(View.VISIBLE);
            mBottomView.setVisibility(View.VISIBLE);
            mIsHide = false;
        } else {
            Animation topExitAnim = AnimationUtils.loadAnimation(
                    TestActivity.this, R.anim.read_setting_top_exit);
            topExitAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mTopView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            Animation bottomExitAnim = AnimationUtils.loadAnimation(
                    TestActivity.this, R.anim.read_setting_bottom_exit);
            bottomExitAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mBottomView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mTopView.startAnimation(topExitAnim);
            mBottomView.setAnimation(bottomExitAnim);
            mIsHide = true;
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

}
