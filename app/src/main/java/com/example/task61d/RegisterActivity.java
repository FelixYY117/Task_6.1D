package com.example.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private EditText confirmPasswordEditText;
    private EditText confirmEmailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setUpRegisterButtonClickListener();
    }

    private void initViews() {
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.buttonRegister);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        confirmEmailEditText = findViewById(R.id.confirmEmailEditText);
    }

    private void setUpRegisterButtonClickListener() {
        registerButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
            String confirmEmail = confirmEmailEditText.getText().toString().trim();

            if (isValidInput(username, email, password, confirmPassword, confirmEmail)) {
                if (isPasswordAndEmailConfirmed(password, confirmPassword, email, confirmEmail)) {
                    registerUser(username, email, password);
                } else {
                    showToast("The password and confirmation password or email and confirmation email do not match");
                }
            } else {
                showToast("Please fill in all the fields");
            }
        });
    }

    private boolean isValidInput(String username, String email, String password, String confirmPassword, String confirmEmail) {
        return !username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && !confirmEmail.isEmpty();
    }

    private boolean isPasswordAndEmailConfirmed(String password, String confirmPassword, String email, String confirmEmail) {
        return password.equals(confirmPassword) && email.equals(confirmEmail);
    }

    private void registerUser(String username, String email, String password) {
        DBHelper dbHelper = new DBHelper(RegisterActivity.this);
        boolean registrationSuccessful = dbHelper.addUser(username, email, password);

        if (registrationSuccessful) {
            showToast("Registration is successful");
            navigateToInterestSelectionActivity(username);
        } else {
            showToast("Registration failed, please try again");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToInterestSelectionActivity(String username) {
        Intent intent = new Intent(RegisterActivity.this, InterestSelectionActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}
