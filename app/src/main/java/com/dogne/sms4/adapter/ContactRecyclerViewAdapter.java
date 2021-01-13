package com.dogne.sms4.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dogne.sms4.R;
import com.dogne.sms4.activity.ContactActivity;
import com.dogne.sms4.activity.EncryptActivity;
import com.dogne.sms4.constant.Constant;
import com.dogne.sms4.storage.Contact;

import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ViewHolder>
{
    private Context mContext;
    private List<Contact> mData;

    public ContactRecyclerViewAdapter(Context mContext, List<Contact> mData)
    {
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    @SuppressWarnings("deprecation")
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.contact_row,parent,false);
        final ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.rAliasCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                assert clipboard != null;
                clipboard.setText(viewHolder.rAalasSid.getText());
            }
        });

        viewHolder.rAvatar.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                    Intent intent = new Intent(new Intent(mContext, ContactActivity.class));
                    intent.putExtra(Constant.ALIASSID, viewHolder.rAalasSid.getText());
                    mContext.startActivity(intent);
            }
        });

        viewHolder.rMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(mContext, EncryptActivity.class));
                intent.putExtra(Constant.PUT_EXTRA_ENCRYPT_RECEIVER, viewHolder.rAalasSid.getText().toString());
                mContext.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position)
    {
        holder.rAalasSid.setText(mData.get(position).aliasSid);
        holder.rAvatar.setText(mData.get(position).aliasSid.substring(0,1).toUpperCase());
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    @SuppressWarnings("WeakerAccess")
    public  static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView rAalasSid;
        private ImageButton rAliasCopy;
        private ImageButton rMessage;
        private TextView rAvatar;

        ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            rAalasSid = itemView.findViewById(R.id.encrypt_contact_row_aliassid_txt);
            rAliasCopy= itemView.findViewById(R.id.encrypt_contact_row_publickey_btn);
            rAvatar= itemView.findViewById(R.id.encrypt_dontact_row_avatar_txt);
            rMessage = itemView.findViewById(R.id.contact_msg_btn);
        }
    }
}
