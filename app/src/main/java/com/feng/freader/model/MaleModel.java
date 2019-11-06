package com.feng.freader.model;

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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/6
 */
public class MaleModel implements IMaleContract.Model {
    private static final String TAG = "fzh";

    private IMaleContract.Presenter mPresenter;
    private Gson mGson = new Gson();

    private List<String> mRankNameList = Constant.MALE_HOT_RANK_NAME;            // 排行榜名称
    private List<HotRankData.NovelInfo> mNovelInfoList = new ArrayList<>();  // 小说信息
    private static AtomicInteger mClock = new AtomicInteger(0);

    public MaleModel(IMaleContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void getHotRankData() {
        for (int i = 0; i < Constant.MALE_HOT_RANK_NUM; i++) {
            mNovelInfoList.add(new HotRankData.NovelInfo());
        }
        // 在进行请求前，重置时钟
        mClock.set(0);
        // TODO 确认终止其他请求

        // 进行请求
        for (int i = 0; i < Constant.MALE_HOT_RANK_NUM; i++) {
            request(mNovelInfoList.get(i), UrlObtainer.getRankNovels(Constant.MALE_HOT_RANK_ID.get(i)));
        }
    }

    private void callback(String errorMsg) {
        if (errorMsg.equals("")) {
            Log.d(TAG, "callback: run 1, mClock = " + mClock.get());
            if (mClock.incrementAndGet() == Constant.MALE_HOT_RANK_NUM) {
                Log.d(TAG, "callback: run 2");
                mPresenter.getHotRankDataSuccess(new HotRankData(mRankNameList, mNovelInfoList));
                mClock.set(0);
            }
        } else {
            Log.d(TAG, "callback: run 3");
            mPresenter.getHotRankDataError(errorMsg);
            mClock.set(0);
            // TODO 终止其他正在进行的网络请求
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

                        callback("");
                    }

                    @Override
                    public void error(String errorMsg) {
                        callback(errorMsg);
                    }
                })
                .build()
                .doRequest();
    }
}
