package in.pureway.cinemaflix.activity.videoEditor.util;

import android.os.Environment;

/**
 * Created by AQEEL on 2/15/2019.
 */
public class Variables {
    public static int recording_duration=18000;
    public static int max_recording_duration=18000;

    public static final String device = "android";
    public static final int GALLERY_SELECTED_VIDEO_LENGTH = 600;
    public static int screen_width;
    public static int screen_height;
    public static final String SelectedAudio_MP3 = "/SelectedAudio.mp3";
    public static final String SelectedAudio_AAC = "/SelectedAudio.aac";
    public static final String root = Environment.getExternalStorageDirectory().toString();
//    public static final String root1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
    public static final String app_folder = root + "/Cinemaflix";
    public static final String app_folder1 = root+"/Download";
    public static final String mp3File = root + "/Cinemaflix/converted.mp3";
    public static final String downloadedmp3File = "converted.mp3";
    public static final String trimmp3File = root + "/Cinemaflix/trimmedAudio.mp3";
    public static final String watermarkedVideo = root + "/Cinemaflix/waterMarkedVideo.mp3";
    public static final String gifFile = app_folder + "/myGif.gif";
    public static final String croppedAudio = app_folder + "/croppedAudio.aac";
    public static final String arGearMediaFolder = app_folder + "/ARMedia";
public static final String arGearVideoFolder = app_folder + "/ARMedia/Videos";
    public static final String arGearEffectsFolder = app_folder + "/ARMedia/Effects";
    public static final String videoThumb = app_folder + "/videoImage.jpeg";
    public static final String videogifFile = app_folder + "/myVideoToGif.GIF";
    public static final String sound_file_extracted = app_folder + "/extracted_sound.mp3";
    public static final String draft_app_folder = app_folder + "/Draft/";
//    public static String speedVideoPath= app_folder+"/speedVideo.mp4";
    //    public static int max_recording_duration = 18000;
    public static int recording_duration_15 = 15000;
    public static int recording_duration_30 = 30000;
    public static int recording_duration_10 = 10000;
    //    public static int recording_duration = 18000;


    static Long tsLong = System.currentTimeMillis() / 1000;
    static String ts = tsLong.toString();

    public static String speedVideoPath= app_folder+"/" + ts +"speedVideo.mp4";


    public static String outputfile = app_folder + "/output.mp4";
    public static String outputfile2 = app_folder + "/output2.mp4";
    public static String output_filter_file = app_folder + "/output-filtered.mp4";
    public static String gallery_trimed_video = app_folder + "/gallery_trimed_video.mp4";
    public static String made_trimed_video = app_folder + "/" + ts + "made_trimed_video.mp4";

    public static String gallery_resize_video = app_folder + "/gallery_resize_video.mp4";
    public static final String islogin = "is_login";
    public static String tag = "Cinemaflix";
    public static String Selected_sound_id = "null";
    public final static int Pick_video_from_gallery = 791;
    public final static String share_video_folder = root + "/sharedVideos";
    public static final String shareVideoName = "/sharedVideo.mp4";
//    public static final String videoDownlaod = "cinmaflix.mp4";
    public static final String videoDownlaod = "CINEMAFLIX_";
    public static final String VideoDownloadName = videoDownlaod;
    public static final String shareVideoLink = share_video_folder+shareVideoName;
    public static final String saveVideos=app_folder+"/saveVideo";


    public static final String  app_hided_folder =root+"/.HidedTicTic/";
    public static int min_time_recording=5000;
    public static String output_frontcamera= app_folder + "/output_frontcamera.mp4";
    public static final String txtFile=app_folder+"/myTextFile.txt";
    public static String thumbnail = app_folder +"/thumb.png";

}
