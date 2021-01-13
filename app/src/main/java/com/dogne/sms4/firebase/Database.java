package com.dogne.sms4.firebase;

import android.content.ClipboardManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dogne.sms4.constant.Constant;
import com.dogne.sms4.rsa.RSA;
import com.dogne.sms4.storage.Contact;
import com.dogne.sms4.storage.Message;
import com.dogne.sms4.storage.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.CLIPBOARD_SERVICE;

public class Database
{

    static void onUserCreated(AppCompatActivity appCompatActivity, String alias, String email)
    {
        try
        {
            final String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            User user =  new User(uid,alias,email);
            Contact contact = new Contact(user.aliasSid,user.uid,user.publicKey);
            storeUser(user);
            storeContact(contact);
            storeContactList(new HashMap<String, Contact>());
            storeInboxList(new HashMap<String, Message>());
            storeOutboxList(new HashMap<String, Message>());

        }
        catch (Exception e)
        {
            Constant.toast(appCompatActivity,e.getMessage(), Toast.LENGTH_LONG);
        }

    }

    public static void onDeleteUser(String a_alias_sid)
    {
        String uid = FirebaseAuth.getInstance().getUid();
        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).delete();

        assert uid != null;
        FirebaseFirestore.getInstance().collection(Constant.USER).document(uid).collection(Constant.LIST).document(Constant.CONTACT).delete();
        FirebaseFirestore.getInstance().collection(Constant.USER).document(uid).collection(Constant.LIST).document(Constant.INBOX).delete();
        FirebaseFirestore.getInstance().collection(Constant.USER).document(uid).collection(Constant.LIST).document(Constant.OUTBOX).delete();

        FirebaseFirestore.getInstance().collection(Constant.USER).document(uid).delete();
        FirebaseFirestore.getInstance().collection(Constant.CONTACT).document(a_alias_sid).delete();
    }

    private static void storeUser(User user)
    {
        CollectionReference cities = FirebaseFirestore.getInstance().collection(Constant.USER);
        cities.document(user.uid).set(user);
    }

    private static void storeContact(Contact contact)
    {
        CollectionReference cities = FirebaseFirestore.getInstance().collection(Constant.CONTACT);
        cities.document(contact.aliasSid).set(contact);
    }

    @SuppressWarnings("deprecation")
    public static void getPublickey(String aliasSid, final AppCompatActivity appCompatActivity)
    {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(Constant.CONTACT).document(aliasSid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot != null) {
                    Map<String, Object> contact = documentSnapshot.getData();
                    if (contact != null)
                    {
                        ClipboardManager clipboard = (ClipboardManager) appCompatActivity.getSystemService(CLIPBOARD_SERVICE);
                        assert clipboard != null;
                        clipboard.setText((String)contact.get(Constant.PUBLICKEY));
                        Constant.toast(appCompatActivity,Constant.PUBLICKEY_SUCCESS,Toast.LENGTH_LONG);
                    }
                    else
                    {
                        Constant.toast(appCompatActivity,Constant.CONTACT_FAILURE,Toast.LENGTH_LONG);
                    }
                }
            }
        });
    }

    private static void storeContactList(Map<String, Contact> contacts)
    {
        FirebaseFirestore.getInstance()
                .collection(Constant.USER)
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection(Constant.LIST)
                .document(Constant.CONTACT)
                .set(contacts);
    }

    private static void storeInboxList(Map<String, Message> messages)
    {
        FirebaseFirestore.getInstance()
                .collection(Constant.USER)
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection(Constant.LIST)
                .document(Constant.INBOX)
                .set(messages);
    }

    private static void storeOutboxList(Map<String, Message> messages)
    {
        FirebaseFirestore.getInstance()
                .collection(Constant.USER)
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection(Constant.LIST)
                .document(Constant.OUTBOX)
                .set(messages);
    }

    public static void addContact(final String aliasSid, final AppCompatActivity appCompatActivity)
    {
        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection(Constant.CONTACT)
                .document(aliasSid);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot != null) {
                    final Contact contact = documentSnapshot.toObject(Contact.class);

                    if (contact != null)
                    {

                        final DocumentReference db = FirebaseFirestore.getInstance()
                                .collection(Constant.USER)
                                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

                        db.collection(Constant.LIST).document(Constant.CONTACT)
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot)
                            {
                                Map<String, Object> contacts;
                                if (documentSnapshot.getData() != null)
                                    contacts = documentSnapshot.getData();
                                else

                                    contacts = new HashMap<>();
                                contacts.put(contact.aliasSid,contact);
                                db.collection(Constant.LIST).document(Constant.CONTACT).set(contacts);

                                Toast.makeText(appCompatActivity,Constant.CONTACT_SUCCESS,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(appCompatActivity,Constant.CONTACT_FAILURE,Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
