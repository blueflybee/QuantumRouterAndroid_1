package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 我的反馈 实体类
 *      version: 1.0
 * </pre>
 */

public class MyFeedBackEntity {
    private String time;
    private String title;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "MyFeedBackEntity{" +
                "time='" + time + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
