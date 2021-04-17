package in.pureway.cinemaflix.activity.videoEditor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.videoEditor.api.ModelContentsResponse;

import java.util.List;

public class MyStickerAdapter extends RecyclerView.Adapter<MyStickerAdapter.ViewHolder> {

    Context context;
    List<ModelContentsResponse.Category.Item> list;

    public interface Listener {
        void onCategorySelected(ModelContentsResponse.Category.Item category);
    }

    private Listener mListener;

    public MyStickerAdapter(Context context, List<ModelContentsResponse.Category.Item> list, Listener mListener) {
        this.context = context;
        this.list = list;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_sticker, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.mButtonCategory.setText(list.get(position).getTitle());
        holder.mButtonCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCategorySelected(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Button mButtonCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mButtonCategory = (Button) itemView.findViewById(R.id.category_button);
        }
    }
}
