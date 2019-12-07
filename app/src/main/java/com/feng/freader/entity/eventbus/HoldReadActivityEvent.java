package com.feng.freader.entity.eventbus;

import com.feng.freader.view.activity.ReadActivity;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/5
 */
public class HoldReadActivityEvent {
    private ReadActivity readActivity;

    public HoldReadActivityEvent(ReadActivity readActivity) {
        this.readActivity = readActivity;
    }

    public ReadActivity getReadActivity() {
        return readActivity;
    }

    public void setReadActivity(ReadActivity readActivity) {
        this.readActivity = readActivity;
    }
}
