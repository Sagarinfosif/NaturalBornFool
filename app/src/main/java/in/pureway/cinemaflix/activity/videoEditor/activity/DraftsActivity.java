package in.pureway.cinemaflix.activity.videoEditor.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.videoEditor.DraftSelectedVideoActivity;
import in.pureway.cinemaflix.activity.videoEditor.util.Variables;
import in.pureway.cinemaflix.adapters.AdapterDrafts;
import in.pureway.cinemaflix.models.ModelDrafts;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DraftsActivity extends AppCompatActivity {
    private RecyclerView rv_drafts;
    List<ModelDrafts> listPaths = new ArrayList<>();
    private AdapterDrafts adapterDrafts;
    private long fileSizeInMB;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(DraftsActivity.this);
        setContentView(R.layout.activity_drafts);
        findIds();
        setRecyler();
    }

    private void setRecyler() {
        final List<File> list = getListFiles(new File(Variables.draft_app_folder));
        for (int i = 0; i < list.size(); i++) {
            ModelDrafts modelDrafts = new ModelDrafts();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, ''yy hh:mm a");
            Date lastModDate = new Date(list.get(i).lastModified());
            String path = list.get(i).getPath();
            modelDrafts.setFilePath(path);
            modelDrafts.setFileDate(dateFormat.format(lastModDate));
            listPaths.add(modelDrafts);
        }
        adapterDrafts = new AdapterDrafts(DraftsActivity.this, listPaths, new AdapterDrafts.Select() {
            @Override
            public void post(int position, String description) {

                try {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(list.get(position).getPath());
                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                    int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                    int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
// retriever.release();

                    App.getSingleton().setIsWidth(String.valueOf(width));
                    App.getSingleton().setIsHeight(String.valueOf(height));


                    File file = new File(list.get(position).getPath());

// Get length of file in bytes
                    long fileSizeInBytes = file.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                    long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                    fileSizeInMB = fileSizeInKB / 1024;


                    long timeInmillisec = Long.parseLong(time);
                    long duration = timeInmillisec / 1000;
// long minutes1 = TimeUnit.MILLISECONDS.toSeconds(timeInmillisec);

                    long hours = duration / 3600;
                    long minutes = (duration - hours * 3600) / 60;
                    long seconds = duration - (hours *3600 + minutes* 60);
// long time1=hours*minutes*seconds;
                    Toast.makeText(DraftsActivity.this, Long.toString(seconds), Toast.LENGTH_SHORT).show();
                    App.getSingleton().setVideoLength(Long.toString(seconds));

                    if (minutes != 0) {
                        long millis = minutes * 60 + seconds;

                        App.getSingleton().setVideoType("1");
                        App.getSingleton().setVideoLength(Long.toString(millis));

                    } else {
                        App.getSingleton().setVideoType("0");

                    }

// Set_Player(uriString);
                } catch (Exception e) {
                    Log.i("exception", "exception===" + e.getMessage());
                }

                startActivity(new Intent(DraftsActivity.this, PostVideoActivity.class)
                        .putExtra(AppConstants.VIDEO_PATH, list.get(position).getPath())
                        .putExtra(AppConstants.VIDEO_DESCRIPTION, description)
                        .putExtra(AppConstants.DRAFT_STATUS, "0"));
            }

            @Override
            public void deletDrafts(int position) {
                deletDialog(position, list);
            }

            @Override
            public void videoUrl(String url) {
                Log.i("draftUrl", "videoUrl: ==" + url);
                startActivity(new Intent(DraftsActivity.this, DraftSelectedVideoActivity.class)
                        .putExtra(AppConstants.VIDEO_PATH, url));
            }
        });
        rv_drafts.setAdapter(adapterDrafts);
    }

    private void deleteDraft(int position, List<File> fileList) {
        File removeFile = new File(fileList.get(position).getPath());
        listPaths.remove(position);
        adapterDrafts.notifyDataSetChanged();
        removeFile.delete();
        Toast.makeText(DraftsActivity.this, "Draft deleted!", Toast.LENGTH_SHORT).show();
    }

    private void deletDialog(final int position, final List<File> fileList) {
        final AlertDialog.Builder al = new AlertDialog.Builder(this, R.style.dialogStyle);
        al.setTitle("Delete Draft ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDraft(position, fileList);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setMessage("Do you want to delete the draft ?").show();
    }

    private void findIds() {
        rv_drafts = findViewById(R.id.rv_drafts);
    }

    //get files from drafts folder
    public static ArrayList<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".mp4")) {
                    if (!inFiles.contains(file)) inFiles.add(file);
                    Log.i("draftsFile", file.toString());
                }
            }
        }
        return inFiles;
    }

    public void backPress(View view) {
        onBackPressed();
    }
}

