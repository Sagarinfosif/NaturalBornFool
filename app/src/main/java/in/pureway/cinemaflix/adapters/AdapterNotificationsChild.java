package in.pureway.cinemaflix.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelNotifications;
import in.pureway.cinemaflix.utils.AppConstants;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterNotificationsChild extends RecyclerView.Adapter<AdapterNotificationsChild.ViewHolder> {

    Context context;
    List<ModelNotifications.Detail.Listdetail> listdetails;
    Select select;

    public interface Select {
        void chooseNotification(int position);

        void followUser(int position);

        void unfollowUser(int position);

        void commentUser(int position, String videoId);

        void videoPlay(int position, String videoId);
        void playDeletedVideo(int position, String videoUrl);
        void DeletedVideo(int position, String Url);
        void adminDialog(int position, String message);

    }

    public AdapterNotificationsChild(Context context, List<ModelNotifications.Detail.Listdetail> listdetails, Select select) {
        this.context = context;
        this.listdetails = listdetails;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notifications_child, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getExtras().getString(AppConstants.NOTI_FOLLOW) != null) {
                    if (intent.getExtras().getString(AppConstants.NOTI_FOLLOW).equalsIgnoreCase(AppConstants.FOLLOW)) {
                        if (holder.getAdapterPosition() == intent.getExtras().getInt(AppConstants.POSITION)) {
                            holder.btn_unfollow_noti.setVisibility(View.VISIBLE);
                            holder.btn_follow_signin.setVisibility(View.GONE);
                        }
                    } else if (intent.getExtras().getString(AppConstants.NOTI_FOLLOW).equalsIgnoreCase(AppConstants.UNFOLLOW)) {
                        if (holder.getAdapterPosition() == intent.getExtras().getInt(AppConstants.POSITION)) {

                            holder.btn_unfollow_noti.setVisibility(View.GONE);
                            holder.btn_follow_signin.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }, new IntentFilter(AppConstants.FOLLOW_UNFOLLOW));


        if (listdetails.get(position).getType().equalsIgnoreCase(AppConstants.NOTI_FOLLOW)) {
            holder.ll_follow_notifications.setVisibility(View.VISIBLE);

            if (listdetails.get(position).getFollowStatus()) {
                holder.btn_follow_signin.setVisibility(View.GONE);
                holder.btn_unfollow_noti.setVisibility(View.VISIBLE);
            } else if (!listdetails.get(position).getFollowStatus()) {
                holder.btn_follow_signin.setVisibility(View.VISIBLE);
                holder.btn_unfollow_noti.setVisibility(View.GONE);
            }
            Log.i("imageNoti", listdetails.get(position).getImage() + " : " + position);
            Glide.with(context).load(listdetails.get(position).getImage()).placeholder(context.getResources().getDrawable(R.drawable.ic_user1)).into(holder.img_follow_noti);
            holder.tv_follow_data.setText(listdetails.get(position).getMessage() + "." + "\n" + listdetails.get(position).getTime());

            holder.btn_follow_signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.followUser(position);
                }
            });

            holder.btn_unfollow_noti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.unfollowUser(position);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.chooseNotification(position);
                }
            });
        } else if (listdetails.get(position).getType().equalsIgnoreCase(AppConstants.NOTI_LIKE)) {
            holder.ll_like_notification.setVisibility(View.VISIBLE);
            Glide.with(context).load(listdetails.get(position).getImage()).placeholder(context.getResources().getDrawable(R.drawable.ic_user)).into(holder.img_like_noti);
            Glide.with(context).load(listdetails.get(position).getVideo()).placeholder(context.getResources().getDrawable(R.drawable.ic_user1)).into(holder.img_like_video);
            holder.tv_like_data.setText(listdetails.get(position).getMessage() + "\n" + listdetails.get(position).getTime());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.chooseNotification(position);
                }
            });

            holder.img_like_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select.videoPlay(position, listdetails.get(position).getVideoId());
                }
            });
        } else if (listdetails.get(position).getType().equalsIgnoreCase(AppConstants.NOTI_COMMENT)) {
            holder.ll_comment_notification.setVisibility(View.VISIBLE);
            Glide.with(context).load(listdetails.get(position).getImage()).placeholder(context.getResources().getDrawable(R.drawable.poat)).into(holder.img_comment_noti);
            Glide.with(context).load(listdetails.get(position).getVideo()).placeholder(context.getResources().getDrawable(R.drawable.poat)).into(holder.img_comment_video);
            holder.tv_comment_data.setText(listdetails.get(position).getMessage() + "\n" + listdetails.get(position).getTime());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select.chooseNotification(position);
//                    select.commentUser(position);
                    select.videoPlay(position, listdetails.get(position).getVideoId());
                }
            });

            holder.img_comment_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select.commentUser(position,listdetails.get(position).getVideoId());
//                    select.videoPlay(position, listdetails.get(position).getVideoId());
                }
            });

        }
        else if (listdetails.get(position).getType().equalsIgnoreCase(AppConstants.NOTI_VIDEO)) {

            holder.ll_uploadVideo_notification.setVisibility(View.VISIBLE);
            Glide.with(context).load(listdetails.get(position).getImage()).placeholder(context.getResources().getDrawable(R.drawable.poat)).into(holder.img_videoUpload_noti);
//            Glide.with(context).load(listdetails.get(position).getVideo()).placeholder(context.getResources().getDrawable(R.drawable.poat)).into(holder.img_videoUpload_noti);
            holder.tv_videoUpload_data.setText(listdetails.get(position).getMessage() + "\n" + listdetails.get(position).getTime());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select.chooseNotification(position);
//                    select.commentUser(position);
//                    select.videoPlay(position, listdetails.get(position).getVideoId());
                }
            });

            holder.btn_videoUpload_noti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select.videoPlay(position, listdetails.get(position).getVideoId());
                }
            });
        }

        else if (listdetails.get(position).getType().equalsIgnoreCase(AppConstants.DELETEVIDEO)) {

            holder.btn_Delete_Video.setVisibility(View.VISIBLE);
            holder.ll_ReportDelete.setVisibility(View.VISIBLE);
//            Glide.with(context).load(listdetails.get(position).getImage()).placeholder(context.getResources().getDrawable(R.drawable.poat)).into(holder.img_videoUpload_noti);
//            Glide.with(context).load(listdetails.get(position).getVideo()).placeholder(context.getResources().getDrawable(R.drawable.poat)).into(holder.img_videoUpload_noti);
            holder.tv_ReportDelete.setText(listdetails.get(position).getMessage() + "\n" + listdetails.get(position).getTime());

//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    select.chooseNotification(position);
////                    select.commentUser(position);
////                    select.videoPlay(position, listdetails.get(position).getVideoId());
//                }
//            });

            holder.btn_Delete_Video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    select.videoPlay(position, listdetails.get(position).getVideoId());
                    select.DeletedVideo(position, listdetails.get(position).getVideoUrl());
                }
            });
        }

        else if (listdetails.get(position).getType().equalsIgnoreCase(AppConstants.ApproveVideo)) {

            holder.btn_approve_Vide.setVisibility(View.GONE);
            holder.ll_ReportApprove.setVisibility(View.VISIBLE);
//            Glide.with(context).load(listdetails.get(position).getImage()).placeholder(context.getResources().getDrawable(R.drawable.poat)).into(holder.img_videoUpload_noti);
//            Glide.with(context).load(listdetails.get(position).getVideo()).placeholder(context.getResources().getDrawable(R.drawable.poat)).into(holder.img_videoUpload_noti);
            holder.tv_ReportApprove.setText(listdetails.get(position).getMessage() + "\n" + listdetails.get(position).getTime());


            holder.btn_approve_Vide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select.videoPlay(position, listdetails.get(position).getVideoId());
                }
            });
        }

        else if (listdetails.get(position).getType().equalsIgnoreCase(AppConstants.REJECTVIDEO)) {

            holder.btn_approve_Vide.setVisibility(View.GONE);
            holder.ll_rejectVideo.setVisibility(View.VISIBLE);
//            Glide.with(context).load(listdetails.get(position).getImage()).placeholder(context.getResources().getDrawable(R.drawable.poat)).into(holder.img_videoUpload_noti);
//            Glide.with(context).load(listdetails.get(position).getVideo()).placeholder(context.getResources().getDrawable(R.drawable.poat)).into(holder.img_videoUpload_noti);
            holder.tv_ll_rejectVideo.setText(listdetails.get(position).getMessage() + "\n" + listdetails.get(position).getTime());


            holder.btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    select.videoPlay(position, listdetails.get(position).getVideoId());
                    select.playDeletedVideo(position, listdetails.get(position).getVideo());
                }
            });
        }


        else if (listdetails.get(position).getType().equalsIgnoreCase(AppConstants.PUSHNOTIFICATION)) {

            holder.ll_AdminMassage.setVisibility(View.VISIBLE);
//            Glide.with(context).load(listdetails.get(position).getImage()).placeholder(context.getResources().getDrawable(R.drawable.poat)).into(holder.img_videoUpload_noti);
//            Glide.with(context).load(listdetails.get(position).getVideo()).placeholder(context.getResources().getDrawable(R.drawable.poat)).into(holder.img_videoUpload_noti);
            holder.tv_ll_AdminMassage.setText(listdetails.get(position).getMessage());
            holder.tv_ll_AdminTime.setText(listdetails.get(position).getTime());


            holder.ll_AdminMassage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select.adminDialog(position, listdetails.get(position).getMessage());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listdetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_like_notification, ll_follow_notifications, ll_comment_notification, ll_uploadVideo_notification,ll_ReportDelete,ll_ReportApprove,ll_rejectVideo,ll_AdminMassage;
        private TextView tv_like_data, tv_follow_data, tv_comment_data, tv_videoUpload_data,tv_ReportDelete,tv_ReportApprove,tv_ll_rejectVideo,tv_ll_AdminMassage,tv_ll_AdminTime;
        private CircleImageView img_like_noti, img_follow_noti, img_comment_noti, img_videoUpload_noti;
        private ImageView img_like_video, img_comment_video,img__ReportDelete,img__ReportApprove,img_ll_rejectVideo;
        private Button btn_follow_signin, btn_unfollow_noti, btn_videoUpload_noti,btn_Delete_Video,btn_reject,btn_approve_Vide;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);



            tv_ll_AdminTime = itemView.findViewById(R.id.tv_ll_AdminTime);
            ll_AdminMassage = itemView.findViewById(R.id.ll_AdminMassage);
            tv_ll_AdminMassage = itemView.findViewById(R.id.tv_ll_AdminMassage);

            btn_approve_Vide = itemView.findViewById(R.id.btn_approve_Vide);
            btn_reject = itemView.findViewById(R.id.btn_reject);
            btn_Delete_Video = itemView.findViewById(R.id.btn_Delete_Video);
            ll_rejectVideo = itemView.findViewById(R.id.ll_rejectVideo);
            img_ll_rejectVideo = itemView.findViewById(R.id.img_ll_rejectVideo);
            tv_ll_rejectVideo = itemView.findViewById(R.id.tv_ll_rejectVideo);

            img__ReportApprove = itemView.findViewById(R.id.img__ReportApprove);
            tv_ReportApprove = itemView.findViewById(R.id.tv_ReportApprove);
            ll_ReportApprove = itemView.findViewById(R.id.ll_ReportApprove);

            ll_ReportDelete = itemView.findViewById(R.id.ll_ReportDelete);
            img__ReportDelete = itemView.findViewById(R.id.img__ReportDelete);
            tv_ReportDelete = itemView.findViewById(R.id.tv_ReportDelete);

            btn_unfollow_noti = itemView.findViewById(R.id.btn_unfollow_noti);
            btn_follow_signin = itemView.findViewById(R.id.btn_follow_signin);
            btn_videoUpload_noti = itemView.findViewById(R.id.btn_videoUpload_noti);
            img_follow_noti = itemView.findViewById(R.id.img_follow_noti);
            tv_follow_data = itemView.findViewById(R.id.tv_follow_data);
            tv_follow_data = itemView.findViewById(R.id.tv_follow_data);
            tv_comment_data = itemView.findViewById(R.id.tv_comment_data);
            tv_videoUpload_data = itemView.findViewById(R.id.tv_videoUpload_data);
            img_like_video = itemView.findViewById(R.id.img_like_video);
            img_comment_video = itemView.findViewById(R.id.img_comment_video);
            img_like_noti = itemView.findViewById(R.id.img_like_noti);
            img_comment_noti = itemView.findViewById(R.id.img_comment_noti);
            img_videoUpload_noti = itemView.findViewById(R.id.img_videoUpload_noti);
            tv_like_data = itemView.findViewById(R.id.tv_like_data);
            ll_follow_notifications = itemView.findViewById(R.id.ll_follow_notifications);
            ll_like_notification = itemView.findViewById(R.id.ll_like_notification);
            ll_comment_notification = itemView.findViewById(R.id.ll_comment_notification);
            ll_uploadVideo_notification = itemView.findViewById(R.id.ll_uploadVideo_notification);
        }
    }
}