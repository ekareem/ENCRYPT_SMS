package com.dogne.sms4.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dogne.sms4.R;
import com.dogne.sms4.activity.DecryptActivity;
import com.dogne.sms4.constant.Constant;
import com.dogne.sms4.storage.Message;

import java.util.List;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder>
{
    private Context mContext;
    private List<Message> mData;
    private Activity activity;
    private String fragment;

    public MessageRecyclerViewAdapter(Context mContext, List<Message>mData, Activity activity, String fragment)
    {
        this.mContext = mContext;
        this.mData = mData;
        this.activity = activity;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.message_row,parent,false);
        final ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.rBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(activity, DecryptActivity.class);

                intent.putExtra(Constant.MESSAGEID, viewHolder.rId.getText().toString());
                intent.putExtra(Constant.PUT_EXTRA_MESSAGE_TYPE, fragment);

                activity.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
        if(fragment.equals(Constant.INBOX))
            holder.rAliasSid.setText(mData.get(position).fromAliasSid);
        if(fragment.equals(Constant.OUTBOX))
            holder.rAliasSid.setText(mData.get(position).toAliasSid);

        holder.rMsg.setText(mData.get(position).message);
        holder.rId.setText(mData.get(position).id);

        if(fragment.equals(Constant.INBOX))
            holder.rAvatar.setText( mData.get(position).fromAliasSid.substring(0,1).toUpperCase());
        else if(fragment.equals(Constant.OUTBOX))
            holder.rAvatar.setText( mData.get(position).toAliasSid.substring(0,1).toUpperCase());
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    @SuppressWarnings("WeakerAccess")
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView rAliasSid;
        private TextView rMsg;
        private TextView rId;
        private Button rBtn;
        private TextView rAvatar;

        private ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            rAliasSid = itemView.findViewById(R.id.encrypt_contact_row_aliassid_txt);
            rMsg = itemView.findViewById(R.id.contact_row_msg_txt);
            rId = itemView.findViewById(R.id.message_row_id_txt);
            rBtn = itemView.findViewById(R.id.message_row_btn);
            rAvatar = itemView.findViewById(R.id.encrypt_dontact_row_avatar_txt);
        }
    }
}
