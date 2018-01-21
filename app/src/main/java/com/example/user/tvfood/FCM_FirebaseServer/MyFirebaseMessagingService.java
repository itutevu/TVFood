package com.example.user.tvfood.FCM_FirebaseServer;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.user.tvfood.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by USER on 28/11/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() != 0) {
            String title = remoteMessage.getData().get("title");
            String content = remoteMessage.getData().get("body");
            createNotification(title,content);
              }
    }

    private void createNotification(String title, String content) {
        //GlobalBus_Event.getBus().post(new EventNotification("1"));
        //Intent intent = new Intent(this, SplashActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        /*PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);*/

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon2)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))
                .setContentText(content)
                .setColor(ContextCompat.getColor(this, R.color.colorBottomBar))
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(notificationSoundURI);
        //.setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(0, mNotificationBuilder.build());
    }
}
