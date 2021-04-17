package in.pureway.cinemaflix.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelComments;
import in.pureway.cinemaflix.utils.AppConstants;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.ViewHolder> {

    Context context;
    List<ModelComments.Detail> list;
    List<ModelComments.Detail.SubComment> listSubcomment;
    SelectComment selectComment;
    AdapterCommentsReply adapterCommentsReply;

    public interface SelectComment {
        void choose(int position);

        void likeComment(int position);

        void viewAll(int position);

        void commentReplyVisible(int position, String commentReply);

        void deleteCommentLongPress(int position, View view);

        void commentReplyClick(int position);

    }

    public AdapterComments(Context context, List<ModelComments.Detail> list, SelectComment selectComment) {
        this.context = context;
        this.list = list;
        this.selectComment = selectComment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comments, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getUserImage()).placeholder(context.getResources().getDrawable(R.drawable.ic_user)).into(holder.img_commenter);
        holder.tv_comment.setText(list.get(position).getComment());
        holder.tv_like_count.setText(list.get(position).getLikeCount());
        holder.commmentTimeTV.setText(list.get(position).getCreated());
        holder.tv_username_commenter.setText(list.get(position).getUsername());
        if (list.get(position).getSubComment().size() == 0) {
            holder.tv_Count_num.setVisibility(View.GONE);
        } else {
            holder.tv_Count_num.setText("View all " + String.valueOf(list.get(position).getSubComment().size()) + " replies");
        }

        holder.tv_Count_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tv_Count_num.setVisibility(View.GONE);
                holder.rc_comment_reply.setVisibility(View.VISIBLE);
            }
        });
        holder.img_commenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectComment.choose(position);
            }
        });

        holder.tv_username_commenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectComment.choose(position);
            }
        });
        holder.img_like_comments.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                selectComment.likeComment(position);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                selectComment.likeComment(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectComment.deleteCommentLongPress(position, v);
                return true;
            }
        });

        if (list.get(position).getLikeStatus()) {
            holder.img_like_comments.setLiked(true);
        }
        holder.tv_reply_commentVis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectComment.commentReplyClick(position);
                holder.tv_reply_commentVis.setVisibility(View.GONE);
                holder.img_send_comment.setVisibility(View.VISIBLE);
                holder.et_add_comment.setVisibility(View.VISIBLE);
                if (holder.getAdapterPosition() == position) {
                    holder.et_add_comment.requestFocus();
                }
            }

        });
        holder.img_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectComment.commentReplyVisible(position, holder.et_add_comment.getText().toString());

            }
        });
        holder.et_add_comment.setText("");

        holder.rc_comment_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectComment.viewAll(position);
            }
        });

        //sub comments recycler view
        holder.adapterCommentsReply = new AdapterCommentsReply(context, list.get(position).getSubComment());
        holder.rc_comment_reply.setAdapter(holder.adapterCommentsReply);

        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getExtras() != null) {
                    if (holder.getLayoutPosition() == intent.getExtras().getInt("position")) {
                        String likeCount = intent.getExtras().getString(AppConstants.COMMENT_LIKE_COUNT);
                        holder.tv_like_count.setText(likeCount);
                    }
                }
            }
        }, new IntentFilter(AppConstants.COMMENTS));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img_commenter;
        private TextView tv_username_commenter, tv_comment, tv_like_count, tv_Count_num, commmentTimeTV;
        private LikeButton img_like_comments;
        private RecyclerView rc_comment_reply;
        private AdapterCommentsReply adapterCommentsReply;
        private ImageView img_send_comment;
        private EditText et_add_comment;
        private TextView tv_reply_commentVis;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tv_Count_num = itemView.findViewById(R.id.tv_Count_num);
            commmentTimeTV = itemView.findViewById(R.id.commmentTimeTV);
            rc_comment_reply = itemView.findViewById(R.id.rc_comment_reply);
            tv_reply_commentVis = itemView.findViewById(R.id.tv_reply_commentVis);
            img_send_comment = itemView.findViewById(R.id.img_send_comment);
            et_add_comment = itemView.findViewById(R.id.et_add_comment);
            img_like_comments = itemView.findViewById(R.id.img_like_comments);
            img_commenter = itemView.findViewById(R.id.img_commenter);
            tv_username_commenter = itemView.findViewById(R.id.tv_username_commenter);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            tv_like_count = itemView.findViewById(R.id.tv_like_count);
        }
    }
}