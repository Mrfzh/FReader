package com.feng.freader.view.activity;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.adapter.ScreenAdapter;
import com.feng.freader.base.BaseActivity;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.constant.Constant;
import com.feng.freader.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AllNovelActivity extends BaseActivity implements View.OnClickListener{

    public static final String KEY_GENDER = "KEY_GENDER";
    public static final String KEY_MAJOR = "KEY_MAJOR";

    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mScreenIv;
    private RecyclerView mNovelListRv;

    private RelativeLayout mScreenRv;
    private RecyclerView mGenderRv;
    private RecyclerView mMajorRv;
    private RecyclerView mMinorRv;
    private RecyclerView mTypeRv;
    private TextView mCancelTv;
    private TextView mEnsureTv;

    private HashMap<String, List<String>> mMajorMap = new HashMap<>();  // 根据 major 得到相应的 minor
    private HashMap<Integer, List<String>> mGenderMap = new HashMap<>(); // 根据 gender 得到相应的 major
    private List<String> mGenderList = new ArrayList<>();   // 根据索引获得相应的 gender
    private List<String> mTypeList = new ArrayList<>();     // 根据索引获得相应的 type
    // 选中的索引或文字
    private int mGender;
    private String mMajor;
    private String mMinor;
    private int mType;
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

    @Override
    protected void doBeforeSetContentView() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_novel;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initData() {
        // 初始化 Map 和 List
        initMapAndList();
        // 初始化选中的项
        mGender = getIntent().getIntExtra(KEY_GENDER, 0);
        String major = getIntent().getStringExtra(KEY_MAJOR);
        mMajor = major == null? Constant.CATEGORY_MAJOR_XH : major;
        mMinor = mMajorMap.containsKey(mMajor)? mMajorMap.get(mMajor).get(0) : "";
        mType = 0;
        // 初始化 Adapter
        initAdapter();
    }

    private void initMapAndList() {
        mMajorMap.put(Constant.CATEGORY_MAJOR_XH, Arrays.asList("东方玄幻","异界大陆","异界争霸","远古神话"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_QH, Arrays.asList("西方奇幻","领主贵族","亡灵异族","魔法校园"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_WX, Arrays.asList("传统武侠","新派武侠","国术武侠"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_XX, Arrays.asList("古典仙侠","幻想修仙","现代修仙","洪荒封神"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_DS, Arrays.asList("都市生活","爱情婚姻","异术超能","恩怨情仇","青春校园","现实百态"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_ZC, Arrays.asList("娱乐明星","官场沉浮","商场职场"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_LS, Arrays.asList("穿越历史","架空历史","历史传记"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_JS, Arrays.asList("军事战争","战争幻想","谍战特工","军旅生涯","抗战烽火"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_YX, Arrays.asList("游戏生涯","电子竞技","虚拟网游","游戏异界"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_JJ, Arrays.asList("体育竞技","篮球运动","足球运动","棋牌桌游"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_KH, Arrays.asList("星际战争","时空穿梭","未来世界","古武机甲","超级科技","进化变异","末世危机"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_LY, Arrays.asList("推理侦探","恐怖惊悚","悬疑探险","灵异奇谈"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_TR, Arrays.asList("武侠同人","影视同人","动漫同人","游戏同人","小说同人"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_GDYQ, Arrays.asList("穿越时空","古代历史","古典架空","宫闱宅斗","经商种田"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_XDYQ, Arrays.asList("豪门总裁","都市生活","婚恋情感","商战职场","异术超能"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_CA, Arrays.asList("古代纯爱","现代纯爱"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_XHQH, Arrays.asList("玄幻异世","奇幻魔法"));
        mMajorMap.put(Constant.CATEGORY_MAJOR_WXXX, Arrays.asList("武侠","仙侠"));

        mGenderMap.put(0, Arrays.asList(Constant.CATEGORY_MAJOR_XH, Constant.CATEGORY_MAJOR_QH,
                Constant.CATEGORY_MAJOR_WX, Constant.CATEGORY_MAJOR_XX, Constant.CATEGORY_MAJOR_DS, Constant.CATEGORY_MAJOR_ZC,
                Constant.CATEGORY_MAJOR_LS, Constant.CATEGORY_MAJOR_JS, Constant.CATEGORY_MAJOR_YX, Constant.CATEGORY_MAJOR_JJ,
                Constant.CATEGORY_MAJOR_KH, Constant.CATEGORY_MAJOR_LY, Constant.CATEGORY_MAJOR_TR, Constant.CATEGORY_MAJOR_QXS));
        mGenderMap.put(1, Arrays.asList( Constant.CATEGORY_MAJOR_GDYQ, Constant.CATEGORY_MAJOR_XDYQ,
                Constant.CATEGORY_MAJOR_QCXY, Constant.CATEGORY_MAJOR_CA, Constant.CATEGORY_MAJOR_XHQH,
                Constant.CATEGORY_MAJOR_WXXX));
        mGenderMap.put(2, Arrays.asList(Constant.CATEGORY_MAJOR_CBXS, Constant.CATEGORY_MAJOR_ZJMZ,
                Constant.CATEGORY_MAJOR_CGLZ, Constant.CATEGORY_MAJOR_RWSK, Constant.CATEGORY_MAJOR_JGLC, Constant.CATEGORY_MAJOR_SHSS,
                Constant.CATEGORY_MAJOR_YEJK, Constant.CATEGORY_MAJOR_QCYQ, Constant.CATEGORY_MAJOR_WWYB, Constant.CATEGORY_MAJOR_ZZJS));

        mGenderList = Arrays.asList(Constant.CATEGORY_GENDER_MALE, Constant.CATEGORY_GENDER_FEMALE, Constant.CATEGORY_GENDER_PRESS);

        mTypeList = Arrays.asList(Constant.CATEGORY_TYPE_HOT, Constant.CATEGORY_TYPE_NEW, Constant.CATEGORY_TYPE_REPUTATION,
                Constant.CATEGORY_TYPE_OVER);
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
        mGenderAdapter = new ScreenAdapter(this, mGenderTextList, mGenderSelectedList);

        // major
        mMajorTextList = mGenderMap.get(mGender);
        for (int i = 0; i < mMajorTextList.size(); i++) {
            if (mMajor.equals(mMajorTextList.get(i))) {
                mMajorSelectedList.add(true);
            } else {
                mMajorSelectedList.add(false);
            }
        }
        mMajorAdapter = new ScreenAdapter(this, mMajorTextList, mMajorSelectedList);

        // minor
        if (!mMinor.equals("")) {
            mMinorTextList = mMajorMap.get(mMajor);
            for (int i = 0; i < mMinorTextList.size(); i++) {
                if (mMinor.equals(mMinorTextList.get(i))) {
                    mMinorSelectedList.add(true);
                } else {
                    mMinorSelectedList.add(false);
                }
            }
            mMinorAdapter = new ScreenAdapter(this, mMinorTextList, mMinorSelectedList);
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
        mTypeAdapter = new ScreenAdapter(this, mTypeTextList, mTypeSelectedList);
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

        mTypeRv = findViewById(R.id.rv_all_novel_type);
        LinearLayoutManager typeManager = new LinearLayoutManager(this);
        typeManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTypeRv.setLayoutManager(typeManager);
        mTypeRv.setAdapter(mTypeAdapter);

        mCancelTv = findViewById(R.id.tv_all_novel_screen_cancel);
        mCancelTv.setOnClickListener(this);
        mEnsureTv = findViewById(R.id.tv_all_novel_screen_ensure);
        mEnsureTv.setOnClickListener(this);
    }

    @Override
    protected void doAfterInit() {
        StatusBarUtil.setDarkColorStatusBar(this);
        getWindow().setStatusBarColor(getResources().getColor(R.color.all_novel_top_bar_bg));
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
                    mScreenRv.setVisibility(View.VISIBLE);
                } else {
                    mScreenRv.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_all_novel_screen_cancel:
                break;
            case R.id.tv_all_novel_screen_ensure:
                break;
            default:
                break;
        }
    }
}
