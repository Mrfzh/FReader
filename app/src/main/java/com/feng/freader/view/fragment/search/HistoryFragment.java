package com.feng.freader.view.fragment.search;

import android.view.View;
import android.widget.ImageView;

import com.feng.freader.R;
import com.feng.freader.adapter.HistoryAdapter;
import com.feng.freader.base.BaseFragment;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.constant.EventBusCode;
import com.feng.freader.db.DatabaseManager;
import com.feng.freader.entity.eventbus.Event;
import com.feng.freader.entity.eventbus.SearchUpdateInputEvent;
import com.feng.freader.util.EventBusUtil;
import com.feng.freader.widget.FlowLayout;
import com.feng.freader.widget.TipDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/9
 */
public class HistoryFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG = "HistoryFragment";
    private static final int MAX_LINES = 3;     // 最多显示的行数

    private FlowLayout mHistoryListFv;
    private ImageView mClearAllIv;

    private DatabaseManager mManager;
    private HistoryAdapter mHistoryAdapter;
    private List<String> mContentList = new ArrayList<>();  // 搜索内容集合

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history;
    }

    @Override
    protected void initData() {
        mManager = DatabaseManager.getInstance();
        mContentList = mManager.queryAllHistory();
    }

    @Override
    protected void initView() {
        mHistoryListFv = getActivity().findViewById(R.id.fv_history_history_list);
        // 设置 Adapter
        mHistoryAdapter = new HistoryAdapter(getActivity(), mContentList);
        mHistoryAdapter.setOnHistoryAdapterListener(new HistoryAdapter.HistoryAdapterListener() {
            @Override
            public void clickWord(String word) {
                // 通知 SearchActivity 进行搜索
                Event<SearchUpdateInputEvent> event = new Event<>(EventBusCode.SEARCH_UPDATE_INPUT,
                        new SearchUpdateInputEvent(word));
                EventBusUtil.sendEvent(event);
            }
        });
        mHistoryListFv.setAdapter(mHistoryAdapter);
        // 设置最多显示的行数
        mHistoryListFv.setMaxLines(MAX_LINES);
        // 获取显示的 item 数
        mHistoryListFv.post(new Runnable() {
            @Override
            public void run() {
                int count = mHistoryListFv.getVisibleItemCount();
                if (count < mContentList.size() / 2) {
                    // 从数据库删除多余的历史记录
                    mManager.deleteHistories(mContentList.size() - count);
                }
            }
        });

        mClearAllIv = getActivity().findViewById(R.id.iv_history_clear_all);
        mClearAllIv.setOnClickListener(this);
        if (mContentList.size() != 0) {
            mClearAllIv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void doInOnCreate() {

    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    /**
     * 更新搜索历史
     */
    public void updateHistory() {
        if (mHistoryAdapter == null) {
            return;
        }
        // 更新历史搜索视图
        mContentList = mManager.queryAllHistory();
        if (mContentList.size() != 0) {
            mClearAllIv.setVisibility(View.VISIBLE);
        }
        mHistoryAdapter.updateList(mContentList);   // 记得要先更新 Adapter 的数据
        mHistoryListFv.updateView();        // 重新根据 Adapter 的 数据来设置视图
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_history_clear_all:
                // 弹出 Dialog 再次确认是否清空
                TipDialog dialog = new TipDialog.Builder(getActivity())
                        .setContent("是否清空历史搜索")
                        .setEnsure("是")
                        .setCancel("不了")
                        .setOnClickListener(new TipDialog.OnClickListener() {
                            @Override
                            public void clickEnsure() {
                                // 清空数据库，并更新页面
                                mManager.deleteAllHistories();
                                mContentList.clear();
                                mHistoryListFv.updateView();
                                // 图标隐藏
                                mClearAllIv.setVisibility(View.GONE);
                            }

                            @Override
                            public void clickCancel() {

                            }
                        })
                        .build();
                dialog.show();
                break;
            default:
                break;
        }
    }
}
