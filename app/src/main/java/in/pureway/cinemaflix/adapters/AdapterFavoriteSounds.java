package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelFavoriteSounds;

import java.util.List;

public class AdapterFavoriteSounds extends RecyclerView.Adapter<AdapterFavoriteSounds.ViewHolder> {

    Context context;
    List<ModelFavoriteSounds.Detail> list;
    Select select;

    public interface Select {
        void playSound(View view, int position);

        void download(int position);

        void addFavorite(int position);
    }

    public AdapterFavoriteSounds(Context context, List<ModelFavoriteSounds.Detail> list, Select select) {
        this.context = context;
        this.list = list;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_favorite_sounds, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Glide.with(context).load(list.get(position).getSoundImg()).placeholder(context.getDrawable(R.drawable.sond)).into(holder.img_fav_sounds);
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

        holder.img_remove_favotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.addFavorite(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_play, img_pause, img_select_sound, img_remove_favotes,img_fav_sounds;
        private SpinKitView loading_progress;
        private TextView tv_sound_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_fav_sounds = itemView.findViewById(R.id.img_fav_sounds);
            img_play = itemView.findViewById(R.id.img_play);
            img_pause = itemView.findViewById(R.id.img_pause);
            img_select_sound = itemView.findViewById(R.id.img_select_sound);
            img_remove_favotes = itemView.findViewById(R.id.img_remove_favotes);
            loading_progress = itemView.findViewById(R.id.loading_progress);
            tv_sound_name = itemView.findViewById(R.id.tv_sound_name);
        }
    }
}
