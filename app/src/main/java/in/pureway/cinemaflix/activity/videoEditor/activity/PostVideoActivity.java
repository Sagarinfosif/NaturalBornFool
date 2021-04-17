package in.pureway.cinemaflix.activity.videoEditor.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.FileUploadService;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.Video_Recording.Preview_Video_A;
import in.pureway.cinemaflix.activity.videoEditor.util.VideoAudioFFMPEG;
import in.pureway.cinemaflix.activity.HomeActivity;
import in.pureway.cinemaflix.activity.videoEditor.util.Functions;
import in.pureway.cinemaflix.activity.videoEditor.util.Variables;
import in.pureway.cinemaflix.adapters.AdapterAddedHashtags;
import in.pureway.cinemaflix.adapters.HashTagListAdapter;
import in.pureway.cinemaflix.models.ModelHashTag;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.VideoMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static in.pureway.cinemaflix.activity.videoEditor.util.Variables.thumbnail;


public class PostVideoActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ImageView img_video_thumb;
    private EditText et_video_description;
    private EditText et_hashtag_video;
    private String videoPath = "", allowComments = "1", allowDuet = "1", allowDownload = "1", description = "", videolength;
    String videoType;
    private TextView tv_view_video;
    public static final int PRIVACY_RC = 10;
    public static final String COMMENTS = "comments";
    public static final String DOWNLOADS = "downloads";
    public static final String DUETS = "duets";
    String hashtag = "", viewVideo = "0";
    private VideoMvvm videoMvvm;
    private List<ModelHashTag.Detail> listHashTags = new ArrayList<>();
    private RecyclerView recycler_hashtags;
    List<String> listAddedHashTags = new ArrayList<>();
    private AdapterAddedHashtags adapterAddedHashtags;
    private boolean hashTagFocues = false;
    private Switch switch_save_video;
    private int type = 0;
    private int Drafts = 0;
    private int SaveVideo = 1;
    private String savedVideoPath = "",videoHeight="",videoWidth="";
    private Spinner spinner_privacy_video;
    private Switch switch_download, switch_duet_react, switch_comments;
    private String getThumbnail="",thumbnailPath;

    public static String USER_ID = "USER_ID", DESCRIPTION = "DESCRIPTION", VIEW_VIDEO = "VIEW_VIDEO", ALLOW_COMMENT = "ALLOW_COMMENT", ALLOW_DOWNLOAD = "ALLOW_DOWNLOAD", ALLOW_DUET = "ALLOW_DUET", HASHTAG = "HASHTAG", SOUND_ID = "SOUND_ID", SOUND_TITLE = "SOUND_TITLE", IS_DUET = "IS_DUET", HEIGHT = "HEIGHT", WIDTH, URL, SOUND_FILE, VIDEO_PATH = "VIDEO_PATH", VIDEOLENGTH = "videolength", VIDEOTYPE = "videotype";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(PostVideoActivity.this);
        setContentView(R.layout.activity_post_video);
        videoMvvm = ViewModelProviders.of(PostVideoActivity.this).get(VideoMvvm.class);

        videolength = App.getSingleton().getVideoLength();
        videoType = App.getSingleton().getVideoType();
        videoHeight = App.getSingleton().getIsHeight();
        videoWidth = App.getSingleton().getIsWidth();

        findIds();
//        autoCompleteHashTags();
//        setAddedRecyclerHashTag();
        if (getIntent().getExtras() != null) {   //video from drafts

            if (getIntent().hasExtra(AppConstants.VIDEO_PATH)) {

                videoPath = getIntent().getExtras().getString(AppConstants.VIDEO_PATH);
            }
            if (getIntent().hasExtra(AppConstants.VIDEO_DESCRIPTION)) {
                description = getIntent().getExtras().getString(AppConstants.VIDEO_DESCRIPTION);
                et_video_description.setText(description);
            }
        } else {
            videoPath = Variables.output_filter_file;   //video direct from preview activity
        }
        thumbnail(videoPath);
//        saveVideo();
//        Log.i("videoPath", videoPath);
        Bitmap bmThumbnail;
        bmThumbnail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        if (bmThumbnail != null) {
            Glide.with(PostVideoActivity.this).load(bmThumbnail).placeholder(R.drawable.poat).error(R.drawable.poat).centerCrop().into(img_video_thumb);
        }
//        if (bmThumbnail!=null) {
//            thumbnailPath=BitMapToString(bmThumbnail);
//            Log.i("thumbnailPath", "thumbnailPath == "+thumbnailPath);
//        }

        if (App.getSingleton().getSoundId() == null) {
//            extractAudio();
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    //TODO thumb time stamp
//
    private void thumbnail(String uriString) {
        long time = System.currentTimeMillis();
        String ts = String.valueOf(time);
        File thumbnailFile = new File(Variables.app_folder+"/"+ts+"thumb.png");
//        File thumbnailFile = new File(Variables.thumbnail); //delete speeded video if exists
//        if (thumbnailFile.exists()) {
//            thumbnailFile.delete();
//        }
        String[] thumbnailCommand = new String[]{"-i", uriString, "-ss", "00:00:03.000", "-vframes", "1", thumbnailFile.getAbsolutePath()};



        VideoAudioFFMPEG videoAudioFFMPEG = new VideoAudioFFMPEG(PostVideoActivity.this, new VideoAudioFFMPEG.Thumbnail() {
            @Override
            public void onThumbnailCompleteListener() {
                getThumbnail=thumbnailFile.getAbsolutePath();
                thumbnail=getThumbnail;
                Log.i("thumbnailPath", "thumbnailPath == "+getThumbnail);
//                Toast.makeText(PlayerActivity.this, String.valueOf(thumbnailFile.getAbsolutePath()), Toast.LENGTH_SHORT).show();
            }
        });
        videoAudioFFMPEG.thumbnailCommand(thumbnailCommand);
    }

    private void extractAudio() {

        File file = new File(Variables.trimmp3File);
        if (file.exists()) {
            file.delete();
        }


        String[] audioExtractionCommand = new String[]{"-i", videoPath, "-f", "mp3", "-ab", "192000", "-vn", Variables.trimmp3File};
        VideoAudioFFMPEG videoAudioFFMPEG = new VideoAudioFFMPEG(PostVideoActivity.this, new VideoAudioFFMPEG.AudioExtraction() {
            @Override
            public void onAudioExtractCompletedListener() {
                Log.i("myFfmpegExtraction", "completed");
                Toast.makeText(PostVideoActivity.this, "completed", Toast.LENGTH_SHORT).show();
            }
        });
        videoAudioFFMPEG.executeAudioExtractionCommand(audioExtractionCommand);
    }

    private void setAddedRecyclerHashTag() {
        adapterAddedHashtags = new AdapterAddedHashtags(PostVideoActivity.this, listAddedHashTags, new AdapterAddedHashtags.Select() {
            @Override
            public void deleteHashtag(int position) {
                listAddedHashTags.remove(position);
                adapterAddedHashtags.notifyDataSetChanged();
            }
        });
        recycler_hashtags.setAdapter(adapterAddedHashtags);
    }

//    private void autoCompleteHashTags() {
//        et_hashtag_video.setHint("#" + getText(R.string.search));
//
//        et_hashtag_video.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                if (et_hashtag_video.getText().length() > 1) {
//                    seacrhHastags(et_hashtag_video.getText().toString());
//                }
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (et_hashtag_video.getText().length() > 1) {
//                    seacrhHastags(et_hashtag_video.getText().toString());
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (et_hashtag_video.getText().length() > 1) {
//                    seacrhHastags(et_hashtag_video.getText().toString());
//                }
//            }
//        });
//    }

//    private void seacrhHastags(String search) {
//        videoMvvm.searchHashtagsVideo(PostVideoActivity.this, search).observe(PostVideoActivity.this, new Observer<ModelHashTag>() {
//            @Override
//            public void onChanged(ModelHashTag modelHashTag) {
//                if (modelHashTag.getSuccess().equalsIgnoreCase("1")) {
//                    listHashTags = modelHashTag.getDetails();
//                    HashTagListAdapter hashTagListAdapter = new HashTagListAdapter(PostVideoActivity.this, R.layout.autocomplete_custom_layout, listHashTags);
//                    et_hashtag_video.setAdapter(hashTagListAdapter);
//                    et_hashtag_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Log.i("hashTagSelect", listHashTags.get(position).getHashtag());
//                            String hashTag = listHashTags.get(position).getHashtag();
//                            if (listAddedHashTags.contains(hashTag)) {
//                                et_hashtag_video.setText("");
//                                Toast.makeText(PostVideoActivity.this, "Hashtag already added.", Toast.LENGTH_SHORT).show();
//                            } else {
//                                listAddedHashTags.add(hashTag);
//                                adapterAddedHashtags.notifyDataSetChanged();
//                                et_hashtag_video.setText("");
//                            }
//                        }
//                    });
//                } else {
//                    ModelHashTag.Detail detail = new ModelHashTag.Detail();
//                    detail.setHashtag(et_hashtag_video.getText().toString());
//                    detail.setVideoCount("0 ");
//                    detail.setId("");
//                    detail.setCreated("");
//                    List<ModelHashTag.Detail> listNew = new ArrayList<>();
//                    listNew.add(detail);
//
//                    HashTagListAdapter hashTagListAdapter = new HashTagListAdapter(PostVideoActivity.this, R.layout.autocomplete_custom_layout, listNew);
//                    et_hashtag_video.setAdapter(hashTagListAdapter);
//                    et_hashtag_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            String hashTag = listNew.get(position).getHashtag();
//                            if (listAddedHashTags.contains(hashTag)) {
//                                et_hashtag_video.setText("");
//                                Toast.makeText(PostVideoActivity.this, "Hashtag already added.", Toast.LENGTH_SHORT).show();
//                            } else {
//                                char[] array = hashTag.toCharArray();
//                                String zero = String.valueOf(array[0]);
//                                if (!zero.equalsIgnoreCase("#")) {
//                                    hashTag = "#" + hashTag;
//                                }
//                                listAddedHashTags.add(hashTag);
//                                adapterAddedHashtags.notifyDataSetChanged();
//                                et_hashtag_video.setText("");
//                            }
//                        }
//                    });
//                }
//            }
//        });
//    }

    private void findIds() {

        switch_download = findViewById(R.id.switch_download);
        switch_duet_react = findViewById(R.id.switch_duet_react);
        switch_comments = findViewById(R.id.switch_comments);
        spinner_privacy_video = findViewById(R.id.spinner_privacy_video);

        switch_save_video = findViewById(R.id.switch_save_video);
        if (App.getSingleton().getGalleryStatus().equalsIgnoreCase("1")){
            switch_save_video.setChecked(false);
        }
        switch_save_video.setOnCheckedChangeListener(this);
        et_video_description = findViewById(R.id.et_video_description);
        img_video_thumb = findViewById(R.id.img_video_thumb);
        et_hashtag_video = findViewById(R.id.et_hashtag_video);
        recycler_hashtags = findViewById(R.id.recycler_hashtags);
        tv_view_video = findViewById(R.id.tv_view_video);
        tv_view_video.setOnClickListener(this);
        findViewById(R.id.tv_top_edit).setOnClickListener(this);
        findViewById(R.id.btn_save_drafts).setOnClickListener(this);
        findViewById(R.id.btn_video_post).setOnClickListener(this);
        findViewById(R.id.tv_privacy_settings_pos).setOnClickListener(this);
        findViewById(R.id.img_cancel_post).setOnClickListener(this);

        et_hashtag_video.setHint("#" + getText(R.string.search));

        if (getIntent().getStringExtra(AppConstants.DRAFT_STATUS) != null) {
            if (getIntent().getStringExtra(AppConstants.DRAFT_STATUS).equalsIgnoreCase("0")) {
                switch_save_video.setChecked(false);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_drafts:
                type = Drafts;
                File source = new File(videoPath);
                File destination = new File(Variables.draft_app_folder);
                copyFileOrDirectory(source.toString(), destination.toString());

                break;

            case R.id.tv_privacy_settings_pos:
                Intent intent = new Intent(PostVideoActivity.this, PostPrivacySettingsActivity.class);
                intent.putExtra(COMMENTS, allowComments);
                intent.putExtra(DUETS, allowDuet);
                intent.putExtra(DOWNLOADS, allowDownload);
                startActivityForResult(intent, PRIVACY_RC);
                break;

            case R.id.img_cancel_post:
                onBackPressed();
                break;

            case R.id.tv_top_edit:
            case R.id.btn_video_post:
//                addWatermark();
                if (spinner_privacy_video.getSelectedItemPosition() == 0) {
                    viewVideo = "0";
                } else {
                    viewVideo = "1";
                }
                getValues();
//                addWatermark();
                uploadVideo();
                break;

            case R.id.tv_view_video:
                openDialog();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void getImageThumb(String file) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        Bitmap bMap = retriever.getFrameAtTime(1000);

        //create a file to write bitmap data
        File f = new File(Variables.videoThumb);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Convert bitmap to byte array
        Bitmap bitmap = bMap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void copyFileOrDirectory(String srcDir, String dstDir) {
        Functions.Show_loader(PostVideoActivity.this, false, false);
        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());
            if (src.isDirectory()) {
                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);
                    Functions.cancel_loader();
                    Toast.makeText(this, "Saved To Drafts", Toast.LENGTH_SHORT).show();
                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("draftsSave", e.getMessage());
        }
    }

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        try {
            InputStream in = new FileInputStream(sourceFile);
            OutputStream out = new FileOutputStream(destFile);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

            //Renaming file in drafts after copying from main folder
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();

            if (type == Drafts) {
                File renameFile = new File(Variables.draft_app_folder + "output-filtered.mp4");
                File afterRename = new File(Variables.draft_app_folder + "/" + ts + "drafts.mp4");
                renameFile.renameTo(afterRename);
                Log.i("filerenamed", "drafts");
                Toast.makeText(this, "Saved To Drafts", Toast.LENGTH_SHORT).show();
            } else {
                File renameFile = new File(Variables.saveVideos + "/output2.mp4");
                File afterRename = new File(Variables.saveVideos + "/" + ts + "savedVideo.mp4");
                renameFile.renameTo(afterRename);
                savedVideoPath = afterRename.toString();
                Log.i("filerenamed", "saved");
                Log.i("filerenamed", savedVideoPath);
            }
            Log.i("draftsSave", "draftssavedCopyFile");
        } catch (Exception e) {
            Log.i("draftsSave", e.getMessage());
        }
        Functions.cancel_loader();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadVideo() {
        if (!App.getSharedpref().isLogin(PostVideoActivity.this)) {
            Toast.makeText(this, "You are not logged in!", Toast.LENGTH_SHORT).show();
        } else {
            if (listAddedHashTags.size() != 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < listAddedHashTags.size(); i++) {
                    if (i == listAddedHashTags.size()) {
                        stringBuilder.append(listAddedHashTags.get(i));
                    } else {
                        stringBuilder.append(listAddedHashTags.get(i) + ",");
                    }
                }
                hashtag = et_hashtag_video.getText().toString();
                hashtag = stringBuilder.toString();
            } else {
                hashtag = "";
            }
            String userID = CommonUtils.userId(PostVideoActivity.this);
            String soundId = "";
            if (App.getSingleton().getSoundId() != null && !App.getSingleton().getSoundId().equalsIgnoreCase("")) {
                soundId = App.getSingleton().getSoundId();
            }
            String soundTitle = "Original Sound by" + App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getUsername();
            if (App.getSingleton().getSoundName() != null) {
                soundTitle = App.getSingleton().getSoundName();
            }
            description = et_video_description.getText().toString();

//            String soundFile = "";
//            MultipartBody.Part audioPart;
//            if (App.getSingleton().getSoundId() == null || App.getSingleton().getSoundId().equalsIgnoreCase("")) {
//                soundFile = Variables.trimmp3File;
//                File audioFile = new File(soundFile);
//                RequestBody reqFile = RequestBody.create(MediaType.parse("audio/*"), audioFile);
//                audioPart = MultipartBody.Part.createFormData("soundFile", audioFile.getName(), reqFile);
//            } else {
//                RequestBody reqFile = RequestBody.create(MediaType.parse("audio/*"), "");
//                audioPart = MultipartBody.Part.createFormData("soundFile", "", reqFile);
//            }
//            File file = new File(videoPath);

//            long outputCompressVideosize = file.length();
//            long fileSizeInKB = outputCompressVideosize / 1024;
//            long fileSizeInMB = fileSizeInKB / 1024;
//            String s =videoPath + "\n" + fileSizeInMB + "mb4";
//            File file1 = new File(s);


//            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            MultipartBody.Part rb_video = MultipartBody.Part.createFormData("videoPath", file.getName(), requestFile);

//            RequestBody rb_userId = RequestBody.create(MediaType.parse("text/plain"), userID);
//            RequestBody rb_description = RequestBody.create(MediaType.parse("text/plain"), description);
//            RequestBody rb_view_video = RequestBody.create(MediaType.parse("text/plain"), viewVideo);
//            RequestBody rb_comments = RequestBody.create(MediaType.parse("text/plain"), allowComments);
//            RequestBody rb_downloads = RequestBody.create(MediaType.parse("text/plain"), allowDownload);
//            RequestBody rb_duets = RequestBody.create(MediaType.parse("text/plain"), allowDuet);
//            RequestBody rb_hashtag = RequestBody.create(MediaType.parse("text/plain"), hashtag);
//            RequestBody rb_soundId = RequestBody.create(MediaType.parse("text/plain"), soundId);
//            RequestBody rb_soundTitle = RequestBody.create(MediaType.parse("text/plain"), soundTitle);
//            RequestBody rb_isDuet = RequestBody.create(MediaType.parse("text/plain"), "false");
//            RequestBody rb_height = RequestBody.create(MediaType.parse("text/plain"), "0");
//            RequestBody rb_width = RequestBody.create(MediaType.parse("text/plain"), "0");
//            RequestBody rb_dueturl = RequestBody.create(MediaType.parse("text/plain"), "0");
//
//            if(MyVideoEditorActivity.isDuet){
//                rb_isDuet = RequestBody.create(MediaType.parse("text/plain"), "true");
//                rb_height = RequestBody.create(MediaType.parse("text/plain"), MyVideoEditorActivity.video_height);
//                rb_width = RequestBody.create(MediaType.parse("text/plain"), MyVideoEditorActivity.video_width);
//                rb_dueturl = RequestBody.create(MediaType.parse("text/plain"), MyVideoEditorActivity.duet_url);
//
//            }

            Intent intent = new Intent(PostVideoActivity.this, FileUploadService.class);
            hashtag = et_hashtag_video.getText().toString();

            intent.putExtra(USER_ID, userID);
            intent.putExtra(DESCRIPTION, description);
            intent.putExtra(VIEW_VIDEO, viewVideo);
            intent.putExtra(ALLOW_COMMENT, allowComments);
            intent.putExtra(ALLOW_DOWNLOAD, allowDownload);
            intent.putExtra(ALLOW_DUET, allowDuet);
            intent.putExtra(HASHTAG, hashtag);
            intent.putExtra(SOUND_ID, "");
            intent.putExtra(SOUND_TITLE, "");
            intent.putExtra(AppConstants.VIDEOTHUMB, getThumbnail);
            intent.putExtra(VIDEO_PATH, videoPath);
            intent.putExtra(VIDEOLENGTH, "");
            intent.putExtra(VIDEOTYPE, videoType);
            intent.putExtra(IS_DUET,  "false");
            intent.putExtra(HEIGHT,  videoHeight);
            intent.putExtra(WIDTH,  videoWidth);
            intent.putExtra(URL,  "0");
//
//            if(MyVideoEditorActivity.isDuet){
//                intent.putExtra(IS_DUET,  "true");
//                intent.putExtra(HEIGHT,  MyVideoEditorActivity.video_height);
//                intent.putExtra(WIDTH,  MyVideoEditorActivity.video_width);
//                intent.putExtra(URL,  MyVideoEditorActivity.duet_url);
//            }


            startService(intent);
            startActivity(new Intent(PostVideoActivity.this, HomeActivity.class).putExtra("fragment", ""));
            finishAffinity();


//            videoMvvm.uploadVideo(PostVideoActivity.this, rb_userId, rb_hashtag, rb_description, rb_comments, rb_duets, rb_downloads, rb_video, audioPart, rb_view_video, rb_soundId, rb_soundTitle,rb_isDuet,rb_height,rb_width,rb_dueturl).observe(
//                    PostVideoActivity.this, new Observer<Map>() {
//                        @Override
//                        public void onChanged(Map map) {
//
////                            Toast.makeText(PostVideoActivity.this, "map = " + map, Toast.LENGTH_SHORT).show();
//                            if (map.get("success").equals("1")) {
//                                //delete file from after posting
//                                File deleteDraftFile = new File(videoPath);
//                                if(!App.getSingleton().isGalleryVideo())
//                                {
//                                    deleteDraftFile.delete();
//                                }
////                                watermark.delete();
//
//                                File soundFile = new File(Variables.trimmp3File);
//                                if (soundFile.exists()) {
//                                    soundFile.delete();
//                                }
//
//
//
//                                Toast.makeText(PostVideoActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(PostVideoActivity.this, HomeActivity.class).putExtra("fragment", AppConstants.VIDEO_POST));
//                                finishAffinity();
//                            } else {
//                                Toast.makeText(PostVideoActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PRIVACY_RC) {
            if (data.getExtras() != null) {
                allowComments = data.getStringExtra(PostVideoActivity.COMMENTS);
                allowDownload = data.getStringExtra(PostVideoActivity.DOWNLOADS);
                allowDuet = data.getStringExtra(PostVideoActivity.DUETS);
            } else {
                Log.i("activityResult", "null");
            }
        }
    }

    private void openDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(PostVideoActivity.this);
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_video_privacy, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(PostVideoActivity.this).create();
        dailogbox.setCancelable(true);
        dailogbox.setView(confirmdailog);

        final Spinner spinner_privacy_video = confirmdailog.findViewById(R.id.spinner_privacy_video);

        confirmdailog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogbox.dismiss();
            }
        });

        confirmdailog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner_privacy_video.getSelectedItemPosition() == 0) {
                    viewVideo = "0";
                } else {
                    viewVideo = "1";
                }
                tv_view_video.setText(spinner_privacy_video.getSelectedItem().toString());
                dailogbox.dismiss();
            }
        });

        dailogbox.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_save_video:
                if (isChecked) {
//                    saveVideo();
                } else {
                    deleteSavedFile(savedVideoPath);
                }
                break;
        }
    }

    private void deleteSavedFile(String videoPath) {
        File file = new File(videoPath);
        file.delete();
    }

    private void saveVideo() {
        type = SaveVideo;
        File saveVideoFolder = new File(Variables.saveVideos);
        if (!saveVideoFolder.exists()) {
            saveVideoFolder.mkdir();
        }
        File source = new File(videoPath);
        copyFileOrDirectory(source.toString(), saveVideoFolder.toString());
    }


    private void addWatermark() {

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        File Watermarkedfile = new File(Variables.root + "/CanCre/" + ts + "waterMarkedVideo.mp4");

        File file = new File(Variables.root + "/CanCre/logo.gif");
        if (!file.exists()) {
            Log.i("mobile", "photo_exists");
            Toast.makeText(this, "Logo not exits", Toast.LENGTH_SHORT).show();
            return;
        }

        VideoAudioFFMPEG videoAudioFFMPEG = new VideoAudioFFMPEG(PostVideoActivity.this, new VideoAudioFFMPEG.WaterMarkAdd() {
            @Override
            public void onWaterMarkAddListener() {
//                uploadVideo();
            }
        });

        videoAudioFFMPEG.executeWaterMarkCommand(addwaterMark(file.getAbsolutePath(), videoPath, Watermarkedfile.getAbsolutePath()));
    }

    public static String[] addwaterMark(String imageUrl, String videoUrl, String outputUrl) {
        String[] commands = new String[8];
// commands[0] = "ffmpeg";
//Enter
        commands[0] = "-i";
        commands[1] = videoUrl;
//watermark
        commands[2] = "-i";
        commands[3] = imageUrl;//The picture address here is replaced with a video with a transparent channel to synthesize a dynamic video mask.
        commands[4] = "-filter_complex";
        commands[5] = "overlay=15:15";
//Overwrite output
        commands[6] = "-y";// directly overwrite the output file
// Output file
        commands[7] = outputUrl;
        return commands;
    }


    private void getValues() {
        if (switch_download.isChecked()) {
            allowDownload = "1";
        } else {
            allowDownload = "0";
        }
        if (switch_comments.isChecked()) {
            allowComments = "1";

        } else {
            allowComments = "0";
        }
        if (switch_duet_react.isChecked()) {
//            allowDuet = "1";
            allowDuet = "0";
        } else {
            allowDuet = "0";
        }
    }
}
