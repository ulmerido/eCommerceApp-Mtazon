package com.example.ido.appex2.Notification;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceHandler extends FirebaseInstanceIdService
{
    private static String TAG = "FirebaseInstanceHandler";
    private String m_DeviceToken;

    @Override
    public void onCreate()
    {
        super.onCreate();
        m_DeviceToken= FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    public void onTokenRefresh()
    {
        super.onTokenRefresh();
        // Get updated InstanceID token.
        m_DeviceToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + m_DeviceToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }
}
