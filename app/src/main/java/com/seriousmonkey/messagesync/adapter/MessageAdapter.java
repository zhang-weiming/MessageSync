package com.seriousmonkey.messagesync.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.seriousmonkey.messagesync.R;
import com.seriousmonkey.messagesync.entity.ShortMessageItem;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView nameOrAddress;
        TextView date;
        TextView desc;

        public ViewHolder(View v) {
            super(v);
            icon = (ImageView) v.findViewById(R.id.contact_icon);
            nameOrAddress = (TextView) v.findViewById(R.id.name_or_address);
            date = (TextView) v.findViewById(R.id.date);
            desc = (TextView) v.findViewById(R.id.message_desc);
        }
    }

    /**
     * 短信列表
     */
    private List<ShortMessageItem> mMessages;

//    private Context mContext;

    public MessageAdapter(List<ShortMessageItem> data) {
        mMessages = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        mContext = parent.getContext();
        //LayoutInflater.from指定写法
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        ShortMessageItem msgItem = mMessages.get(position);
        holder.icon.setImageResource(
                msgItem.getIcon() == 0 ? getDefaultContactIcon() : msgItem.getIcon()
        );
        holder.nameOrAddress.setText(
                msgItem.getName() != null ? msgItem.getName() : msgItem.getAddress()
        );
        holder.date.setText(msgItem.getDate());
        holder.desc.setText(msgItem.getDesc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Click " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    private int getDefaultContactIcon() {
        return R.drawable.ic_contact;
    }
}