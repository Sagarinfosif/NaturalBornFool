package in.pureway.cinemaflix.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.AdapterPagerFiles;
import in.pureway.cinemaflix.utils.CommonUtils;

public class UploadFilesActivity extends AppCompatActivity {
private TabLayout tab_files;
private ViewPager vp_upload_files;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(UploadFilesActivity.this);
        setContentView(R.layout.activity_upload_files);

        findIds();
    }

    private void findIds() {

        vp_upload_files=findViewById(R.id.vp_upload_files);
        tab_files=findViewById(R.id.tab_files);

        tab_files.addTab(tab_files.newTab().setText("Videos"));
        tab_files.addTab(tab_files.newTab().setText("Images"));

        vp_upload_files.setAdapter(new AdapterPagerFiles(UploadFilesActivity.this));
        vp_upload_files.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_files));
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
