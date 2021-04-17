package in.pureway.cinemaflix.activity.Video_Recording;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

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

import in.pureway.cinemaflix.FileUploadService;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.HomeActivity;
import in.pureway.cinemaflix.activity.videoEditor.activity.PostPrivacySettingsActivity;
import in.pureway.cinemaflix.activity.videoEditor.util.Functions;
import in.pureway.cinemaflix.activity.videoEditor.util.Variables;
import in.pureway.cinemaflix.activity.videoEditor.util.VideoAudioFFMPEG;
import in.pureway.cinemaflix.adapters.AdapterAddedHashtags;
import in.pureway.cinemaflix.adapters.HashTagListAdapter;
import in.pureway.cinemaflix.models.ModelHashTag;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.VideoMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

public class PostVideo_A extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private RoundedImageView img_video_thumb;
    private EditText et_video_description;
    private AutoCompleteTextView et_hashtag_video;
    private String videoPath = "", allowComments = "1", allowDuet = "1", allowDownload = "1", description = "";
    private TextView tv_view_video;
    public static final int PRIVACY_RC = 10;
    public static final String COMMENTS = "comments";
    public static final String DOWNLOADS = "downloads";
    public static final String DUETS = "duets";
    String hashtag = "", viewVideo = "0",thumbnail="";
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
    private String savedVideoPath = "";
    long time = System.currentTimeMillis();
    String ts = String.valueOf(time);
//    File compressedFile = new File(Variables.app_folder + "/" + ts + "compressedVideo.mp4");
//    public static String compressedVideo=app_folder+"/compressedVideo.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(PostVideo_A.this);
        setContentView(R.layout.activity_post_video_);
        videoMvvm = ViewModelProviders.of(PostVideo_A.this).get(VideoMvvm.class);
        thumbnail = getIntent().getStringExtra("thumbnail");
//        videoPath= Variables.output_filter_file;
        videoPath = getIntent().getStringExtra(AppConstants.VIDEO_PATH);
        findIds();
        autoCompleteHashTags();
        setAddedRecyclerHashTag();
        if (getIntent().getExtras() != null) {   //video from drafts
            if (getIntent().hasExtra(AppConstants.VIDEO_PATH)) {
                videoPath = getIntent().getExtras().getString(AppConstants.VIDEO_PATH);
//                videoPath= Variables.output_filter_file;
//                videoPath=Variables.outputfile;
            }
            if (getIntent().hasExtra(AppConstants.VIDEO_DESCRIPTION)) {
                description = getIntent().getExtras().getString(AppConstants.VIDEO_DESCRIPTION);
                et_video_description.setText(description);
            }
        }

        if(App.getSingleton().isGalleryVideo()){
//            Toast.makeText(this, "Cannot be saved. Video is already from gallery !", Toast.LENGTH_SHORT).show();
            switch_save_video.setEnabled(false);
        }
        Log.i("videoPath", videoPath);
        Bitmap bmThumbnail;

        bmThumbnail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        if (bmThumbnail != null) {
            Glide.with(PostVideo_A.this).load(bmThumbnail).placeholder(R.drawable.poat).error(R.drawable.poat).centerCrop().into(img_video_thumb);
        }

        if (App.getSingleton().getSoundId() == null || App.getSingleton().getSoundId().equalsIgnoreCase("")) {
            extractAudio();
        } else {
//            compressVideo(videoPath);
        }
    }

    private void extractAudio() {
        File file = new File(Variables.trimmp3File);
        if (file.exists()) {
            file.delete();
        }
        String[] complexCommand = {"-y", "-i", videoPath, "-vn", "-ar", "44100", "-ac", "2", "-b:a", "256k", "-f", "mp3", Variables.trimmp3File};
        VideoAudioFFMPEG videoAudioFFMPEG = new VideoAudioFFMPEG(PostVideo_A.this, new VideoAudioFFMPEG.AudioExtraction() {
            @Override
            public void onAudioExtractCompletedListener() {
                Log.i("myFfmpegExtraction", "completed");
//                compressVideo(videoPath);
            }
        });
        videoAudioFFMPEG.executeAudioExtractionCommand(complexCommand);
    }

//    private void compressVideo(String videoPath1) {
//        if (compressedFile.exists()) {
//            compressedFile.delete();
//        }
//        VideoAudioFFMPEG videoAudioFFMPEG = new VideoAudioFFMPEG(PostVideo_A.this, new VideoAudioFFMPEG.CompressVideo() {
//            @Override
//            public void onCompressCompletedListener() {
//                File file1 = new File(videoPath1);
//                if (file1.exists()) {
//                    file1.delete();
//                }
//            }
//        });
//
//        String[] command = new String[]{"-i", videoPath1, "-b:v", "2048k", "-s", "1000x600", "-fs", "2048k", "-vcodec", "mpeg4", "-acodec", "copy", compressedFile.getAbsolutePath()};
//        videoAudioFFMPEG.compressVideoCommand(command);
//    }

    private void setAddedRecyclerHashTag() {
        adapterAddedHashtags = new AdapterAddedHashtags(PostVideo_A.this, listAddedHashTags, new AdapterAddedHashtags.Select() {
            @Override
            public void deleteHashtag(int position) {
                listAddedHashTags.remove(position);
                adapterAddedHashtags.notifyDataSetChanged();
            }
        });
        recycler_hashtags.setAdapter(adapterAddedHashtags);
    }


    private void autoCompleteHashTags() {
        et_hashtag_video.setHint("#" + getText(R.string.search));
        et_hashtag_video.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_hashtag_video.getText().length() > 1) {
                    seacrhHastags(et_hashtag_video.getText().toString());
                }
            }
        });
    }

    private void seacrhHastags(String search) {
        videoMvvm.searchHashtagsVideo(PostVideo_A.this, search).observe(PostVideo_A.this, new Observer<ModelHashTag>() {
            @Override
            public void onChanged(ModelHashTag modelHashTag) {
                if (modelHashTag.getSuccess().equalsIgnoreCase("1")) {
                    listHashTags = modelHashTag.getDetails();
                    HashTagListAdapter hashTagListAdapter = new HashTagListAdapter(PostVideo_A.this, R.layout.autocomplete_custom_layout, listHashTags);
                    et_hashtag_video.setAdapter(hashTagListAdapter);
                    et_hashtag_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.i("hashTagSelect", listHashTags.get(position).getHashtag());
                            String hashTag = listHashTags.get(position).getHashtag();
                            if (listAddedHashTags.contains(hashTag)) {
                                et_hashtag_video.setText("");
                                Toast.makeText(PostVideo_A.this, "Hashtag already added.", Toast.LENGTH_SHORT).show();
                            } else {
                                listAddedHashTags.add(hashTag);
                                adapterAddedHashtags.notifyDataSetChanged();
                                et_hashtag_video.setText("");
                            }
                        }
                    });
                } else {
                    ModelHashTag.Detail detail = new ModelHashTag.Detail();
                    detail.setHashtag(et_hashtag_video.getText().toString());
                    detail.setVideoCount("0 ");
                    detail.setId("");
                    detail.setCreated("");
                    List<ModelHashTag.Detail> listNew = new ArrayList<>();
                    listNew.add(detail);

                    HashTagListAdapter hashTagListAdapter = new HashTagListAdapter(PostVideo_A.this, R.layout.autocomplete_custom_layout, listNew);
                    et_hashtag_video.setAdapter(hashTagListAdapter);
                    et_hashtag_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String hashTag = listNew.get(position).getHashtag();
                            if (listAddedHashTags.contains(hashTag)) {
                                et_hashtag_video.setText("");
                                Toast.makeText(PostVideo_A.this, "Hashtag already added.", Toast.LENGTH_SHORT).show();
                            } else {
                                char[] array = hashTag.toCharArray();
                                String zero = String.valueOf(array[0]);
                                if (!zero.equalsIgnoreCase("#")) {
                                    hashTag = "#" + hashTag;
                                    if (hashTag.contains(" ")) {
                                        hashTag.replace(" ", "%20").trim();
                                    }
                                }
                                listAddedHashTags.add(hashTag);
                                adapterAddedHashtags.notifyDataSetChanged();
                                et_hashtag_video.setText("");
                            }
                        }
                    });
                }
            }
        });
    }


    private void findIds() {
        switch_save_video = findViewById(R.id.switch_save_video);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_drafts:
                type = Drafts;

                File source = null;
                if (new File(videoPath).exists()) {
                    source = new File(videoPath);
                } else {
                    source = new File(videoPath);
                }

                File destination = new File(Variables.draft_app_folder);
                copyFileOrDirectory(source.toString(), destination.toString());
                break;

            case R.id.tv_privacy_settings_pos:
                Intent intent = new Intent(PostVideo_A.this, PostPrivacySettingsActivity.class);
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
        File audioFile = new File(Variables.trimmp3File);
        File mainvideoPath = new File(videoPath);
        File txtFile = new File(Variables.txtFile);
        File[] filePaths = new File[]{audioFile, mainvideoPath, mainvideoPath, txtFile};
        for (int i = 0; i < filePaths.length; i++) {
            if (filePaths[i].exists()) {
                filePaths[i].delete();
                Log.i("filePathsDeletion : ", String.valueOf(filePaths[i].delete()));
                Log.i("filePathsDeletion : ", String.valueOf(filePaths[i].exists()));
                Log.i("filePathsDeletion:onBackPressed", filePaths[i].getAbsolutePath());
            }
        }
        App.getSingleton().setGalleryVideo(false);
        App.getSingleton().setSoundName(null);
        App.getSingleton().setSoundId(null);
//        startActivity(new Intent(PostVideo_A.this, Video_Recoder_A.class));
        finish();
    }

//    @Override
//    protected void onDestroy() {
//        File audioFile = new File(Variables.trimmp3File);
//        File mainvideoPath = new File(videoPath);
//        File txtFile = new File(Variables.txtFile);
//        File[] filePaths = new File[]{audioFile, mainvideoPath, mainvideoPath, txtFile};
//        for (int i = 0; i < filePaths.length; i++) {
//            if (filePaths[i].exists()) {
//                filePaths[i].delete();
//                Log.i("filePathsDeletion : ", String.valueOf(filePaths[i].delete()));
//                Log.i("filePathsDeletion : ", String.valueOf(filePaths[i].exists()));
//                Log.i("filePathsDeletion:onBackPressed", filePaths[i].getAbsolutePath());
//            }
//        }
//        super.onDestroy();
//    }

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
        Functions.Show_loader(PostVideo_A.this, false, false);
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
                if (App.getSingleton().isGalleryVideo()) {

                } else {
                    File renameFile = new File(Variables.saveVideos + "/output2.mp4");
                    File afterRename = new File(Variables.saveVideos + "/" + ts + "savedVideo.mp4");
                    renameFile.renameTo(afterRename);
                    savedVideoPath = afterRename.toString();
                }
                Log.i("filerenamed", "saved");
                Log.i("filerenamed", savedVideoPath);
            }
            Log.i("draftsSave", "draftssavedCopyFile");
        } catch (Exception e) {
            Log.i("draftsSave", e.getMessage());
        }
        Functions.cancel_loader();
    }

    private void uploadVideo() {
        if (!App.getSharedpref().isLogin(PostVideo_A.this)) {
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
                hashtag = stringBuilder.toString();
            } else {
                hashtag = "";
            }
            String userID = CommonUtils.userId(PostVideo_A.this);
            String soundId = "";
            if (App.getSingleton().getSoundId() != null && !App.getSingleton().getSoundId().equalsIgnoreCase("")) {
                soundId = App.getSingleton().getSoundId();
            }
            String soundTitle = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getUsername() + " - Original Sound";
            if (App.getSingleton().getSoundName() != null) {
                soundTitle = App.getSingleton().getSoundName();
            }

            description = et_video_description.getText().toString();

            App.getSingleton().setGalleryVideo(false);

            Log.i("Gallery Video : Upload video Post Act", String.valueOf(App.getSingleton().isGalleryVideo()));
            Intent intent = new Intent(PostVideo_A.this, FileUploadService.class);
            intent.putExtra("userId", userID);
            intent.putExtra("hastag", hashtag);
            intent.putExtra("description", description);
            intent.putExtra("comments", allowComments);
            intent.putExtra("duets", allowDuet);
            intent.putExtra("downloads", allowDownload);
            intent.putExtra("video", new File(videoPath).getPath());
            intent.putExtra("videoPath", videoPath);
            intent.putExtra("viewVideo", viewVideo);
            intent.putExtra("soundId", soundId);
            intent.putExtra("soundtitle", soundTitle);
            intent.putExtra("thumbnail", thumbnail);

            startService(intent);
            startActivity(new Intent(PostVideo_A.this, HomeActivity.class).putExtra("fragment", ""));
            finishAffinity();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PRIVACY_RC) {
            if (data.getExtras() != null) {
                allowComments = data.getStringExtra(PostVideo_A.COMMENTS);
                allowDownload = data.getStringExtra(PostVideo_A.DOWNLOADS);
                allowDuet = data.getStringExtra(PostVideo_A.DUETS);
            } else {
                Log.i("activityResult", "null");
            }
        }
    }

    private void openDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(PostVideo_A.this);
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_video_privacy, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(PostVideo_A.this).create();
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
                    viewVideo = "1";
                } else {
                    viewVideo = "0";
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
                    saveVideo();
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
        copyFileOrDirectory(source.getAbsolutePath(), saveVideoFolder.getAbsolutePath());
    }
}
