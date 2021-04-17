package in.pureway.cinemaflix.activity.sounds.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.sounds.fragments.SoundsDeviceFragment;
import in.pureway.cinemaflix.adapters.AdapterOtherUserViewPager;
import in.pureway.cinemaflix.activity.sounds.fragments.DiscoverSoundsFragment;
import in.pureway.cinemaflix.activity.sounds.fragments.FavoritesSoundsFragment;
import in.pureway.cinemaflix.utils.CommonUtils;

public class SoundsActivity extends AppCompatActivity {
    private ViewPager vp_sounds;
    private TabLayout tab_sounds;
    private EditText et_search_sounds;
    public static int Sounds_list_Request_code = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(SoundsActivity.this);
        setContentView(R.layout.activity_sounds);

        findIds();
    }

    private void findIds() {
        findViewById(R.id.et_search_sounds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SoundsActivity.this, SearchSoundActivity.class);
                startActivityForResult(intent, Sounds_list_Request_code);
                overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
//                startActivity(new Intent(SoundsActivity.this, SearchSoundActivity.class));
            }
        });
        vp_sounds = findViewById(R.id.vp_sounds);
        tab_sounds = findViewById(R.id.tab_sounds);
        AdapterOtherUserViewPager adapterOtherUserViewPager = new AdapterOtherUserViewPager(getSupportFragmentManager());
        adapterOtherUserViewPager.addFrag(new DiscoverSoundsFragment(), "Discover");
        adapterOtherUserViewPager.addFrag(new FavoritesSoundsFragment(), "Favorites");
        adapterOtherUserViewPager.addFrag(new SoundsDeviceFragment(), "Device");
        vp_sounds.setAdapter(adapterOtherUserViewPager);
        tab_sounds.setupWithViewPager(vp_sounds);
    }

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Sounds_list_Request_code) {
            if (data != null) {
                if (data.getStringExtra("isSelected").equals("yes")) {
                    Intent output = new Intent();
                    output.putExtra("isSelected", "yes");
                    output.putExtra("sound_name", data.getExtras().getString("sound_name"));
                    output.putExtra("sound_id", data.getExtras().getString("id"));
                    setResult(RESULT_OK, output);
                    finish();
                }
            }
        }
    }
}
