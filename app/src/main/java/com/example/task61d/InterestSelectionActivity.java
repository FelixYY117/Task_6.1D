package com.example.task61d;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class InterestSelectionActivity extends AppCompatActivity {

    private CheckBox[] checkBoxes;
    private Button continueButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_selection);

        // 初始化数据库帮助类
        dbHelper = new DBHelper(this);

        // 初始化CheckBox和Button
        initializeViews();

        // 设置继续按钮的点击事件
        setUpContinueButtonClickListener();

        // 设置CheckBox的点击事件
        setUpCheckBoxClickListeners();
    }

    private void initializeViews() {
        checkBoxes = new CheckBox[]{
                findViewById(R.id.checkBoxTechnology),
                findViewById(R.id.checkBoxBasketball),
                // 其他CheckBox初始化...
        };

        continueButton = findViewById(R.id.continueButton);
    }

    private void setUpContinueButtonClickListener() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取当前用户的用户名
                String username = getLoggedInUsername();

                // 清除用户之前选择的兴趣
                dbHelper.clearUserInterests(username);

                // 获取当前选中的兴趣
                List<String> selectedInterests = getSelectedInterests();

                // 将选中的兴趣保存到数据库
                for (String interest : selectedInterests) {
                    dbHelper.insertUserInterest(username, interest);
                }

                // 启动MainActivity
                startMainActivity();
            }
        });
    }

    private String getLoggedInUsername() {
        // 从SharedPreferences或其他机制获取当前登录的用户名
        return "currentUsername";
    }

    private void setUpCheckBoxClickListeners() {
        for (final CheckBox checkBox : checkBoxes) {
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleCheckBoxBackground(checkBox);
                }
            });
        }
    }

    private void toggleCheckBoxBackground(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            checkBox.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            checkBox.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }

    private List<String> getSelectedInterests() {
        List<String> selectedInterests = new ArrayList<>();
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {
                selectedInterests.add(checkBox.getText().toString());
            }
        }
        return selectedInterests;
    }

    private void startMainActivity() {
        Intent intent = new Intent(InterestSelectionActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
