package com.feng.freader.view.fragment.main;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.base.BaseFragment;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.constant.EventBusCode;
import com.feng.freader.entity.bean.Version;
import com.feng.freader.entity.eventbus.Event;
import com.feng.freader.util.FileUtil;
import com.feng.freader.util.NetUtil;
import com.feng.freader.util.VersionUtil;
import com.feng.freader.widget.TipDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/20
 */
public class MoreFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG = "MoreFragment";

    private View mCheckUpdateV;
    private TextView mVersionTv;
    private View mClearV;
    private TextView mCacheSizeTv;
    private View mAboutV;

    @Override
    protected void doInOnCreate() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mCheckUpdateV = getActivity().findViewById(R.id.v_more_check_update);
        mCheckUpdateV.setOnClickListener(this);
        mVersionTv = getActivity().findViewById(R.id.tv_more_version);
        mVersionTv.setText(VersionUtil.getVersionName(getActivity()));

        mClearV = getActivity().findViewById(R.id.v_more_clear);
        mClearV.setOnClickListener(this);
        mCacheSizeTv = getActivity().findViewById(R.id.tv_more_cache_size);
        mCacheSizeTv.setText(FileUtil.getLocalCacheSize());

        mAboutV = getActivity().findViewById(R.id.v_more_about);
        mAboutV.setOnClickListener(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        switch (event.getCode()) {
            case EventBusCode.MORE_INTO:
                mCacheSizeTv.setText(FileUtil.getLocalCacheSize());
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_more_check_update:
                if (!NetUtil.hasInternet(getActivity())) {
                    showShortToast("当前无网络，请检查网络后重试");
                    break;
                }
                final int currVersionCode = VersionUtil.getVersionCode(getActivity());
                BmobQuery<Version> bmobQuery = new BmobQuery<>();
                bmobQuery.getObject("A7ht0006", new QueryListener<Version>() {
                    @Override
                    public void done(final Version version, BmobException e) {
                        if (version != null) {
                            if (version.getVersionCode() > currVersionCode) {
                                new TipDialog.Builder(getActivity())
                                        .setContent("检测到有新版本，是否进行更新（注意：更新后书架数据将清除）")
                                        .setEnsure("是")
                                        .setCancel("不了")
                                        .setOnClickListener(new TipDialog.OnClickListener() {
                                            @Override
                                            public void clickEnsure() {
                                                try {
                                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                                    intent.setData(Uri.parse(version.getAddr()));
                                                    startActivity(intent);
                                                } catch (NullPointerException e) {
                                                    showShortToast("抱歉，下载地址出错");
                                                }
                                            }

                                            @Override
                                            public void clickCancel() {

                                            }
                                        })
                                        .build()
                                        .show();
                            } else {
                                showShortToast("已经是最新版本");
                            }
                        } else {
                            showShortToast("已经是最新版本");
                        }
                    }
                });
                break;
            case R.id.v_more_clear:
                final TipDialog tipDialog = new TipDialog.Builder(getActivity())
                        .setContent("是否清除缓存")
                        .setCancel("否")
                        .setEnsure("是")
                        .setOnClickListener(new TipDialog.OnClickListener() {
                            @Override
                            public void clickEnsure() {
                                FileUtil.clearLocalCache();
                                mCacheSizeTv.setText(FileUtil.getLocalCacheSize());
                            }

                            @Override
                            public void clickCancel() {

                            }
                        })
                        .build();
                tipDialog.show();
                break;
            case R.id.v_more_about:
                break;
            default:
                break;
        }
    }
}
