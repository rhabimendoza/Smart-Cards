package com.example.smardcard_appdev;

public class ContentItem {

    long questionId, stackId;
    String questionText;
    String answerText;

    public ContentItem(long questionId, long stackId, String questionText, String answerText) {
        this.questionId = questionId;
        this.stackId = stackId;
        this.questionText = questionText;
        this.answerText = answerText;
    }

    public long getquestionId() {
        return questionId;
    }

    public long getstackId(){
        return stackId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getAnswerText() {
        return answerText;
    }
}