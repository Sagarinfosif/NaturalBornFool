package in.pureway.cinemaflix.activity.findFriends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.google.android.material.card.MaterialCardView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.OtherUserProfileActivity;
import in.pureway.cinemaflix.adapters.AdapterFindContacts;
import in.pureway.cinemaflix.javaClasses.FollowUnfollowUser;
import in.pureway.cinemaflix.models.ModelFindContacts;
import in.pureway.cinemaflix.mvvm.FollowMvvm;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FindContactActivity extends AppCompatActivity {

    private List<String> list = new ArrayList<>();
    private FollowMvvm followMvvm;
    private MaterialCardView card_no_contacts;
    private Activity activity = FindContactActivity.this;
    private String userId = CommonUtils.userId(activity);
    private AdapterFindContacts adapterFindContacts;
    private List<ModelFindContacts.Detail> fetchedList = new ArrayList<>();
    private RecyclerView rv_find_contact;
    private int positionA = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(FindContactActivity.this);
        setContentView(R.layout.activity_find_contact);

        followMvvm = ViewModelProviders.of(FindContactActivity.this).get(FollowMvvm.class);
        findIds();
        fetchContacts();
    }

    private void findIds() {
        card_no_contacts = findViewById(R.id.card_no_contacts);
        rv_find_contact = findViewById(R.id.rv_find_contact);
    }

    private void fetchContacts() {
        Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phone.moveToNext()) {
            String name = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactImage = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
            list.add(phoneNumber);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                stringBuilder.append(list.get(i));
            } else {
                stringBuilder.append(list.get(i) + ",");
            }
        }
        sendDataToBackEnd(stringBuilder.toString());
    }

    private void sendDataToBackEnd(String numbers) {
        followMvvm.findContacts(activity, userId, numbers).observe(FindContactActivity.this, new Observer<ModelFindContacts>() {
            @Override
            public void onChanged(ModelFindContacts modelFollowers) {
                if (modelFollowers.getSuccess().equalsIgnoreCase("1")) {
                    Log.i("findContacts", modelFollowers.getMessage());
                    fetchedList = modelFollowers.getDetails();
                    adapterFindContacts = new AdapterFindContacts(activity, fetchedList, new AdapterFindContacts.Select() {
                        @Override
                        public void followBack(final int position) {
                            positionA = position;
                            FollowUnfollowUser followUnfollowUser = new FollowUnfollowUser(FindContactActivity.this, new FollowUnfollowUser.followCallBack() {
                                @Override
                                public void followInterfaceCall(Map map) {
                                    if (map.get("success").equals("1")) {
                                        if (map.get("following_status").equals(true)) {
                                            Intent intent = new Intent(AppConstants.FOLLOW_UNFOLLOW);
                                            intent.putExtra(AppConstants.FOLLOW_UNFOLLOW, AppConstants.FOLLOW);
                                            intent.putExtra(AppConstants.POSITION, position);
                                            LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                                        } else {
                                            Intent intent = new Intent(AppConstants.FOLLOW_UNFOLLOW);
                                            intent.putExtra(AppConstants.FOLLOW_UNFOLLOW, AppConstants.UNFOLLOW);
                                            intent.putExtra(AppConstants.POSITION, position);
                                            LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                                        }
                                    } else {
                                        Log.i("follow", map.get("message").toString());
                                    }
                                }
                            });
                            followUnfollowUser.followUnfollow(fetchedList.get(position).getUserId());
                        }
                        @Override
                        public void moveToProfile(int position) {
                            startActivity(new Intent(activity, OtherUserProfileActivity.class)
                                    .putExtra(AppConstants.OTHER_USER_ID, fetchedList.get(position).getUserId()));
                            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                        }
                    });
                    rv_find_contact.setAdapter(adapterFindContacts);
                } else {
                    card_no_contacts.setVisibility(View.VISIBLE);
                    rv_find_contact.setVisibility(View.GONE);
                    Log.i("findContacts", modelFollowers.getMessage());
                }
            }
        });
    }

    public void backPress(View view) {
        onBackPressed();
    }

}