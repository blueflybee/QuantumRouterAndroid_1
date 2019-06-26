package com.fernandocejas.android10.sample.domain.mapper;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class FeedBackRsp<T> {

    private String feedbackContent;

    private T replyContent;

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public T getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(T replyContent) {
        this.replyContent = replyContent;
    }

    @Override
    public String toString() {
        return "FeedBackResponse{" +
                "feedbackContent='" + feedbackContent + '\'' +
                ", replyContent=" + replyContent +
                '}';
    }

    public static class ReplyContent {

        private String time;
        private String type;
        private String content;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "ReplyContent{" +
                    "time='" + time + '\'' +
                    ", type='" + type + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}
