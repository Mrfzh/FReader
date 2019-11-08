package com.feng.freader.view.activity;

import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.base.BaseActivity;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.util.EditTextUtil;
import com.feng.freader.util.StatusBarUtil;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/8
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private EditText mSearchBarEt;
    private TextView mSearchTv;

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

        mSearchTv = findViewById(R.id.tv_search_search_text);
        mSearchTv.setOnClickListener(this);
    }

    @Override
    protected void doAfterInit() {
        // 更改状态栏颜色
        StatusBarUtil.setLightColorStatusBar(this);
        getWindow().setStatusBarColor(getResources().getColor(R.color.search_bg));

        // EditText 获得焦点并显示软键盘
        EditTextUtil.focusAndShowSoftKeyboard(this, mSearchBarEt);
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
                showShortToast("click search");
                break;
            default:
                break;
        }
    }
}
