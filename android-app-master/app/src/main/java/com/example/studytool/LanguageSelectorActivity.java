package com.example.studytool;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import java.util.Locale;

public class LanguageSelectorActivity extends Activity {
    private Button chineseBtn;
    private Button englishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 在setContentView之前设置主题
//        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);

        // 确保窗口背景不为空
        getWindow().setBackgroundDrawableResource(android.R.color.white);

        setContentView(R.layout.activity_language_selector);

        initViews();
    }

    private void initViews() {
        // 延迟初始化视图，避免阻塞主线程
        new android.os.Handler().postDelayed(() -> {
            chineseBtn = findViewById(R.id.chineseBtn);
            englishBtn = findViewById(R.id.englishBtn);

            chineseBtn.setOnClickListener(v -> setLocale("zh"));
            englishBtn.setOnClickListener(v -> setLocale("en"));
        }, 50);
    }


    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }

        // 修复：使用正确的方式更新配置
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // 重新创建Activity以应用语言变更
        recreate();

        // 延迟跳转，确保语言配置生效
        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(this, SubjectSelectorActivity.class);
            startActivity(intent);
            finish();
        }, 100);
    }
}
