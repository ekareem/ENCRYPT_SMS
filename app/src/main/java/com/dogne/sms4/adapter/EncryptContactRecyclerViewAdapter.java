package com.dogne.sms4.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dogne.sms4.R;
import com.dogne.sms4.activity.EncryptActivity;
import com.dogne.sms4.storage.Contact;

import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

public class EncryptContactRecyclerViewAdapter extends RecyclerView.Adapter<EncryptContactRecyclerViewAdapter.ViewHolder>
{
    private Context mContext;
    private List<Contact> mData;
    private EncryptActivity encryptActivity;
    public EncryptContactRecyclerViewAdapter(Context mContext, List<Contact> mData, EncryptActivity encryptActivity)
    {
        this.mContext = mContext;
        this.mData = mData;
        this.encryptActivity = encryptActivity;
    }

    @NonNull
    @Override
    @SuppressWarnings("deprecation")
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.encrypt_contact_row,parent,false);
        final ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.rCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                assert clipboard != null;
                clipboard.setText(viewHolder.rPublicKey.getText());

            }
        });

        viewHolder.rAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(encryptActivity.getaTxtReceiver().getText().toString().length() == 0)
                    encryptActivity.getaTxtReceiver().append(viewHolder.rAalasSid.getText().toString());
                else
                    encryptActivity.getaTxtReceiver().setText(viewHolder.rAalasSid.getText().toString());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.rAalasSid.setText(mData.get(position).aliasSid);
        holder.rAvatar.setText(mData.get(position).aliasSid.substring(0,1).toUpperCase());
        holder.rPublicKey.setText(mData.get(position).publicKey);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @SuppressWarnings("WeakerAccess")
    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        private TextView rAalasSid;
        private ImageButton rCopy;
        private TextView rAvatar;
        private TextView rPublicKey;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rAalasSid = itemView.findViewById(R.id.encrypt_contact_row_aliassid_txt);
            rCopy = itemView.findViewById(R.id.encrypt_contact_row_publickey_btn);
            rAvatar = itemView.findViewById(R.id.encrypt_dontact_row_avatar_txt);
            rPublicKey = itemView.findViewById(R.id.encrypt_contact_row_publickey_txt);
        }
    }
}
