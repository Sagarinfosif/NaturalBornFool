package in.pureway.cinemaflix.adapters;

import android.app.Activity;
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
import in.pureway.cinemaflix.models.ModelFollowers;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterOtherFollowers extends RecyclerView.Adapter<AdapterOtherFollowers.ViewHolder> {

    Activity mcontext;
    List<ModelFollowers.Detail> list;
    Select select;

    public interface Select {
        void followBack(int position);

        void moveToProfile(int position);
    }

    public AdapterOtherFollowers(Activity context, List<ModelFollowers.Detail> list, Select select) {
        this.mcontext = context;
        this.list = list;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.list_other_followers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        LocalBroadcastManager.getInstance(mcontext).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getExtras() != null) {
                    String follow_unfollow = intent.getExtras().getString(AppConstants.FOLLOW_UNFOLLOW);
                    if (holder.getAdapterPosition() == intent.getExtras().getInt(AppConstants.POSITION)) {
                        if (follow_unfollow.equalsIgnoreCase(AppConstants.FOLLOW)) {
                            holder.btn_follow_back.setText("Following");
                        } else if (follow_unfollow.equalsIgnoreCase(AppConstants.UNFOLLOW)) {
                            holder.btn_follow_back.setText("Follow");
                        }
                    }
                }
            }
        }, new IntentFilter(AppConstants.FOLLOW_UNFOLLOW));

        Glide.with(mcontext).load(list.get(position).getImage()).placeholder(mcontext.getResources().getDrawable(R.drawable.ic_user1)).into(holder.img_following);

        if (list.get(position).getName() != null) {
            if (list.get(position).getName().equalsIgnoreCase("")) {
                holder.tv_following_name.setVisibility(View.GONE);
            } else {
                holder.tv_following_name.setText(list.get(position).getName());
            }
        }

        if (CommonUtils.userId(mcontext).equalsIgnoreCase(list.get(position).getUserId())) {
            holder.btn_follow_back.setVisibility(View.GONE);
        }
        if (list.get(position).getFriendStatus()) {
            holder.btn_follow_back.setText(mcontext.getResources().getString(R.string.friends));
        }

        holder.tv_following_username.setText(list.get(position).getUsername());

        holder.btn_follow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.followBack(position);
            }
        });

        holder.img_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.moveToProfile(position);
            }
        });
        holder.tv_following_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.moveToProfile(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img_following;
        private TextView tv_following_username, tv_following_name;
        private Button btn_follow_back;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_follow_back = itemView.findViewById(R.id.btn_follow_contact);
            tv_following_name = itemView.findViewById(R.id.tv_contact_name);
            tv_following_username = itemView.findViewById(R.id.tv_contact_username);
            img_following = itemView.findViewById(R.id.img_find_contacts);

        }
    }
}

