package com.feng.freader.view.activity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.base.BaseActivity;
import com.feng.freader.constract.IReadContract;
import com.feng.freader.entity.data.DetailedChapterData;
import com.feng.freader.presenter.ReadPresenter;
import com.feng.freader.util.StatusBarUtil;
import com.feng.freader.widget.PageView;

/**
 * 小说阅读界面
 *
 * @author Feng Zhaohao
 * Created on 2019/11/25
 */
public class ReadActivity extends BaseActivity<ReadPresenter>
        implements IReadContract.View {
    private static final String TAG = "ReadActivity";
    public static final String KEY_URL = "read_key_url";

    private PageView mPageView;
    private TextView mNovelTitleTv;
    private TextView mNovelProgressTv;
    private TextView mStateTv;

    private String mUrl;    // 章节 url

    @Override
    protected void doBeforeSetContentView() {
        StatusBarUtil.setLightColorStatusBar(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_read;
    }

    @Override
    protected ReadPresenter getPresenter() {
        return new ReadPresenter();
    }

    @Override
    protected void initData() {
        mUrl = getIntent().getStringExtra(KEY_URL);
    }

    @Override
    protected void initView() {
        mPageView = findViewById(R.id.pv_read_page_view);
        mPageView.setPageViewListener(new PageView.PageViewListener() {
            @Override
            public void updateProgress(String progress) {
                mNovelProgressTv.setText(progress);
            }
        });

        mNovelTitleTv = findViewById(R.id.tv_read_novel_title);
        mNovelProgressTv = findViewById(R.id.tv_read_novel_progress);
        mStateTv = findViewById(R.id.tv_read_state);
    }

    @Override
    protected void doAfterInit() {
        // 获取具体章节信息
        if (mUrl != null) {
            Log.d(TAG, "doAfterInit: 开始请求数据");
            mPresenter.getDetailedChapterData(mUrl);
        } else {
            mStateTv.setText("获取 url 失败");
            Log.d(TAG, "doAfterInit: 获取 url 失败");
//            mPageView.setContent(Constant.NOVEL_CONTENT);
//            showShortToast("获取 url 失败");
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    public void getDetailedChapterDataSuccess(DetailedChapterData data) {
        if (data == null) {
            mStateTv.setText("获取不到相关数据");
            Log.d(TAG, "getDetailedChapterDataSuccess: data = null");
            return;
        }
        mStateTv.setVisibility(View.GONE);
        mPageView.setContent(data.getContent());
        mNovelTitleTv.setText(data.getName());
    }

    @Override
    public void getDetailedChapterDataError(String errorMsg) {
        Log.d(TAG, "getDetailedChapterDataError: errorMsg = " + errorMsg);
        mStateTv.setText(errorMsg);
    }

}
