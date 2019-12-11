package com.feng.freader.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.feng.freader.constant.Constant;
import com.feng.freader.constract.IReadContract;
import com.feng.freader.entity.bean.CatalogBean;
import com.feng.freader.entity.bean.DetailedChapterBean;
import com.feng.freader.entity.data.DetailedChapterData;
import com.feng.freader.http.OkhttpCall;
import com.feng.freader.http.OkhttpUtil;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/25
 */
public class ReadModel implements IReadContract.Model {
    private static final String TAG = "ReadModel";

    private IReadContract.Presenter mPresenter;
    private Gson mGson;

    public ReadModel(IReadContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mGson = new Gson();
    }

    @Override
    public void getChapterUrlList(String url) {
        OkhttpUtil.getRequest(url, new OkhttpCall() {
            @Override
            public void onResponse(String json) {   // 得到 json 数据
                CatalogBean bean = mGson.fromJson(json, CatalogBean.class);
                if (bean.getCode() != 0) {
                    mPresenter.getChapterUrlListError("未找到相关数据");
                    return;
                }
                List<String> chapterUrlList = new ArrayList<>();
                List<CatalogBean.ListBean> list = bean.getList();
                for (CatalogBean.ListBean item : list) {
                    chapterUrlList.add(item.getUrl());
                }
                mPresenter.getChapterUrlListSuccess(chapterUrlList);
            }

            @Override
            public void onFailure(String errorMsg) {
                mPresenter.getChapterUrlListError(errorMsg);
            }
        });
    }

    @Override
    public void getDetailedChapterData(String url) {
        OkhttpUtil.getRequest(url, new OkhttpCall() {
            @Override
            public void onResponse(String json) {   // 得到 json 数据
                DetailedChapterBean bean = mGson.fromJson(json, DetailedChapterBean.class);
                if (bean.getCode() != 0) {
                    mPresenter.getDetailedChapterDataError("未找到相关数据");
                    return;
                }
                StringBuilder contentBuilder = new StringBuilder();
                contentBuilder.append("    ");
                for (String s : bean.getContent()) {
                    contentBuilder.append(s);
                    contentBuilder.append("\n");
                }
                DetailedChapterData data = new DetailedChapterData(bean.getNum(),
                        contentBuilder.toString());
                mPresenter.getDetailedChapterDataSuccess(data);
            }

            @Override
            public void onFailure(String errorMsg) {
                mPresenter.getDetailedChapterDataError(errorMsg);
            }
        });
    }

    @Override
    public void loadTxt(final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(filePath);
                BufferedReader br = null;
                StringBuilder builder = null;
                String error = "";
                try {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"));
                    builder = new StringBuilder();
                    String str;
                    while ((str = br.readLine()) != null) {
                        builder.append(str);
                        builder.append("\n");
                    }
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "e1 = " + e.getMessage());
                    e.printStackTrace();
                    error = Constant.NOT_FOUND_FROM_LOCAL;
                } catch (UnsupportedEncodingException e) {
                    Log.d(TAG, "e2 = " + e.getMessage());
                    e.printStackTrace();
                    error = e.getMessage();
                } catch (IOException e) {
                    Log.d(TAG, "e3 = " + e.getMessage());
                    e.printStackTrace();
                    error = e.getMessage();
                } finally {
                    try {
                        if (br != null) {
                            br.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                final String finalError = error;
                final String text =  builder == null? "" : builder.toString();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (finalError.equals("")) {
                            mPresenter.loadTxtSuccess(text);
                        } else {
                            mPresenter.loadTxtError(finalError);
                        }
                    }
                });
            }
        }).start();
    }
}
