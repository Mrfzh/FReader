package com.feng.freader.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.feng.freader.R;
import com.feng.freader.adapter.CatalogAdapter;
import com.feng.freader.base.BaseActivity;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.constant.EventBusCode;
import com.feng.freader.entity.epub.EpubTocItem;
import com.feng.freader.entity.epub.OpfData;
import com.feng.freader.entity.eventbus.EpubCatalogInitEvent;
import com.feng.freader.entity.eventbus.Event;
import com.feng.freader.entity.eventbus.HoldReadActivityEvent;
import com.feng.freader.util.StatusBarUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class EpubCatalogActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private RecyclerView mListRv;

    // 通过 EventBus 初始化
    private ReadActivity mReadActivity;
    private List<EpubTocItem> mEpubTocItemList = new ArrayList<>();
    private OpfData mOpfData;
    private String mNovelUrl;
    private String mName;
    private String mCover;

    private List<String> mChapterNameList = new ArrayList<>();

    @Override
    protected void doBeforeSetContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //隐藏标题栏
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_epub_catalog;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initData() {
        for (int i = 0; i < mEpubTocItemList.size(); i++) {
            mChapterNameList.add(mEpubTocItemList.get(i).getTitle());
        }
    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.iv_epub_catalog_back);
        mBackIv.setOnClickListener(this);

        mListRv = findViewById(R.id.rv_epub_catalog_list);
        mListRv.setLayoutManager(new LinearLayoutManager(this));
        CatalogAdapter adapter = new CatalogAdapter(this, mChapterNameList);
        adapter.setOnCatalogListener(new CatalogAdapter.CatalogListener() {
            @Override
            public void clickItem(int position) {
                mReadActivity.finish();
                int chapterIndex = 0;
                // 计算阅读的章节
                for (int i = 0; i < mOpfData.getSpine().size(); i++) {
                    if (mEpubTocItemList.get(position).getPath()
                            .equals(mOpfData.getSpine().get(i))) {
                        chapterIndex = i;
                        break;
                    }
                }
                // 跳转活动
                Intent intent = new Intent(EpubCatalogActivity.this, ReadActivity.class);
                // 小说 url（本地小说为 filePath），参数类型为 String
                intent.putExtra(ReadActivity.KEY_NOVEL_URL, mNovelUrl);
                // 小说名，参数类型为 String
                intent.putExtra(ReadActivity.KEY_NAME, mName);
                // 小说封面 url，参数类型为 String
                intent.putExtra(ReadActivity.KEY_COVER, mCover);
                // 小说类型，0 为网络小说， 1 为本地 txt 小说，2 为本地 epub 小说
                // 参数类型为 int（非必需，不传的话默认为 0）
                intent.putExtra(ReadActivity.KEY_TYPE, 2);
                // 开始阅读的章节索引，参数类型为 int（非必需，不传的话默认为 0）
                intent.putExtra(ReadActivity.KEY_CHAPTER_INDEX, chapterIndex);
                startActivity(intent);
                finish();
            }
        });
        mListRv.setAdapter(adapter);
    }

    @Override
    protected void doAfterInit() {
        StatusBarUtil.setLightColorStatusBar(this);
        getWindow().setStatusBarColor(getResources().getColor(R.color.catalog_bg));
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(Event event) {
        switch (event.getCode()) {
            case EventBusCode.EPUB_CATALOG_INIT:
                if (event.getData() instanceof EpubCatalogInitEvent) {
                    EpubCatalogInitEvent e = (EpubCatalogInitEvent) event.getData();
                    mReadActivity = e.getReadActivity();
                    mEpubTocItemList = e.getTocItemList();
                    mOpfData = e.getOpfData();
                    mNovelUrl = e.getNovelUrl();
                    mName = e.getNovelName();
                    mCover = e.getNovelCover();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_epub_catalog_back:
                finish();
                break;
            default:
                break;
        }
    }
}
