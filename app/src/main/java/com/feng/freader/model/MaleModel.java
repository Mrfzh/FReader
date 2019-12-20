package com.feng.freader.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.feng.freader.constant.Constant;
import com.feng.freader.constract.IMaleContract;
import com.feng.freader.entity.bean.CategoryNovelsBean;
import com.feng.freader.entity.bean.HotRankBean;
import com.feng.freader.entity.data.DiscoveryNovelData;
import com.feng.freader.entity.data.HotRankData;
import com.feng.freader.http.OkhttpCall;
import com.feng.freader.http.OkhttpUtil;
import com.feng.freader.http.UrlObtainer;
import com.feng.freader.httpUrlUtil.HttpUrlRequestBuilder;
import com.feng.freader.httpUrlUtil.Request;
import com.feng.freader.httpUrlUtil.Response;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/6
 */
public class MaleModel implements IMaleContract.Model {
    private static final String TAG = "MaleModel";

    private IMaleContract.Presenter mPresenter;
    private Gson mGson = new Gson();

    private CountDownLatch mCountDownLatch;     // 用于实现主线程在所有子线程执行完毕后再执行
    private List<String> mErrorMsgList = new ArrayList<>();  // 记录网络请求的错误信息

    private List<String> mRankNameList = Constant.MALE_HOT_RANK_NAME;            // 排行榜名称
    private List<HotRankData.NovelInfo> mNovelInfoList = new ArrayList<>();  // 小说信息

    public MaleModel(IMaleContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    /**
     * 获取热门榜单信息
     */
    @Override
    public void getHotRankData() {
        for (int i = 0; i < Constant.MALE_HOT_RANK_NUM; i++) {
            mNovelInfoList.add(new HotRankData.NovelInfo());
        }
        // 开启多个线程进行请求
        mCountDownLatch = new CountDownLatch(Constant.MALE_HOT_RANK_NUM);
        mErrorMsgList.clear();
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

    /**
     * 获取发现页的分类小说数据
     */
    @Override
    public void getCategoryNovels() {
        final List<DiscoveryNovelData> dataList = new ArrayList<>();
        final int[] finishCount = {0};
        final int n = 3;  // 类别数
        final int num = 6;    // 获取条数（最终得到的可能多于 6 条）
        List<String> majorList = Arrays.asList(Constant.CATEGORY_MAJOR_XH,
                Constant.CATEGORY_MAJOR_DS, Constant.CATEGORY_MAJOR_WX);
        for (int i = 0; i < n; i++) {
            String url = UrlObtainer.getCategoryNovels(Constant.CATEGORY_GENDER_MALE,
                    majorList.get(i), num);
            Log.d(TAG, "getCategoryNovels: i = " + i +
                    ", url = " + url);
            dataList.add(null);
            final int finalI = i;
            OkhttpUtil.getRequest(url, new OkhttpCall() {
                @Override
                public void onResponse(String json) {   // 得到 json 数据
                    finishCount[0]++;
                    CategoryNovelsBean bean = mGson.fromJson(json, CategoryNovelsBean.class);
                    if (bean.isOk()) {
                        DiscoveryNovelData discoveryNovelData = new DiscoveryNovelData();
                        List<CategoryNovelsBean.BooksBean> books = bean.getBooks();
                        List<String> novelNameList = new ArrayList<>();
                        List<String> coverUrlList = new ArrayList<>();
                        for (int j = 0; j < Math.min(books.size(), num); j++) {
                            novelNameList.add(books.get(j).getTitle());
                            coverUrlList.add("statics.zhuishushenqi.com" + books.get(j).getCover());
                        }
                        discoveryNovelData.setNovelNameList(novelNameList);
                        discoveryNovelData.setCoverUrlList(coverUrlList);
                        dataList.set(finalI, discoveryNovelData);
                    }
                    if (finishCount[0] == n) {
                        boolean hasFinished = true;
                        for (int j = 0; j < n; j++) {
                            if (dataList.get(j) == null) {
                                hasFinished = false;
                                mPresenter.getCategoryNovelsError("获取分类小说失败");
                                break;
                            }
                        }
                        if (hasFinished) {
                            mPresenter.getCategoryNovelsSuccess(dataList);
                        }
                    }
                }

                @Override
                public void onFailure(String errorMsg) {
                    finishCount[0]++;
                    Log.d(TAG, "getCategoryNovels onFailure: " + errorMsg);
                    if (finishCount[0] == n) {
                        mPresenter.getCategoryNovelsError("获取分类小说失败");
                    }
                }
            });
        }
    }

    private void doAfterRequest() {
        // 判断请求是否成功
        if (mErrorMsgList.isEmpty()) {
            mPresenter.getHotRankDataSuccess(new HotRankData(mRankNameList, mNovelInfoList));
        } else {
            mPresenter.getHotRankDataError(mErrorMsgList);
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

                        mCountDownLatch.countDown();
                    }

                    @Override
                    public void error(String errorMsg) {
                        mErrorMsgList.add(errorMsg);
                        mCountDownLatch.countDown();
                    }
                })
                .build()
                .doRequest();
    }
}
