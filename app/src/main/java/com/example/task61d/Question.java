package com.example.task61d;

import com.google.gson.annotations.SerializedName;
import okhttp3.OkHttpClient;
import java.util.List;


public class Question {
    @SerializedName("question")
    private String question;
    @SerializedName("options")
    private List<String> options;
    @SerializedName("correct_answer")
    private String correct_answer;

    //构造函数
    public Question(String questionText, List<String> options, String correctAnswer) {
        this.question = questionText;
        this.options = options;
        this.correct_answer = correctAnswer;
    }

    
    public String getQuestionText() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correct_answer;
    }

    //获取选项索引
    public int getCorrectOptionIndex() {
        switch (correct_answer) {
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
            case "D":
                return 3;
            default:
                return -1;
        }
    }
}
