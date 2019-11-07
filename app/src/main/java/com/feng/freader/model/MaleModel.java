package com.feng.freader.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.feng.freader.constant.Constant;
import com.feng.freader.constract.IMaleContract;
import com.feng.freader.entity.bean.HotRankBean;
import com.feng.freader.entity.data.HotRankData;
import com.feng.freader.http.UrlObtainer;
import com.feng.freader.httpUrlUtil.HttpUrlRequestBuilder;
import com.feng.freader.httpUrlUtil.Request;
import com.feng.freader.httpUrlUtil.Response;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/6
 */
public class MaleModel implements IMaleContract.Model {
    private static final String TAG = "fzh";

    private IMaleContract.Presenter mPresenter;
    private Gson mGson = new Gson();

    private CountDownLatch mCountDownLatch;     // 用于实现主线程在所有子线程执行完毕后再执行
    private String mReturnMsg = "";             // 记录网络请求的状态信息

    private List<String> mRankNameList = Constant.MALE_HOT_RANK_NAME;            // 排行榜名称
    private List<HotRankData.NovelInfo> mNovelInfoList = new ArrayList<>();  // 小说信息

    public MaleModel(IMaleContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void getHotRankData() {
        for (int i = 0; i < Constant.MALE_HOT_RANK_NUM; i++) {
            mNovelInfoList.add(new HotRankData.NovelInfo());
        }
        // 开启多个线程进行请求
        mCountDownLatch = new CountDownLatch(Constant.MALE_HOT_RANK_NUM);
        mReturnMsg = "";
        for (int i = 0; i < Constant.MALE_HOT_RANK_NUM; i++) {
            request(mNovelInfoList.get(i), UrlObtainer.getRankNovels(Constant.MALE_HOT_RANK_ID.get(i)));
        }
        // 等待请求线程完成
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 因为阻塞了主线程的话，主线程就不能处理消息了，所以改为阻塞子线程
                    mCountDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 切换主线程进行后续处理
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        doAfterRequest();
                    }
                });
            }
        }).start();
    }

    private void doAfterRequest() {
        // 判断请求是否成功
        if (mReturnMsg.equals("")) {
            mPresenter.getHotRankDataSuccess(new HotRankData(mRankNameList, mNovelInfoList));
        } else {
            mPresenter.getHotRankDataError(mReturnMsg);
        }
    }


    /**
     * 进行网络请求，得到 HotRankBean 并转化为 NovelInfo
     *
     * @param novelInfo 存放 NovelInfo
     * @param url 要进行请求的 url
     */
    private void request(final HotRankData.NovelInfo novelInfo, String url) {
        // 构造请求
        Request request = new Request.Builder()
                .setUrl(url)
                .build();
        // 执行请求
        HttpUrlRequestBuilder.getInstance()
                .setRequest(request)
                .setResponse(new Response() {
                    @Override
                    public void success(String response) {
                        HotRankBean hotRankBean = mGson.fromJson(response, HotRankBean.class);
                        HotRankBean.RankingBean rankingBean = hotRankBean.getRanking();
                        List<HotRankBean.RankingBean.BooksBean> books = rankingBean.getBooks();

                        List<String> idList = new ArrayList<>();
                        List<String> nameList = new ArrayList<>();
                        List<String> shortInfoList = new ArrayList<>();
                        List<String> coverList = new ArrayList<>();
                        for (HotRankBean.RankingBean.BooksBean book : books) {
                            idList.add(book.get_id());
                            nameList.add(book.getTitle());
                            shortInfoList.add(book.getShortIntro());
                            coverList.add(book.getCover());
                        }
                        novelInfo.setIdList(idList);
                        novelInfo.setNameList(nameList);
                        novelInfo.setShortInfoList(shortInfoList);
                        novelInfo.setCoverList(coverList);

                        Log.d(TAG, "success: run 1");
                        mCountDownLatch.countDown();
                    }

                    @Override
                    public void error(String errorMsg) {
                        Log.d(TAG, "error: run 1");
                        mReturnMsg = errorMsg;
                        mCountDownLatch.countDown();
                    }
                })
                .build()
                .doRequest();
    }
}
