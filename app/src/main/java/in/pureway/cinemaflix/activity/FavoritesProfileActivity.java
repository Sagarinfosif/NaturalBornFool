package in.pureway.cinemaflix.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.AdapterOtherUserViewPager;
import in.pureway.cinemaflix.fragments.favorites.HastagsSavedFragment;
import in.pureway.cinemaflix.fragments.favorites.SoundsSavedFragment;
import in.pureway.cinemaflix.fragments.favorites.VideosSavedFragment;
import in.pureway.cinemaflix.utils.CommonUtils;

public class FavoritesProfileActivity extends AppCompatActivity {
    private TabLayout tab_favorites;
    private ViewPager view_pager_favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(FavoritesProfileActivity.this);
        setContentView(R.layout.activity_favorites_profile);

        findIds();
    }

    private void findIds() {
        view_pager_favorites = findViewById(R.id.view_pager_favorites);
        tab_favorites = findViewById(R.id.tab_favorites);

        AdapterOtherUserViewPager adapterOtherUserViewPager=new AdapterOtherUserViewPager(getSupportFragmentManager());
        adapterOtherUserViewPager.addFrag(new VideosSavedFragment(),getResources().getString(R.string.videos));
        adapterOtherUserViewPager.addFrag(new HastagsSavedFragment(),getResources().getString(R.string.hashtags));
//        adapterOtherUserViewPager.addFrag(new SoundsSavedFragment(),getResources().getString(R.string.sounds));
        //TODO enable saved effects
//        adapterOtherUserViewPager.addFrag(new EffectsSavedFragment(),getResources().getString(R.string.effects));
        view_pager_favorites.setAdapter(adapterOtherUserViewPager);
        tab_favorites.setupWithViewPager(view_pager_favorites);
    }

    public void backPress(View view) {
        onBackPressed();
    }
}
