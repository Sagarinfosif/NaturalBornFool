package in.pureway.cinemaflix.adapters;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;


public class AdapterVideoHome1 extends RecyclerView.Adapter<AdapterVideoHome1.ViewHolder> {

    Context context;
    private Activity activity;
    Select select;
    List<ModelVideoTesting.Detail> list;
    //    List<ModelVideoList.Detail.HastagList> hashTagList = new ArrayList<>();
    private int size = 1;
    HashTagHomeListAdapter hashTagHomeListAdapter;

    public interface Select {
        void likeVideo(int position);

        void unlikeVideo(int position);

        void addFriends(int position);

        void duetLayout(int position);

        void download(int position);

        void share(int position);

        void shareAddVideo(int position);

        void user(int position);

        void comments(int position);

        void sounds(int position);

        void folowUnfollow(String userId, int position);

        void hashTags(String id);

        void sideMenu(int position);

        void adMoveTOBroswer(int position);

//        void follow_unfollow(int position);
    }

    public AdapterVideoHome1(Context context, List<ModelVideoTesting.Detail> list, Select select) {
        this.context = context;
        this.select = select;
        this.list = list;
//        this.hashTagList = hashTagList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_video_home1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if (list.get(position).getAdvertisementStatus().equalsIgnoreCase("1")) {
            holder.ll_share.setVisibility(View.GONE);
            holder.ll_recycler_add.setVisibility(View.GONE);
            holder.ll_comments.setVisibility(View.GONE);
            holder.tv_likes_video_home.setVisibility(View.GONE);
            holder.btn_like_video.setVisibility(View.GONE);
            holder.duet_sound_layout.setVisibility(View.GONE);



            holder.tv_sound_video.setVisibility(View.GONE);
//            holder.tv_follow.setVisibility(View.GONE);
            holder.img_follow_live_user.setVisibility(View.GONE);

            holder.tv_name_video_home.setTextSize(18);
            holder.tv_description.setTextSize(16);
            holder.tv_name_video_home.setText(list.get(position).getSponsored());
            holder.tv_description.setText(list.get(position).getVideoTitle());


            holder.ll_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.shareAddVideo(position);
                }
            });

            holder.tv_add_Click.setVisibility(View.VISIBLE);
            holder.tv_add_Click.setText(list.get(position).getButtonName());
            holder.tv_add_Click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.adMoveTOBroswer(position);
                }
            });
        } else {

            holder.ll_share.setVisibility(View.GONE);

            holder.img_follow_live_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (list.get(position).getFollowStatus().equalsIgnoreCase("0")) {
                        holder.img_follow_live_user.setText("Follow");

//                        Glide.with(context).load(R.drawable.ic_baseline_add_24).into(holder.img_follow_live_user);
                        select.folowUnfollow(list.get(position).getUserId(), position);
                    } else if (list.get(position).getFollowStatus().equalsIgnoreCase("1")) {

                        holder.img_follow_live_user.setText("Following");

//                        Glide.with(context).load(R.drawable.ic_checked).into(holder.img_follow_live_user);
                        select.folowUnfollow(list.get(position).getUserId(), position);
                    }

//                    if (list.get(position).getFollowStatus().equalsIgnoreCase("1")) {
//                        holder.tv_follow.setVisibility(View.GONE);
//                        select.folowUnfollow(position);
//
//                    } else {
//                        holder.tv_follow.setText("Follow");
//                        select.folowUnfollow(position);
//
//                    }

                }
            });

// TODO comment

            if (list.get(position).getFollowStatus().equalsIgnoreCase("0")) {
                holder.img_follow_live_user.setText("Follow");
//                Glide.with(context).load(R.drawable.ic_baseline_add_24).into(holder.img_follow_live_user);

            } else {

                holder.img_follow_live_user.setText("Following");

//                Glide.with(context).load(R.drawable.ic_checked).into(holder.img_follow_live_user);
            }



//            holder.tv_follow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (list.get(position).getFollowStatus().equalsIgnoreCase("1")) {
//                        holder.tv_follow.setVisibility(View.GONE);
//                        select.folowUnfollow(position);
//
//                    } else {
//                        holder.tv_follow.setText("Follow");
//                        select.folowUnfollow(position);
//
//                    }
//
//                }
//            });

//            if (list.get(position).getFollowStatus().equalsIgnoreCase("1")) {
//                holder.tv_follow.setVisibility(View.GONE);
//            } else {
//                holder.tv_follow.setText("Follow");
//
//            }
            holder.tv_name_video_home.setText(list.get(position).getUsername());
            holder.tv_description.setText(list.get(position).getDescription());

            if (!list.get(position).getHashtagTitle().equalsIgnoreCase("")) {
                holder.tv_hashtags_list.setText(list.get(position).getHashtagTitle());

            } else {
                holder.tv_hashtags_list.setVisibility(View.GONE);
            }
            holder.tv_sound_video.setText(list.get(position).getSoundTitle());
            holder.tv_likes_video_home.setText(list.get(position).getLikeCount());
            holder.tv_comments_count.setText(list.get(position).getCommentCount());
            holder.tv_followers_count.setText(list.get(position).getFollowers());

            if (list.get(position).getDescription().equalsIgnoreCase("")) {
                holder.tv_description.setVisibility(View.GONE);
            }
            if (list.get(position).getAllowDuetReact() != null) {
                if (list.get(position).getAllowDuetReact().equalsIgnoreCase("0")) {
                    holder.ll_duet_layout.setVisibility(View.GONE);
                }
            }

            if (list.get(position).getHashtagTitle().equalsIgnoreCase("")) {
                holder.tv_hashtags_list.setVisibility(View.GONE);
            }

            if (list.get(position).getSoundTitle() != null) {
                if (list.get(position).getSoundTitle().equalsIgnoreCase("")) {
                    holder.tv_sound_video.setVisibility(View.GONE);
                }
            }
//        if (list.get(position).getAllowComment().equalsIgnoreCase("0")) {
//            holder.ll_comments.setVisibility(View.GONE);
//        }

            if (list.get(position).getImage()!=null){
                try{
                    Glide.with(context).load(list.get(position).getImage()).placeholder(R.drawable.profile).into(holder.img_poster);

                }catch (Exception e){
//                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            if (list.get(position).getLikeStatus()) {
                if (App.getSharedpref().isLogin(context)) {
                    holder.btn_like_video.setLiked(true);

                } else {
                    holder.btn_like_video.setLiked(false);
                }
            } else {
                if (App.getSharedpref().isLogin(context)) {

                    holder.btn_like_video.setLiked(false);


                } else {
                    holder.btn_like_video.setLiked(false);
                }
            }

            holder.btn_like_video.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {

                    if (App.getSharedpref().isLogin(context)) {
                        select.likeVideo(position);
                    } else {
                        select.likeVideo(position);

                        holder.btn_like_video.setLiked(false);
                    }

                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    if (App.getSharedpref().isLogin(context)) {

                        select.likeVideo(position);

                    } else {
                        holder.btn_like_video.setLiked(false);
                    }

                }
            });


            holder.ll_recycler_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.user(position);
                }
            });

            holder.ll_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.share(position);
                }
            });

            holder.tv_name_video_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.user(position);
                }
            });

            holder.ll_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.comments(position);
                }
            });

            holder.ll_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.download(position);
                }
            });


            holder.tv_sound_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.sounds(position);
                }
            });

            holder.ll_sounds_video_hme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.sounds(position);
                }
            });
            holder.ll_sounds_video_hme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.sounds(position);
                }
            });

            holder.ll_duet_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.duetLayout(position);
                }
            });
            if (App.getSharedpref().isLogin(context)) {

                LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (intent.getExtras() != null) {
//                        if (App.getSharedpref().isLogin(context)) {

                            if (holder.getAdapterPosition() == intent.getExtras().getInt("position")) {
                                if (intent.getExtras().getInt("type") == 1) {

                                    if (intent.getExtras().getBoolean("isLike")) {
                                        if (App.getSharedpref().isLogin(context)) {
                                            holder.btn_like_video.setLiked(true);
                                        } else {
                                            holder.btn_like_video.setLiked(false);
                                        }
                                    } else {
                                        if (App.getSharedpref().isLogin(context)) {
                                            holder.btn_like_video.setLiked(false);
                                        } else {
                                            holder.btn_like_video.setLiked(false);
                                        }
                                    }
                                }
                                String likeCount = intent.getExtras().getString("likeCount");
                                holder.tv_likes_video_home.setText(likeCount);
//                            }
                            }
                        }
                    }
                }, new IntentFilter("like"));
            }
            holder.readModeTv.setVisibility(View.GONE);

//        if (list.get(position).getHastagLists().size()>0||list.get(position).getHastagLists()!=null) {
//            hashTagList.clear();
//            if (list.get(position).getHastagLists().size() > size) {
//                for (int i = 0; i < size; i++) {
//                    hashTagList.add(list.get(position).getHastagLists().get(i));
//                }
//                holder.readModeTv.setVisibility(View.VISIBLE);
//            } else {
//                holder.readModeTv.setVisibility(View.GONE);
//            }
//        }

            if (list.get(position).getHastagLists().size() > 0) {
                holder.hashTagRV.setVisibility(View.VISIBLE);
            }
//        hashTagHomeListAdapter = new HashTagHomeListAdapter(context, list.get(position).getHastagLists(), new HashTagHomeListAdapter.Select() {
//            @Override
//            public void hashTags(String hashTagId) {
//                select.hashTags(hashTagId);
//            }
//        });
//
//        holder.hashTagRV.setAdapter(hashTagHomeListAdapter);

//        holder.hashTagRV.setAdapter(new HashTagHomeListAdapter(context, hashTagList, new HashTagHomeListAdapter.Select() {
//            @Override
//            public void hashTags(String hashTagId) {
//                select.hashTags(hashTagId);
//            }
//        }));

//        holder.readModeTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hashTagList.clear();
//                if (holder.readModeTv.getText().toString().equalsIgnoreCase("Read More..")){
//                    holder.readModeTv.setText("Show Less..");
//
//                    hashTagList= list.get(position).getHastagLists();
//                }else {
//                    holder.readModeTv.setText("Read More..");
//
//                    if (list.get(position).getHastagLists().size()>size){
//                        for (int i =0; i<size; i++){
//                            hashTagList.add(list.get(position).getHastagLists().get(i));
//                        }
//                    }
//                }
//
//                hashTagHomeListAdapter.notifyDataSetChanged();
//
//            }
//        });

        }
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getExtras() != null) {
                    String follow_unfollow = intent.getExtras().getString(AppConstants.FOLLOW_UNFOLLOW);
                    if (holder.getAdapterPosition() == intent.getExtras().getInt(AppConstants.POSITION)) {
                        if (follow_unfollow != null) {
                            if (follow_unfollow.equalsIgnoreCase(AppConstants.FOLLOW)) {
                                holder.img_follow_live_user.setVisibility(View.GONE);
                            } else if (follow_unfollow.equalsIgnoreCase(AppConstants.UNFOLLOW)) {
                                holder.img_follow_live_user.setText(context.getResources().getString(R.string.follow));
                            }
                        }
                    }
                }
            }
        }, new IntentFilter(AppConstants.FOLLOW_UNFOLLOW));

        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.hasExtra(AppConstants.COMMENT_COUNT)) {
                    String commentCount = intent.getStringExtra(AppConstants.COMMENT_COUNT);
                    int broadPosition = intent.getExtras().getInt(AppConstants.POSITION);
                    if (holder.getAdapterPosition() == broadPosition) {
                        holder.tv_comments_count.setText(commentCount);
                    }
                }
            }
        }, new IntentFilter(AppConstants.COMMENT_COUNT_ACTION));


        if (list.get(position).getVideolength() != null) {


        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static LikeButton likeButton;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_likes_video, ll_recycler_add, ll_sounds_video_hme, ll_share, ll_comments, ll_duet_layout, duet_sound_layout, ll_download;
        private VideoView video_home;
        private TextView tv_name_video_home, readModeTv, tv_sound_video, tv_hashtags_list, tv_description, tv_likes_video_home, tv_comments_count, tv_followers_count, tv_add_Click;
        private LikeButton btn_like_video;
        private CircleImageView img_poster;
        private RecyclerView hashTagRV;
//        private TextView tv_follow;
        //        private ImageView visbilityHeartIV;
        private ImageView img_Side_menu;
        private ProgressBar tv_progress_bar;
private TextView img_follow_live_user;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_add_Click = itemView.findViewById(R.id.tv_add_Click);
            img_Side_menu = itemView.findViewById(R.id.img_Side_menu);
            duet_sound_layout = itemView.findViewById(R.id.duet_sound_layout);
            tv_progress_bar = itemView.findViewById(R.id.tv_progress_bar);
//            tv_follow = itemView.findViewById(R.id.tv_follow);
            tv_followers_count = itemView.findViewById(R.id.tv_followers_count);
            img_poster = itemView.findViewById(R.id.img_poster);
            btn_like_video = itemView.findViewById(R.id.btn_like_video);
            likeButton = btn_like_video;

            tv_comments_count = itemView.findViewById(R.id.tv_comments_count);
            tv_likes_video_home = itemView.findViewById(R.id.tv_likes_video_home);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_hashtags_list = itemView.findViewById(R.id.tv_hashtags_list);
            ll_duet_layout = itemView.findViewById(R.id.duet_sound_layout);
            ll_sounds_video_hme = itemView.findViewById(R.id.ll_sounds_video_hme);
            tv_sound_video = itemView.findViewById(R.id.tv_sound_video);
            tv_name_video_home = itemView.findViewById(R.id.tv_name_video_home);
//            video_home = itemView.findViewById(R.id.video_home);
            ll_share = itemView.findViewById(R.id.ll_share);
            ll_likes_video = itemView.findViewById(R.id.ll_likes_video);
            ll_recycler_add = itemView.findViewById(R.id.ll_recycler_add);
            ll_comments = itemView.findViewById(R.id.ll_comments);
            hashTagRV = itemView.findViewById(R.id.hashTagRV);
            readModeTv = itemView.findViewById(R.id.readModeTv);
            ll_download = itemView.findViewById(R.id.ll_download);
            img_follow_live_user = itemView.findViewById(R.id.followVideo);

            img_Side_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.sideMenu(getLayoutPosition());
                }
            });
//            visbilityHeartIV = itemView.findViewById(R.id.visbilityHeartIV);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
