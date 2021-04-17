package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelFavoriteHashTag;

import java.util.List;

public class AdapterHashTagsSaved extends RecyclerView.Adapter<AdapterHashTagsSaved.ViewHolder> {

    Context context;
    List<ModelFavoriteHashTag.Detail> list;
    Select select;
    public interface Select {
        void moveToHashTag(int position);
    }

    public AdapterHashTagsSaved(Context context, List<ModelFavoriteHashTag.Detail> list, Select select) {
        this.context = context;
        this.list = list;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_saved_hashtags, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_hashtag_name.setText(list.get(position).getHashtag());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.moveToHashTag(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_hash_used,tv_hashtag_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_hash_used=itemView.findViewById(R.id.tv_hash_used);
            tv_hashtag_name=itemView.findViewById(R.id.tv_hashtag_name);
        }
    }
}
