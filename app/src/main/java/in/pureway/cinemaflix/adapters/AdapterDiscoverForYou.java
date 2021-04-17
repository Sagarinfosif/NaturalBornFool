package in.pureway.cinemaflix.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelSounds;
import in.pureway.cinemaflix.utils.AppConstants;

import java.util.List;

public class AdapterDiscoverForYou extends RecyclerView.Adapter<AdapterDiscoverForYou.ViewHolder> {

    Context context;
    List<ModelSounds.Detail> list;
    Select select;

    public interface Select {
        void playSound(View view, int position);

        void download(int position);

        void addFavorite(int position);
    }

    public AdapterDiscoverForYou(Context context, List<ModelSounds.Detail> list, Select select) {
        this.context = context;
        this.list = list;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_discover_sounds, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String favorite = list.get(position).getFavStatus();
        if (favorite.equalsIgnoreCase("0")) {
            holder.img_fav_sounds.setVisibility(View.VISIBLE);
        } else {
            holder.img_fav_added_sounds.setVisibility(View.VISIBLE);
        }

        holder.tv_sound_name.setText(list.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.playSound(v, position);
            }
        });
        holder.img_select_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.download(position);
            }
        });

        holder.img_fav_sounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.addFavorite(position);
            }
        });

        holder.img_fav_added_sounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.addFavorite(position);
            }
        });

        Glide.with(context).load(list.get(position).getSoundImg()).placeholder(context.getDrawable(R.drawable.sond)).into(holder.img_cover_sounds);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int broadposition = intent.getExtras().getInt(AppConstants.POSITION);
                if (holder.getAdapterPosition() == broadposition) {
                    //check which image is visible
                    if (holder.img_fav_added_sounds.getVisibility() == View.VISIBLE) {
                        holder.img_fav_added_sounds.setVisibility(View.GONE);
                        holder.img_fav_sounds.setVisibility(View.VISIBLE);
                    } else if (holder.img_fav_sounds.getVisibility() == View.VISIBLE) {
                        holder.img_fav_added_sounds.setVisibility(View.VISIBLE);
                        holder.img_fav_sounds.setVisibility(View.GONE);
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
        private ImageView img_play, img_pause,img_cover_sounds;
        private SpinKitView loading_progress;
        private ImageView img_select_sound, img_fav_sounds, img_fav_added_sounds;
        private TextView tv_sound_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_cover_sounds = itemView.findViewById(R.id.img_cover_sounds);
            tv_sound_name = itemView.findViewById(R.id.tv_sound_name);
            img_fav_added_sounds = itemView.findViewById(R.id.img_fav_added_sounds);
            img_fav_sounds = itemView.findViewById(R.id.img_fav_sounds);
            img_select_sound = itemView.findViewById(R.id.img_select_sound);
            loading_progress = itemView.findViewById(R.id.loading_progress);
            img_play = itemView.findViewById(R.id.img_play);
            img_pause = itemView.findViewById(R.id.img_pause);
        }
    }
}
