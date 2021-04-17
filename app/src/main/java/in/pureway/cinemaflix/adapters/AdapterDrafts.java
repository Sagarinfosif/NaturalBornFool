package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelDrafts;

import java.util.List;

public class AdapterDrafts extends RecyclerView.Adapter<AdapterDrafts.ViewHolder> {
    Context context;
    List<ModelDrafts> list;
    Select select;

    public interface Select {
        void post(int position,String desciption);
        void deletDrafts(int position);
        void videoUrl(String url);
    }

    public AdapterDrafts(Context context, List<ModelDrafts> list,Select select) {
        this.context = context;
        this.list = list;
        this.select=select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_drafts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Glide.with(context).load(list.get(position).getFilePath()).into(holder.img_drafts);
        holder.tv_draft_date.setText(list.get(position).getFileDate());
        holder.tv_post_drafts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.post(position,holder.et_add_description.getText().toString());
            }
        });

        holder.tv_delete_drafts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.deletDrafts(position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.videoUrl(list.get(position).getFilePath());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_drafts;
        private TextView tv_draft_date, tv_post_drafts,tv_delete_drafts;
        private EditText et_add_description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_delete_drafts = itemView.findViewById(R.id.tv_delete_drafts);
            et_add_description = itemView.findViewById(R.id.et_add_description);
            tv_post_drafts = itemView.findViewById(R.id.tv_post_drafts);
            tv_draft_date = itemView.findViewById(R.id.tv_draft_date);
            img_drafts = itemView.findViewById(R.id.img_drafts);
        }
    }
}