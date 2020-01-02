package com.feng.freader.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.adapter.EmptyAdapter;
import com.feng.freader.adapter.NovelAdapter;
import com.feng.freader.adapter.ScreenAdapter;
import com.feng.freader.base.BaseActivity;
import com.feng.freader.base.BasePagingLoadAdapter;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.constant.Constant;
import com.feng.freader.constract.IAllNovelContract;
import com.feng.freader.entity.data.ANNovelData;
import com.feng.freader.entity.data.RequestCNData;
import com.feng.freader.presenter.AllNovelPresenter;
import com.feng.freader.util.StatusBarUtil;
import com.feng.freader.widget.LoadMoreScrollListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AllNovelActivity extends BaseActivity<AllNovelPresenter>
        implements View.OnClickListener, IAllNovelContract.View {

    private static final String TAG = "AllNovelActivity";
    public static final String KEY_GENDER = "KEY_GENDER";
    public static final String KEY_MAJOR = "KEY_MAJOR";

    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mScreenIv;
    private RecyclerView mNovelListRv;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mRefreshSrv;

    private View mFrontBgV;
    private RelativeLayout mScreenRv;
    private RecyclerView mGenderRv;
    private RecyclerView mMajorRv;
    private RecyclerView mMinorRv;
    private RecyclerView mTypeRv;
    private TextView mCancelTv;
    private TextView mEnsureTv;

    private HashMap<String, List<String>> mMinorMap = new HashMap<>();  // 根据 major 得到相应的 minor
    private List<List<String>> mMajorList = new ArrayList<>(); // 根据索引得到相应的 major
    private List<String> mGenderList = new ArrayList<>();   // 根据索引获得相应的 gender
    private List<String> mTypeList = new ArrayList<>();     // 根据索引获得相应的 type
    // 选中的索引或文字
    private int mGender;
    private String mMajor;
    private String mMinor;
    private int mType;
    private int mTempGender;
    private String mTempMajor;
    private String mTempMinor;
    private int mTempType;
    // 列表相关
    private ScreenAdapter mGenderAdapter;
    private ScreenAdapter mMajorAdapter;
    private ScreenAdapter mMinorAdapter;
    private ScreenAdapter mTypeAdapter;
    private List<String> mGenderTextList = new ArrayList<>();
    private List<Boolean> mGenderSelectedList = new ArrayList<>();
    private List<String> mMajorTextList = new ArrayList<>();
    private List<Boolean> mMajorSelectedList = new ArrayList<>();
    private List<String> mMinorTextList = new ArrayList<>();
    private List<Boolean> mMinorSelectedList = new ArrayList<>();
    private List<String> mTypeTextList = new ArrayList<>();
    private List<Boolean> mTypeSelectedList = new ArrayList<>();
    private NovelAdapter mNovelAdapter;
    private List<ANNovelData> mDataList;
    
    private boolean mIsSearching = false;
    
    // 分页加载相关
    private int mCurrStart = 0;         // 开始请求
    private boolean mIsToEnd = false;   // 是否已经到底底部
    private boolean mIsLoadingMore = false; // 是否正在加载更多

    @Override
    protected void doBeforeSetContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_novel;
    }

    @Override
    protected AllNovelPresenter getPresenter() {
        return new AllNovelPresenter();
    }

    @Override
    protected void initData() {
        // 初始化 Map 和 List
        initMapAndList();
        Log.d(TAG, "initData: mMajorList = " + mMajorList);
        // 初始化选中的项
        mGender = getIntent().getIntExtra(KEY_GENDER, 0);
        String major = getIntent().getStringExtra(KEY_MAJOR);
        mMajor = major == null? Constant.CATEGORY_MAJOR_XH : major;
        mMinor = mMinorMap.containsKey(mMajor)? mMinorMap.get(mMajor).get(0) : "";
        mType = 0;
        // 初始化 Adapter
        initAdapter();
    }

    private void initMapAndList() {
        mMinorMap.put(Constant.CATEGORY_MAJOR_XH, strings2List(new String[] {"东方玄幻","异界大陆","异界争霸","远古神话"}));
        mMinorMap.put(Constant.CATEGORY_MAJOR_QH, strings2List(new String[] {"西方奇幻","亡灵异族","魔法校园"}));
        mMinorMap.put(Constant.CATEGORY_MAJOR_WX, strings2List(new String[] {"传统武侠","新派武侠","国术武侠"}));
        mMinorMap.put(Constant.CATEGORY_MAJOR_XX, strings2List(new String[] {"古典仙侠","幻想修仙","现代修仙","洪荒封神"}));
        mMinorMap.put(Constant.CATEGORY_MAJOR_DS, strings2List(new String[] {"都市生活","异术超能","青春校园"}));
        mMinorMap.put(Constant.CATEGORY_MAJOR_ZC, strings2List(new String[] {"娱乐明星","商场职场"}));
        mMinorMap.put(Constant.CATEGORY_MAJOR_LS, strings2List(new String[] {"穿越历史","架空历史","历史传记"}));
        mMinorMap.put(Constant.CATEGORY_MAJOR_JS, strings2List(new String[] {"军事战争","战争幻想","谍战特工","抗战烽火"}));
        mMinorMap.put(Constant.CATEGORY_MAJOR_YX, strings2List(new String[] {"电子竞技","虚拟网游","游戏异界"}));
        mMinorMap.put(Constant.CATEGORY_MAJOR_JJ, strings2List(new String[] {"体育竞技","篮球运动","足球运动"}));
        mMinorMap.put(Constant.CATEGORY_MAJOR_KH, strings2List(new String[] {"时空穿梭","未来世界","古武机甲","末世危机"}));
        mMinorMap.put(Constant.CATEGORY_MAJOR_LY, strings2List(new String[] {"推理侦探","悬疑探险"}));
        mMinorMap.put(Constant.CATEGORY_MAJOR_TR, strings2List(new String[] {"小说同人"}));
        mMinorMap.put(Constant.CATEGORY_MAJOR_GDYQ, strings2List(new String[] {"穿越时空","古典架空","宫闱宅斗","经商种田"}));
        mMinorMap.put(Constant.CATEGORY_MAJOR_XDYQ, strings2List(new String[] {"豪门总裁","都市生活","婚恋情感","异术超能"}));
        mMinorMap.put(Constant.CATEGORY_MAJOR_XHQH, strings2List(new String[] {"玄幻异世","奇幻魔法"}));
        mMinorMap.put(Constant.CATEGORY_MAJOR_WXXX, strings2List(new String[] {"仙侠"}));

        mMajorList.add(strings2List(new String[] {Constant.CATEGORY_MAJOR_XH, Constant.CATEGORY_MAJOR_QH,
                Constant.CATEGORY_MAJOR_WX, Constant.CATEGORY_MAJOR_XX, Constant.CATEGORY_MAJOR_DS, Constant.CATEGORY_MAJOR_ZC,
                Constant.CATEGORY_MAJOR_LS, Constant.CATEGORY_MAJOR_JS, Constant.CATEGORY_MAJOR_YX, Constant.CATEGORY_MAJOR_JJ,
                Constant.CATEGORY_MAJOR_KH, Constant.CATEGORY_MAJOR_LY, Constant.CATEGORY_MAJOR_TR, Constant.CATEGORY_MAJOR_QXS}));
        mMajorList.add(strings2List(new String[] {Constant.CATEGORY_MAJOR_GDYQ, Constant.CATEGORY_MAJOR_XDYQ,
                Constant.CATEGORY_MAJOR_QCXY, Constant.CATEGORY_MAJOR_XHQH,
                Constant.CATEGORY_MAJOR_WXXX}));
        mMajorList.add(strings2List(new String[] {Constant.CATEGORY_MAJOR_CBXS, Constant.CATEGORY_MAJOR_ZJMZ,
                Constant.CATEGORY_MAJOR_CGLZ, Constant.CATEGORY_MAJOR_RWSK, Constant.CATEGORY_MAJOR_JGLC, Constant.CATEGORY_MAJOR_SHSS,
                Constant.CATEGORY_MAJOR_YEJK, Constant.CATEGORY_MAJOR_QCYQ, Constant.CATEGORY_MAJOR_WWYB, Constant.CATEGORY_MAJOR_ZZJS}));

        mGenderList = strings2List(new String[] {Constant.CATEGORY_GENDER_MALE, Constant.CATEGORY_GENDER_FEMALE, Constant.CATEGORY_GENDER_PRESS});

        mTypeList = strings2List(new String[] {Constant.CATEGORY_TYPE_HOT, Constant.CATEGORY_TYPE_NEW, Constant.CATEGORY_TYPE_REPUTATION,
                Constant.CATEGORY_TYPE_OVER});
    }

    private List<String> strings2List(String[] strings) {
        List<String> list = new ArrayList<>();
        for (String s : strings) {
            list.add(s);
        }
        return list;
    }

    private void initAdapter() {
        // gender
        mGenderTextList = Arrays.asList(Constant.CATEGORY_GENDER_MALE_TEXT, Constant.CATEGORY_GENDER_FEMALE_TEXT,
                Constant.CATEGORY_GENDER_PRESS_TEXT);
        for (int i = 0; i < mGenderTextList.size(); i++) {
            if (i == mGender) {
                mGenderSelectedList.add(true);
            } else {
                mGenderSelectedList.add(false);
            }
        }
        mGenderAdapter = new ScreenAdapter(this, mGenderTextList, mGenderSelectedList,
                new ScreenAdapter.ScreenListener() {
            @Override
            public void clickItem(int position) {
                // 点击 gender，更新 major
                mTempGender = position;
                // 更新 gender
                mGenderSelectedList.clear();
                for (int i = 0; i < mGenderTextList.size(); i++) {
                    if (i == position) {
                        mGenderSelectedList.add(true);
                    } else {
                        mGenderSelectedList.add(false);
                    }
                }
                mGenderAdapter.notifyDataSetChanged();
                // 更新 major
                mMajorTextList.clear();
                Log.d(TAG, "clickItem: mMajorList = " + mMajorList);
                mMajorTextList.addAll(mMajorList.get(position));
                mMajorSelectedList.clear();
                for (int i = 0; i < mMajorTextList.size(); i++) {
                    if (i == 0) {
                        mMajorSelectedList.add(true);
                    } else {
                        mMajorSelectedList.add(false);
                    }
                }
                mTempMajor = mMajorTextList.get(0);
                mMajorAdapter.notifyDataSetChanged();
                // 更新 minor
                if (!mMinorMap.containsKey(mTempMajor)) {
                    mTempMinor = "";
                    mMinorRv.setVisibility(View.GONE);
                } else {
                    mMinorRv.setVisibility(View.VISIBLE);
                    mMinorTextList.clear();
                    mMinorTextList.addAll(mMinorMap.get(mTempMajor));
                    mTempMinor = mMinorTextList.get(0);
                    mMinorSelectedList.clear();
                    for (int i = 0; i < mMinorTextList.size(); i++) {
                        if (i == 0) {
                            mMinorSelectedList.add(true);
                        } else {
                            mMinorSelectedList.add(false);
                        }
                    }
                    mMinorAdapter.notifyDataSetChanged();
                }
            }
        });

        // major
         /* 注意，不能直接引用 mMajorList 的元素，不然之后清除 mMajorTextList 时会把 mMajorList 的元素也删掉
            后面初始化 mMinorTextList 时同理 */
        mMajorTextList = new ArrayList<>(mMajorList.get(mGender));
        for (int i = 0; i < mMajorTextList.size(); i++) {
            if (mMajor.equals(mMajorTextList.get(i))) {
                mMajorSelectedList.add(true);
            } else {
                mMajorSelectedList.add(false);
            }
        }
        mMajorAdapter = new ScreenAdapter(this, mMajorTextList, mMajorSelectedList,
                new ScreenAdapter.ScreenListener() {
            @Override
            public void clickItem(int position) {
                // 更新 major
                mMajorSelectedList.clear();
                for (int i = 0; i < mMajorTextList.size(); i++) {
                    if (i == position) {
                        mMajorSelectedList.add(true);
                    } else {
                        mMajorSelectedList.add(false);
                    }
                }
                mTempMajor = mMajorTextList.get(position);
                mMajorAdapter.notifyDataSetChanged();
                // 更新 minor
                if (!mMinorMap.containsKey(mTempMajor)) {
                    mTempMinor = "";
                    mMinorRv.setVisibility(View.GONE);
                } else {
                    mMinorRv.setVisibility(View.VISIBLE);
                    mMinorTextList.clear();
                    mMinorTextList.addAll(mMinorMap.get(mTempMajor));
                    mMinorSelectedList.clear();
                    for (int i = 0; i < mMinorTextList.size(); i++) {
                        if (i == 0) {
                            mMinorSelectedList.add(true);
                        } else {
                            mMinorSelectedList.add(false);
                        }
                    }
                    mTempMinor = mMinorTextList.get(0);
                    mMinorAdapter.notifyDataSetChanged();
                }
            }
        });

        // minor
        if (!mMinor.equals("")) {
            mMinorTextList = new ArrayList<>(mMinorMap.get(mMajor));
            for (int i = 0; i < mMinorTextList.size(); i++) {
                if (mMinor.equals(mMinorTextList.get(i))) {
                    mMinorSelectedList.add(true);
                } else {
                    mMinorSelectedList.add(false);
                }
            }
            mMinorAdapter = new ScreenAdapter(this, mMinorTextList, mMinorSelectedList,
                    new ScreenAdapter.ScreenListener() {
                        @Override
                        public void clickItem(int position) {
                            // 更新 minor
                            mMinorSelectedList.clear();
                            for (int i = 0; i < mMinorTextList.size(); i++) {
                                if (i == position) {
                                    mMinorSelectedList.add(true);
                                } else {
                                    mMinorSelectedList.add(false);
                                }
                            }
                            mTempMinor = mMinorTextList.get(position);
                            mMinorAdapter.notifyDataSetChanged();
                        }
                    });
        } else {
            mMinorAdapter = new ScreenAdapter(this, mMinorTextList, mMinorSelectedList,
                    new ScreenAdapter.ScreenListener() {
                        @Override
                        public void clickItem(int position) {
                            // 更新 minor
                            mMinorSelectedList.clear();
                            for (int i = 0; i < mMinorTextList.size(); i++) {
                                if (i == position) {
                                    mMinorSelectedList.add(true);
                                } else {
                                    mMinorSelectedList.add(false);
                                }
                            }
                            mTempMinor = mMinorTextList.get(position);
                            mMinorAdapter.notifyDataSetChanged();
                        }
                    });
        }

        // type
        mTypeTextList = Arrays.asList(Constant.CATEGORY_TYPE_HOT_TEXT, Constant.CATEGORY_TYPE_NEW_TEXT,
                Constant.CATEGORY_TYPE_REPUTATION_TEXT, Constant.CATEGORY_TYPE_OVER_TEXT);
        for (int i = 0; i < mTypeTextList.size(); i++) {
            if (i == mType) {
                mTypeSelectedList.add(true);
            } else {
                mTypeSelectedList.add(false);
            }
        }
        mTypeAdapter = new ScreenAdapter(this, mTypeTextList, mTypeSelectedList,
                new ScreenAdapter.ScreenListener() {
            @Override
            public void clickItem(int position) {
                // 更新 type
                mTypeSelectedList.clear();
                for (int i = 0; i < mTypeTextList.size(); i++) {
                    if (i == position) {
                        mTypeSelectedList.add(true);
                    } else {
                        mTypeSelectedList.add(false);
                    }
                }
                mTempType = position;
                mTypeAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void initView() {
        mBackIv = findViewById(R.id.iv_all_novel_back);
        mBackIv.setOnClickListener(this);
        mTitleTv = findViewById(R.id.tv_all_novel_title);
        mTitleTv.setText(mMajor);
        mScreenIv = findViewById(R.id.iv_all_novel_screen);
        mScreenIv.setOnClickListener(this);
        mNovelListRv = findViewById(R.id.rv_all_novel_novel_list);
        mNovelListRv.setLayoutManager(new LinearLayoutManager(this));
        mNovelListRv.addOnScrollListener(new LoadMoreScrollListener(new LoadMoreScrollListener.LoadMore() {
            @Override
            public void loadMore() {
                if (mNovelAdapter != null && !mIsToEnd) {
                    // 加载更多
                    mNovelAdapter.loadingMore();
                }
            }
        }));
        mNovelListRv.setAdapter(new EmptyAdapter());

        mProgressBar = findViewById(R.id.pb_all_novel);

        mFrontBgV = findViewById(R.id.v_all_novel_front_bg);
        mScreenRv = findViewById(R.id.rv_all_novel_screen);

        mGenderRv = findViewById(R.id.rv_all_novel_gender);
        LinearLayoutManager genderManager = new LinearLayoutManager(this);
        genderManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mGenderRv.setLayoutManager(genderManager);
        mGenderRv.setAdapter(mGenderAdapter);

        mMajorRv = findViewById(R.id.rv_all_novel_major);
        LinearLayoutManager majorManager = new LinearLayoutManager(this);
        majorManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMajorRv.setLayoutManager(majorManager);
        mMajorRv.setAdapter(mMajorAdapter);

        mMinorRv = findViewById(R.id.rv_all_novel_minor);
        LinearLayoutManager minorManager = new LinearLayoutManager(this);
        minorManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMinorRv.setLayoutManager(minorManager);
        mMinorRv.setAdapter(mMinorAdapter);
        if (mMinor.equals("")) {
            mMinorRv.setVisibility(View.GONE);
        }

        mTypeRv = findViewById(R.id.rv_all_novel_type);
        LinearLayoutManager typeManager = new LinearLayoutManager(this);
        typeManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTypeRv.setLayoutManager(typeManager);
        mTypeRv.setAdapter(mTypeAdapter);

        mCancelTv = findViewById(R.id.tv_all_novel_screen_cancel);
        mCancelTv.setOnClickListener(this);
        mEnsureTv = findViewById(R.id.tv_all_novel_screen_ensure);
        mEnsureTv.setOnClickListener(this);

        mRefreshSrv = findViewById(R.id.srv_all_novel_refresh);
        mRefreshSrv.setColorSchemeColors(getResources().getColor(R.color.colorAccent));   //设置颜色
        mRefreshSrv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新时的操作
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 请求小说信息
                        mCurrStart = 0;
                        requestNovels(false);
                    }
                }, 500);
            }
        });
    }

    @Override
    protected void doAfterInit() {
        StatusBarUtil.setDarkColorStatusBar(this);
        getWindow().setStatusBarColor(getResources().getColor(R.color.all_novel_top_bar_bg));
        // 请求小说信息
        requestNovels(true);
    }

    /**
     * 请求小说信息
     */
    private void requestNovels(boolean showProgressBar) {
        RequestCNData requestCNData = new RequestCNData();
        requestCNData.setGender(mGenderList.get(mGender));
        requestCNData.setMajor(mMajor);
        requestCNData.setMinor(mMinor);
        requestCNData.setType(mTypeList.get(mType));
        requestCNData.setStart(mCurrStart);
        requestCNData.setNum(Constant.NOVEL_PAGE_NUM);
        mIsSearching = true;
        if (showProgressBar && !mIsLoadingMore) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        mPresenter.getNovels(requestCNData);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_all_novel_back:
                finish();
                break;
            case R.id.iv_all_novel_screen:
                if (mScreenRv.getVisibility() == View.GONE) {
                    // 展开筛选栏
                    mFrontBgV.setVisibility(View.VISIBLE);
                    mFrontBgV.setClickable(true);
                    mFrontBgV.setFocusable(true);
                    mScreenRv.setVisibility(View.VISIBLE);
                    // 筛选前记录相关变量
                    mTempGender = mGender;
                    mTempMajor = mMajor;
                    mTempMinor = mMinor;
                    mTempType = mType;
                } else {
                    mFrontBgV.setVisibility(View.GONE);
                    mScreenRv.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_all_novel_screen_cancel:
                mFrontBgV.setVisibility(View.GONE);
                mScreenRv.setVisibility(View.GONE);
                break;
            case R.id.tv_all_novel_screen_ensure:
                mFrontBgV.setVisibility(View.GONE);
                mScreenRv.setVisibility(View.GONE);
                // 更新相关变量
                mGender = mTempGender;
                mMajor = mTempMajor;
                mMinor = mTempMinor;
                mType = mTempType;
                mCurrStart = 0;
                // 查找小说信息
                requestNovels(true);
                break;
            default:
                break;
        }
    }

    /**
     * 获取小说信息成功
     */
    @Override
    public void getNovelsSuccess(List<ANNovelData> dataList, boolean isEnd) {
        mProgressBar.setVisibility(View.GONE);
        mRefreshSrv.setRefreshing(false);
        mIsSearching = false;

        mIsToEnd = isEnd;
        // 更新列表数据
        if (mIsLoadingMore) {   // 加载更多
            mDataList.addAll(dataList);
            if (isEnd) {
                mNovelAdapter.setLastedStatus();
            }
            mNovelAdapter.updateList();
            mIsLoadingMore = false;
        } else {    // 第一次加载
            if (mNovelAdapter == null) {
                mDataList = dataList;
                mNovelAdapter = new NovelAdapter(this, mDataList,
                        new BasePagingLoadAdapter.LoadMoreListener() {
                            @Override
                            public void loadMore() {
                                // 加载下一页
                                mCurrStart += Constant.NOVEL_PAGE_NUM;
                                mIsLoadingMore = true;
                                requestNovels(true);
                            }
                        },
                        new NovelAdapter.NovelListener() {
                            @Override
                            public void clickItem(String novelName) {
                                if (mRefreshSrv.isRefreshing()) {
                                    return;
                                }
                                if (mIsSearching) {
                                    return;
                                }
                                Intent intent = new Intent(AllNovelActivity.this, SearchActivity.class);
                                // 传递小说名，进入搜查页后直接显示该小说的搜查结果
                                intent.putExtra(SearchActivity.KEY_NOVEL_NAME, novelName);
                                startActivity(intent);
                            }
                        });
                mNovelListRv.setAdapter(mNovelAdapter);
            } else {
                mDataList.clear();
                mDataList.addAll(dataList);
                mNovelAdapter.notifyDataSetChanged();
            }
        }

        // 更新标题
        String title = mMinor.equals("")? mMajor : mMinor;
        mTitleTv.setText(title);
    }

    /**
     * 获取小说信息失败
     */
    @Override
    public void getNovelsError(String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        mRefreshSrv.setRefreshing(false);
        mIsSearching = false;

        showShortToast("加载数据失败");
        Log.d(TAG, "getNovelsError: " + errorMsg);
        if (mIsLoadingMore) {
            mNovelAdapter.setErrorStatus();
            mIsLoadingMore = false;
        }
    }

}
