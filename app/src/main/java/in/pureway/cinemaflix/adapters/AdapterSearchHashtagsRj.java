package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.SearchAllPojo;

import java.util.List;

public class AdapterSearchHashtagsRj extends RecyclerView.Adapter<AdapterSearchHashtagsRj.ViewHolder> {


    Context context;
    private List<SearchAllPojo.Details.HasTagList> list;


    Select select;

    public interface Select {
        void hashtags(int position,String count);
    }

    public AdapterSearchHashtagsRj(Context context, List<SearchAllPojo.Details.HasTagList> list, Select select) {
        this.context = context;
        this.list = list;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_hash_tags_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        holder.tv_hashTag.setText(list.get(position).getHashtag());
        holder.tv_noOfPosts.setText(list.get(position).getVideoCount() + " " + context.getString(R.string.posts));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.hashtags(position,list.get(position).getVideoCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView peopleLinearLayout, videoLinearLayout, hashTagsLinearLayout;
        private ImageView userImage, iv_thumbnail;
        private TextView userName, fullNAme, tv_hashTag, tv_noOfPosts, tv_videoViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            peopleLinearLayout = itemView.findViewById(R.id.card_peopleType);
            videoLinearLayout = itemView.findViewById(R.id.card_videoType);
            hashTagsLinearLayout = itemView.findViewById(R.id.card_hashTagType);

//            tv_videoViews = itemView.findViewById(R.id.tv_videoViews);
            iv_thumbnail = itemView.findViewById(R.id.iv_thumbnail);

            userImage = itemView.findViewById(R.id.img_following);
            userName = itemView.findViewById(R.id.tv_following_username);
            fullNAme = itemView.findViewById(R.id.tv_following_name);


            tv_hashTag = itemView.findViewById(R.id.tv_hashTag);
            tv_noOfPosts = itemView.findViewById(R.id.tv_noOfPosts);


        }
    }
}