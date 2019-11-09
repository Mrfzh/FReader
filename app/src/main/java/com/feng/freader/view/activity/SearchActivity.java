package com.feng.freader.view.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.base.BaseActivity;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.util.EditTextUtil;
import com.feng.freader.util.SoftInputUtil;
import com.feng.freader.util.StatusBarUtil;
import com.feng.freader.view.fragment.search.HistoryFragment;
import com.feng.freader.view.fragment.search.SearchResultFragment;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/8
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "SearchActivity";

    private ImageView mBackIv;
    private EditText mSearchBarEt;
    private TextView mSearchTv;
    private ImageView mDeleteSearchTextIv;

    private HistoryFragment mHistoryFragment;
    private SearchResultFragment mSearchResultFragment;
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private boolean mIsShowSearchResFg = false;     // 是否正在显示搜索结果 Fragment

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

    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.iv_search_back);
        mBackIv.setOnClickListener(this);

        mSearchBarEt = findViewById(R.id.et_search_search_bar);
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

        // EditText 获得焦点并显示软键盘
        EditTextUtil.focusAndShowSoftKeyboard(this, mSearchBarEt);

        // 显示历史搜索页面
        showHistoryFg();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_back:
                finish();
                break;
            case R.id.tv_search_search_text:
                // 点击搜索后隐藏软键盘
                SoftInputUtil.hideSoftInput(SearchActivity.this);

                if (mIsShowSearchResFg) {
                    // TODO 如果此时已经是搜索结果 Fg，就直接更新它
                } else {
                    showSearchResFg();
                }
                // TODO 更新历史记录
                break;
            case R.id.iv_search_delete_search_text:
                // 删除 EditText 内容
                mSearchBarEt.setText("");
            default:
                break;
        }
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
        ft.hide(mHistoryFragment);
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
        ft.show(mHistoryFragment);
        ft.commit();
        mIsShowSearchResFg = false;
    }
}
