package org.vpbxcommunicator.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.vpbxcommunicator.LinphoneActivity;
import org.vpbxcommunicator.R;

public class FirebaseNotificationHelper extends FirebaseMessagingService {

    private static final String TAG = "FirebaseListener";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        String image = message.getNotification().getIcon();
        String title = message.getNotification().getTitle();
        String text = message.getNotification().getBody();
        String sound = message.getNotification().getSound();
        String notificationType = message.getData().get("notificationType");

        // testing
        String click_action = message.getData().get("click_action");

        int id = 0;
        Object obj = message.getData().get("id");
        if (obj != null) {
            id = Integer.valueOf(obj.toString());
        }

        // if(notification is message)
        this.sendNotification(new NotificationData(image, id, title, text, sound));

        // test
        // this.triggerCall();

        // this.sendMessage(message);
    }

    private void triggerCall() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClass(this, LinphoneActivity.class);
        intent.putExtra("FirebaseCall", true);
        startService(intent);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = null;
        try {
            notificationBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.vpbx_launcher_green_round)
                            .setContentTitle("This is a call")
                            .setContentText("This is a call")
                            .setAutoCancel(true)
                            .setSound(
                                    RingtoneManager.getDefaultUri(
                                            RingtoneManager.TYPE_NOTIFICATION))
                            .setContentIntent(pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (notificationBuilder != null) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            int hardcodedID = 123123;
            notificationManager.notify(hardcodedID, notificationBuilder.build());

            /*NotificationsManager notificationsManager = new NotificationsManager(this);
            notificationsManager.startForeground(
                    notificationBuilder.build(), notificationData.getId());*/
        } else {
            Log.d(TAG, "Notification object could not be created");
        }
    }

    private void sendNotification(NotificationData notificationData) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClass(this, LinphoneActivity.class);
        intent.putExtra("FirebasePushText", true);
        startService(intent);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this, 0 /* Request code*/, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = null;
        try {
            notificationBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.vpbx_launcher_green_round)
                            .setContentTitle(
                                    URLDecoder.decode(notificationData.getTitle(), "UTF-8"))
                            .setContentText(
                                    URLDecoder.decode(notificationData.getTextMessage(), "UTF-8"))
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setSound(
                                    RingtoneManager.getDefaultUri(
                                            RingtoneManager.TYPE_NOTIFICATION))
                            .setContentIntent(pendingIntent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (notificationBuilder != null) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(notificationData.getId(), notificationBuilder.build());

            /*NotificationsManager notificationsManager = new NotificationsManager(this);
            notificationsManager.startForeground(
                    notificationBuilder.build(), notificationData.getId());*/
        } else {
            Log.d(TAG, "Notification object could not be created");
        }
    }

    // FirebaseInstanceID - get token
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("New token ", s);
    }

    /*create a notifiable in order to use Compatibility.createMessageNotification()
    private Notifiable createNotifiable(RemoteMessage message) {
        int messageID = Integer.parseInt(message.getMessageId());
        Notifiable notif = new Notifiable(messageID);

        // create notifiablemessage from remote message
        NotifiableMessage notifiableMessage = createNotifMessage(message);

        // add notif fields
        notif.addMessage(notifiableMessage);
        notif.setIsGroup(false);

        return notif;
    }

    // create notifiable message
    private NotifiableMessage createNotifMessage(RemoteMessage remoteMessage) {
        String message = remoteMessage.getNotification().getBody();
        String sender = remoteMessage.getNotification().getTitle();
        long time = remoteMessage.getSentTime();

        // filepath uri and filename = null
        NotifiableMessage notifiableMessage =
                new NotifiableMessage(message, sender, time, null, null);

        return notifiableMessage;
    }*/
}
