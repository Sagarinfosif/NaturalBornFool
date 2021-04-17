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
import in.pureway.cinemaflix.models.ModelLikedVideos;

import java.io.InputStream;
import java.util.List;

public class AdapterLikedVideos extends RecyclerView.Adapter<AdapterLikedVideos.ViewHolder> {

    Context context;
    Select select;
    List<ModelLikedVideos.Detail> list;

    public interface Select {
        void video(int position);
    }

    public AdapterLikedVideos(Context context, List<ModelLikedVideos.Detail> list, Select select) {
        this.context = context;
        this.select = select;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_videos_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

//        try {

//            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
//            Glide.with(context).load(R.raw.fxr9).into(holder.img_profile_videos);

//            Glide.with(context)
//                    .asGif()
//                    .load(R.raw.fxr9)
//                    .skipMemoryCache(true)
//                    .apply(RequestOptions.diskCacheStrategyOf( DiskCacheStrategy.RESOURCE)
//                            .centerCrop())
//                    .into(holder.img_profile_videos);
//
//        }catch (Exception e){
//
//        }

        Glide.with(context).load(list.get(position).getVideoPath()).into(holder.img_profile_videos);
        holder.tv_likes_profile_videos.setText(list.get(position).getViewCount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.video(position);
            }
        });
    }

    public String LoadData(String inFile) {
        String tContents = "";

        try {
            InputStream stream = context.getAssets().open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (Exception e) {
            // Handle exceptions here
        }

        return tContents;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_likes_profile_videos;
        private ImageView img_profile_videos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_likes_profile_videos = itemView.findViewById(R.id.tv_likes_profile_videos);
            img_profile_videos = itemView.findViewById(R.id.img_profile_videos);
        }
    }
}
