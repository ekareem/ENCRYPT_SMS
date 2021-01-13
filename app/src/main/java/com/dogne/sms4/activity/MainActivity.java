package com.dogne.sms4.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dogne.sms4.notification.NotificationReceiver;
import com.dogne.sms4.R;
import com.dogne.sms4.adapter.PageAdapter;
import com.dogne.sms4.constant.Constant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.dogne.sms4.notification.App.CHANNEL_1_ID;

public class MainActivity extends AppCompatActivity
{
    private TabLayout a_tab_layout;
    private FloatingActionButton a_float_btn;
    private FirebaseAuth.AuthStateListener f_AuthListener;
    private FloatingActionButton a_reload_btn;
    private ViewPager a_view_pager;
    private PageAdapter pageAdapter;
    public int tabOncreate;
    NotificationCompat.Builder builder;
    NotificationManagerCompat notificationManager;
    String privateKey;
    int start = 0;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try
        {
            init();
            on_user_signed_in();
            connect_layout();
            tab_functionality();
            update();
        }
        catch(Exception e)
        {
            Constant.toast(MainActivity.this,e.getMessage(), Toast.LENGTH_LONG);
        }
    }

    public void init()
    {
        notificationManager = NotificationManagerCompat.from(this);
        tabOncreate = 0;
    }

    public void connect_layout()
    {
        a_float_btn = findViewById(R.id.main_float_btn);
        a_tab_layout = (TabLayout) findViewById(R.id.main_tab_layout);
        a_reload_btn = findViewById(R.id.main_reload_btn);

        String uid = FirebaseAuth.getInstance().getUid();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(Constant.USER).document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> user = documentSnapshot.getData();
                privateKey = ((String) user.get(Constant.PRIVATEKEY));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_profile_xml:
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {

        new AlertDialog.Builder(MainActivity.this)
                .setMessage("Pressing back will log you out")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();

                        //startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

    private void tab_functionality()
    {
        a_tab_layout.addTab(a_tab_layout.newTab().setIcon(R.drawable.ic_inbox_24dp).setText(Constant.INBOX));
        a_tab_layout.addTab(a_tab_layout.newTab().setIcon(R.drawable.ic_send_24dp).setText(Constant.OUTBOX));
        a_tab_layout.addTab(a_tab_layout.newTab().setIcon(R.drawable.ic_contacts_24dp).setText(Constant.CONTACT));
        a_tab_layout.selectTab(a_tab_layout.getTabAt(tabOncreate));

        a_tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
        a_float_btn.setImageResource(R.drawable.ic_message);

        a_view_pager = findViewById(R.id.main_pager_view);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), a_tab_layout.getTabCount());
        a_view_pager.setAdapter(pageAdapter);
        a_view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(a_tab_layout));

        a_tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                a_view_pager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 2)
                {
                    a_float_btn.setImageResource(R.drawable.ic_person_add);
                }
                else
                {
                    a_float_btn.setImageResource(R.drawable.ic_message);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void floatingActionButtonOnclick(View view)
    {
        int  tab = a_tab_layout.getSelectedTabPosition();


        if (tab == 2)
        {
            startActivity(new Intent(MainActivity.this,AddContactActivity.class));
        }
        else
        {
            startActivity(new Intent(MainActivity.this, EncryptActivity.class));
        }


    }

    private void on_user_signed_in()
    {
        f_AuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        };
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(f_AuthListener);
    }

    public void update()
    {
        FirebaseFirestore.getInstance().collection(Constant.STORAGE_COLLECTION_USER)
                .document(FirebaseAuth.getInstance().getUid())
                .collection(Constant.STORAGE_COLLECTION_LIST)
                .document(Constant.STORAGE_DOCUMENT_INBOX).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null && documentSnapshot.exists() && start != 0)
                {
                    notification(documentSnapshot.getData());
                    reload();
                }
                start = 1;
            }
        });

        FirebaseFirestore.getInstance().collection(Constant.STORAGE_COLLECTION_USER)
                .document(FirebaseAuth.getInstance().getUid())
                .collection(Constant.STORAGE_COLLECTION_LIST)
                .document(Constant.STORAGE_DOCUMENT_OUTBOX).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null && documentSnapshot.exists())
                {
                    reload();

                }
            }
        });
        FirebaseFirestore.getInstance().collection(Constant.STORAGE_COLLECTION_USER)
                .document(FirebaseAuth.getInstance().getUid())
                .collection(Constant.STORAGE_COLLECTION_LIST)
                .document(Constant.STORAGE_DOCUMENT_CONTACT).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null && documentSnapshot.exists())
                {
                    reload();
                }
            }
        });


    }

    public void reload()
    {
        int  tab = a_tab_layout.getSelectedTabPosition();

        a_tab_layout.removeAllTabs();
        tab_functionality();

        a_tab_layout.selectTab( a_tab_layout.getTabAt(tab));
    }

    public void reloadOnClick(View view)
    {

        reload();
    }

    private void notification(Map<String, Object> data)
    {
        List sortedKeys=new ArrayList(data.keySet());
        Collections.sort(sortedKeys);
        if(sortedKeys.size() > 0)
        {
            Map<String, Object> recent = (Map<String, Object>) data.get(sortedKeys.get(sortedKeys.size() - 1));
            sendOnChannel1(
                    (String)recent.get(Constant.FROMALIASSID),
                    (String)recent.get(Constant.MESSAGE),
                    (String)recent.get(Constant.MESSAGEID)
            );
        }
        reload();
    }

    public void sendOnChannel1(String fromAliasSid, final String message,String mid)
    {
        Intent activityIntent = new Intent(MainActivity.this,MainActivity.class);
        //activityIntent.putExtra(Constant.MESSAGEID,mid);
        //activityIntent.putExtra("MESSAGE_TYPE",Constant.INBOX);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,activityIntent,0);

        final Intent broadcastIntent = new Intent(this, NotificationReceiver.class);

        broadcastIntent.putExtra(Constant.PRIVATEKEY,privateKey);
        broadcastIntent.putExtra(Constant.MESSAGE,message);

        PendingIntent actionIntent = PendingIntent.getBroadcast(this,0,broadcastIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle(fromAliasSid)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_contacts_24dp,"DECRYPT",actionIntent)
                .build();

        notificationManager.notify(1,notification);
    }

}
