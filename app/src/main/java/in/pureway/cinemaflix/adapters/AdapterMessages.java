package in.pureway.cinemaflix.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.models.ModelSendMessage;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class AdapterMessages extends RecyclerView.Adapter<AdapterMessages.ViewHolder> {

    Activity context;
    List<ModelSendMessage.Detail> list;
    String path;
    Select select;

    public interface Select {
        void deleteMessage(int position, String messageId, View view);

        void moveToProfile(int position);

        void showImage(Drawable drawable);
    }

    public AdapterMessages(Activity context, List<ModelSendMessage.Detail> list, String path, Select select) {
        this.context = context;
        this.list = list;
        this.path = path;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_message_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                select.deleteMessage(position, String.valueOf(list.get(position).getId()), v);
                return true;
            }
        });

        holder.receiverImageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.moveToProfile(position);
            }
        });

        holder.receiverImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.moveToProfile(position);
            }
        });

        if (list.get(position).getMessageType().equalsIgnoreCase("1")) {
            holder.imageLL.setVisibility(View.GONE);
            if (list.get(position).getSenderId().equalsIgnoreCase(CommonUtils.userId(context))) {
                holder.receiverRL.setVisibility(View.GONE);
                holder.senderRL.setVisibility(View.VISIBLE);
                holder.senderMsg.setText(list.get(position).getMessage());
                holder.senderTime.setText(list.get(position).getTime());
                Glide.with(context).load(App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getImage()).placeholder(context.getDrawable(R.drawable.ic_user1)).into(holder.senderImg);
            } else {
                holder.senderRL.setVisibility(View.GONE);
                holder.receiverRL.setVisibility(View.VISIBLE);
                holder.receiverMsg.setText(list.get(position).getMessage());
                holder.receiverTime.setText(list.get(position).getTime());
                Glide.with(context).load(path).placeholder(context.getDrawable(R.drawable.ic_user1)).into(holder.receiverImg);
            }
        } else {
            holder.messageLL.setVisibility(View.GONE);
            if (list.get(position).getSenderId().equalsIgnoreCase(CommonUtils.userId(context))) {
                holder.receiverImageLL.setVisibility(View.GONE);
                holder.senderImageRL.setVisibility(View.VISIBLE);
                Glide.with(context).load(list.get(position).getImage()).into(holder.senderImageMsg);
                holder.senderImageTime.setText(list.get(position).getTime());
                Glide.with(context).load(App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getImage()).into(holder.senderImageImg);
            } else {
                holder.senderImageRL.setVisibility(View.GONE);
                holder.receiverImageLL.setVisibility(View.VISIBLE);
                Glide.with(context).load(list.get(position).getImage()).into(holder.receiverImageMsg);
                holder.receiverImageTime.setText(list.get(position).getTime());
                Glide.with(context).load(path).into(holder.receiverImageImg);
            }
        }

        holder.senderImageMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.showImage(holder.senderImageMsg.getDrawable());
            }
        });

        holder.receiverImageMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.showImage(holder.receiverImageMsg.getDrawable());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout senderRL, senderImageRL;
        private LinearLayout receiverRL, receiverImageLL, imageLL, messageLL;
        private TextView receiverMsg, receiverTime, senderMsg, senderTime, receiverImageTime, senderImageTime;
        private ImageView receiverImg, senderImg, receiverImageImg, senderImageImg;
        private RoundedImageView receiverImageMsg, senderImageMsg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            senderRL = itemView.findViewById(R.id.senderRL);
            receiverRL = itemView.findViewById(R.id.receiverRL);
            receiverMsg = itemView.findViewById(R.id.receiverMsg);
            senderMsg = itemView.findViewById(R.id.senderMsg);
            receiverTime = itemView.findViewById(R.id.receiverTime);
            senderTime = itemView.findViewById(R.id.senderTime);
            receiverImg = itemView.findViewById(R.id.receiverImg);
            senderImg = itemView.findViewById(R.id.senderImg);
            receiverImageLL = itemView.findViewById(R.id.receiverImageLL);
            receiverImageImg = itemView.findViewById(R.id.receiverImageImg);
            receiverImageMsg = itemView.findViewById(R.id.receiverImageMsg);
            receiverImageTime = itemView.findViewById(R.id.receiverImageTime);
            senderImageRL = itemView.findViewById(R.id.senderImageRL);
            senderImageImg = itemView.findViewById(R.id.senderImageImg);
            senderImageMsg = itemView.findViewById(R.id.senderImageMsg);
            senderImageTime = itemView.findViewById(R.id.senderImageTime);
            messageLL = itemView.findViewById(R.id.messageLL);
            imageLL = itemView.findViewById(R.id.imageLL);

        }
    }
}