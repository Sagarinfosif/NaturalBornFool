package in.pureway.cinemaflix.Live_Stream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelLiveUsers;
import in.pureway.cinemaflix.mvvm.LiveMvvm;
import in.pureway.cinemaflix.mvvm.ProfileMvvm;
import in.pureway.cinemaflix.utils.CommonUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LiveMainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recycler_live;
    private OtherLiveAdapter otherLiveAdapter;
    private List<ModelLiveUsers.Detail> list_hot_users = new ArrayList<>();
    private List<ModelLiveUsers.Detail> list_follow_users = new ArrayList<>();
    private MaterialCardView card_no_live;
    private TabLayout tab_lay;
    private LiveMvvm liveMvvm;
    private ProfileMvvm profileMvvm;
    private FloatingActionButton btn_go_live;
    private String status = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_main);

        liveMvvm = ViewModelProviders.of(LiveMainActivity.this).get(LiveMvvm.class);
        profileMvvm = ViewModelProviders.of(LiveMainActivity.this).get(ProfileMvvm.class);
        findIds();
        getStatus();

        tab_lay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        setFollowRecycler();
                        break;

                    case 1:
                        setHotRecycler();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setFollowRecycler();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setFollowRecycler();
    }

    private void getStatus() {
        profileMvvm.getVerifyStatus(LiveMainActivity.this, CommonUtils.userId(LiveMainActivity.this)).observe(LiveMainActivity.this, new Observer<Map>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    status = map.get("status").toString();
                    if (status.equals("1")){
                        btn_go_live.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(LiveMainActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setFollowRecycler() {
        liveMvvm.getFollowedLive(LiveMainActivity.this, CommonUtils.userId(LiveMainActivity.this)).observe(LiveMainActivity.this, new Observer<ModelLiveUsers>() {
            @Override
            public void onChanged(ModelLiveUsers modelLiveUsers) {
                if (modelLiveUsers.getSuccess().equalsIgnoreCase("1")) {
                    list_hot_users = modelLiveUsers.getDetails();
                    otherLiveAdapter = new OtherLiveAdapter(LiveMainActivity.this, list_hot_users, new OtherLiveAdapter.Select() {
                        @Override
                        public void position(int pose) {
                            startActivity(new Intent(LiveMainActivity.this, OtherLiveActivity.class)
                                    .putExtra("name", list_hot_users.get(pose).getName())
                                    .putExtra("resourceuri", list_hot_users.get(pose).getResourceUri())
                                    .putExtra("username", list_hot_users.get(pose).getUsername())
                                    .putExtra("follow", true)
                                    .putExtra("userimage", list_hot_users.get(pose).getImage())
                                    .putExtra("live_user_id", list_hot_users.get(pose).getUserId()));
                        }
                    });
                    recycler_live.setAdapter(otherLiveAdapter);
                } else {
                    recycler_live.setVisibility(View.GONE);
                    card_no_live.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setHotRecycler() {
        if (list_hot_users.size() > 0) {
            list_hot_users.clear();
            if (otherLiveAdapter != null) {
                otherLiveAdapter.notifyDataSetChanged();
            }
        }
        liveMvvm.getLiveusers(LiveMainActivity.this, CommonUtils.userId(LiveMainActivity.this)).observe(LiveMainActivity.this, new Observer<ModelLiveUsers>() {
            @Override
            public void onChanged(ModelLiveUsers modelLiveUsers) {
                if (modelLiveUsers.getSuccess().equalsIgnoreCase("1")) {
                    list_hot_users = modelLiveUsers.getDetails();
                    otherLiveAdapter = new OtherLiveAdapter(LiveMainActivity.this, list_hot_users, new OtherLiveAdapter.Select() {
                        @Override
                        public void position(int pose) {
                            startActivity(new Intent(LiveMainActivity.this, OtherLiveActivity.class)
                                    .putExtra("name", list_hot_users.get(pose).getName())
                                    .putExtra("resourceuri", list_hot_users.get(pose).getResourceUri())
                                    .putExtra("username", list_hot_users.get(pose).getUsername())
                                    .putExtra("userimage", list_hot_users.get(pose).getImage())
                                    .putExtra("follow", false)
                                    .putExtra("live_user_id", list_hot_users.get(pose).getUserId()));
                        }
                    });
                    recycler_live.setAdapter(otherLiveAdapter);
                    card_no_live.setVisibility(View.GONE);
                } else {
                    recycler_live.setVisibility(View.GONE);
                    card_no_live.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void findIds() {
        tab_lay = findViewById(R.id.tab_lay);
        card_no_live = findViewById(R.id.card_no_live);
        recycler_live = findViewById(R.id.recycler_live);
        btn_go_live =findViewById(R.id.btn_go_live);
        btn_go_live.setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);

        tab_lay.addTab(tab_lay.newTab().setText("Follow"));
        tab_lay.addTab(tab_lay.newTab().setText("Hot"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go_live:
                startActivity(new Intent(this, GoLiveActivity.class));
                break;
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}