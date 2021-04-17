package in.pureway.cinemaflix.Live_Stream;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelFirebaseDatabaseRead;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {

    Context context;
    List<ModelFirebaseDatabaseRead> list;

    public CommentRecyclerAdapter(Context context, List<ModelFirebaseDatabaseRead> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_on_live, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getUserImage()).placeholder(context.getDrawable(R.drawable.ic_user1)).into(holder.civ_user);
        holder.txt_user_comment.setText(list.get(position).getComment() );
        holder.txt_user_name.setText(list.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civ_user;
        private TextView txt_user_name, txt_user_comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            civ_user = itemView.findViewById(R.id.civ_user);
            txt_user_name = itemView.findViewById(R.id.txt_user_name);
            txt_user_comment = itemView.findViewById(R.id.txt_user_comment);
        }
    }
}

