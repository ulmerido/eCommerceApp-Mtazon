package com.example.ido.appex2.Notification;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.example.ido.appex2.Notification.NotificationHandler;
import com.example.ido.appex2.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseInstanceService extends FirebaseMessagingService
{
    private static final String TAG ="PushNotificationFB";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.e(TAG, "onMessageReceived()>>");

        super.onMessageReceived(remoteMessage);
        Map<String, String> dataReceive;
        dataReceive = remoteMessage.getData();

        if(dataReceive != null)
        {
            NotificationHandler notify = new NotificationHandler(this, remoteMessage);
            notify.displyNotification();
        }

        Log.e(TAG, "onMessageReceived() <<");
    }
}
