package com.example.studytool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import model.Message;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {
    private List<Message> messageList;

    public ChatAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.senderTextView.setText(message.getRole().equals("user") ? "你" : "AI");
        holder.contentTextView.setText(message.getContent());

        // 根据发送者设置不同的对齐方式和颜色
        if (message.getRole().equals("user")) {
            holder.contentTextView.setBackgroundResource(R.drawable.message_bubble);
        } else {
            holder.contentTextView.setBackgroundResource(R.drawable.message_bubble);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView senderTextView;
        TextView contentTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }
}
