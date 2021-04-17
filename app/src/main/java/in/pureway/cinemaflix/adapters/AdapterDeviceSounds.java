package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.AudioModel;

import java.util.List;

public class AdapterDeviceSounds extends RecyclerView.Adapter<AdapterDeviceSounds.ViewHolder> {

    Context context;
    List<AudioModel> list;
    Select select;

    public interface Select {
        void playSounds(View view, int position);

        void selectSound(String title, String path);
    }

    public AdapterDeviceSounds(Context context, List<AudioModel> list, Select select) {
        this.context = context;
        this.list = list;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_device_sounds, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_sound_name.setText(list.get(position).getaName());
        holder.tv_sound_artist.setText(list.get(position).getaArtist());

//
//        Uri uri = Uri.parse(list.get(position).getaPath());
//        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//        mmr.setDataSource(context, uri);
//        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//        int millSecond = Integer.parseInt(durationStr);
//
//        long seconds = TimeUnit.MILLISECONDS.toSeconds(millSecond);
//
//        holder.tv_sound_length.setText(String.valueOf(seconds));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.playSounds(v, position);
            }
        });

        holder.img_select_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.selectSound(list.get(position).getaName(), list.get(position).getaPath());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_play, img_pause;
        private SpinKitView loading_progress;
        private ImageView img_select_sound, soundImage;
        private TextView tv_sound_name, tv_sound_artist, tv_sound_length;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_sound_length = itemView.findViewById(R.id.tv_sound_length);
            tv_sound_artist = itemView.findViewById(R.id.tv_sound_artist);
            tv_sound_name = itemView.findViewById(R.id.tv_sound_name);
            img_select_sound = itemView.findViewById(R.id.img_select_sound);
            loading_progress = itemView.findViewById(R.id.loading_progress);
            img_play = itemView.findViewById(R.id.img_play);
            img_pause = itemView.findViewById(R.id.img_pause);
            soundImage = itemView.findViewById(R.id.soundImage);
        }
    }
}
