package com.feng.freader.view.activity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.base.BaseActivity;
import com.feng.freader.constant.EventBusCode;
import com.feng.freader.constract.IReadContract;
import com.feng.freader.db.DatabaseManager;
import com.feng.freader.entity.data.BookshelfNovelDbData;
import com.feng.freader.entity.data.DetailedChapterData;
import com.feng.freader.entity.eventbus.Event;
import com.feng.freader.presenter.ReadPresenter;
import com.feng.freader.util.EventBusUtil;
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
    public static final String KEY_CHAPTER_URL = "read_key_chapter_url";
    public static final String KEY_NOVEL_URL = "read_key_novel_url";
    public static final String KEY_NAME = "read_key_name";
    public static final String KEY_COVER = "read_key_cover";
    public static final String KEY_POSITION = "read_key_position";

    private PageView mPageView;
    private TextView mNovelTitleTv;
    private TextView mNovelProgressTv;
    private TextView mStateTv;

    private String mChapterUrl;    // 章节 url
    private String mNovelUrl;   // 小说 url
    private String mName;   // 小说名
    private String mCover;  // 小说封面
    private int mPosition;  // 文本开始读取位置

    private DatabaseManager mDbManager;

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
        mChapterUrl = getIntent().getStringExtra(KEY_CHAPTER_URL);
        mNovelUrl = getIntent().getStringExtra(KEY_NOVEL_URL);
        mName = getIntent().getStringExtra(KEY_NAME);
        mCover = getIntent().getStringExtra(KEY_COVER);
        mPosition = getIntent().getIntExtra(KEY_POSITION, 0);
        Log.d(TAG, "initData: mChapterUrl = " + mChapterUrl + ", mNovelUrl = " + mNovelUrl +
                ", mName = " + mName + ", mCover = " + mCover);

        mDbManager = DatabaseManager.getInstance();
    }

    @Override
    protected void initView() {
        mPageView = findViewById(R.id.pv_read_page_view);
        mPageView.setPosition(mPosition);
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
        if (mChapterUrl != null) {
            Log.d(TAG, "doAfterInit: 开始请求数据");
            mPresenter.getDetailedChapterData(mChapterUrl);
        } else {
            mStateTv.setText("获取 url 失败");
            Log.d(TAG, "doAfterInit: 获取 url 失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 将书籍信息存入数据库
        mDbManager.deleteBookshelfNovel(mNovelUrl);
        BookshelfNovelDbData dbData = new BookshelfNovelDbData(mNovelUrl, mName,
                mCover, mChapterUrl, mPageView.getPosition());
        mDbManager.insertBookshelfNovel(dbData);

        Log.d(TAG, "dbData = " + mDbManager.queryAllBookshelfNovel());

        // 更新书架页面数据
        Event event = new Event(EventBusCode.BOOKSHELF_UPDATE_LIST);
        EventBusUtil.sendEvent(event);
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
