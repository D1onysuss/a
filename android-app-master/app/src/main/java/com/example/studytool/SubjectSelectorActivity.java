package com.example.studytool;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SubjectSelectorActivity extends Activity {
    private String[] subjects;
    private SubjectGridAdapter adapter;
    private TextView selectedTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_selector);

        GridView subjectGrid = findViewById(R.id.subjectGrid);
        selectedTips = findViewById(R.id.selectedTips);

        subjects = new String[]{
                getString(R.string.chinese_subject),
                getString(R.string.math_subject),
                getString(R.string.english_subject),
                getString(R.string.history_subject),
                getString(R.string.geography_subject),
                getString(R.string.politics_subject),
                getString(R.string.physics_subject),
                getString(R.string.chemistry_subject),
                getString(R.string.biology_subject)
        };
        // 初始化适配器
        adapter = new SubjectGridAdapter(this, subjects);
        subjectGrid.setAdapter(adapter);
    }

    // 跳转到聊天界面
    public void jumpToChat() {
        List<String> selected = adapter.getSelectedSubjects();
        if (selected.isEmpty()) return;

        // 更新提示文字
        String tips = getString(R.string.selected_subjects) + String.join("、", selected);
        selectedTips.setText(tips);

        // 传递选中的学科给AI聊天界面
        Intent intent = new Intent(this, MainActivity.class);
        intent.putStringArrayListExtra("SELECTED_SUBJECTS", (ArrayList<String>) selected);
        startActivity(intent);
        finish(); // 关闭选择页
    }

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
}
