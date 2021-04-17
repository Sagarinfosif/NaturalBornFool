package in.pureway.cinemaflix.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.findFriends.FindFriendsActivity;
import in.pureway.cinemaflix.adapters.AdapterOtherUserViewPager;
import in.pureway.cinemaflix.fragments.follow.my.FollowersFragment;
import in.pureway.cinemaflix.fragments.follow.my.FollowingFragment;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

public class FollowersFollowingActivity extends AppCompatActivity {
private TabLayout tab_follow;
private ViewPager vp_follow;
private TextView tv_username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(FollowersFollowingActivity.this);
        setContentView(R.layout.activity_followers_following);
        findIds();
        setup();


    }

    private void findIds() {
        tv_username=findViewById(R.id.tv_username);
        vp_follow=findViewById(R.id.vp_follow);
        tab_follow=findViewById(R.id.tab_follow);
    }

    private void setup() {
        AdapterOtherUserViewPager adapterOtherUserViewPager=new AdapterOtherUserViewPager(getSupportFragmentManager());
        adapterOtherUserViewPager.addFrag(new FollowingFragment(),"Following");
        adapterOtherUserViewPager.addFrag(new FollowersFragment(),"Followers");
        vp_follow.setAdapter(adapterOtherUserViewPager);
        tab_follow.setupWithViewPager(vp_follow);

        tab_follow.getTabAt(getIntent().getExtras().getInt(AppConstants.POSITION)).select();

        String username= App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA,ModelLoginRegister.class).getDetails().getUsername();
        tv_username.setText(username);
    }

    public void backpress(View view) {
        onBackPressed();
    }

    public void findFriends(View view) {
        startActivity(new Intent(FollowersFollowingActivity.this, FindFriendsActivity.class));
    }
}