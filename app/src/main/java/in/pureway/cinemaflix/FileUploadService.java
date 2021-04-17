package in.pureway.cinemaflix;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import in.pureway.cinemaflix.R;

import in.pureway.cinemaflix.activity.HomeActivity;
import in.pureway.cinemaflix.activity.videoEditor.activity.MyVideoEditorActivity;
import in.pureway.cinemaflix.activity.videoEditor.util.Variables;
import in.pureway.cinemaflix.retrofit.ApiClient;
import in.pureway.cinemaflix.retrofit.ApiInterface;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;

import java.io.File;
import java.util.Map;
import java.util.Random;

import in.pureway.cinemaflix.activity.videoEditor.activity.PostVideoActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUploadService extends Service {
    String userId, hashTag, description, allowComment, allowDuetReact, allowDownloads, viewVideo, soundId, soundTitle, isDuet, heightDuet, widthDuet, duetUrl, soundFile, videoPath, videolength, videoType;
    Context context;
    String videoHeight="",videoWidth="",thumbnail;

   private  MultipartBody.Part rb_videoThumb,rb_video;

    //    NotificationChannel mChannel;
//    Notification notification;
//    Uri defaultSound;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        showServiceNotification(videoPath);

        videoType = App.getSingleton().getVideoType();
        videolength = App.getSingleton().getVideoLength();
        videoHeight = App.getSingleton().getIsHeight();
        videoWidth = App.getSingleton().getIsWidth();


        if (intent.getExtras() != null) {
            Toast.makeText(context, "Uploading..", Toast.LENGTH_SHORT).show();
            userId = intent.getExtras().getString(PostVideoActivity.USER_ID);
            hashTag = intent.getExtras().getString(PostVideoActivity.HASHTAG);
            description = intent.getExtras().getString(PostVideoActivity.DESCRIPTION);
            viewVideo = intent.getExtras().getString(PostVideoActivity.VIEW_VIDEO);
            allowComment = intent.getExtras().getString(PostVideoActivity.ALLOW_COMMENT);
            allowDownloads = intent.getExtras().getString(PostVideoActivity.ALLOW_DOWNLOAD);
            allowDuetReact = intent.getExtras().getString(PostVideoActivity.ALLOW_DUET);
            soundId = intent.getExtras().getString(PostVideoActivity.SOUND_ID);
            soundTitle = intent.getExtras().getString(PostVideoActivity.SOUND_TITLE);
            videoPath = intent.getExtras().getString(PostVideoActivity.VIDEO_PATH);
            thumbnail = intent.getExtras().getString(AppConstants.VIDEOTHUMB);
//            videolength = intent.getExtras().getString(PostVideoActivity.VIDEOLENGTH);
//            videoType = intent.getExtras().getString(PostVideoActivity.VIDEOTYPE);


            RequestBody rb_userId = RequestBody.create(MediaType.parse("text/plain"), userId);
            RequestBody rb_description = RequestBody.create(MediaType.parse("text/plain"), description);
            RequestBody rb_view_video = RequestBody.create(MediaType.parse("text/plain"), viewVideo);
            RequestBody rb_comments = RequestBody.create(MediaType.parse("text/plain"), allowComment);
            RequestBody rb_downloads = RequestBody.create(MediaType.parse("text/plain"), allowDownloads);
            RequestBody rb_duets = RequestBody.create(MediaType.parse("text/plain"), allowDuetReact);
            RequestBody rb_hashtag = RequestBody.create(MediaType.parse("text/plain"), hashTag);
            RequestBody rb_soundId = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody rb_soundTitle = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody rb_videolength = RequestBody.create(MediaType.parse("text/plain"), videolength);
            RequestBody rb_videoType = RequestBody.create(MediaType.parse("text/plain"), videoType);
            RequestBody rb_isDuet = RequestBody.create(MediaType.parse("text/plain"), "false");
            RequestBody rb_height = RequestBody.create(MediaType.parse("text/plain"), videoHeight);
            RequestBody rb_width = RequestBody.create(MediaType.parse("text/plain"), videoWidth);
            RequestBody rb_dueturl = RequestBody.create(MediaType.parse("text/plain"), "0");



            if (thumbnail!=null){
                File file1 = new File(thumbnail);
                final RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
                rb_videoThumb = MultipartBody.Part.createFormData("imageThumb", file1.getName(), requestFile1);
            }else {
                File file1 = new File(thumbnail);
                final RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                rb_videoThumb = MultipartBody.Part.createFormData("imageThumb", file1.getName(), requestFile1);
            }


            File file = new File(videoPath);

            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            rb_video = MultipartBody.Part.createFormData("videoPath", file.getName(), requestFile);



            String soundFile = "";
            MultipartBody.Part audioPart;
            if (App.getSingleton().getSoundId() == null || App.getSingleton().getSoundId().equalsIgnoreCase("")) {
                soundFile = Variables.trimmp3File;
                File audioFile = new File(soundFile);
                RequestBody reqFile = RequestBody.create(MediaType.parse("audio/*"), "");
                audioPart = MultipartBody.Part.createFormData("soundFile", audioFile.getName(), reqFile);
            } else {
                RequestBody reqFile = RequestBody.create(MediaType.parse("audio/*"), "");
                audioPart = MultipartBody.Part.createFormData("soundFile", "", reqFile);
            }


            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            apiInterface.uploadVideo(rb_userId, rb_hashtag, rb_description, rb_comments, rb_duets, rb_downloads, rb_video, audioPart, rb_view_video, rb_soundId, rb_soundTitle, rb_videolength, rb_videoType, rb_isDuet, rb_height, rb_width, rb_dueturl,rb_videoThumb).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    if (response.body() != null) {
                        Log.i("uploadInfo", response.body().get("message").toString());
//
                        if (response.body().get("success").equals("1")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                setBookingOreoNotification("Video Uploaded", "Video Uploaded Successfully", videoPath);
                            } else {
                                showNotification("Video Uploaded", "Video Uploaded Successfully", videoPath);
                            }
//
//                            File soundFile = new File(Variables.trimmp3File);
//                            if (soundFile.exists()) {
//                                soundFile.delete();
//                            }
                            Toast.makeText(getApplicationContext(), response.body().get("message").toString(), Toast.LENGTH_SHORT).show();
                            stopSelf();
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                setBookingOreoNotification("Failed", "Video Uploading Failed", videoPath);
                            } else {
                                showNotification("Failed", "Video Uploading Failed", videoPath);
                            }
                            Toast.makeText(getApplicationContext(), response.body().get("message").toString(), Toast.LENGTH_SHORT).show();
                            stopSelf();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    stopSelf();
                    Log.i("uploadInfo", t.getMessage());
                    Toast.makeText(getApplicationContext(), "UPLOADED FAILED", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return Service.START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private NotificationManager mManager;

    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    NotificationChannel mChannel;
    Notification notification;
    Uri defaultSound;
    String CHANNEL_ID = "my_channel_01";// The id of the channel.
    CharSequence name = "CanCre";// The user-visible name of the channel.

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setBookingOreoNotification(String title, String message, String imagePath) {
        Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(imagePath, MediaStore.Images.Thumbnails.MICRO_KIND);
        Intent intent = new Intent(context, HomeActivity.class).putExtra("fragment", AppConstants.VIDEO_POST);
//        Intent intent = new Intent(this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", AppConstants.VIDEO_POST);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_ONE_SHOT);
        defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.enableLights(true);
            mChannel.setVibrationPattern(new long[]{200, 200, 200, 200});
            mChannel.setLightColor(Color.GREEN);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setGroup("MyVideoUpload")
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.logo)
                    .setSound(defaultSound)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setLargeIcon(bmThumbnail)
                    .setGroup("VIDEO_UPLOADED")
                    .setChannelId(CHANNEL_ID)
                    .build();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getManager().createNotificationChannel(mChannel);
        }
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        getManager().notify(m, notification);
    }

    private void showNotification(String title, String message, String imagePath) {
        Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(imagePath, MediaStore.Images.Thumbnails.MICRO_KIND);
// imageview_micro.setImageBitmap(bmThumbnail);
        Intent intent = new Intent(context, HomeActivity.class).putExtra("fragment", AppConstants.VIDEO_POST);
//        Intent intent = new Intent(this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("fragment", AppConstants.VIDEO_POST);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_ONE_SHOT);
        defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notification = new NotificationCompat.Builder(this)
                .setGroup("MyVideoUpload")
                .setContentText(message)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setGroup("VIDEO_UPLOADED")
                .setLargeIcon(bmThumbnail)
                .setVibrate(new long[]{200, 200, 200, 200})
                .setSound(defaultSound)
                .build();
        getManager().notify(m, notification);
    }

    private void showServiceNotification(String imagePath) {
//        Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(imagePath, MediaStore.Images.Thumbnails.MICRO_KIND);
        Intent notificationIntent = new Intent(context, HomeActivity.class).putExtra("fragment", AppConstants.VIDEO_POST);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), "upload_service_channel")
                .setContentTitle("Uploading video")
                .setContentText("Video uploading in progress...")
                .setSmallIcon(R.drawable.logo)
//                .setLargeIcon(bmThumbnail)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "upload_service_channel",
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            getManager().createNotificationChannel(serviceChannel);
        }
    }
}
