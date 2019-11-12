package com.feng.freader.entity.eventbus;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/12
 */
public class SearchUpdateInputEvent {
    private String input;

    public SearchUpdateInputEvent(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
