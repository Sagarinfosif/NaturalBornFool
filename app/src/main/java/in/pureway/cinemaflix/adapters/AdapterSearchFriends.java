package in.pureway.cinemaflix.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelSearchFriend;
import in.pureway.cinemaflix.utils.AppConstants;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterSearchFriends extends RecyclerView.Adapter<AdapterSearchFriends.ViewHolder> {

    Context context;
    List<ModelSearchFriend.Detail> list;
    Select select;

    public interface Select {
        void followUnfollow(int position);
        void moveToId(int position);
    }

    public AdapterSearchFriends(Context context, List<ModelSearchFriend.Detail> list, Select select) {
        this.context = context;
        this.list = list;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_following, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getImage()).placeholder(context.getResources().getDrawable(R.drawable.ic_user1)).into(holder.img_following);
        if (!list.get(position).getName().equalsIgnoreCase("")) {
            holder.tv_following_name.setText(list.get(position).getName());
        } else {
            holder.tv_following_name.setVisibility(View.GONE);
        }
        holder.tv_following_username.setText(list.get(position).getUsername());

        String status = list.get(position).getStatus();
        switch (status) {
            case "0":
                holder.btn_follow_user.setText(context.getString(R.string.follow));
                break;

            case "1":
                holder.btn_follow_user.setText(context.getString(R.string.following));
                break;

            case "2":
                holder.btn_follow_user.setText(context.getString(R.string.follow_back));
                break;

            case "3":
                holder.btn_follow_user.setText(context.getString(R.string.friends));
                break;
        }

        holder.img_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.moveToId(position);
            }
        });

        holder.tv_following_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.moveToId(position);
            }
        });

        holder.btn_follow_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.followUnfollow(position);
            }
        });

        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (holder.getAdapterPosition() == intent.getExtras().getInt(AppConstants.POSITION)) {
                    String followunFollow = intent.getExtras().getString(AppConstants.FOLLOW_UNFOLLOW);
                    if (followunFollow.equalsIgnoreCase(AppConstants.FOLLOW)) {
                        holder.btn_follow_user.setText("Unfollow");
                    } else {
                        holder.btn_follow_user.setText("Follow");
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
        private CircleImageView img_following;
        private TextView tv_following_username, tv_following_name;
        private Button btn_follow_user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_follow_user = itemView.findViewById(R.id.btn_follow_user);
            tv_following_name = itemView.findViewById(R.id.tv_following_name);
            tv_following_username = itemView.findViewById(R.id.tv_following_username);
            img_following = itemView.findViewById(R.id.img_following);
        }
    }
}
