package in.pureway.cinemaflix.activity.findFriends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.OtherUserProfileActivity;
import in.pureway.cinemaflix.adapters.AdapterSearchFriends;
import in.pureway.cinemaflix.javaClasses.FollowUnfollowUser;
import in.pureway.cinemaflix.models.ModelSearchFriend;
import in.pureway.cinemaflix.mvvm.FollowMvvm;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchFriendsActivity extends AppCompatActivity {
    private RecyclerView rv_search_friends;
    private EditText et_search_friends;
    private FollowMvvm followMvvm;
    List<ModelSearchFriend.Detail> list = new ArrayList<>();
    private AdapterSearchFriends adapterSearchFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(SearchFriendsActivity.this);
        setContentView(R.layout.activity_search_friends);

//        followMvvm = ViewModelProviders.of(SearchFriendsActivity.this).get(FollowMvvm.class);
//        findIds();
//        textwatcher();
    }

//    private void textwatcher() {
//        et_search_friends.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (et_search_friends.getText().length() >= 2) {
//                    setRecycler();
//                } else {
//
//                }
//            }
//        });
//    }
//
//    private void setRecycler() {
//        if (list.size() != 0) {    //clear previous search list everytime we search for new user
//            list.clear();
//            adapterSearchFriends.notifyDataSetChanged();
//        }
//        String searchValue = et_search_friends.getText().toString().trim();
//        followMvvm.searchFriends(SearchFriendsActivity.this, CommonUtils.userId(SearchFriendsActivity.this), searchValue).observe(SearchFriendsActivity.this, new Observer<ModelSearchFriend>() {
//            @Override
//            public void onChanged(ModelSearchFriend modelFollowers) {
//                if (modelFollowers.getSuccess().equalsIgnoreCase("1")) {
//                    Log.i("searchFriends", modelFollowers.getMessage());
//                    list = modelFollowers.getDetails();
//                    adapterSearchFriends = new AdapterSearchFriends(SearchFriendsActivity.this, list, new AdapterSearchFriends.Select() {
//                        @Override
//                        public void moveToId(int position) {
//                            startActivity(new Intent(SearchFriendsActivity.this, OtherUserProfileActivity.class).putExtra(AppConstants.OTHER_USER_ID, list.get(position).getId()));
//                            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//                        }
//
//                        @Override
//                        public void followUnfollow(int position) {
//                            follow(position);
//                        }
//                    });
//                    rv_search_friends.setAdapter(adapterSearchFriends);
//                } else {
//                    Log.i("searchFriends", modelFollowers.getMessage());
//                }
//            }
//        });
//    }
//
//    private void follow(final int position) {
//        FollowUnfollowUser followUnfollowUser=new FollowUnfollowUser(SearchFriendsActivity.this, new FollowUnfollowUser.followCallBack() {
//            @Override
//            public void followInterfaceCall(Map map) {
//                if (map.get("success").equals("1")) {
//                    if (map.get("following_status").equals(true)) {
//                        Intent intent = new Intent(AppConstants.FOLLOW_UNFOLLOW);
//                        intent.putExtra(AppConstants.FOLLOW_UNFOLLOW, AppConstants.FOLLOW);
//                        intent.putExtra(AppConstants.POSITION, position);
//                        LocalBroadcastManager.getInstance(SearchFriendsActivity.this).sendBroadcast(intent);
//                    } else {
//                        Intent intent = new Intent(AppConstants.FOLLOW_UNFOLLOW);
//                        intent.putExtra(AppConstants.FOLLOW_UNFOLLOW, AppConstants.UNFOLLOW);
//                        intent.putExtra(AppConstants.POSITION, position);
//                        LocalBroadcastManager.getInstance(SearchFriendsActivity.this).sendBroadcast(intent);
//                    }
//                } else {
//                    Log.i("follow", map.get("message").toString());
//                }
//            }
//        });
//        followUnfollowUser.followUnfollow(list.get(position).getId());
//    }
//
//    private void findIds() {
//        et_search_friends = findViewById(R.id.et_search_friends);
//        rv_search_friends = findViewById(R.id.rv_search_friends);
//    }
//
//    public void backPress(View view) {
//        onBackPressed();
//    }
}