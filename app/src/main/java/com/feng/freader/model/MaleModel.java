package com.feng.freader.model;

import android.util.Log;

import com.feng.freader.constant.Constant;
import com.feng.freader.constract.IMaleContract;
import com.feng.freader.entity.bean.CategoryNovelsBean;
import com.feng.freader.entity.bean.HotRankBean;
import com.feng.freader.entity.data.DiscoveryNovelData;
import com.feng.freader.http.OkhttpBuilder;
import com.feng.freader.http.OkhttpCall;
import com.feng.freader.http.OkhttpUtil;
import com.feng.freader.http.UrlObtainer;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/6
 */
public class MaleModel implements IMaleContract.Model {
    private static final String TAG = "MaleModel";
    private static final int RANK_NOVEL_NUM = 3;     // 每个排行榜展示的小说数

    private IMaleContract.Presenter mPresenter;
    private Gson mGson = new Gson();

    public MaleModel(IMaleContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    /**
     * 获取热门榜单信息
     */
    @Override
    public void getHotRankData() {
        final List<List<String>> novelNameList = new ArrayList<>();
        final int[] findCount = {0};
        for (int i = 0; i < Constant.MALE_HOT_RANK_NUM; i++) {
            novelNameList.add(new ArrayList<String>());
            String url = UrlObtainer.getRankNovels(Constant.MALE_HOT_RANK_ID.get(i));
            final int finalI = i;
            OkhttpBuilder builder = new OkhttpBuilder.Builder()
                    .setUrl(url)
                    .setOkhttpCall(new OkhttpCall() {
                        @Override
                        public void onResponse(String json) {   // 得到 json 数据
                            HotRankBean bean = mGson.fromJson(json, HotRankBean.class);
                            if (bean.isOk()) {
                                List<HotRankBean.RankingBean.BooksBean> books = bean.getRanking().getBooks();
                                List<String> list = novelNameList.get(finalI);
                                for (int j = 0; j < Math.min(RANK_NOVEL_NUM, books.size()); j++) {
                                    list.add(books.get(j).getTitle());
                                }
                            }
                            findCount[0]++;
                            if (findCount[0] == Constant.MALE_HOT_RANK_NUM) {
                                boolean isSucceed = true;
                                for (int j = 0; j < novelNameList.size(); j++) {
                                    if (novelNameList.get(j).isEmpty()) {
                                        isSucceed = false;
                                    }
                                }
                                if (isSucceed) {
                                    mPresenter.getHotRankDataSuccess(novelNameList);
                                } else {
                                    mPresenter.getHotRankDataError("获取数据失败");
                                }
                            }
                        }

                        @Override
                        public void onFailure(String errorMsg) {
                            Log.d(TAG, "getHotRankData onFailure: " + errorMsg);
                            findCount[0]++;
                            if (findCount[0] == Constant.MALE_HOT_RANK_NUM) {
                                mPresenter.getHotRankDataError("获取数据失败");
                            }
                        }
                    })
                    .build();
            OkhttpUtil.getRequest(builder);
        }
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
            dataList.add(null);
            final int finalI = i;
            OkhttpBuilder builder = new OkhttpBuilder.Builder()
                    .setUrl(url)
                    .setOkhttpCall(new OkhttpCall() {
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
                                    coverUrlList.add("http://statics.zhuishushenqi.com" + books.get(j).getCover());
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
                    })
                    .build();
            OkhttpUtil.getRequest(builder);
        }
    }


}
