package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelComments;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterCommentsReply extends RecyclerView.Adapter<AdapterCommentsReply.ViewHolder> {

    Context context;
    List<ModelComments.Detail.SubComment> list;
    SelectComment selectComment;

    public interface SelectComment {
        void choose(int position);

        void likeComment(int position);

        void deleteCommentLongPress(int position, View view);
    }

    public AdapterCommentsReply(Context context, List<ModelComments.Detail.SubComment> list) {
        this.context = context;
        this.list = list;
        this.selectComment = selectComment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_reply, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        holder.tv_comment.setText(list.get(position).getComment());
        holder.tv_username_commenter.setText(list.get(position).getName());
        holder.commmentTimeTV.setText(list.get(position).getCreated());
        if (!list.get(position).getUserImage().equalsIgnoreCase("")) {
            Glide.with(context).load(list.get(position).getUserImage()).into(holder.img_commenter);
        }
        else {
            Glide.with(context).load(R.drawable.ic_user).into(holder.img_commenter);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_comment, tv_username_commenter, commmentTimeTV;
        private CircleImageView img_commenter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_username_commenter=itemView.findViewById(R.id.tv_username_commenter);
            tv_comment=itemView.findViewById(R.id.tv_comment);
            commmentTimeTV=itemView.findViewById(R.id.commmentTimeTV);
            img_commenter=itemView.findViewById(R.id.img_commenter);

        }
    }
}