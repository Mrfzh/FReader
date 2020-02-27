package com.feng.freader.entity.data;

import java.io.Serializable;
import java.util.List;

/**
 * 发现页小说数据
 *
 * @author Feng Zhaohao
 * Created on 2019/12/20
 */
public class DiscoveryNovelData implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> novelNameList;
    private List<String> coverUrlList;

    public DiscoveryNovelData() {
    }

    public List<String> getNovelNameList() {
        return novelNameList;
    }

    public void setNovelNameList(List<String> novelNameList) {
        this.novelNameList = novelNameList;
    }

    public List<String> getCoverUrlList() {
        return coverUrlList;
    }

    public void setCoverUrlList(List<String> coverUrlList) {
        this.coverUrlList = coverUrlList;
    }

    @Override
    public String toString() {
        return "DiscoveryNovelData{" +
                "novelNameList=" + novelNameList +
                ", coverUrlList=" + coverUrlList +
                '}';
    }
}
