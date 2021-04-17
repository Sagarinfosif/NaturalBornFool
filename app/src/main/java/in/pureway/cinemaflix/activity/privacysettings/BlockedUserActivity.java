package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.AdapterBlockedUser;
import in.pureway.cinemaflix.models.ModelBlock;
import in.pureway.cinemaflix.mvvm.ProfileMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlockedUserActivity extends AppCompatActivity {
    private RecyclerView recycler_block;
    private Activity activity;
    List<ModelBlock.Detail> list = new ArrayList<>();
    private MaterialCardView card_no_videos;
    private ProfileMvvm profileMvvm;
    String userId = CommonUtils.userId(activity);
    private AdapterBlockedUser adapterBlockedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(BlockedUserActivity.this);
        setContentView(R.layout.activity_blocked_user);

        profileMvvm = ViewModelProviders.of(BlockedUserActivity.this).get(ProfileMvvm.class);
        activity = BlockedUserActivity.this;
        findIds();
        blockListApi();
    }

    private void blockListApi() {
        profileMvvm.blockList(activity, userId).observe(BlockedUserActivity.this, new Observer<ModelBlock>() {
            @Override
            public void onChanged(ModelBlock modelBlock) {
                if (modelBlock.getSuccess().equalsIgnoreCase("1")) {
                    list = modelBlock.getDetails();
                    adapterBlockedUser = new AdapterBlockedUser(activity, list, new AdapterBlockedUser.Select() {
                        @Override
                        public void blockUnblock(int position) {
                            deletDialog(position, list.get(position).getUsername());
                        }
                    });
                    recycler_block.setAdapter(adapterBlockedUser);
                } else {
                    recycler_block.setVisibility(View.GONE);
                    card_no_videos.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void deletDialog(final int position, String username) {
        final AlertDialog.Builder al = new AlertDialog.Builder(this, R.style.dialogStyle);
        al.setTitle("Unblock user ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callApiBlockUnblock(position);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setMessage("Are you sure want to unblock " + username + "?").show();
    }

    private void callApiBlockUnblock(final int position) {
        final String blockUserId = list.get(position).getId();
        profileMvvm.blockUser(activity, userId, blockUserId).observe(BlockedUserActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").toString().equalsIgnoreCase("1")) {
                    list.remove(position);
                    adapterBlockedUser.notifyDataSetChanged();

                    List<String> blockedUserList = new ArrayList<>();
                    if (App.getSingleton().getBlockedUserList() != null && App.getSingleton().getBlockedUserList().size() > 0) {
                        blockedUserList = App.getSingleton().getBlockedUserList();
                    }
                    for (int i = 0; i < blockedUserList.size(); i++) {
                        if (blockedUserList.get(i).equalsIgnoreCase(blockUserId)) {
                            blockedUserList.remove(i);
                        }
                    }
                    App.getSingleton().setBlockedUserList(blockedUserList);
                } else {
                    Toast.makeText(activity, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findIds() {
        card_no_videos = findViewById(R.id.card_no_videos);
        recycler_block = findViewById(R.id.recycler_block);
    }

    public void back(View view) {
        onBackPressed();
    }
}