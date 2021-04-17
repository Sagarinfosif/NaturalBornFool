package in.pureway.cinemaflix.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.SearchAllPojo;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterSearchPeopleRj extends RecyclerView.Adapter<AdapterSearchPeopleRj.ViewHolder> {
    Context context;
    private List<SearchAllPojo.Details.PersonList> list;
    Select select;

    public interface Select {
        void followUnfollow(int position);

        void moveToProfile(int position);
    }

    public AdapterSearchPeopleRj(Context context, List<SearchAllPojo.Details.PersonList> list, Select select) {
        this.context = context;
        this.list = list;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_people_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        if (App.getSharedpref().isLogin(context)) {
            holder.btn_follow_back.setVisibility(View.VISIBLE);
        } else {
            holder.btn_follow_back.setVisibility(View.GONE);

        }
        if (!list.get(position).getImage().equalsIgnoreCase("")) {
            Glide.with(context).load(list.get(position).getImage()).into(holder.userImage);
        }

        holder.userName.setText(list.get(position).getUsername());

        if (!list.get(position).getName().equalsIgnoreCase("")) {
            holder.fullNAme.setText(list.get(position).getName());
        } else {
            holder.fullNAme.setVisibility(View.GONE);
        }

        if (list.get(position).getFollowStatus().equalsIgnoreCase("1")) {
            holder.btn_follow_back.setText(context.getResources().getString(R.string.following));
        } else if (list.get(position).getFollowStatus().equalsIgnoreCase("2")) {
            holder.btn_follow_back.setText(context.getResources().getString(R.string.follow_back));
        } else if (list.get(position).getFollowStatus().equalsIgnoreCase("3")) {
            holder.btn_follow_back.setText(context.getResources().getString(R.string.friends));
        } else {
            holder.btn_follow_back.setText(context.getResources().getString(R.string.follow));
        }

        holder.btn_follow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.followUnfollow(position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.moveToProfile(position);
            }
        });

        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String follow_unfollow = intent.getExtras().getString(AppConstants.NOTI_FOLLOW);
                if (holder.getAdapterPosition() == intent.getExtras().getInt(AppConstants.POSITION)) {
                    if (follow_unfollow != null) {
                        if (follow_unfollow.equalsIgnoreCase(AppConstants.FOLLOW)) {
                            holder.btn_follow_back.setText(context.getResources().getString(R.string.following));
                        } else if (follow_unfollow.equalsIgnoreCase(AppConstants.UNFOLLOW)) {
                            holder.btn_follow_back.setText(context.getResources().getString(R.string.follow));
                        }
                    }

                }
            }
        }, new IntentFilter(AppConstants.FOLLOW_UNFOLLOW));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView peopleLinearLayout, videoLinearLayout, hashTagsLinearLayout;
        private CircleImageView userImage;
        private ImageView iv_thumbnail;
        private TextView userName, fullNAme, tv_hashTag, tv_noOfPosts, tv_videoViews;
        private Button btn_follow_back;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            peopleLinearLayout = itemView.findViewById(R.id.card_peopleType);
            videoLinearLayout = itemView.findViewById(R.id.card_videoType);
            hashTagsLinearLayout = itemView.findViewById(R.id.card_hashTagType);
//            tv_videoViews=itemView.findViewById(R.id.tv_videoViews);
            iv_thumbnail = itemView.findViewById(R.id.iv_thumbnail);
            userImage = itemView.findViewById(R.id.img_following);
            userName = itemView.findViewById(R.id.tv_following_username);
            fullNAme = itemView.findViewById(R.id.tv_following_name);
            btn_follow_back = itemView.findViewById(R.id.btn_follow_back);
            tv_hashTag = itemView.findViewById(R.id.tv_hashTag);
            tv_noOfPosts = itemView.findViewById(R.id.tv_noOfPosts);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position);
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }
}