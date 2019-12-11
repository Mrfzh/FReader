package com.feng.freader.view.fragment.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.adapter.BookshelfNovelsAdapter;
import com.feng.freader.base.BaseFragment;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.constant.EventBusCode;
import com.feng.freader.db.DatabaseManager;
import com.feng.freader.entity.data.BookshelfNovelDbData;
import com.feng.freader.entity.eventbus.Event;
import com.feng.freader.util.FileUtil;
import com.feng.freader.util.RecyclerViewUtil;
import com.feng.freader.util.StatusBarUtil;
import com.feng.freader.view.activity.ReadActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/20
 */
public class BookshelfFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG = "BookshelfFragment";

    private RecyclerView mBookshelfNovelsRv;
    private TextView mLocalAddTv;
    private ImageView mLocalAddIv;

    private List<BookshelfNovelDbData> mDataList = new ArrayList<>();
    private DatabaseManager mDbManager;
    private BookshelfNovelsAdapter mBookshelfNovelsAdapter;

    @Override
    protected void doInOnCreate() {
        StatusBarUtil.setLightColorStatusBar(getActivity());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected void initData() {
        mDbManager = DatabaseManager.getInstance();
        mDataList = mDbManager.queryAllBookshelfNovel();
    }

    @Override
    protected void initView() {
        initBookshelfNovelsRv();

        mLocalAddTv = getActivity().findViewById(R.id.tv_bookshelf_add);
        mLocalAddTv.setOnClickListener(this);
        mLocalAddIv = getActivity().findViewById(R.id.iv_bookshelf_add);
        mLocalAddIv.setOnClickListener(this);
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
            case EventBusCode.BOOKSHELF_UPDATE_LIST:
                updateList();
                break;
            default:
                break;
        }
    }

    private void initBookshelfNovelsRv() {
        mBookshelfNovelsRv = getActivity().findViewById(R.id.rv_bookshelf_bookshelf_novels_list);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mBookshelfNovelsRv.setLayoutManager(gridLayoutManager);
        initAdapter();
        mBookshelfNovelsRv.setAdapter(mBookshelfNovelsAdapter);
    }

    private void initAdapter() {
        mBookshelfNovelsAdapter = new BookshelfNovelsAdapter(getActivity(), mDataList);
        mBookshelfNovelsAdapter.setBookshelfNovelListener(new BookshelfNovelsAdapter.BookshelfNovelListener() {
            @Override
            public void clickItem(int position) {
                Intent intent = new Intent(getActivity(), ReadActivity.class);
                // 小说 url
                intent.putExtra(ReadActivity.KEY_NOVEL_URL, mDataList.get(position).getNovelUrl());
                // 小说名
                intent.putExtra(ReadActivity.KEY_NAME, mDataList.get(position).getName());
                // 小说封面 url
                intent.putExtra(ReadActivity.KEY_COVER, mDataList.get(position).getCover());
                // 小说类型
                intent.putExtra(ReadActivity.KEY_TYPE, mDataList.get(position).getType());
                // 开始阅读的位置
                intent.putExtra(ReadActivity.KEY_CHAPTER_INDEX, mDataList.get(position).getChapterIndex());
                intent.putExtra(ReadActivity.KEY_POSITION, mDataList.get(position).getPosition());
                startActivity(intent);
            }
        });
    }

    /**
     * 更新列表信息
     */
    private void updateList() {
        mDataList.clear();
        mDataList.addAll(mDbManager.queryAllBookshelfNovel());
        Log.d(TAG, "updateList: list = " + mDataList);
        mBookshelfNovelsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bookshelf_add:
            case R.id.iv_bookshelf_add:
                // 导入本机小说
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");      // 最近文件（任意类型）
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) { // 选择了才继续
            Uri uri = data.getData();
            String filePath = FileUtil.uri2FilePath(getActivity(), uri);
            File file = new File(filePath);
            String fileName = file.getName();
            Log.d(TAG, "onActivityResult: fileLen = " + file.length());
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);  // 后缀名
            if (suffix.equals("txt")) {
                if (mDbManager.isExistInBookshelfNovel(filePath)) {
                    showShortToast("该小说已导入");
                    return;
                }
                if (file.length() > 100000000) {
                    showShortToast("文件过大");
                    return;
                }
                // 将该小说的数据存入数据库
                BookshelfNovelDbData dbData = new BookshelfNovelDbData(filePath, file.getName(),
                        "", 0, 0, 1);
                mDbManager.insertBookshelfNovel(dbData);
                // 更新列表
                updateList();
            } else {
                showShortToast("不支持该类型");
            }
        }
    }
}
