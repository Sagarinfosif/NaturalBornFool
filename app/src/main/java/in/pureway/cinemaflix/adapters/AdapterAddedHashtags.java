package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.pureway.cinemaflix.R;

import java.util.List;

public class AdapterAddedHashtags extends RecyclerView.Adapter<AdapterAddedHashtags.ViewHolder> {

    Context context;
    List<String> list;
    Select select;

    public interface Select {
        void deleteHashtag(int position);
    }

    public AdapterAddedHashtags(Context context, List<String> list, Select select) {
        this.context = context;
        this.list = list;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_hashtags, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_hashtag.setText(list.get(position));
        holder.img_delete_hashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.deleteHashtag(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_hashtag;
        private ImageView img_delete_hashtag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_hashtag = itemView.findViewById(R.id.tv_hashtag);
            img_delete_hashtag = itemView.findViewById(R.id.img_delete_hashtag);
        }
    }
}
