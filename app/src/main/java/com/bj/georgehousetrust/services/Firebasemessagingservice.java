package com.bj.georgehousetrust.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.bj.georgehousetrust.R;
import com.bj.georgehousetrust.activities.SplashScreen;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import static android.os.Build.VERSION.SDK_INT;
public class Firebasemessagingservice extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID = "Notifications" ;
    PendingIntent pendingIntent;
    int notificationID=10;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            int notificationID = new Random().nextInt(3000);
            if (SDK_INT >= Build.VERSION_CODES.O) {
                setupChannels(notificationManager);
            }


        if (SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }
        Intent intent = new Intent(Firebasemessagingservice.this, SplashScreen.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            pendingIntent = PendingIntent.getActivity(this , 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        }else
        {
            pendingIntent = PendingIntent.getActivity(this , 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.applogo);

            Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("message"))
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSound(notificationSoundUri)
                    .setContentIntent(pendingIntent);

            //Set notification color to match your app color template
            if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                notificationBuilder.setColor(getResources().getColor(R.color.white));
            }
            notificationManager.notify(notificationID, notificationBuilder.build());

    }



    private void setupChannels(NotificationManager notificationManager){
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Fast Connection";

        NotificationChannel adminChannel;
        if (SDK_INT >= Build.VERSION_CODES.O) {
            adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);

            adminChannel.setDescription(adminChannelDescription);
            adminChannel.enableLights(true);
            adminChannel.setLightColor(Color.RED);
            adminChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            adminChannel.enableVibration(true);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(adminChannel);
            }
        }
    }


}
