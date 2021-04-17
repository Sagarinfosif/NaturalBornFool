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
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.GetSoundDetailsPojo;

import java.util.List;

public class AdapterSoundDetails extends RecyclerView.Adapter<AdapterSoundDetails.ViewHolder> {

    Context context;
    private List<GetSoundDetailsPojo.Details.SoundVideo> list;

    Select select;

    public interface Select{
        void moveToVideo(int position);
    }

    public AdapterSoundDetails(Context context, List<GetSoundDetailsPojo.Details.SoundVideo> list, Select select) {
        this.context = context;
        this.list = list;
        this.select=select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_videos_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        if (!list.get(position).getVideoPath().equalsIgnoreCase("")) {
            Glide.with(context).load(list.get(position).getVideoPath()).into(holder.videoThumbnail);
        }
        holder.videoViews.setText(list.get(position).getViewVideo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.moveToVideo(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView videoThumbnail;
        private TextView videoViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoThumbnail = itemView.findViewById(R.id.img_profile_videos);
            videoViews = itemView.findViewById(R.id.tv_likes_profile_videos);
        }
    }


    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public int getItemViewType(int position) {
        return (position);
    }
}