package com.feng.freader.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.feng.freader.constant.Constant;
import com.feng.freader.constract.IReadContract;
import com.feng.freader.entity.bean.CatalogBean;
import com.feng.freader.entity.bean.DetailedChapterBean;
import com.feng.freader.entity.data.DetailedChapterData;
import com.feng.freader.entity.epub.EpubData;
import com.feng.freader.entity.epub.OpfData;
import com.feng.freader.http.OkhttpCall;
import com.feng.freader.http.OkhttpUtil;
import com.feng.freader.util.EpubUtils;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParserException;

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

    private OpfData mOpfData;

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

    /**
     * 从 epub 文件中读取到 Opf 文件信息
     *
     * @param filePath epub 路径
     */
    @Override
    public void getOpfData(final String filePath) {
        File file = new File(filePath);
        final String savePath = Constant.EPUB_SAVE_PATH + "/" + file.getName();
        File saveFile = new File(savePath);
        if (saveFile.exists()) {
            getOpfDataImpl(savePath);
            if (mOpfData != null) {
                getOpfDataSuccess(mOpfData);
            }
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EpubUtils.unZip(filePath, savePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    getOpfDataError("解压失败，可能文件被加密");
                    return;
                }
                getOpfDataImpl(savePath);
                if (mOpfData != null) {
                    getOpfDataSuccess(mOpfData);
                }
            }
        }).start();
    }

    private void getOpfDataImpl(String savePath) {
        try {
            // 先得到 opf 文件的位置
            String opfPath = EpubUtils.getOpfPath(savePath);
            Log.d(TAG, "unZipEpub: opfPath = " + opfPath);
            // 解析 opf 文件
            mOpfData = EpubUtils.parseOpf(opfPath);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            getOpfDataError("Xml 解析出错");
        } catch (IOException e) {
            e.printStackTrace();
            getOpfDataError("I/O 错误");
        }
    }

    private void getOpfDataError(final String errorMsg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPresenter.getOpfDataError(errorMsg);
            }
        });
    }

    private void getOpfDataSuccess(final OpfData opfData) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPresenter.getOpfDataSuccess(opfData);
            }
        });
    }

    /**
     * 解析 html/xhtml 文件，得到 Epub 章节数据
     *
     * @param filePath 读取的文件路径
     */
    @Override
    public void getEpubChapterData(String filePath) {
        try {
            File file = new File(filePath);
            Document document = Jsoup.parse(file, null);
            Elements allElements = document.getAllElements();
            final List<EpubData> dataList = new ArrayList<>();
            StringBuilder builder = new StringBuilder();    // 存放文本
            for (Element element : allElements) {
                // 该值大于 1 时，表示内部还有其他标签，跳过
                if (element.getAllElements().size() > 1) {
                    continue;
                }
                // <div> 获取一个段落
                if (element.is("div")) {
                    if (element.text().equals("")) {
                        continue;
                    }
                    builder.append(element.text());
                    builder.append("\n");
                }
                // <p> 获取一个段落
                else if (element.is("p")) {
                    if (element.text().equals("")) {
                        continue;
                    }
                    builder.append(element.text());
                    builder.append("\n");
                }
                // <img> 获取图片地址
                else if (element.is("img")) {
                    if (element.attr("src").equals("")) {
                        continue;
                    }
                    if (!builder.toString().equals("")) {
                        EpubData epubData = new EpubData(builder.toString(), EpubData.TYPE.TEXT);
                        dataList.add(epubData);
                        builder = new StringBuilder();
                    }
                    String picPath = file.getParent() + "/" + element.attr("src");
                    EpubData epubData = new EpubData(picPath, EpubData.TYPE.IMG);
                    dataList.add(epubData);
                }
                // <a> 获取超链接
                else if (element.is("a")) {
                    if (element.text().equals("")) {
                        continue;
                    }
                    if (!builder.toString().equals("")) {
                        EpubData epubData = new EpubData(builder.toString(), EpubData.TYPE.TEXT);
                        dataList.add(epubData);
                        builder = new StringBuilder();
                    }
                    EpubData epubData = new EpubData(element.text(), EpubData.TYPE.LINK);
                    dataList.add(epubData);
                }
            }
            if (!builder.toString().equals("")) {
                EpubData epubData = new EpubData(builder.toString(), EpubData.TYPE.TEXT);
                dataList.add(epubData);
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mPresenter.getEpubChapterDataSuccess(dataList);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mPresenter.getEpubChapterDataError("解析 html/xhtml：I/O 错误");
                }
            });
        }
    }
}
