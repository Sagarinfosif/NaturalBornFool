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
import in.pureway.cinemaflix.models.ModelMyUploadedVideos;

import java.util.List;

public class AdapterVideoProfile extends RecyclerView.Adapter<AdapterVideoProfile.ViewHolder> {

    Context context;
    Select select;
    List<ModelMyUploadedVideos.Detail> list;
    String userId, loginId;

    public interface Select {
        void drafts(int position);
        void videoPlayList(int position);
        void onLongClick(int position);
    }

    public AdapterVideoProfile(Context context, List<ModelMyUploadedVideos.Detail> list, Select select, String userId, String loginId) {
        this.context = context;
        this.select = select;
        this.list = list;
        this.userId = userId;
        this.loginId = loginId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_videos_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        //if userid equals login id...show drafts else hide drafts
        if (userId.equalsIgnoreCase(loginId)) {
//            Glide.with(context).load(list.get(position).getVideoPath()).into(holder.img_profile_videos);
            Glide.with(context).load(list.get(position).getImageThumb()).placeholder(R.drawable.app_icon).into(holder.img_profile_videos);
            holder.tv_likes_profile_videos.setText(list.get(position).getViewCount());


            if (list.get(position).getVideoType().equalsIgnoreCase("2")) {

            }
                if (list.get(position).getRejectStatus().equalsIgnoreCase("2")){
                holder.tv_reject.setVisibility(View.VISIBLE);
            }
            else {
                holder.tv_reject.setVisibility(View.GONE);

            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.videoPlayList(position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    select.onLongClick(position);
                    return true;
                }
            });


        } else {
            Glide.with(context).load(list.get(position).getVideoPath()).into(holder.img_profile_videos);
            holder.tv_likes_profile_videos.setText(list.get(position).getViewCount());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.videoPlayList(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_likes_profile_videos,tv_reject;
        private ImageView img_profile_videos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_reject = itemView.findViewById(R.id.tv_reject);
            tv_likes_profile_videos = itemView.findViewById(R.id.tv_likes_profile_videos);
            img_profile_videos = itemView.findViewById(R.id.img_profile_videos);
        }
    }
}
