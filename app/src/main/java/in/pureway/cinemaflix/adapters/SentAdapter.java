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
import in.pureway.cinemaflix.Live_Stream.GIftHistoryModel;
import in.pureway.cinemaflix.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SentAdapter extends RecyclerView.Adapter<SentAdapter.ViewHolder> {

    private Context context;
    private List<GIftHistoryModel.Detail> list;

    public SentAdapter(Context context, List<GIftHistoryModel.Detail> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sent,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        Glide.with(context).load(list.get(position).getImage()).into(holder.circleImageView);
        Glide.with(context).load(list.get(position).getGiftImage()).into(holder.imageView);
        holder.time.setText(list.get(position).getCreated());
        holder.coin.setText(list.get(position).getGiftCoin());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name,time,coin;
        CircleImageView circleImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coin=itemView.findViewById(R.id.tv_coin);
            circleImageView=itemView.findViewById(R.id.iv_image);
            name=itemView.findViewById(R.id.tv_sentName);
            time=itemView.findViewById(R.id.tv_time);
            imageView=itemView.findViewById(R.id.giftImage);


        }
    }
}
