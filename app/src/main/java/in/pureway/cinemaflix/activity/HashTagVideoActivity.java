package in.pureway.cinemaflix.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.AdapterHashTagDetails;
import in.pureway.cinemaflix.models.ModelHashTagsDetails;
import in.pureway.cinemaflix.mvvm.SoundsMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HashTagVideoActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recycler_hash_tags;
    private ImageView img_shoot_hashtags, img_star_filled;
    private SoundsMvvm soundsMvvm;
    private Activity activity = HashTagVideoActivity.this;
    private String userId = "0", hashTagId = "",videoCount="";
    private TextView tv_fvrtText, tv_hashtag_tags, tv_hashtag_owner, tv_videos_count;
    private LinearLayout ll_add_favorites;
    private int startLimit = 0, limit;
    private List<ModelHashTagsDetails.Details.HashtagVideo> list = new ArrayList<>();
    private boolean isScroll = false, hitStatus = false;
    private GridLayoutManager gridLayoutManager;

    int currentItes, totalItem, scrollOutItems;
    private AdapterHashTagDetails adapterHashTagDetails;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(HashTagVideoActivity.this);
        setContentView(R.layout.activity_hash_tags_video);
        if (App.getSharedpref().isLogin(activity)) {
            userId = CommonUtils.userId(activity);
        }
        if (getIntent().getStringExtra(AppConstants.HASHTAG_COUNT)!=null){
            videoCount=getIntent().getStringExtra(AppConstants.HASHTAG_COUNT);

        }
        if (getIntent().getStringExtra(AppConstants.HASHTAG_ID)!=null){
            hashTagId = getIntent().getStringExtra(AppConstants.HASHTAG_ID);
        }
//        if (getIntent().getExtras() != null) {
////            hashTagId = getIntent().getExtras().getString(AppConstants.HASHTAG_ID);
//
//
//        }
        soundsMvvm = ViewModelProviders.of(HashTagVideoActivity.this).get(SoundsMvvm.class);
        findIds();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        getHashTagDetails();



        gridLayoutManager = new GridLayoutManager(HashTagVideoActivity.this, 3);
        recycler_hash_tags.setLayoutManager(gridLayoutManager);
        adapterHashTagDetails = new AdapterHashTagDetails(activity, list, new AdapterHashTagDetails.Select() {
            @Override
            public void chooseVideo(int position) {
                Intent intent = new Intent(activity, SelectedVideoActivity.class);
                App.getSingleton().setHashTagDetailList(list);
                intent.putExtra(AppConstants.POSITION, position);
                intent.putExtra(AppConstants.SINGLE_VIDEO_TYPE, AppConstants.HASHTAG);
                startActivity(intent);
            }
        });
        recycler_hash_tags.setAdapter(adapterHashTagDetails);

        recycler_hash_tags.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScroll = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItes = gridLayoutManager.getChildCount();
                totalItem = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();
                if (isScroll && (currentItes + scrollOutItems == limit)) {
//data fetch
                    isScroll = false;
                    if (!hitStatus) {
                        getHashTagDetails();
                    } else {
                        Toast.makeText(HashTagVideoActivity.this, "Loading more videos", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void getHashTagDetails() {
        progressDialog.show();
        hitStatus = true;
        soundsMvvm.hashTagDetails(activity, hashTagId, userId, String.valueOf(startLimit)).observe(HashTagVideoActivity.this, new Observer<ModelHashTagsDetails>() {
            @Override
            public void onChanged(final ModelHashTagsDetails modelHashTagsDetails) {
                hitStatus = false;
                if (modelHashTagsDetails.getSuccess().equalsIgnoreCase("1")) {

                    for (int i =0 ; i<modelHashTagsDetails.getDetails().getHashtagVideo().size(); i++){
                        list.add(modelHashTagsDetails.getDetails().getHashtagVideo().get(i));
                    }
                    limit = list.size();
                    startLimit = limit;

                    tv_hashtag_tags.setText(modelHashTagsDetails.getDetails().getHashtagInfo().getHashtagMainTitle());
                    tv_hashtag_owner.setVisibility(View.GONE);
//                    tv_hashtag_owner.setText(modelHashTagsDetails.getDetails().getHashtagInfo().getUsername());
//                    tv_videos_count.setText(modelHashTagsDetails.getDetails().getHashtagInfo().getVideoCount() + " videos");

                    if (modelHashTagsDetails.getDetails().getHashtagInfo().getFavoritesStatus().equalsIgnoreCase("1")) {
                        tv_fvrtText.setText(R.string.removeFromFvrt);
                        Glide.with(activity).load(R.drawable.ic_starfilled).into(img_star_filled);
                    } else {
                        tv_fvrtText.setText(R.string.addToFvrt);
                        Glide.with(activity).load(R.drawable.ic_star).into(img_star_filled);
                    }

                } else {
                    Toast.makeText(activity, modelHashTagsDetails.getMessage(), Toast.LENGTH_SHORT).show();
                }
//                adapterHashTagDetails.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });
    }

    private void findIds() {
        img_star_filled = findViewById(R.id.img_star_filled);
        tv_videos_count = findViewById(R.id.tv_videos_count);
        tv_hashtag_owner = findViewById(R.id.tv_hashtag_owner);
        tv_hashtag_tags = findViewById(R.id.tv_hashtag_tags);
        tv_fvrtText = findViewById(R.id.tv_fvrtText);
        recycler_hash_tags = findViewById(R.id.recycler_hash_tags);
        findViewById(R.id.ll_add_favorites).setOnClickListener(this);

        tv_videos_count.setText(videoCount + " videos");
    }

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add_favorites:
                addToFavorites();
                break;

        }
    }

    private void addToFavorites() {
        soundsMvvm.addhashTagFav(HashTagVideoActivity.this, userId, hashTagId).observe(HashTagVideoActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    if (tv_fvrtText.getText().equals(getResources().getString(R.string.removeFromFvrt))) {
                        tv_fvrtText.setText(getResources().getString(R.string.add_to_favorites));
                        Glide.with(activity).load(R.drawable.ic_star).into(img_star_filled);
                    } else {

                        tv_fvrtText.setText(getResources().getString(R.string.removeFromFvrt));
                        Glide.with(activity).load(R.drawable.ic_starfilled).into(img_star_filled);
                    }
                } else {
                    Toast.makeText(activity, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
