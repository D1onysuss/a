package com.example.studytool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import manager.DeepSeekManager;
import service.ChatCallback;
import model.Message;
import java.util.Locale;
import java.util.Objects;

import android.os.Build;

public class MainActivity extends Activity {
    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private Button sendButton, backButton;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;
    private DeepSeekManager deepSeekManager;
    private List<String> selectedSubjects; // 存储从选择页传递的学科列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 从学科选择页获取选中的学科
        selectedSubjects = getIntent().getStringArrayListExtra("SELECTED_SUBJECTS");

        // 验证学科选择（如果未选择或异常，返回选择页）
        if (selectedSubjects == null || selectedSubjects.isEmpty()) {
            Toast.makeText(this, getString(R.string.please_select_subject), Toast.LENGTH_SHORT).show();
            finish(); // 关闭当前页面
            return;
        }

        // 初始化视图和数据
        initViews();
        initData();

        if (selectedSubjects != null && !selectedSubjects.isEmpty()) {
            String subjectText = getString(R.string.selected_subjects) +
                    String.join(", ", selectedSubjects);
            Toast.makeText(this, subjectText, Toast.LENGTH_LONG).show();
        }

        // 初始化AI管理器并发送学科信息
        deepSeekManager = new DeepSeekManager(this);
        sendSubjectsToAI();
    }

    /**
     * 初始化视图组件
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        // 配置RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setHasFixedSize(true);

        // 发送按钮点击事件
        sendButton.setOnClickListener(v -> sendMessage());
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SubjectSelectorActivity.class);
            startActivity(intent);
        });

        backButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            return false;
        });

        // 输入框回车发送消息
        messageEditText.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
    }

    /**
     * 初始化消息列表和适配器
     */
    private void initData() {
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        chatRecyclerView.setAdapter(chatAdapter);
    }

    //获取当前语言
    public String getCurrentLanguage() {
        Configuration config = getResources().getConfiguration();
        Locale currentLocale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentLocale = config.getLocales().get(0);
        } else {
            currentLocale = config.locale;
        }

        return currentLocale.getLanguage();
    }

    /**
     * 向AI发送选中的学科信息（作为对话上下文）
     */
    private void sendSubjectsToAI() {

        String subjectText = getString(R.string.learning_subjects_1) +
                String.join(getString(R.string.punctuation1), selectedSubjects) +
                getString(R.string.learning_subjects_2);

//        if(Objects.equals(getCurrentLanguage(), "en")) {
//            subjectText = "The subject I need to study now is: " +
//                    String.join(", ", selectedSubjects) +
//                    ". Please communicate with me based on the knowledge background of these subjects and answer related questions. Please answer by English.";
//        }

        // 添加到本地消息列表（模拟用户发送）
        Message subjectMessage = new Message("user", subjectText);
        messageList.add(subjectMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        chatRecyclerView.scrollToPosition(messageList.size() - 1);

        // 发送给AI并处理回复
        deepSeekManager.sendMessage(subjectText, new ChatCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    // 添加AI回复到消息列表
                    Message aiMessage = new Message("assistant", response);
                    messageList.add(aiMessage);
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    chatRecyclerView.scrollToPosition(messageList.size() - 1);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, getString(R.string.error) + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    /**
     * 发送用户输入的消息到AI
     */
    private void sendMessage() {
        String userInput = messageEditText.getText().toString().trim();

        // 验证输入不为空
        if (userInput.isEmpty()) {
            Toast.makeText(this, getString(R.string.please_enter_message), Toast.LENGTH_SHORT).show();
            return;
        }

        // 添加用户消息到本地列表
        Message userMessage = new Message("user", userInput);
        messageList.add(userMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        chatRecyclerView.scrollToPosition(messageList.size() - 1);

        // 清空输入框
        messageEditText.setText("");

        // 发送消息到AI
        deepSeekManager.sendMessage(userInput, new ChatCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    // 添加AI回复到列表
                    Message aiMessage = new Message("assistant", response);
                    messageList.add(aiMessage);
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    chatRecyclerView.scrollToPosition(messageList.size() - 1);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, getString(R.string.please_enter_message) + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
