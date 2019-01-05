package com.example.ido.appex2.Notification;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.ido.appex2.Activities.AudioBookDetailsActivity;
import com.example.ido.appex2.Activities.UserActivity;
import com.example.ido.appex2.Adapter.AudioBookWithKey;
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

public class NotificationHandler
{
    private AudioBook m_AudioBook;
    private Context m_Context;
    private Map<String,String> m_Data;
    private String m_AudioBookKey;
    private final String TAG = "NotificationHandler";
    private CountDownLatch m_Done;
    private RemoteMessage m_remoteMessage;


    private static final String CHANNEL_ID ="fcm_default_channel";

    public NotificationHandler(Context i_Context,RemoteMessage i_remoteMessage)
    {
        Log.e(TAG, "Ctr() >>");
        m_Context=i_Context;
        m_remoteMessage =i_remoteMessage;
        m_Data = m_remoteMessage.getData();
        Log.e(TAG, "Ctr() <<");

    }

    public void displyNotification()
    {
        Log.e(TAG, "displyNotification() >>");

        if(m_Data.get("discount") != null)
        {
            Log.e(TAG, "go to BookSale()" + m_Data.get("discount"));
            displyCampain_BookSale();
        }
        else if(m_Data.get("profile")!= null)
        {
            displyCampain_updateProfile();
        }
        else
        {
            displyCampain_deafult();
        }

        Log.e(TAG, "displyNotification() <<");

    }

    private void displyCampain_BookSale()
    {
        Log.e(TAG, "displyCampain_BookSale() >>");
        Uri soundRri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        m_AudioBookKey = m_Data.get("AudioBookKey");
        findAudioBook();

        Intent intent = new Intent(m_Context, AudioBookDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("discount", m_Data.get("discount"));
        intent.putExtra("Key", m_AudioBookKey);

        Log.e(TAG, "audio: "+m_AudioBook.getName());

        intent.putExtra("AudioBook",m_AudioBook);

        PendingIntent pendingIntent = PendingIntent.getActivity(m_Context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(m_Context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifi)
                .setContentTitle("One Time Sale! $" + m_Data.get("discount")+ " off")
                .setContentText("AudioBook in discount: " + m_AudioBook.getName())
                .setContentIntent(pendingIntent)
                .setSound(soundRri)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(m_Context);
        notificationManagerCompat.notify(1,builder.build());
    }

    private void displyCampain_deafult()
    {
        Intent intent = new Intent(m_Context, UserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(m_Context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        RemoteMessage.Notification notification =m_remoteMessage.getNotification();
        if(notification == null)
            return;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(m_Context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifi)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(m_remoteMessage.getPriority());

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(m_Context);
        notificationManagerCompat.notify(1,builder.build());
    }

    private void displyCampain_updateProfile()
    {
        Intent intent = new Intent(m_Context, UserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(m_Context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri soundRri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String fname =FirebaseAuth.getInstance().getCurrentUser().getDisplayName().split(" ", 2)[0];


        NotificationCompat.Builder builder = new NotificationCompat.Builder(m_Context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifi)
                .setContentTitle("Hey " + fname+"! we love you <3")
                .setContentText("Want to update your profile?")
                .setContentIntent(pendingIntent)
                .setSound(soundRri)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(m_Context);
        notificationManagerCompat.notify(1,builder.build());
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
