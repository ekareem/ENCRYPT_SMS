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
import com.dogne.sms4.adapter.ContactRecyclerViewAdapter;
import com.dogne.sms4.constant.Constant;
import com.dogne.sms4.storage.Contact;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ContactFragment extends Fragment
{
    private RecyclerView mRecyclerView;
    private ContactRecyclerViewAdapter mContactRecyclerViewAdapter;
    private List<Contact> mContactList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View mView = inflater.inflate(R.layout.fragment_contact, container, false);
        mRecyclerView = mView.findViewById(R.id.inbox_recycler);

        mRecyclerView.setLayoutManager((new LinearLayoutManager(getActivity())));
        mContactList = new ArrayList<>();
        createContactList();
        return mView;
    }

    /**
     * _@purpose gets list of contact and adds it to recycler view
     */
    @SuppressWarnings("unchecked")
    private void createContactList()
    {
        FirebaseFirestore.getInstance()
                .collection(Constant.USER)
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection(Constant.LIST)
                .document(Constant.CONTACT)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> contacts;
                        contacts = documentSnapshot.getData();
                        if (contacts != null)
                        {
                            ArrayList keyList = new ArrayList(contacts.keySet());
                            Collections.sort(keyList);

                            for (int i =0  ; i < keyList.size(); i++)
                            {
                                String key = (String) keyList.get(i);
                                Map<String, Object> data= (Map<String, Object>) contacts.get(key);

                                Contact contact = new Contact();

                                contact.aliasSid = (String) data.get(Constant.ALIASSID);
                                contact.uid = (String) data.get(Constant.UID);
                                contact.publicKey = (String) data.get(Constant.PUBLICKEY);
                                mContactList.add(contact);
                            }
                            mContactRecyclerViewAdapter = new ContactRecyclerViewAdapter(getContext(),mContactList);
                            mRecyclerView.setAdapter(mContactRecyclerViewAdapter);
                        }
                    }
                });
    }
}
