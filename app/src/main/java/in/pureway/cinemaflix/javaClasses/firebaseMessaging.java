package in.pureway.cinemaflix.javaClasses;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.HomeActivity;
import in.pureway.cinemaflix.activity.chat.NotificationMessagesActivity;
import in.pureway.cinemaflix.models.ModelSendMessage;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.Serializable;
import java.util.Random;

public class firebaseMessaging extends FirebaseMessagingService {
    private final int NOTIFICATION_ID = 10;
    NotificationChannel channel = null;
    Uri defaultSound;
    Notification notification;
    NotificationChannel mChannel;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String type = remoteMessage.getData().get("type");
        if (type.equalsIgnoreCase(AppConstants.NOTI_FOLLOW)) {
            String loginId = remoteMessage.getData().get("loginId");
            String otherUserId = remoteMessage.getData().get("userId");

            App.getSingleton().setMyId(loginId);
            App.getSingleton().setOtherUserId(otherUserId);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setBookingOreoNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), "", type, "");
            } else {
                showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), "", type, "");
            }
        } else if (type.equalsIgnoreCase(AppConstants.NOTI_LIKE)) {

            String loginId = remoteMessage.getData().get("loginId");
            String otherUserId = remoteMessage.getData().get("userId");

            App.getSingleton().setMyId(loginId);
            App.getSingleton().setOtherUserId(otherUserId);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setBookingOreoNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), "", type, "");
            } else {
                showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), "", type, "");
            }
        } else if (type.equalsIgnoreCase(AppConstants.VIDEO)) {
            String loginId = remoteMessage.getData().get("loginId");
            String otherUserId = remoteMessage.getData().get("userId");

            App.getSingleton().setMyId(loginId);
            App.getSingleton().setOtherUserId(otherUserId);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setBookingOreoNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), "", type, "");
            } else {
                showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), "", type, "");
            }
        } else if (type.equalsIgnoreCase(AppConstants.COMMENT)) {
            String loginId = remoteMessage.getData().get("loginId");
            String otherUserId = remoteMessage.getData().get("userId");

            App.getSingleton().setMyId(loginId);
            App.getSingleton().setOtherUserId(otherUserId);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setBookingOreoNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), "", type, "");
            } else {
                showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), "", type, "");
            }
        }else if (type.equalsIgnoreCase(AppConstants.REJECTVIDEO)) {
            String loginId = remoteMessage.getData().get("loginId");
            String otherUserId = remoteMessage.getData().get("userId");

            App.getSingleton().setMyId(loginId);
            App.getSingleton().setOtherUserId(otherUserId);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setBookingOreoNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), "", type, "");
            } else {
                showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), "", type, "");
            }
        }
        else if (type.equalsIgnoreCase(AppConstants.ApproveVideo)) {
            String loginId = remoteMessage.getData().get("loginId");
            String otherUserId = remoteMessage.getData().get("userId");

            App.getSingleton().setMyId(loginId);
            App.getSingleton().setOtherUserId(otherUserId);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setBookingOreoNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), "", type, "");
            } else {
                showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), "", type, "");
            }
        }

        else if (type.equalsIgnoreCase(AppConstants.DELETEVIDEO)) {
            String loginId = remoteMessage.getData().get("loginId");
            String otherUserId = remoteMessage.getData().get("userId");

            App.getSingleton().setMyId(loginId);
            App.getSingleton().setOtherUserId(otherUserId);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setBookingOreoNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), "", type, "");
            } else {
                showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), "", type, "");
            }
        }




        else if (type.equalsIgnoreCase(AppConstants.PUSH_NOTIFICATION)) {
            String loginId = remoteMessage.getData().get("loginId");
            String otherUserId = remoteMessage.getData().get("userId");

            App.getSingleton().setMyId(loginId);
            App.getSingleton().setOtherUserId(otherUserId);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setBookingOreoNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), "", type, "");
            } else {
                showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), "", type, "");
            }
        } else if (type.equalsIgnoreCase(AppConstants.MESSAGE)) {
            String loginId = remoteMessage.getData().get("loginId");
            String otherUserId = remoteMessage.getData().get("userId");
            App.getSingleton().setMyId(loginId);
            App.getSingleton().setOtherUserId(otherUserId);
            if (!App.isAppOnForeground(getApplicationContext(), getApplicationContext().getPackageName())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    setBookingOreoNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("mainMessage"), AppConstants.MESSAGE, remoteMessage.getData().get("image"));
                } else {
                    showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("mainMessage"), AppConstants.MESSAGE, remoteMessage.getData().get("image"));
                }


            } else {
                ModelSendMessage.Detail detail = new ModelSendMessage.Detail();
                detail.setImage(remoteMessage.getData().get("image"));
                detail.setId(Integer.parseInt(remoteMessage.getData().get("id")));
                detail.setSenderId(remoteMessage.getData().get("sender_id"));
                detail.setReciverId(remoteMessage.getData().get("reciver_id"));
                detail.setMessage(remoteMessage.getData().get("mainMessage"));
                detail.setMessageType(remoteMessage.getData().get("messageType"));
                detail.setTime(remoteMessage.getData().get("time"));
                detail.setCreated(remoteMessage.getData().get("created"));

                Intent intent = new Intent(AppConstants.ACTION_MESSAGE_NOTIFICATION);
                intent.putExtra(AppConstants.OTHER_USER_ID, remoteMessage.getData().get("sender_id"));
                intent.putExtra(AppConstants.MESSAGE_RESPOSNE, (Serializable) detail);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                Log.i("notification", remoteMessage.getData().get("type"));
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setBookingOreoNotification(String title, String message, String s, String type, String image) {
        Intent intent = null;
        if (type == AppConstants.MESSAGE) {
            intent = new Intent(this, NotificationMessagesActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent(this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", AppConstants.NOTIFICATION);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101,
                intent, PendingIntent.FLAG_ONE_SHOT);

        defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

// Sets an ID for the notification, so it can be updated.
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "Cinemaflix";// The user-visible name of the channel.

        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }
        final Notification.Builder builder = new Notification.Builder(getApplicationContext());
// Create a notification and set the notification channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setGroup(type)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.logo)
                    .setSound(defaultSound)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .build();

            if (type == AppConstants.MESSAGE && !s.equalsIgnoreCase("")) {
                builder.setStyle(new Notification.BigTextStyle(builder)
                        .bigText(s)
                        .setBigContentTitle(message)
                        .setSummaryText("You Received a New Message"))
                        .setContentTitle("Title")
                        .setContentText(message)
                        .setVibrate(new long[]{200, 200, 200, 200})
                        .setSound(defaultSound)
                        .setChannelId(CHANNEL_ID)
                        .setContentIntent(pendingIntent)
                        .setGroup(type)
                        .setSmallIcon(R.drawable.logo);
            }

        }
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(mChannel);
        }

// Issue the notification.
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        if (type == AppConstants.MESSAGE) {
            if (image.equalsIgnoreCase("")) {
                mNotificationManager.notify(m, builder.build());
            } else {
                builder.setStyle(new Notification.BigTextStyle(builder)
                        .setBigContentTitle(message)
                        .setSummaryText("You Received a Message."))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setGroup(AppConstants.MESSAGE)
                        .setVibrate(new long[]{200, 200, 200, 200})
                        .setSound(defaultSound)
                        .setSmallIcon(R.drawable.logo);

                Glide.with(getApplicationContext()).asBitmap().load(image).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        builder.setLargeIcon(resource);
                        mNotificationManager.notify(m, builder.build());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
            }
        } else {
            mNotificationManager.notify(m, notification);
        }
    }

    private void showNotification(String title, String message, String sx, String type, String image) {
        Intent intent = null;
        if (type == AppConstants.MESSAGE) {
            intent = new Intent(this, NotificationMessagesActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent(this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", AppConstants.NOTIFICATION);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_ONE_SHOT);
        defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        if (type == AppConstants.MESSAGE) {
            final Notification.Builder builder = new Notification.Builder(getApplicationContext());
            Notification.BigTextStyle textStyle = new Notification.BigTextStyle(builder);
            if (image.equalsIgnoreCase("")) {
                builder.setStyle(new Notification.BigTextStyle(builder)
                        .bigText(sx)
                        .setBigContentTitle(message)
                        .setSummaryText("You Received a New Message"))
                        .setContentTitle(title)
                        .setContentText(message)
                        .setVibrate(new long[]{200, 200, 200, 200})
                        .setSound(defaultSound)
                        .setContentIntent(pendingIntent)
                        .setGroup(type)
                        .setSmallIcon(R.drawable.logo);

                notificationManager.notify(m, builder.build());
            } else {
                Notification.BigPictureStyle pictureStyle = new Notification.BigPictureStyle(builder);
                builder.setStyle(pictureStyle
                        .setBigContentTitle(message)
                        .setSummaryText("You Received a Message"))
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setGroup(AppConstants.MESSAGE)
                        .setVibrate(new long[]{200, 200, 200, 200})
                        .setSound(defaultSound)
                        .setSmallIcon(R.drawable.logo);

                Glide.with(getApplicationContext()).asBitmap().load(image).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        builder.setLargeIcon(resource);
                        notificationManager.notify(m, builder.build());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
            }
        } else {
            notification = new NotificationCompat.Builder(this)
                    .setGroup(type)
                    .setContentText(message)
                    .setContentTitle(title)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{200, 200, 200, 200})
                    .setSound(defaultSound)
                    .build();
            notificationManager.notify(m, notification);
        }
    }

}