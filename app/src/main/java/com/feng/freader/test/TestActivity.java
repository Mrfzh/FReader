package com.feng.freader.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.feng.freader.R;
import com.feng.freader.base.BaseActivity;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.constant.Constant;
import com.feng.freader.db.DatabaseHelper;
import com.feng.freader.db.DatabaseManager;
import com.feng.freader.widget.FlowLayout;
import com.feng.freader.widget.TipDialog;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends BaseActivity {

    private static final String TAG = "TestActivity";

    private Button mCreateDbBtn;
    private Button mInsertBtn;
    private Button mSearchBtn;

    private DatabaseManager mManager;

    @Override
    protected void doBeforeSetContentView() {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //隐藏状态栏
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //隐藏标题栏
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
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
        mCreateDbBtn = findViewById(R.id.btn_test_create_db);
        mCreateDbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipDialog dialog = new TipDialog.Builder(TestActivity.this) // 传入 Context
                        .setTitle("注意")                         // 设置标题
                        .setContent("是否要清空历史搜索")            // 设置内容
                        .setEnsure("是")                         // 设置确定按钮的内容
                        .setCancel("不了")                        // 设置否定按钮的内容
                        .setOnClickListener(new TipDialog.OnClickListener() {   // 监听按钮的点击
                            @Override
                            public void clickEnsure() {
                                // 点击确定按钮
                            }

                            @Override
                            public void clickCancel() {
                                // 点击否定按钮
                            }
                        })
                        .build();   // 构建完毕
                dialog.show();      // 显示 Dialog
            }
        });

        mInsertBtn = findViewById(R.id.btn_test_insert);
        mInsertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除前 30 条数据
                mManager.deleteHistories(30);
                Log.d(TAG, "delete data");
            }
        });

        mSearchBtn = findViewById(R.id.btn_test_search);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查询表中所有数据
                Log.d(TAG, mManager.searchAllHistory().toString());
            }
        });
    }

    @Override
    protected void doAfterInit() {

    }

    @Override
    protected boolean isRegisterEventBus() {
        return false;
    }

}
