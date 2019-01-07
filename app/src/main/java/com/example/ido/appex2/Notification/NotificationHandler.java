package com.example.ido.appex2.Notification;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.ido.appex2.Activities.AudioBookDetailsActivity;
import com.example.ido.appex2.Activities.UserActivity;
import com.example.ido.appex2.Adapter.AudioBookWithKey;
import com.example.ido.appex2.Analytics.AnalyticsManager;
import com.example.ido.appex2.R;
import com.example.ido.appex2.entities.AudioBook;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NotificationHandler
{
    private AudioBook m_AudioBook;
    private Context m_Context;
    private Map<String,String> m_Data;
    private String m_AudioBookKey;
    private final String TAG = "NotificationHandler";
    private CountDownLatch m_Done;
    private RemoteMessage m_remoteMessage;
    private NotificationManager m_notificationManager;
    private Uri m_SoundUri;
    private   NotificationCompat.Builder m_NotificationBuilder;


    private static final String CHANNEL_ID ="fcm_default_channel";

    public NotificationHandler(Context i_Context,RemoteMessage i_remoteMessage)
    {
        Log.e(TAG, "Ctr() >>");
        m_Context=i_Context;
        m_remoteMessage =i_remoteMessage;
        m_Data = m_remoteMessage.getData();

        m_notificationManager = (NotificationManager) m_Context.getSystemService(Context.NOTIFICATION_SERVICE);
        m_SoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        m_NotificationBuilder= new NotificationCompat.Builder(m_Context, CHANNEL_ID);

        Log.e(TAG, "Ctr() <<");

    }

    public void displyNotification()
    {
        Log.e(TAG, "displyNotification() >>");

        m_NotificationBuilder.setChannelId(CHANNEL_ID)
                .setSound(m_SoundUri)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_notifi);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            Log.e(TAG, "this is Oreo+()");

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Default", NotificationManager.IMPORTANCE_DEFAULT);
            m_notificationManager.createNotificationChannel(channel);

        }

        if(m_Data.get("discount") != null)
        {
            Log.e(TAG, "go to BookSale()" + m_Data.get("discount"));
            displyCampain_BookSale();
        }
        else if(m_Data.get("profile")!= null)
        {
            Log.e(TAG, "go to profile()" + m_Data.get("discount"));
            displyCampain_updateProfile();
        }
        else
        {
            Log.e(TAG, "go to deafult()" + m_Data.get("discount"));
            displyCampain_deafult();
        }

        Log.e(TAG, "displyNotification() <<");

    }

    private void displyCampain_BookSale()
    {
        Log.e(TAG, "displyCampain_BookSale() >>");
        AnalyticsManager.getInstance().init(getApplicationContext());
        m_AudioBookKey = m_Data.get("AudioBookKey");
        findAudioBook();
        Intent intent = new Intent(m_Context, AudioBookDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("discount", m_Data.get("discount"));
        intent.putExtra("Key", m_AudioBookKey);
        intent.putExtra("AudioBook",m_AudioBook);
        PendingIntent pendingIntent = PendingIntent.getActivity(m_Context, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        m_NotificationBuilder.setContentTitle("One Time Sale! $" + m_Data.get("discount")+ " off")
                .setContentText("AudioBook in discount: " + m_AudioBook.getName())
                .setContentIntent(pendingIntent);

        //NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(m_Context);
        //notificationManagerCompat.notify(1,m_NotificationBuilder.build());
        m_notificationManager.notify(0,m_NotificationBuilder.build());


    }



    private void displyCampain_deafult()
    {
        Log.e(TAG, "displyCampain_deafult() >>");

        Intent intent = new Intent(m_Context, UserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(m_Context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        RemoteMessage.Notification notification =m_remoteMessage.getNotification();
        if(notification == null)
            return;

        m_NotificationBuilder.setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setContentIntent(pendingIntent);

        m_notificationManager.notify(0,m_NotificationBuilder.build());

    }

    private void displyCampain_updateProfile()
    {

        NotificationManager m_notificationManager = (NotificationManager) m_Context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(m_Context, UserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(m_Context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String fname =FirebaseAuth.getInstance().getCurrentUser().getDisplayName().split(" ", 2)[0];


        m_NotificationBuilder.setContentTitle("Hey " + fname+"! we love you <3")
                .setContentText("Want to update your profile?")
                .setContentIntent(pendingIntent);

        m_notificationManager.notify(0,m_NotificationBuilder.build());

    }

    private void findAudioBook()
    {
        Log.e(TAG, "findAudioBook()>>");

        DatabaseReference AllBooksRef = FirebaseDatabase.getInstance().getReference("AudioBooks");
         m_Done = new CountDownLatch(1);

        AllBooksRef.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName)
            {
                AudioBookWithKey bookWithKey = new AudioBookWithKey(snapshot.getKey(), snapshot.getValue(AudioBook.class));
                if(m_AudioBookKey.equals(bookWithKey.getKey()))
                {
                    m_AudioBook = bookWithKey.getAudioBook();
                    m_Done.countDown();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {}
            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}
            @Override
            public void onChildRemoved(DataSnapshot snapshot) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        try
        {
            Log.e(TAG, "wait>>");
            m_Done.await();
            Log.e(TAG, "wait<<");

        }
        catch(Exception e)
        {
        }

        Log.e(TAG, "findAudioBook()<<");

    }

}
