package com.feng.freader.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.adapter.CatalogAdapter;
import com.feng.freader.base.BaseActivity;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.constant.Constant;
import com.feng.freader.constant.EventBusCode;
import com.feng.freader.constract.ICatalogContract;
import com.feng.freader.entity.data.CatalogData;
import com.feng.freader.entity.eventbus.Event;
import com.feng.freader.entity.eventbus.HoldReadActivityEvent;
import com.feng.freader.http.UrlObtainer;
import com.feng.freader.presenter.CatalogPresenter;
import com.feng.freader.util.NetUtil;
import com.feng.freader.util.StatusBarUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CatalogActivity extends BaseActivity<CatalogPresenter>
        implements ICatalogContract.View, View.OnClickListener {
    private static final String TAG = "CatalogActivity";
    private static final String ORDER_POSITIVE = "↑正序";
    private static final String ORDER_REVERSE = "↓倒序";
    public static final String KEY_URL = "catalog_key_url";
    public static final String KEY_NAME = "catalog_key_name";
    public static final String KEY_COVER = "catalog_key_cover";

    private ImageView mBackIv;
    private ImageView mRefreshIv;
    private TextView mChapterCountTv;
    private TextView mChapterOrderTv;
    private RecyclerView mCatalogListRv;
    private ProgressBar mProgressBar;
    private TextView mErrorPageTv;

    private CatalogAdapter mCatalogAdapter;
    private String mUrl;
    private String mName;
    private String mCover;

    /*
     * 如果是在 ReadActivity 通过点击目录跳转过来，那么持有该 ReadActivity 的引用，
     * 之后如果跳转到新的章节时，利用该引用结束旧的 ReadActivity
     */
    private ReadActivity mReadActivity;

    private List<String> mChapterNameList = new ArrayList<>();
    private List<String> mChapterUrlList = new ArrayList<>();

    private boolean mIsReverse = false;     // 是否倒序显示章节
    private boolean mIsReversing = false;   // 是否正在倒置，正在倒置时倒置操作无效
    private boolean mIsRefreshing = true;

    @Override
    protected void doBeforeSetContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //隐藏标题栏
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_catalog;
    }

    @Override
    protected CatalogPresenter getPresenter() {
        return new CatalogPresenter();
    }

    @Override
    protected void initData() {
        mUrl = getIntent().getStringExtra(KEY_URL);
        mName = getIntent().getStringExtra(KEY_NAME);
        mCover = getIntent().getStringExtra(KEY_COVER);
    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.iv_catalog_back);
        mBackIv.setOnClickListener(this);
        mRefreshIv = findViewById(R.id.iv_catalog_refresh);
        mRefreshIv.setOnClickListener(this);

        mChapterCountTv = findViewById(R.id.tv_catalog_chapter_count);

        mChapterOrderTv = findViewById(R.id.tv_catalog_chapter_order);
        mChapterOrderTv.setOnClickListener(this);

        mCatalogListRv = findViewById(R.id.rv_catalog_list);
        mCatalogListRv.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar = findViewById(R.id.pb_catalog);

        mErrorPageTv = findViewById(R.id.tv_catalog_error_page);
    }

    @Override
    protected void doAfterInit() {
        StatusBarUtil.setLightColorStatusBar(this);
        getWindow().setStatusBarColor(getResources().getColor(R.color.catalog_bg));

        if (mUrl != null) {
            mPresenter.getCatalogData(UrlObtainer.getCatalogInfo(mUrl));
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(Event event) {
        switch (event.getCode()) {
            case EventBusCode.CATALOG_HOLD_READ_ACTIVITY:
                if (event.getData() instanceof HoldReadActivityEvent) {
                    HoldReadActivityEvent holdReadActivityEvent = (HoldReadActivityEvent) event.getData();
                    mReadActivity = holdReadActivityEvent.getReadActivity();
                }
                break;
            default:
                break;
        }
    }

    private void initAdapter() {
        mCatalogAdapter = new CatalogAdapter(this, mChapterNameList);
        mCatalogAdapter.setOnCatalogListener(new CatalogAdapter.CatalogListener() {
            @Override
            public void clickItem(int position) {
                if (!NetUtil.hasInternet(CatalogActivity.this)) {
                    showShortToast("当前无网络，请检查网络后重试");
                    return;
                }
                // 点击 item，跳转到相应小说阅读页
                Intent intent = new Intent(CatalogActivity.this, ReadActivity.class);
                intent.putExtra(ReadActivity.KEY_NOVEL_URL, mUrl);
                intent.putExtra(ReadActivity.KEY_NAME, mName);
                intent.putExtra(ReadActivity.KEY_COVER, mCover);
                intent.putExtra(ReadActivity.KEY_CHAPTER_INDEX, position);
                intent.putExtra(ReadActivity.KEY_IS_REVERSE, mIsReverse);
                startActivity(intent);
                // 跳转后活动结束
                if (mReadActivity != null) {
                    mReadActivity.finish();
                }
                finish();
            }
        });
    }

    /**
     * 获取目录数据成功
     */
    @Override
    public void getCatalogDataSuccess(CatalogData catalogData) {
        mIsRefreshing = false;
        mProgressBar.setVisibility(View.GONE);
        mErrorPageTv.setVisibility(View.GONE);
        mChapterOrderTv.setVisibility(View.VISIBLE);
        if (catalogData == null) {
            String s = "网络请求失败，请确认网络连接正常后，刷新页面";
            mErrorPageTv.setText(s);
            mErrorPageTv.setVisibility(View.VISIBLE);
            return;
        }
        mChapterNameList = catalogData.getChapterNameList();
        mChapterUrlList = catalogData.getChapterUrlList();
        if (mIsReverse) {   // 如果是倒序显示的话需要先倒置
            Collections.reverse(mChapterNameList);
            Collections.reverse(mChapterUrlList);
        }

        int count = mChapterUrlList.size();
        mChapterCountTv.setText("共" + count + "章");
        initAdapter();
        mCatalogListRv.setAdapter(mCatalogAdapter);
    }

    /**
     * 获取目录数据失败
     */
    @Override
    public void getCatalogDataError(String errorMsg) {
        mIsRefreshing = false;
        mProgressBar.setVisibility(View.GONE);
        mChapterOrderTv.setVisibility(View.GONE);
        if (errorMsg.equals(Constant.NOT_FOUND_CATALOG_INFO)
            || errorMsg.equals(Constant.JSON_ERROR)) {
            String s = "很抱歉，该小说链接已失效，请阅读其他源";
            mErrorPageTv.setText(s);
        } else {
            Log.d(TAG, "getCatalogDataError: errorMsg = " + errorMsg);
            String s = "网络请求失败，请确认网络连接正常后，刷新页面";
            mErrorPageTv.setText(s);
        }
        mErrorPageTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_catalog_back:
                finish();
                break;
            case R.id.iv_catalog_refresh:
                refresh();
                break;
            case R.id.tv_catalog_chapter_order:
                if (mIsReversing || mIsRefreshing) {
                    return;
                }
                if (mIsReverse) {
                    // 正序显示章节
                    mChapterOrderTv.setText(ORDER_POSITIVE);
                    mIsReversing = true;
                    Collections.reverse(mChapterNameList);
                    Collections.reverse(mChapterUrlList);
                    mCatalogAdapter.notifyDataSetChanged();
                    mIsReverse = false;
                    mIsReversing = false;
                } else {
                    // 倒序显示章节
                    mChapterOrderTv.setText(ORDER_REVERSE);
                    mIsReversing = true;
                    Collections.reverse(mChapterNameList);
                    Collections.reverse(mChapterUrlList);
                    mCatalogAdapter.notifyDataSetChanged();
                    mIsReverse = true;
                    mIsReversing = false;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 刷新页面
     */
    private void refresh() {
        if (mIsRefreshing) {    // 已经在刷新了
            return;
        }
        mIsRefreshing = true;
        mProgressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getCatalogData(UrlObtainer.getCatalogInfo(mUrl));
            }
        }, 300);
    }
}
