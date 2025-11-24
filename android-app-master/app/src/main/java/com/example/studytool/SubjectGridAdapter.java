package com.example.studytool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

public class SubjectGridAdapter extends BaseAdapter {
    private Context context;
    private String[] subjects;
    private List<String> selectedSubjects = new ArrayList<>(); // 存储选中的学科

    public SubjectGridAdapter(Context context, String[] subjects) {
        this.context = context;
        this.subjects = subjects;
    }

    @Override
    public int getCount() {
        return subjects.length;
    }

    @Override
    public Object getItem(int position) {
        return subjects[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_subject_checkbox, parent, false);
            holder = new ViewHolder();
            holder.checkBox = convertView.findViewById(R.id.subjectCheckbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String subject = subjects[position];
        holder.checkBox.setText(subject);

        // 恢复选中状态
        holder.checkBox.setChecked(selectedSubjects.contains(subject));

        // 勾选事件监听
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedSubjects.add(subject);
            } else {
                selectedSubjects.remove(subject);
            }

            // 当有选择时自动触发跳转（如果需要手动点击确定按钮可删除此部分）
            if (!selectedSubjects.isEmpty()) {
                ((SubjectSelectorActivity) context).jumpToChat();
            }
        });

        return convertView;
    }

    // 获取选中的学科列表
    public List<String> getSelectedSubjects() {
        return selectedSubjects;
    }

    static class ViewHolder {
        CheckBox checkBox;
    }
}
