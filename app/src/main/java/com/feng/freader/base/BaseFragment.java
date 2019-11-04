package com.feng.freader.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feng.freader.util.EventBusUtil;
import com.feng.freader.util.ToastUtil;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/20
 */
public abstract class BaseFragment<V extends BasePresenter> extends Fragment {

    protected V mPresenter;   //该Fragment对应的Presenter

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }

        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
        doInOnCreate();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);  //第三个参数一定要设为false;
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }

        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
    }

    /**
     * 获取fragment布局
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 在进行初始化布局前初始化相关数据
     */
    protected abstract void initData();

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 在onCreate方法中执行的操作
     */
    protected abstract void doInOnCreate();

    /**
     * 获取Presenter实例
     *
     * @return 相应的Presenter实例，没有则返回 null
     */
    protected abstract V getPresenter();


    /**
     * 是否注册EventBus，注册后才可以订阅事件
     *
     * @return 若需要注册 EventBus，则返回 true；否则返回 false
     */
    protected abstract boolean isRegisterEventBus();


    /**
     * 显示 Toast
     *
     * @param content
     */
    protected void showShortToast(String content) {
        ToastUtil.showToast(getContext(), content);
    }

    /**
     * 跳转到活动
     *
     * @param activity 新活动.class
     */
    protected void jump2Activity(Class activity) {
        startActivity(new Intent(getContext(), activity));
    }

    /**
     * 带Bundle的跳转活动
     *
     * @param activity 新活动.class
     * @param bundle
     */
    protected void jump2ActivityWithBundle(Class activity, Bundle bundle) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent, bundle);
    }

}
