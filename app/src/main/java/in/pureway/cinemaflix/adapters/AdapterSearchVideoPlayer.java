package in.pureway.cinemaflix.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.utils.App;
import com.like.LikeButton;
import com.like.OnLikeListener;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.SearchAllPojo;
import in.pureway.cinemaflix.utils.AppConstants;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterSearchVideoPlayer extends RecyclerView.Adapter<AdapterSearchVideoPlayer.ViewHolder> {
    Context context;
    List<SearchAllPojo.Details.VideoList> list;
    Select select;

    public interface Select {
        void comments(int position);

        void share(int position);

        void sounds(int position);

        void duetLayout(int position);

        void likeVideo(int position);

        void hashTags(int position);

        void moveToProfile(int position);
        void download(int position);

        void folowUnfollow(int position);
        void sideMenu(int position);

    }

    public AdapterSearchVideoPlayer(Context context, List<SearchAllPojo.Details.VideoList> listSearchVideo, Select select) {
        this.context = context;
        this.list = listSearchVideo;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_video_single, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

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
        if (App.getSharedpref().isLogin(context)) {

            LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getExtras() != null) {
                        String likeCounts = intent.getExtras().getString(AppConstants.LIKE_COUNT);
                        if (holder.getAdapterPosition() == intent.getExtras().getInt(AppConstants.POSITION)) {
                            if (intent.getExtras().getInt("type") == 1) {
                                if (holder.btn_like_video.isLiked()) {
                                    holder.btn_like_video.setLiked(false);
//                                holder.visbilityHeartIVSIN.setVisibility(View.VISIBLE);
                                } else {
                                    holder.btn_like_video.setLiked(true);
//                                holder.visbilityHeartIVSIN.setVisibility(View.GONE);
                                }
                            }
                            holder.tv_likes_video_home.setText(likeCounts);
                        }
                    }
                }
            }, new IntentFilter(AppConstants.TYPE_LIKED));
        }

        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getExtras() != null) {
                    String follow_unfollow = intent.getExtras().getString(AppConstants.FOLLOW_UNFOLLOW);
                    if (holder.getAdapterPosition() == intent.getExtras().getInt(AppConstants.POSITION)) {
                        if (follow_unfollow.equalsIgnoreCase(AppConstants.FOLLOW)) {
                            holder.tv_follow.setText(context.getResources().getString(R.string.following));
                            holder.tv_follow.setVisibility(View.GONE);

                        } else if (follow_unfollow.equalsIgnoreCase(AppConstants.UNFOLLOW)) {
                            holder.tv_follow.setText(context.getResources().getString(R.string.follow));
                        }
                    }
                }
            }
        }, new IntentFilter(AppConstants.FOLLOW_UNFOLLOW));

        holder.tv_name_video_home.setText(list.get(position).getUsername());
        holder.tv_description.setText(list.get(position).getDescription());
        holder.tv_hashtags_list.setText(list.get(position).getHashtagTitle());
        holder.tv_sound_video.setText(list.get(position).getSoundTitle());
        holder.tv_likes_video_home.setText(list.get(position).getLikeCount());
        holder.tv_comments_count.setText(list.get(position).getCommentCount());
        holder.tv_followers_count.setText(list.get(position).getFollowers());


        holder.tv_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getFollowStatus().equalsIgnoreCase("1")||list.get(position).getFollowStatus().equalsIgnoreCase("true")) {
                    holder.tv_follow.setText("Following");
                    holder.tv_follow.setVisibility(View.GONE);
                    select.folowUnfollow(position);

                } else {
                    holder.tv_follow.setText("Follow");
                    select.folowUnfollow(position);

                }

            }
        });

        if (list.get(position).getFollowStatus().equalsIgnoreCase("1")||list.get(position).getFollowStatus().equalsIgnoreCase("true")) {
            holder.tv_follow.setText("Following");
            holder.tv_follow.setVisibility(View.GONE);
        }
        else {
            holder.tv_follow.setText("Follow");

        }
        if (list.get(position).getDescription().equalsIgnoreCase("")) {
            holder.tv_description.setVisibility(View.GONE);
        }
        if (list.get(position).getHashTag().equalsIgnoreCase("")) {
            holder.tv_hashtags_list.setVisibility(View.GONE);
        }
        if (list.get(position).getAllowDuetReact() != null) {
            if (list.get(position).getAllowDuetReact().equalsIgnoreCase("0")) {
                holder.ll_duet_layout.setVisibility(View.GONE);
            }
        }
        Glide.with(context).load(list.get(position).getImage()).placeholder(context.getDrawable(R.drawable.ic_user1)).into(holder.img_poster);

        if (list.get(position).getLikeStatus()) {
            holder.btn_like_video.setLiked(true);
//            holder.visbilityHeartIVSIN.setVisibility(View.GONE);
        } else {
            holder.btn_like_video.setLiked(false);

//            holder.visbilityHeartIVSIN.setVisibility(View.VISIBLE);
        }
        holder.btn_like_video.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
//                select.likeVideo(position);
                if (App.getSharedpref().isLogin(context)) {
                    select.likeVideo(position);
                } else {
                    holder.btn_like_video.setLiked(false);
                }
//                holder.btn_like_video.setLiked(true);

//                holder.visbilityHeartIVSIN.setVisibility(View.GONE);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
//                select.likeVideo(position);

                if (App.getSharedpref().isLogin(context)) {
                    select.likeVideo(position);
                } else {
                    holder.btn_like_video.setLiked(false);
                }
//                holder.btn_like_video.setLiked(true);

//                holder.visbilityHeartIVSIN.setVisibility(View.VISIBLE);
            }
        });

        holder.ll_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.download(position);
            }
        });

        holder.ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.share(position);
            }
        });

        holder.ll_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.comments(position);
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

        holder.tv_hashtags_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.hashTags(position);
            }
        });

        holder.ll_recycler_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.moveToProfile(position);
            }
        });
        holder.ll_duet_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.duetLayout(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_likes_video, ll_duet_layout, ll_recycler_add, ll_sounds_video_hme, ll_share, ll_comments,ll_download;
        private VideoView video_home;
        private TextView tv_name_video_home, tv_sound_video, tv_hashtags_list, tv_description, tv_likes_video_home, tv_comments_count, tv_followers_count;
        private LikeButton btn_like_video;
        private CircleImageView img_poster;
        //        private ImageView visbilityHeartIVSIN;
        private TextView tv_follow;
        private ImageView img_Side_menu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_download = itemView.findViewById(R.id.ll_download);
            img_Side_menu = itemView.findViewById(R.id.img_Side_menu);
            tv_follow = itemView.findViewById(R.id.tv_follow);

            ll_duet_layout = itemView.findViewById(R.id.duet_sound_layout);
            tv_followers_count = itemView.findViewById(R.id.tv_followers_count);
            img_poster = itemView.findViewById(R.id.img_poster);
            btn_like_video = itemView.findViewById(R.id.btn_like_video);
            tv_comments_count = itemView.findViewById(R.id.tv_comments_count);
            tv_likes_video_home = itemView.findViewById(R.id.tv_likes_video_home);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_hashtags_list = itemView.findViewById(R.id.tv_hashtags_list);
            ll_sounds_video_hme = itemView.findViewById(R.id.ll_sounds_video_hme);
            tv_sound_video = itemView.findViewById(R.id.tv_sound_video);
            tv_name_video_home = itemView.findViewById(R.id.tv_name_video_home);
            ll_share = itemView.findViewById(R.id.ll_share);
            ll_likes_video = itemView.findViewById(R.id.ll_likes_video);
            ll_recycler_add = itemView.findViewById(R.id.ll_recycler_add);
            ll_comments = itemView.findViewById(R.id.ll_comments);

            img_Side_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.sideMenu(getLayoutPosition());
                }
            });
//            visbilityHeartIVSIN = itemView.findViewById(R.id.visbilityHeartIVSIN);
        }
    }
}
