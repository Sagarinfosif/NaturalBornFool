package in.pureway.cinemaflix.adapters.Inner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.HashTagHomeListAdapter;
import in.pureway.cinemaflix.models.ModelHashTagsDetails;

import java.util.List;

public class HashTagListAdapter extends RecyclerView.Adapter<HashTagListAdapter.MyViewHolder> {

    Context context;
    List<ModelHashTagsDetails.Details.HashtagVideo.HastagList_> hashTagList;
    HashTagHomeListAdapter.Select select;

    public interface Select {
        void hashTags(String hashTagId);
    }

    public HashTagListAdapter(Context context, List<ModelHashTagsDetails.Details.HashtagVideo.HastagList_> hashTagList, HashTagHomeListAdapter.Select select) {
        this.context = context;
        this.hashTagList = hashTagList;
        this.select = select;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hash_tag, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (position < hashTagList.size()) {
            holder.hashTagTitle.setText(hashTagList.get(position).getHashtag()+",");
        }else {
            holder.hashTagTitle.setText(hashTagList.get(position).getHashtag());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select.hashTags(hashTagList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return hashTagList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView hashTagTitle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            hashTagTitle = itemView.findViewById(R.id.hashTagTitle);
        }
    }
}
