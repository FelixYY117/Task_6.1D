package com.example.task61d;

import java.util.List;
import okhttp3.OkHttpClient;
public class QuizResponse {
    public List<Question> quiz;


    public List<Question> getQuiz() {
        return quiz;
    }

    public void setQuiz(List<Question> quiz) {
        this.quiz = quiz;
        
    }
}
