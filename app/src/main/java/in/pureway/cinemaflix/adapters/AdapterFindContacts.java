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
import in.pureway.cinemaflix.models.ModelFindContacts;
import in.pureway.cinemaflix.utils.AppConstants;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFindContacts extends RecyclerView.Adapter<AdapterFindContacts.ViewHolder> {

    Context context;
    List<ModelFindContacts.Detail> list;
    Select select;

    public interface Select {
        void followBack(int position);

        void moveToProfile(int position);
    }

    public AdapterFindContacts(Context context, List<ModelFindContacts.Detail> list, Select select) {
        this.context = context;
        this.list = list;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_other_followers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getImage()).placeholder(context.getResources().getDrawable(R.drawable.ic_user1)).into(holder.img_find_contacts);
        if (!list.get(position).getName().equalsIgnoreCase("")) {
            holder.tv_contact_name.setText(list.get(position).getName());
        } else {
            holder.tv_contact_name.setVisibility(View.GONE);
        }

        holder.tv_contact_username.setText(list.get(position).getUsername());

        String statusFollow = list.get(position).getFollowStatus();
        if (statusFollow.equalsIgnoreCase("0")) {
            holder.btn_follow_contact.setText(context.getResources().getString(R.string.follow));
        } else if (statusFollow.equalsIgnoreCase("1")) {
            holder.btn_follow_contact.setText(context.getResources().getString(R.string.following));
        } else if (statusFollow.equalsIgnoreCase("2")) {
            holder.btn_follow_contact.setText(context.getResources().getString(R.string.follow));
        } else {
            holder.btn_follow_contact.setText(context.getResources().getString(R.string.friends));
        }

        holder.btn_follow_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.followBack(position);
            }
        });

        holder.tv_contact_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.moveToProfile(position);
            }
        });

        holder.img_find_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.moveToProfile(position);
            }
        });

        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getExtras() != null) {
                    String follow_unfollow = intent.getExtras().getString(AppConstants.FOLLOW_UNFOLLOW);
                    if (holder.getAdapterPosition() == intent.getExtras().getInt(AppConstants.POSITION)) {
                        if (follow_unfollow.equalsIgnoreCase(AppConstants.FOLLOW)) {
                            holder.btn_follow_contact.setText(context.getResources().getString(R.string.following));
                        } else if (follow_unfollow.equalsIgnoreCase(AppConstants.UNFOLLOW)) {
                            holder.btn_follow_contact.setText(context.getResources().getString(R.string.follow));
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
        private CircleImageView img_find_contacts;
        private TextView tv_contact_username, tv_contact_name;
        private Button btn_follow_contact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_find_contacts = itemView.findViewById(R.id.img_find_contacts);
            tv_contact_username = itemView.findViewById(R.id.tv_contact_username);
            tv_contact_name = itemView.findViewById(R.id.tv_contact_name);
            btn_follow_contact = itemView.findViewById(R.id.btn_follow_contact);
        }
    }
}
