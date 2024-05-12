package com.example.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import java.util.Random;

public class QuizInitial extends AppCompatActivity {
    private String username;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_initial);

        TextView usernameTextView = findViewById(R.id.textusername);
        ImageButton startButton = findViewById(R.id.buttonContinue);
        TextView descriptionTextView = findViewById(R.id.testtaskDescription);

        // Get username from intent
        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("username");
        }

        // Set username text
        usernameTextView.setText(username);

        // Get interests from database
        DBHelper dbHelper = new DBHelper(this);
        List<String> interests = dbHelper.getUserInterests(username);

        // Set description text
        if (interests.isEmpty()) {
            // Handle empty interests list, e.g., use a default topic or prompt user to add interests
            description = "AI"; // Default topic
        } else {
            // Randomly select an interest as the quiz topic
            int index = new Random().nextInt(interests.size());
            description = interests.get(index);
        }
        descriptionTextView.setText(String.format("Click the following button to get into the quiz of %s", description));

        // Start button onClickListener
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Quiz activity
                startQuiz();
            }
        });
    }

    private void startQuiz() {
        Intent intent = new Intent(QuizInitial.this, Quiz.class);
        intent.putExtra("description", description);
        startActivity(intent);
    }
}

