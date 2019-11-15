package com.feng.freader.test;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
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
import com.feng.freader.util.BlurUtil;

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

                Glide.with(TestActivity.this)
                        .load("https://www.xbiquge6.com/cover/83/83692/83692s.jpg")
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.cover_place_holder)
                                .transform(new BitmapTransformation() {
                                    @Override
                                    protected Bitmap transform(@NonNull BitmapPool pool,
                                                               @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                                        // 对得到的 Bitmap 进行虚化处理
                                        return BlurUtil.blurBitmap(TestActivity.this, toTransform, 5, 8);
                                    }

                                    @Override
                                    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

                                    }
                                }))
                        .into(mCoverIv);

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
