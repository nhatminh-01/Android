package com.hutech.exampractice.Models;

public class QuestionModel {
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int correctAns; // chỉ mục đáp án đúng
    private int selectedAns; // Chọn đáp án của người dùng
    private int status; // Trạng thái
    private boolean isBookmarked;
    private String qID;



    public QuestionModel(String qID, String question, String optionA, String optionB, String optionC, String optionD, int correctAns, int selectedAns, int status, boolean isBookmarked) {
        this.qID = qID;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAns = correctAns;
        this.selectedAns = selectedAns;
        this.status = status;
        this.isBookmarked = isBookmarked;

    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public int getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(int correctAns) {
        this.correctAns = correctAns;
    }

    // lấy trạng thái
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    public String getqID() {
        return qID;
    }

    public void setqID(String qID) {
        this.qID = qID;
    }

    public int getSelectedAns() {
        return selectedAns;
    }

    public void setSelectedAns(int selectedAns) {
        this.selectedAns = selectedAns;
    }
}
