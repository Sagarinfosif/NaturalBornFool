package in.pureway.cinemaflix.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.findFriends.FindFriendsActivity;
import in.pureway.cinemaflix.adapters.AdapterOtherUserViewPager;
import in.pureway.cinemaflix.fragments.follow.other.OtherFollowersFragment;
import in.pureway.cinemaflix.fragments.follow.other.OtherFollowingFragment;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

public class OtherFollowersFollowingActivity extends AppCompatActivity {
    private TabLayout tab_follow;
    private ViewPager vp_follow;
    private TextView tv_username;
    private boolean showMyFollowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(OtherFollowersFollowingActivity.this);
        setContentView(R.layout.activity_other_followers_following);
        findIds();
        setup();
    }

    private void findIds() {
        showMyFollowing = getIntent().getExtras().getBoolean(AppConstants.SHOW_MY_FOLLOWING);
        tv_username = findViewById(R.id.tv_username);
        vp_follow = findViewById(R.id.vp_follow);
        tab_follow = findViewById(R.id.tab_follow);
    }

    private void setup() {
        String username = getIntent().getExtras().getString(AppConstants.OTHER_USER_NAME);
        String otherUserId = getIntent().getExtras().getString(AppConstants.OTHER_USER_ID);
        AdapterOtherUserViewPager adapterOtherUserViewPager = new AdapterOtherUserViewPager(getSupportFragmentManager());
        int position = getIntent().getExtras().getInt(AppConstants.POSITION);
//        if (showMyFollowing) {
            adapterOtherUserViewPager.addFrag(new OtherFollowingFragment(otherUserId), "Following");
//        }
//        else {
            adapterOtherUserViewPager.addFrag(new OtherFollowersFragment(otherUserId), "Followers");

//        }
        vp_follow.setAdapter(adapterOtherUserViewPager);
        tab_follow.setupWithViewPager(vp_follow);
        tv_username.setText(username);
        if (position == 0 && showMyFollowing || position == 1 && showMyFollowing) {
            tab_follow.getTabAt(getIntent().getExtras().getInt(AppConstants.POSITION)).select();
        }else {
            tab_follow.getTabAt(getIntent().getExtras().getInt(AppConstants.POSITION)).select();

        }
    }

    public void backpress(View view) {
        onBackPressed();
    }

    public void findFriends(View view) {
        startActivity(new Intent(OtherFollowersFollowingActivity.this, FindFriendsActivity.class));
    }
}