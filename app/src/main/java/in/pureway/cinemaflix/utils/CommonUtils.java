package in.pureway.cinemaflix.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.utils.Utils;
import com.bumptech.glide.Glide;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.PrivacySettingsActivity;
import in.pureway.cinemaflix.activity.videoEditor.util.Variables;
import in.pureway.cinemaflix.activity.videoEditor.util.VideoAudioFFMPEG;
import in.pureway.cinemaflix.models.ModelLoginRegister;

import com.downloader.BuildConfig;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;

import java.io.File;
import java.util.Locale;


public class CommonUtils {

    private static ProgressDialog progressDialog;
    private static AlertDialog customprogressDialog;
    private static AlertDialog dailogbox;
    private static int b = 0;
    private static int bb = 0;
    private static ProgressBar progressBarOne;
    private static TextView textViewProgressOne;
    private static Button button_cancel;
    private static Dialog dialog;
  private static DownloadRequest prDownloader;


    private static Handler handler = new Handler();

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void shareIntent(Context context, String title, String url) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);

        context.startActivity(Intent.createChooser(shareIntent, "Share via"));

    }


    public static void shareVideo(Context context, String title, String message) {
        message = "message";
        Intent shareIntent = new Intent();
        title = "title";
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.getPackage();
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        shareIntent.setType("text/plain");

        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Title");

        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Variables.shareVideoLink));
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Download CINEMAFLIX- Short & Long Videos App by Pureway\n" +
                "Enjoy the most viral & trending short & long videos\n" +
                "Click here to download now\n" + "https://play.google.com/store/apps/details?id=in.pureway.cinemaflix");

        shareIntent.setType("video/mp4");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "Share Via"));


    }


    public static void shareVideoS(Context context, String url) {
//        message = "message";
        Intent shareIntent = new Intent();
//        title = "title";
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.getPackage();
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        shareIntent.setType("text/plain");

        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Title");

        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Variables.shareVideoLink));
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Download CINEMAFLIX- Short & Long Videos App by Pureway\n" +
                "Enjoy the most viral & trending short & long videos\n" +
                "Click here to download now\n" + "https://play.google.com/store/apps/details?id=in.pureway.cinemaflix");

        shareIntent.setType("video/mp4");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareVideoDownloadIntent(shareIntent, context, url);
        context.startActivity(Intent.createChooser(shareIntent, "Share Via"));


    }


    public static void showProgress(Activity activity, String message) {
        progressDialog = new ProgressDialog(activity, R.style.DialogStyle);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();

        }
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static String userId(Activity activity) {
        String userId = "";
        if (App.getSharedpref().isLogin(activity)) {
            userId = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getId();
        }
        return userId;
    }

    public static void sendSmsInvite(String srcNumber, String username, Context context) {


        String shareMessage = "Hello there, \n" + "I'm invited you to join CINEMAFLIX.\n\n" + "Download CINEMAFLIX from PlayStore.";
        shareMessage = shareMessage + "\n https://play.google.com/store/apps/details?id=in.pureway.cinemaflix";
        Uri sms_uri = Uri.parse("smsto:" + srcNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        intent.putExtra("sms_body", shareMessage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    // this will hide the bottom mobile navigation controll
    public static void hideNavigation(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


// This work only for android 4.4+
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
// Code below is to handle presses of Volume up or Volume down.
// Without this, after pressing volume buttons, the navigation bar will
// show up and won't hide
            final View decorView = activity.getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
    }

    public static void changeLanguage(Activity activity) {
        App.changeLanguage(activity, App.getSharedpref().getString(AppConstants.APP_LANGUAGE));
    }

    public static void spinLoader(Activity activity) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_spinkit, null);
        dailogbox = new AlertDialog.Builder(activity).create();
        dailogbox.setCancelable(true);
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setView(confirmdailog);
        dailogbox.show();
    }

    public static void dismissSpinKit() {
        if (dailogbox != null && dailogbox.isShowing()) {
            dailogbox.dismiss();
        }
    }

    public static void customLoader(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View confirmdailog = layoutInflater.inflate(R.layout.custom_loading_layout, null);
        customprogressDialog = new AlertDialog.Builder(context).create();
        customprogressDialog.setCancelable(true);
        customprogressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customprogressDialog.setView(confirmdailog);
        customprogressDialog.show();
        ImageView gifImageView = confirmdailog.findViewById(R.id.custom_loading_imageView);
        Glide.with(context).load(R.raw.dots).centerCrop().into(gifImageView);
        customprogressDialog.show();
    }

    public static void hideCustomLoader() {
        if (customprogressDialog.isShowing()) {
            customprogressDialog.dismiss();
        }
    }

    public static void downloadLogo(final Context context, final String url) {

        DownloadRequest prDownloader;
        PRDownloader.initialize(context);

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Downloading...");

//        progressDialog.show();

        prDownloader = PRDownloader.download(url, Variables.app_folder, "logo.gif")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                });

        prDownloader.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                Log.i("dowbloadCOmplete", "dowbloadCOmplete");
                progressDialog.dismiss();
//                Toast.makeText(context, "logo Downloaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Error error) {
                progressDialog.dismiss();
            }
        });

    }


    public static void addWatermark(Context context, String url) {

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        File Watermarkedfile = new File(Variables.share_video_folder + "/" + ts + Variables.shareVideoName);

        File file = new File(Variables.root + "/Cinemaflix/logo.gif");
        if (!file.exists()) {
            Log.i("mobile", "photo_exists");
// Toast.makeText(this, "Logo not exits", Toast.LENGTH_SHORT).show();
            return;
        }

        VideoAudioFFMPEG videoAudioFFMPEG = new VideoAudioFFMPEG(context, new VideoAudioFFMPEG.WaterMarkAdd() {
            @Override
            public void onWaterMarkAddListener() {

                shareVideo(context, "", "");


            }

        });
//        shareVideo(context, "", "");

        videoAudioFFMPEG.executeWaterMarkCommand(addwaterMark(file.getAbsolutePath(), url, Watermarkedfile.getAbsolutePath()));
    }

    public static String[] addwaterMark(String imageUrl, String videoUrl, String outputUrl) {
//         -i "input.mp4"  "drawtext=text='a watermark':x=10:y=H-th-10:fontfile=/pathto/font.ttf:fontsize=10:fontcolor=white:shadowcolor=black:shadowx=2:shadowy=2" "output.mp4"
//        String[] command1 = {"ffmpeg","-i", videoUrl,  "-vf", "drawtext=text='a watermark':x=10:y=H-th-10:fontfile=/pathto/font.ttf:fontsize=10:fontcolor=white:shadowcolor=black:shadowx=2:shadowy=2",  outputUrl};


//        String[] command = new String[]{"-i", outputUrl, "-vcodec", "h264", "-b:v", "1000k", "-acodec", "mp3" ,"-preset", "ultrafast", Variables.share_video_folder + "/" + Variables.shareVideoName};

        String[] command1 = {"-i", videoUrl, "-i", imageUrl, "-filter_complex", "overlay=15:15", outputUrl};
//        String[] commands = new String[8];
//// commands[0] = "ffmpeg";
////Enter
//        commands[0] = "-i";
//        commands[1] = videoUrl;
////watermark
//        commands[2] = "-i";
//        commands[3] = imageUrl;//The picture address here is replaced with a video with a transparent channel to synthesize a dynamic video mask.
//        commands[4] = "-filter_complex";
//        commands[5] = "overlay=15:15";
////Overwrite output
//        commands[6] = "-y";// directly overwrite the output file
//// Output file
//        commands[7] = outputUrl;

        File deleteDraftFile = new File(videoUrl);
        if (deleteDraftFile.exists()) {
            deleteDraftFile.delete();
        }

        return command1;
    }

    private static void shareVideoDownloadIntent(Intent shareIntent, final Context context, final String url) {

        DownloadRequest prDownloader;
        PRDownloader.initialize(context);

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Sharing...");
        progressDialog.show();
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        File sahredFolder = new File(url);
        if (!sahredFolder.exists()) {
            sahredFolder.mkdir();
        }
        prDownloader = PRDownloader.download(url, Variables.share_video_folder, "Download Video" + "/" + Variables.shareVideoName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        Log.i("download", "onStartOrResume");
// addWatermark(context,url );
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {
                        Log.i("download", "onPause");
                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        Log.i("download", "onCancel");
                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        Log.i("download", progress.toString());
                    }
                });

        prDownloader.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                Log.i("dowbloadCOmplete", "dowbloadCOmplete");

                progressDialog.dismiss();
//                addWatermark(context,url);
                shareVideo(context, "", "");

            }

            @Override
            public void onError(Error error) {
                progressDialog.dismiss();
            }
        });

    }

    public static void videoDownload(final Context context, final String url) {
        DownloadRequest prDownloader;
        PRDownloader.initialize(context);
        share_Dialog(context);

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Sharing...");
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        b = 0;
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressDialog.show();
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        progressDialog.setIndeterminate(true);
//        progressBarOne.setIndeterminate(true);

        File sahredFolder = new File(url);
        if (!sahredFolder.exists()) {
            sahredFolder.mkdir();
        }
//        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Downloader/";
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Videos";

//        prDownloader = PRDownloader.download(url, Variables.app_folder1, ts+Variables.videoDownlaod)
        prDownloader = PRDownloader.download(url, path, Variables.videoDownlaod + ts + ".mp4")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        Log.i("download", "onStartOrResume");
// addWatermark(context,url );

//                        new Thread(new Runnable() {
//                            public void run() {
//
//                                while (b < 80) {
//
////
//                                    handler.post(new Runnable() {
//                                        public void run() {
//
//                                            progressDialog.setProgress(b);
//                                        }
//
//                                    });
//                                    b += 1;
//                                    try {
//
//                                        // Sleep for 200 milliseconds.
//                                        Thread.sleep(100);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }).start();

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {
                        Log.i("download", "onPause");
                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                        Log.i("download", "onCancel");
                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {


                        Log.i("download", progress.toString());

                    }
                });

        prDownloader.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                Log.i("dowbloadCOmplete", "dowbloadCOmplete");
                bb = progressDialog.getProgress();

                progressDialog.setProgress(bb);


//                while (bb < 100) {
//
////
//                    handler.post(new Runnable() {
//                        public void run() {
//
//                            progressDialog.setProgress(bb);
//
//                        }
//
//                    });
//                    bb += 1;
//
//                }

//                new Thread(new Runnable() {
//                    public void run() {
//                        try {
//
//                            // Sleep for 200 milliseconds.
////                            shareVideo(context, "", "");
////                                            addWatermark(context,url);
//
//                            progressDialog.dismiss();
//
//
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }).start();
                Toast.makeText(context, "Video download successfully", Toast.LENGTH_SHORT).show();

                MediaScannerConnection.scanFile(context,
                        new String[]{Environment.getExternalStorageDirectory().toString()},
                        null,
                        new MediaScannerConnection.OnScanCompletedListener() {

                            public void onScanCompleted(String path, Uri uri) {

                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });

//                addWatermark(context,url);

            }

            @Override
            public void onError(Error error) {
                Toast.makeText(context,"error.getServerErrorMessage()",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }


//    public static void shareVideoDownload(final Context context, final String url) {
//        DownloadRequest prDownloader;
//        PRDownloader.initialize(context);
//
//        final ProgressDialog progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Sharing...");
//        progressDialog.setProgress(0);
//        progressDialog.setMax(100);
//        b = 0;
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//
//        progressDialog.show();
//        Long tsLong = System.currentTimeMillis() / 1000;
//        String ts = tsLong.toString();
//
//
//        File sahredFolder = new File(url);
//        if (!sahredFolder.exists()) {
//            sahredFolder.mkdir();
//        }
//        prDownloader = PRDownloader.download(url, Variables.share_video_folder, Variables.shareVideoName)
//                .build()
//                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
//                    @Override
//                    public void onStartOrResume() {
//                        Log.i("download", "onStartOrResume");
//// addWatermark(context,url );
//
//                        new Thread(new Runnable() {
//                            public void run() {
//
//                                while (b < 80) {
//
////
//                                    handler.post(new Runnable() {
//                                        public void run() {
//
//                                            progressDialog.setProgress(b);
//                                        }
//
//                                    });
//                                    b += 1;
//                                    try {
//
//                                        // Sleep for 200 milliseconds.
//                                        Thread.sleep(100);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }).start();
//
//                    }
//                })
//                .setOnPauseListener(new OnPauseListener() {
//                    @Override
//                    public void onPause() {
//                        Log.i("download", "onPause");
//                    }
//                })
//                .setOnCancelListener(new OnCancelListener() {
//                    @Override
//                    public void onCancel() {
//                        Log.i("download", "onCancel");
//                    }
//                })
//                .setOnProgressListener(new OnProgressListener() {
//                    @Override
//                    public void onProgress(Progress progress) {
//
//
//                        Log.i("download", progress.toString());
//
//                    }
//                });
//
//        prDownloader.start(new OnDownloadListener() {
//            @Override
//            public void onDownloadComplete() {
//                Log.i("dowbloadCOmplete", "dowbloadCOmplete");
//                bb =progressDialog.getProgress();
//
//                progressDialog.setProgress(bb);
//
//
//                while (bb < 100) {
//
////
//                    handler.post(new Runnable() {
//                        public void run() {
//
//                            progressDialog.setProgress(bb);
//
//                        }
//
//                    });
//                    bb += 1;
//
//                }
//
//                new Thread(new Runnable() {
//                    public void run() {
//                        try {
//
//                            // Sleep for 200 milliseconds.
//                            shareVideo(context, "", "");
////                                            addWatermark(context,url);
//
//                            progressDialog.dismiss();
//
//
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }).start();
//
////                addWatermark(context,url);
//
//            }
//
//            @Override
//            public void onError(Error error) {
//                progressDialog.dismiss();
//            }
//        });
//
//    }


    public static void shareVideoDownload(final Context context, final String url) {
        PRDownloader.initialize(context);
//        progressBarOne.setIndeterminate(true);


        share_Dialog(context);
//        final ProgressDialog progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Sharing...");
//        progressDialog.setProgress(0);
//        progressDialog.setMax(100);
//        b = 0;
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//
//        progressDialog.show();
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        progressBarOne.setIndeterminate(true);

        File sahredFolder = new File(url);
        if (!sahredFolder.exists()) {
            sahredFolder.mkdir();
        }
        prDownloader = PRDownloader.download(url, Variables.share_video_folder, Variables.shareVideoName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        Log.i("download", "onStartOrResume");
// addWatermark(context,url );
                        progressBarOne.setIndeterminate(false);


                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {
                        Log.i("download", "onPause");
                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        dialog.dismiss();
                        Log.i("download", "onCancel");
                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                        long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                        progressBarOne.setProgress((int) progressPercent);
                        textViewProgressOne.setText(getProgressDisplayLine(progress.currentBytes, progress.totalBytes));

                        Log.i("download", progress.toString());

                    }
                });

        prDownloader.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                Log.i("dowbloadCOmplete", "dowbloadCOmplete");

                dialog.dismiss();
                shareVideo(context, "", "");

//                addWatermark(context,url);

            }

            @Override
            public void onError(Error error) {
                dialog.dismiss();
            }
        });

    }


    private static void share_Dialog(Context context) {
        dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_progress_layout);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);


//        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        progressBarOne = dialog.findViewById(R.id.progressBarOne);
        button_cancel = dialog.findViewById(R.id.button_cancel);
        textViewProgressOne = dialog.findViewById(R.id.textViewProgressOne);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prDownloader.cancel();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    public static String getProgressDisplayLine(long currentBytes, long totalBytes) {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes);
    }

    private static String getBytesToMBString(long bytes) {
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }
}
