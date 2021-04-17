package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelInbox;

import java.util.List;

public class AdapterNotiMessages extends RecyclerView.Adapter<AdapterNotiMessages.ViewHolder> {

    Context context;
    Select select;
    List<ModelInbox.Detail> list;

    public interface Select {
        void goToMesssage(int position);

        void deleteChat(String receiverId,String senderId,int position, View view);
    }

    public AdapterNotiMessages(Context context, List<ModelInbox.Detail> list, Select select) {
        this.context = context;
        this.list = list;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_noti_msg, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        //TODO uncount not correct
        if (list.get(position).getName().equalsIgnoreCase("")) {
            holder.senderName.setText(list.get(position).getUsername());
        } else {
            holder.senderName.setText(list.get(position).getName());
        }
        holder.lastMsg.setText(list.get(position).getMessage());
        holder.date.setText(list.get(position).getTime());

        if (list.get(position).getMessageCount().equalsIgnoreCase("0")) {
            holder.card_msg_count.setVisibility(View.GONE);
        } else {
            holder.unreadCount.setText(list.get(position).getMessageCount());
        }

        Glide.with(context).load(list.get(position).getImage()).placeholder(context.getDrawable(R.drawable.ic_user1)).into(holder.senderImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.goToMesssage(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                select.deleteChat(list.get(position).getReciverId(),list.get(position).getId(),position, v);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView senderImg;
        private TextView senderName, lastMsg, date, unreadCount;
        private MaterialCardView card_msg_count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_msg_count = itemView.findViewById(R.id.card_msg_count);
            senderImg = itemView.findViewById(R.id.senderImg);
            senderName = itemView.findViewById(R.id.senderName);
            lastMsg = itemView.findViewById(R.id.lastMsg);
            date = itemView.findViewById(R.id.date);
            unreadCount = itemView.findViewById(R.id.unreadCount);

        }
    }
}