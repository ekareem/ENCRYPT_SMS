package com.dogne.sms4.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dogne.sms4.R;
import com.dogne.sms4.adapter.MessageRecyclerViewAdapter;
import com.dogne.sms4.constant.Constant;
import com.dogne.sms4.storage.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InboxFragment extends Fragment
{
    private RecyclerView recyclerView;
    private List<Message> messages;

    public InboxFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        recyclerView = view.findViewById(R.id.inbox_recycler);
        recyclerView.setLayoutManager((new LinearLayoutManager(getActivity())));
        messages = new ArrayList<>();
        createMessageList();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * _@purpose gets list of inbox messages form database
     */
    @SuppressWarnings("unchecked")
    private void createMessageList()
    {
        FirebaseFirestore.getInstance().collection(Constant.USER).document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection(Constant.LIST).document(Constant.INBOX)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> messagesL;
                        messagesL = documentSnapshot.getData();
                        if (messagesL != null)
                        {
                            ArrayList keyList = new ArrayList(messagesL.keySet());
                            Collections.sort(keyList);
                            for (int i = keyList.size() - 1; i >= 0; i--) {
                                String key = (String) keyList.get(i);

                                Map<String, Object> data= (Map<String, Object>) messagesL.get(key);

                                Message message = new Message();

                                assert data != null;
                                message.fromAliasSid = (String)data.get(Constant.FROMALIASSID);
                                message.toAliasSid = (String)data.get(Constant.TOALIASSID);
                                message.message = (String)data.get(Constant.MESSAGE);
                                message.id = (String)data.get(Constant.MESSAGEID);
                                messages.add(message);

                            }
                            MessageRecyclerViewAdapter messageRecyclerViewAdapter = new MessageRecyclerViewAdapter(getContext(),messages,getActivity(),Constant.INBOX);
                            recyclerView.setAdapter(messageRecyclerViewAdapter);
                        }

                    }
                });
        Constant.print(messages.toString());
    }
}
