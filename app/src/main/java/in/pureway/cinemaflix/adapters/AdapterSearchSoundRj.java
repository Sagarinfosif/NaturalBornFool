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
import in.pureway.cinemaflix.utils.AppConstants;

import java.util.List;

public class AdapterSearchSoundRj extends RecyclerView.Adapter<AdapterSearchSoundRj.ViewHolder> {
    Context context;
    private List<SearchAllPojo.Details.SoundList> list;
    Select select;

    public interface Select {
        void sounds(String soundId);

        void addToFavorites(int position);
    }

    public AdapterSearchSoundRj(Context context, List<SearchAllPojo.Details.SoundList> list, Select select) {
        this.context = context;
        this.list = list;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search_sound, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if (!list.get(position).getSoundImg().equalsIgnoreCase("")) {
            Glide.with(context).load(list.get(position).getSoundImg()).placeholder(context.getResources().getDrawable(R.drawable.ic_music_note_black_24dp)).into(holder.iv_songImage);
        }

        holder.songPostCount.setText(list.get(position).getSoundCount() + " " + "Posts");
        holder.songName.setText(list.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.sounds(list.get(position).getId());
            }
        });

        if (list.get(position).getFavoritesStatus().equalsIgnoreCase("1")) {
            holder.addToFvrt.setImageResource(R.drawable.ic_starfilled);
        }


        holder.addToFvrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.addToFavorites(position);
            }
        });

        holder.iv_addToFvrt_already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.addToFavorites(position);
            }
        });

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int broadposition = intent.getExtras().getInt(AppConstants.POSITION);
                if (holder.getAdapterPosition() == broadposition) {
                    //check which image is visible
                    if (holder.iv_addToFvrt_already.getVisibility() == View.VISIBLE) {
                        holder.iv_addToFvrt_already.setVisibility(View.GONE);
                        holder.addToFvrt.setVisibility(View.VISIBLE);
                    } else if (holder.addToFvrt.getVisibility() == View.VISIBLE) {
                        holder.addToFvrt.setVisibility(View.GONE);
                        holder.iv_addToFvrt_already.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(AppConstants.SOUNDS_FAVORITES));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView peopleLinearLayout, videoLinearLayout, hashTagsLinearLayout;
        private ImageView userImage, iv_thumbnail, addToFvrt, iv_songImage, iv_addToFvrt_already;
        private TextView userName, fullNAme, tv_hashTag, tv_noOfPosts, tv_videoViews, songName, songPostCount;
        private Button btn_follow_back;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

// peopleLinearLayout=itemView.findViewById(R.id.card_peopleType);
// videoLinearLayout=itemView.findViewById(R.id.card_videoType);
// hashTagsLinearLayout=itemView.findViewById(R.id.card_hashTagType);
//
// tv_videoViews=itemView.findViewById(R.id.tv_videoViews);
// iv_thumbnail=itemView.findViewById(R.id.iv_thumbnail);
//
// userImage=itemView.findViewById(R.id.img_following);
// userName=itemView.findViewById(R.id.tv_following_username);
// fullNAme=itemView.findViewById(R.id.tv_following_name);
// btn_follow_back=itemView.findViewById(R.id.btn_follow_back);
//
//
// tv_hashTag=itemView.findViewById(R.id.tv_hashTag);
// tv_noOfPosts=itemView.findViewById(R.id.tv_noOfPosts);

            iv_addToFvrt_already = itemView.findViewById(R.id.iv_addToFvrt_already);
            songName = itemView.findViewById(R.id.tv_songName);
            songPostCount = itemView.findViewById(R.id.tv_songUsedCOunt);
            iv_songImage = itemView.findViewById(R.id.iv_songImage);
            addToFvrt = itemView.findViewById(R.id.iv_addToFvrt);
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
