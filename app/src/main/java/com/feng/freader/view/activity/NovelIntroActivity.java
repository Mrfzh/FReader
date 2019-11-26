package com.feng.freader.view.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.feng.freader.R;
import com.feng.freader.base.BaseActivity;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.constant.EventBusCode;
import com.feng.freader.entity.data.NovelSourceData;
import com.feng.freader.entity.eventbus.Event;
import com.feng.freader.entity.eventbus.NovelIntroInitEvent;
import com.feng.freader.test.TestActivity;
import com.feng.freader.util.BlurUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.MessageDigest;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/15
 */
public class NovelIntroActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "NovelIntroActivity";
    private static final int NOVEL_INTRODUCE_MAX_LINES = 3; // 小说简介最多显示多少行

    private ImageView mBackIv;
    private ImageView mMenuIv;
    private ImageView mTopBgIv;
    private ImageView mNovelCoverIv;
    private TextView mNovelNameTv;
    private TextView mNovelAuthorTv;
    private TextView mNovelWebSiteTv;
    private TextView mNovelIntroduceTv;
    private ImageView mMoreIntroduceIv;
    private TextView mCatalogTv;

    private NovelSourceData mNovelSourceData;

    @Override
    protected void doBeforeSetContentView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_novel_intro;
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
        mBackIv = findViewById(R.id.iv_novel_intro_back);
        mBackIv.setOnClickListener(this);
        mMenuIv = findViewById(R.id.iv_novel_intro_menu);
        mMenuIv.setOnClickListener(this);

        if (mNovelSourceData == null) {
            showShortToast("no data");
            return;
        }

        mTopBgIv = findViewById(R.id.iv_novel_intro_top_image_bg);
        Glide.with(this)
                .load(mNovelSourceData.getCover())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.cover_place_holder)
                        .error(R.drawable.cover_error)
                        .transform(new BitmapTransformation() {
                            @Override
                            protected Bitmap transform(@NonNull BitmapPool pool,
                                                       @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                                // 对得到的 Bitmap 进行虚化处理
                                return BlurUtil.blurBitmap(NovelIntroActivity.this,
                                        toTransform, 5, 8);
                            }

                            @Override
                            public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

                            }
                        }))
                .into(mTopBgIv);

        mNovelCoverIv = findViewById(R.id.iv_novel_intro_novel_cover);
        Glide.with(this)
                .load(mNovelSourceData.getCover())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.cover_place_holder)
                        .error(R.drawable.cover_error))
                .into(mNovelCoverIv);

        mNovelNameTv = findViewById(R.id.tv_novel_intro_novel_name);
        mNovelNameTv.setText(mNovelSourceData.getName());
        mNovelAuthorTv = findViewById(R.id.tv_novel_intro_novel_author);
        mNovelAuthorTv.setText(mNovelSourceData.getAuthor());
        mNovelWebSiteTv = findViewById(R.id.tv_novel_intro_novel_web_site);
        mNovelWebSiteTv.setText(mNovelSourceData.getUrl());
        mNovelIntroduceTv = findViewById(R.id.tv_novel_intro_novel_introduce);
        mNovelIntroduceTv.setText(mNovelSourceData.getIntroduce());
        mNovelIntroduceTv.setOnClickListener(this);
        mMoreIntroduceIv = findViewById(R.id.iv_novel_intro_more_introduce);

        mNovelIntroduceTv.post(new Runnable() {
            @Override
            public void run() {
                // 判断是否需要隐藏显示更多按钮
                if (mNovelIntroduceTv.getLayout().getLineCount() <= mNovelIntroduceTv.getMaxLines()) {
                    // 隐藏显示更多
                    mMoreIntroduceIv.setVisibility(View.GONE);
                }
            }
        });

        mCatalogTv = findViewById(R.id.tv_novel_intro_catalog);
        mCatalogTv.setOnClickListener(this);
    }

    @Override
    protected void doAfterInit() {

    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventCome(Event event) {
        switch (event.getCode()) {
            case EventBusCode.NOVEL_INTRO_INIT:
                if (event.getData() instanceof NovelIntroInitEvent) {
                    NovelIntroInitEvent novelIntroInitEvent = (NovelIntroInitEvent) event.getData();
                    mNovelSourceData = novelIntroInitEvent.getNovelSourceData();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_novel_intro_novel_introduce:
                // 是否显示小说简介的全部内容
                if (mMoreIntroduceIv.getVisibility() != View.VISIBLE &&
                        mNovelIntroduceTv.getMaxLines() != Integer.MAX_VALUE) {
                    return;
                }
                if (mNovelIntroduceTv.getMaxLines() != Integer.MAX_VALUE) {
                    mMoreIntroduceIv.setVisibility(View.GONE);
                    mNovelIntroduceTv.setMaxLines(Integer.MAX_VALUE);
                } else {
                    mNovelIntroduceTv.setMaxLines(NOVEL_INTRODUCE_MAX_LINES);
                    mMoreIntroduceIv.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_novel_intro_back:
                finish();
                break;
            case R.id.iv_novel_intro_menu:
                PopupMenu popupMenu = new PopupMenu(NovelIntroActivity.this, mMenuIv);
                popupMenu.getMenuInflater().inflate(R.menu.menu_novel_intro, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_novel_intro_show_in_browser:
                                // 在浏览器显示
                                Uri uri = Uri.parse(mNovelSourceData.getUrl());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                try {
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    showShortToast("暂不支持在浏览器打开");
                                }
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                break;
            case R.id.tv_novel_intro_catalog:
                Log.d(TAG, "startActivity: run");
                Intent intent = new Intent(NovelIntroActivity.this, CatalogActivity.class);
                intent.putExtra(CatalogActivity.KEY_URL, mNovelSourceData.getUrl());    // 传递当前小说的 url
                intent.putExtra(CatalogActivity.KEY_NAME, mNovelSourceData.getName());  // 传递当前小说的名字
                intent.putExtra(CatalogActivity.KEY_COVER, mNovelSourceData.getCover()); // 传递当前小说的封面
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
