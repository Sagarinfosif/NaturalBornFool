package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelFavoriteSounds;

import java.util.List;

public class AdapterSavedSouds extends RecyclerView.Adapter<AdapterSavedSouds.ViewHolder> {

    Context context;
    SelectSound Selectsound;
    List<ModelFavoriteSounds.Detail> list;
    int index = -1;

    public interface SelectSound {
        void chooseSound(int position);

        void playSound(View view, int position);

        void download(int postion);

        void removeFavorites(int position);
    }

    public AdapterSavedSouds(Context context, List<ModelFavoriteSounds.Detail> list, SelectSound selectsound) {
        this.context = context;
        Selectsound = selectsound;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_saved_sounds, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_sound_name.setText(list.get(position).getTitle());
//        Glide.with(context).load(list.get(position).getSoundImage()).placeholder(context.getResources().getDrawable(R.drawable.music)).into(holder.img_sound_drawable);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                notifyDataSetChanged();
            }
        });

        if (index == position) {
            if (holder.btn_shoot_with_this.getVisibility() == View.VISIBLE) {
                holder.btn_shoot_with_this.setVisibility(View.GONE);
            } else {
                holder.btn_shoot_with_this.setVisibility(View.VISIBLE);
            }
        }

        holder.img_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selectsound.playSound(v, position);
            }
        });

        holder.img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selectsound.playSound(v, position);
            }
        });
        holder.btn_shoot_with_this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selectsound.download(position);
            }
        });

        holder.img_remove_favotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selectsound.removeFavorites(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Button btn_shoot_with_this;
        private TextView tv_sound_name;
        private ImageView img_remove_favotes, img_pause, img_play, img_sound_drawable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_sound_drawable = itemView.findViewById(R.id.img_sound_drawable);
            img_play = itemView.findViewById(R.id.img_play);
            img_pause = itemView.findViewById(R.id.img_pause);
            img_remove_favotes = itemView.findViewById(R.id.img_remove_favotes);
            tv_sound_name = itemView.findViewById(R.id.tv_sound_name);
            btn_shoot_with_this = itemView.findViewById(R.id.btn_shoot_with_this);
        }
    }
}
