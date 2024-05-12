package com.example.task61d;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import java.util.List;

public class Quiz extends AppCompatActivity {
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private String description;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_content);
        description = getIntent().getStringExtra("description");
        initializeViews();
        fetchData();
    }

    private void initializeViews() {
        questionTextView = findViewById(R.id.textquestions);
        optionsRadioGroup = findViewById(R.id.answerGroup);
        optionsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);
            int selectedOptionIndex = optionsRadioGroup.indexOfChild(selectedButton);
            checkAnswer(selectedOptionIndex, selectedButton);
        });
    }

    private void displayQuestion() {
        if (questions != null && currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            questionTextView.setText(question.getQuestionText());
            optionsRadioGroup.clearCheck();
            List<String> options = question.getOptions();
            for (int i = 0; i < options.size(); i++) {
                RadioButton optionButton = (RadioButton) optionsRadioGroup.getChildAt(i);
                optionButton.setText(options.get(i));
            }
        }
    }

    private void checkAnswer(int selectedOptionIndex, RadioButton clicked) {
        if (questions != null && currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            currentQuestionIndex++;
            if (selectedOptionIndex == currentQuestion.getCorrectOptionIndex()) {
                score++;
            }
            if (currentQuestionIndex < questions.size()) {
                displayQuestion();
            } else {
                showFinalPage();
            }
        }
    }

    private void showFinalPage() {
        setContentView(R.layout.final_page);
        TextView showScore = findViewById(R.id.textshowscore);
        double init_score = (double) score / questions.size() * 100.0;
        int init_score_int = (int) init_score;
        showScore.setText(String.valueOf(init_score_int));
        Button finish = findViewById(R.id.quit);
        Button restart = findViewById(R.id.restart);
        finish.setOnClickListener(v -> finishAffinity());
        restart.setOnClickListener(v -> restartApp());
        for (int i = 0; i < 3; i++) {
            Question question = questions.get(i);
            TextView questionTextView = findViewById(getResources().getIdentifier("question" + (i + 1), "id", getPackageName()));
            TextView answerTextView = findViewById(getResources().getIdentifier("answer" + (i + 1), "id", getPackageName()));
            questionTextView.setText(question.getQuestionText());
            String correctAnswer = question.getOptions().get(question.getCorrectOptionIndex());
            answerTextView.setText(getString(R.string.correct_answer, correctAnswer));
        }
    }

    private void fetchData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(10, TimeUnit.MINUTES).build())
                .build();

        QuizApiService request = retrofit.create(QuizApiService.class);

        request.getQuiz(description).enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(@NonNull Call<QuizResponse> call, @NonNull Response<QuizResponse> response) {
                if (response.isSuccessful()) {
                    QuizResponse quizResponse = response.body();
                    if (quizResponse != null) {
                        questions = quizResponse.getQuiz();
                        displayQuestion();
                    }
                } else {
                    Toast.makeText(Quiz.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuizResponse> call, @NonNull Throwable t) {
                Toast.makeText(Quiz.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void restartApp() {
        Intent i = getIntent();
        finish();
        startActivity(i);
    }

    interface QuizApiService {
        @GET("getQuiz")
        Call<QuizResponse> getQuiz(@Query("topic") String topic);
    }
}
