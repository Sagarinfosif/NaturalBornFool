package in.pureway.cinemaflix.activity.videoEditor.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.videoEditor.api.ModelContentsResponse;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFilterAdapter extends RecyclerView.Adapter<MyFilterAdapter.ViewHolder> {

    private List<ModelContentsResponse.Category.Item> mItems = new ArrayList<>();
    private Context mContext;

    public interface Listener {
        void onFilterSelected(int position, ModelContentsResponse.Category.Item item);
    }

    private Listener mListener;


    public MyFilterAdapter(List<ModelContentsResponse.Category.Item> mItems, Context mContext, Listener listener) {
        this.mItems = mItems;
        this.mContext = mContext;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelContentsResponse.Category.Item mItem;
        mItem = mItems.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFilterSelected(position, mItem);
            }
        });
        if (mItems.get(position).getTitle().contains("_")) {
            String[] value = TextUtils.split(mItems.get(position).getTitle(), "_");
            String fullStr = value[0] + " " + value[1];
            holder.mTextViewTitle.setText(fullStr.toLowerCase());
        } else {
            holder.mTextViewTitle.setText(mItems.get(position).getTitle().toLowerCase());
        }
        Glide.with(mContext).load(mItems.get(position).getThumbnail()).placeholder(mContext.getDrawable(R.drawable.ic_user1)).into(holder.mImageViewItemThumbnail);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTitle;
        private CircleImageView mImageViewItemThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageViewItemThumbnail = (CircleImageView) itemView.findViewById(R.id.item_thumbnail_imageview);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.title_textview);
        }
    }
}
