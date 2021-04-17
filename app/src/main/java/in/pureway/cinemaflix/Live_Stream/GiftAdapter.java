package in.pureway.cinemaflix.Live_Stream;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.GiftModel;

import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder> {

    private Context context;
    List<GiftModel.Detail> list;
    Click click;

    public GiftAdapter(Context context, List<GiftModel.Detail> list,Click click) {
        this.context = context;
        this.list = list;
        this.click=click;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_coins,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getPrimeAccount());
        Glide.with(context).load(list.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.iv_giftImage);
            textView=itemView.findViewById(R.id.tv_giftCoin);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.OnClick(getLayoutPosition(),list.get(getLayoutPosition()).getMerePaise(),list.get(getLayoutPosition()).getPrimeAccount(),list.get(getLayoutPosition()).getId());
                }
            });
        }
    }

    public interface Click{
        void OnClick(int position,String balance,String price,String id);
    }
}
