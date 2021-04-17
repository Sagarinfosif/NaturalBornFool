package in.pureway.cinemaflix.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.videoEditor.activity.GallerySelectedVideoActivity;
import in.pureway.cinemaflix.activity.videoEditor.activity.PlayerActivity;
import in.pureway.cinemaflix.activity.videoEditor.util.Variables;
import in.pureway.cinemaflix.models.UploadSingleVideoPojo;
import in.pureway.cinemaflix.mvvm.VideoMvvm;
import in.pureway.cinemaflix.utils.App;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static in.pureway.cinemaflix.activity.videoEditor.util.Variables.Pick_video_from_gallery;

public class GalleryActivity extends AppCompatActivity {

    private VideoView videoView;
    private TextView path;

    private List<String> mPath;
    private VideoMvvm videoMvvm;
    String selectedVideoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

         videoMvvm= ViewModelProviders.of(this).get(VideoMvvm.class);

        init();

    }

    private void init() {
        findViewById(R.id.btnUploadImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pick_from_gallery();
            }
        });
    }

    private void Pick_from_gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Please pick a video"), Variables.Pick_video_from_gallery);
    }

//    private void uploadVideo() {
//        File file = new File(selectedVideoPath);
//        final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part rb_video = MultipartBody.Part.createFormData("video", file.getName(), requestFile);
//
//        videoMvvm.uploadSingleVideo(this,rb_video).observe(GalleryActivity.this, new Observer<UploadSingleVideoPojo>() {
//            @Override
//            public void onChanged(UploadSingleVideoPojo uploadSingleVideoPojo) {
//                if (uploadSingleVideoPojo.getSuccess().equalsIgnoreCase("1")){
//                    Toast.makeText(GalleryActivity.this, "uploaded", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(GalleryActivity.this, "not Upload", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    //
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("code", "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (requestCode == Pick_video_from_gallery && resultCode == RESULT_OK) {
            Uri selectedVideoUri = data.getData();
            String filemanagerstring = selectedVideoUri.toString();
            String vidPath = filemanagerstring;
            selectedVideoPath = getPath(selectedVideoUri);

            startActivity(new Intent(GalleryActivity.this, GallerySelectedVideoActivity.class).putExtra(PlayerActivity.INTENT_URI, selectedVideoPath));


        }
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        getContentResolver();
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
}