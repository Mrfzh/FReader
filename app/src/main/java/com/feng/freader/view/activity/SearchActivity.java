package com.feng.freader.view.activity;

import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.base.BaseActivity;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.constant.EventBusCode;
import com.feng.freader.db.DatabaseManager;
import com.feng.freader.entity.eventbus.Event;
import com.feng.freader.entity.eventbus.SearchUpdateInputEvent;
import com.feng.freader.util.EditTextUtil;
import com.feng.freader.util.NetUtil;
import com.feng.freader.util.SoftInputUtil;
import com.feng.freader.util.StatusBarUtil;
import com.feng.freader.view.fragment.search.HistoryFragment;
import com.feng.freader.view.fragment.search.SearchResultFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/8
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "SearchActivity";
    public static final String KEY_NOVEL_NAME = "key_novel_name";

    private ImageView mBackIv;
    private EditText mSearchBarEt;
    private TextView mSearchTv;
    private ImageView mDeleteSearchTextIv;

    private HistoryFragment mHistoryFragment;
    private SearchResultFragment mSearchResultFragment;

    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private boolean mIsShowSearchResFg = false;     // 是否正在显示搜索结果 Fragment
    private String mLastSearch = "";        // 记录上一搜索词

    private DatabaseManager mManager;   // 数据库管理类

    @Override
    protected void doBeforeSetContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //隐藏标题栏
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initData() {
        mManager = DatabaseManager.getInstance();
    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.iv_search_back);
        mBackIv.setOnClickListener(this);

        mSearchBarEt = findViewById(R.id.et_search_search_bar);
        // 监听内容变化
        mSearchBarEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    // 隐藏删除 icon
                    mDeleteSearchTextIv.setVisibility(View.GONE);
                    // 如果此时正在显示搜索结果 Fg，移除它
                    if (mIsShowSearchResFg) {
                        removeSearchResFg();
                    }
                    // 显示软键盘
                    EditTextUtil.focusAndShowSoftKeyboard(SearchActivity.this, mSearchBarEt);
                } else {
                    // 显示删除 icon
                    mDeleteSearchTextIv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // 监听软键盘
        mSearchBarEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 点击“完成”或者“下一项”
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_NEXT) {
                    // 进行搜索操作
                    doSearch();
                }
                return false;
            }
        });

        mSearchTv = findViewById(R.id.tv_search_search_text);
        mSearchTv.setOnClickListener(this);

        mDeleteSearchTextIv = findViewById(R.id.iv_search_delete_search_text);
        mDeleteSearchTextIv.setOnClickListener(this);
    }

    @Override
    protected void doAfterInit() {
        // 更改状态栏颜色
        StatusBarUtil.setLightColorStatusBar(this);
        getWindow().setStatusBarColor(getResources().getColor(R.color.search_bg));

        String novelName = getIntent().getStringExtra(KEY_NOVEL_NAME);
        if (novelName != null) {
            // 说明是通过点击列表小说跳转过来的，直接显示该小说的搜查结果
            mSearchBarEt.setText(novelName);
            doSearch();
        } else {
            // EditText 获得焦点并显示软键盘
            EditTextUtil.focusAndShowSoftKeyboard(this, mSearchBarEt);
            // 显示历史搜索页面
            showHistoryFg();
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        switch (event.getCode()) {
            case EventBusCode.SEARCH_UPDATE_INPUT:
                if (event.getData() instanceof SearchUpdateInputEvent) {
                    SearchUpdateInputEvent sEvent = (SearchUpdateInputEvent) event.getData();
                    mSearchBarEt.setText(sEvent.getInput());
                    // 进行搜索
                    doSearch();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_back:
                finish();
                break;
            case R.id.tv_search_search_text:
                doSearch();
                break;
            case R.id.iv_search_delete_search_text:
                // 删除 EditText 内容
                mSearchBarEt.setText("");
            default:
                break;
        }
    }

    /**
     * 进行搜索
     */
    private void doSearch() {
        // 点击搜索后隐藏软键盘
        SoftInputUtil.hideSoftInput(SearchActivity.this);
        if (!NetUtil.hasInternet(this)) {
            showShortToast("当前无网络，请检查网络后重试");
            return;
        }
        // 当前搜索词
        final String searchText = mSearchBarEt.getText().toString().trim();
        if (searchText.equals("")) {
            showShortToast("输入不能为空");
            return; // 不能为空
        }
        if (mIsShowSearchResFg) {
            // 如果此时已经是搜索结果 Fg，就直接更新它
            // 搜索同一个词时不用管
            if (!searchText.equals(mLastSearch)) {
                mSearchResultFragment.update(searchText);
            }
        } else {
            showSearchResFg();
        }
        mLastSearch = searchText;
        // 更新历史记录
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateHistoryDb(searchText);
            }
        }, 500);
    }

    /**
     * 第一次进入搜索页面时，显示历史搜索 Fragment
     */
    private void showHistoryFg() {
        if (mHistoryFragment == null) {
            mHistoryFragment = new HistoryFragment();
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.add(R.id.fv_search_container, mHistoryFragment);
        ft.show(mHistoryFragment);
        ft.commit();
    }

    /**
     * 隐藏历史搜索 Fg，显示搜索结果 Fg
     */
    private void showSearchResFg() {
        if (mSearchResultFragment != null) {
            return;
        }
        mSearchResultFragment = SearchResultFragment.newInstance(
                mSearchBarEt.getText().toString());
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.add(R.id.fv_search_container, mSearchResultFragment);
        if (mHistoryFragment != null) {
            ft.hide(mHistoryFragment);
        }
        ft.show(mSearchResultFragment);
        ft.commit();
        mIsShowSearchResFg = true;
    }

    /**
     * 从容器中移除搜索结果 Fg，显示历史搜索 Fg
     */
    private void removeSearchResFg() {
        if (mSearchResultFragment == null) {
            return;
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.remove(mSearchResultFragment);
        mSearchResultFragment = null;
        if (mHistoryFragment == null) {
            mHistoryFragment = new HistoryFragment();
            ft.add(R.id.fv_search_container, mHistoryFragment);
        }
        ft.show(mHistoryFragment);
        ft.commit();
        mIsShowSearchResFg = false;
    }

    /**
     * 更新历史记录数据库
     *
     * @param word 新输入的词语
     */
    private void updateHistoryDb(String word) {
        mManager.deleteHistory(word);
        mManager.insertHistory(word);
        // 通知历史页面更新历史记录
        if (mHistoryFragment != null) {
            Log.d(TAG, "updateHistoryDb: run");
            mHistoryFragment.updateHistory();
        }
    }
}
