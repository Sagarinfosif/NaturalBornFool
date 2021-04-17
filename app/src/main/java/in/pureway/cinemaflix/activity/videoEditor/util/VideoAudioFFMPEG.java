package in.pureway.cinemaflix.activity.videoEditor.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

public class VideoAudioFFMPEG {

    Context context;

    AudioTrimCompleted audioTrimCompleted;
    AudioExtraction audioExtraction;
    WaterMarkAdd waterMarkAdd;
    GifCompleted gifCompleted;
    MuteVideo muteVideo;
    Thumbnail thumbnail;




    public interface Thumbnail{
        void onThumbnailCompleteListener();
    }
    public interface MuteVideo{
        void onMuteVideoListener();
    }

    public interface WaterMarkAdd {
        void onWaterMarkAddListener();

    }

    public interface AudioExtraction {
        void onAudioExtractCompletedListener();
    }

    public interface AudioTrimCompleted {
        void onMergeCompletedListener();

        void onAudioFormatCompletedListener();

    }

    SpeedVideo speedVideo;

    public interface SpeedVideo{
        void onSpeedCompletedListener();
    }

    public VideoAudioFFMPEG(Context context, SpeedVideo speedVideo) {
        this.context = context;
        this.speedVideo = speedVideo;
    }
    public VideoAudioFFMPEG(Context context, Thumbnail thumbnail) {
        this.context = context;
        this.thumbnail = thumbnail;
    }

    public interface GifCompleted {
        void onGIFCompletedListener();
    }

    public VideoAudioFFMPEG(Context context, AudioTrimCompleted audioTrimCompleted) {
        this.context = context;
        this.audioTrimCompleted = audioTrimCompleted;
    }

    public VideoAudioFFMPEG(Context context, AudioExtraction audioExtraction) {
        this.context = context;
        this.audioExtraction = audioExtraction;
    }

    public VideoAudioFFMPEG(Context context, WaterMarkAdd waterMarkAdd) {
        this.context = context;
        this.waterMarkAdd = waterMarkAdd;
    }

    public VideoAudioFFMPEG(Context context, GifCompleted gifCompleted) {
        this.context = context;
        this.gifCompleted = gifCompleted;
    }


    public void executeTrimCommand(String[] command) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Audio");
        progressDialog.setTitle("Processing...");
        progressDialog.show();
        FFmpeg.executeAsync(command, new ExecuteCallback() {
            @Override
            public void apply(long executionId, int rc) {
                if (rc == RETURN_CODE_SUCCESS) {
                    audioTrimCompleted.onAudioFormatCompletedListener();
                    Log.i(Config.TAG, "Async command execution completed successfully.");
                } else if (rc == RETURN_CODE_CANCEL) {
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    Log.i(Config.TAG, String.format("Async command execution failed with rc=%d.", rc));
                }
                progressDialog.dismiss();
            }
        });
    }

    public void executeMergeCommand(String[] command) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Video");
        progressDialog.setTitle("Processing...");
        progressDialog.show();
        FFmpeg.executeAsync(command, new ExecuteCallback() {
            @Override
            public void apply(long executionId, int rc) {
                if (rc == RETURN_CODE_SUCCESS) {
                    audioTrimCompleted.onMergeCompletedListener();
                    Log.i(Config.TAG, "Async command execution completed successfully.");
                } else if (rc == RETURN_CODE_CANCEL) {
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    Log.i(Config.TAG, String.format("Async command execution failed with rc=%d.", rc));
                }
                progressDialog.dismiss();
            }
        });
    }

    public void executeAudioExtractionCommand(String[] command) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Audio Extraction");
        progressDialog.setTitle("Processing...");
        progressDialog.show();
        FFmpeg.executeAsync(command, new ExecuteCallback() {
            @Override
            public void apply(long executionId, int rc) {
                if (rc == RETURN_CODE_SUCCESS) {
                    audioExtraction.onAudioExtractCompletedListener();
                    Log.i(Config.TAG, "Async command execution completed successfully.");
                } else if (rc == RETURN_CODE_CANCEL) {
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    Log.i(Config.TAG, String.format("Async command execution failed with rc=%d.", rc));
                }
                progressDialog.dismiss();
            }
        });
    }

    public void executeGifCommand(String[] command) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Gif Extraction");
        progressDialog.setTitle("Processing...");
        progressDialog.show();
        FFmpeg.executeAsync(command, new ExecuteCallback() {
            @Override
            public void apply(long executionId, int rc) {
                if (rc == RETURN_CODE_SUCCESS) {
                    gifCompleted.onGIFCompletedListener();
                    Log.i(Config.TAG, "Async command execution completed successfully.");
                } else if (rc == RETURN_CODE_CANCEL) {
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    Log.i(Config.TAG, String.format("Async command execution failed with rc=%d.", rc));
                }
                progressDialog.dismiss();
            }
        });
    }


    public void executeWaterMarkCommand(String[] command) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Video");
        progressDialog.setTitle("Processing...");
        progressDialog.show();
        FFmpeg.executeAsync(command, new ExecuteCallback() {
            @Override
            public void apply(long executionId, int rc) {
                if (rc == RETURN_CODE_SUCCESS) {
                    waterMarkAdd.onWaterMarkAddListener();
                    Log.i(Config.TAG, "Async command execution completed successfully.");
                } else if (rc == RETURN_CODE_CANCEL) {
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    Log.i(Config.TAG, String.format("Async command execution failed with rc=%d.", rc));
                }
                progressDialog.dismiss();
            }
        });
    }

    public void speedCommand(String[] command,String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setTitle("Processing...");
        progressDialog.show();
        FFmpeg.executeAsync(command, new ExecuteCallback() {
            @Override
            public void apply(long executionId, int rc) {
                if (rc == RETURN_CODE_SUCCESS) {
                    speedVideo.onSpeedCompletedListener();
                    Log.i(Config.TAG, "Async command execution completed successfully.");
                } else if (rc == RETURN_CODE_CANCEL) {
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    Log.i(Config.TAG, String.format("Async command execution failed with rc=%d.", rc));
                }
                progressDialog.dismiss();
            }
        });
    }

    public void muteVideoCommand(String[] command,String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setTitle("Processing...");
        progressDialog.show();
        FFmpeg.executeAsync(command, new ExecuteCallback() {
            @Override
            public void apply(long executionId, int rc) {
                if (rc == RETURN_CODE_SUCCESS) {
                    speedVideo.onSpeedCompletedListener();
                    Log.i(Config.TAG, "Async command execution completed successfully.");
                } else if (rc == RETURN_CODE_CANCEL) {
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    Log.i(Config.TAG, String.format("Async command execution failed with rc=%d.", rc));
                }
                progressDialog.dismiss();
            }
        });
    }

    public void thumbnailCommand(String[] command) {
        ProgressDialog progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("message");
        progressDialog.setTitle("Processing...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(true);


        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
// do something
                FFmpeg.cancel();
                Log.i("thumbnailCommand","Cancelled");
            }
        });

        FFmpeg.executeAsync(command, new ExecuteCallback() {
            @Override
            public void apply(long executionId, int rc) {
                if (rc == RETURN_CODE_SUCCESS) {
// videoSmoothningCommad(smoothVideo,progressDialog);
                    thumbnail.onThumbnailCompleteListener();
                    Log.i(Config.TAG, "Thumbnail Async command execution completed successfully.");
                } else if (rc == RETURN_CODE_CANCEL) {
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    Log.i(Config.TAG, String.format("Async command execution failed with rc=%d.", rc));
                }
                progressDialog.dismiss();
            }
        });
    }
}
