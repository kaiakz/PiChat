package com.kaiakz.pichat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> msgList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView senderName;
        TextView senderTimestamp;

        ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            senderName = (TextView) view.findViewById(R.id.tv_name);
            senderTimestamp = (TextView) view.findViewById(R.id.tv_timestamp);
        }
    }

    MessageAdapter(List<Message> msgList) {
        this.msgList = msgList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message msg = msgList.get(position);
        holder.senderName.setText(msg.getSender());
        holder.senderTimestamp.setText(msg.getTimestamp());
        holder.imageView.setImageBitmap(msg.getBitmap());
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }


}
