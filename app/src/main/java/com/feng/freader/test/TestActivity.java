package com.feng.freader.test;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


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
import com.google.gson.Gson;

import java.security.MessageDigest;

public class TestActivity extends BaseActivity {

    private static final String TAG = "TestActivity";

    private ImageView mCoverIv;
    private Button mBlurBtn;

    @Override
    protected void doBeforeSetContentView() {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //隐藏状态栏
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //隐藏标题栏
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
        mCoverIv = findViewById(R.id.iv_test_cover);

        mBlurBtn = findViewById(R.id.btn_test_blur);
        mBlurBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://api.pingcc.cn/?xsurl1=https://www.xbiquge6.com/69_69491/";
                OkhttpUtil.getRequest(url, new OkhttpCall() {
                    @Override
                    public void onResponse(String json) {
                        Gson gson = new Gson();
                        CatalogBean bean = gson.fromJson(json, CatalogBean.class);
                        Log.d(TAG, "onResponse: " + bean);
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        Log.d(TAG, "onFailure: " + errorMsg);
                    }
                });
            }
        });
    }

    @Override
    protected void doAfterInit() {

    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

}
