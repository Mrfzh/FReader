package com.feng.freader.entity.eventbus;

import com.feng.freader.entity.epub.EpubTocItem;
import com.feng.freader.entity.epub.OpfData;
import com.feng.freader.view.activity.ReadActivity;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/14
 */
public class EpubCatalogInitEvent {
    private ReadActivity readActivity;
    private List<EpubTocItem> tocItemList;
    private OpfData opfData;
    private String novelUrl;
    private String novelName;
    private String novelCover;

    public EpubCatalogInitEvent(ReadActivity readActivity, List<EpubTocItem> tocItemList,
                                OpfData opfData, String novelUrl, String novelName, String novelCover) {
        this.readActivity = readActivity;
        this.tocItemList = tocItemList;
        this.opfData = opfData;
        this.novelUrl = novelUrl;
        this.novelName = novelName;
        this.novelCover = novelCover;
    }

    public ReadActivity getReadActivity() {
        return readActivity;
    }

    public void setReadActivity(ReadActivity readActivity) {
        this.readActivity = readActivity;
    }

    public List<EpubTocItem> getTocItemList() {
        return tocItemList;
    }

    public void setTocItemList(List<EpubTocItem> tocItemList) {
        this.tocItemList = tocItemList;
    }

    public OpfData getOpfData() {
        return opfData;
    }

    public void setOpfData(OpfData opfData) {
        this.opfData = opfData;
    }

    public String getNovelUrl() {
        return novelUrl;
    }

    public void setNovelUrl(String novelUrl) {
        this.novelUrl = novelUrl;
    }

    public String getNovelName() {
        return novelName;
    }

    public void setNovelName(String novelName) {
        this.novelName = novelName;
    }

    public String getNovelCover() {
        return novelCover;
    }

    public void setNovelCover(String novelCover) {
        this.novelCover = novelCover;
    }
}
