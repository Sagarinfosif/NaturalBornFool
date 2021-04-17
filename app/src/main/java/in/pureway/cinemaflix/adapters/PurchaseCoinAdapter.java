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
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.CoinModel;


import java.util.List;

public class PurchaseCoinAdapter extends RecyclerView.Adapter<PurchaseCoinAdapter.ViewHolder> {


    private Context context;
    private List<CoinModel.Detail> listcoin;
    private Click click;

    public PurchaseCoinAdapter(Context context, List<CoinModel.Detail> listcoin,Click click) {
        this.context = context;
        this.listcoin = listcoin;
        this.click=click;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_purchasecoins,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(listcoin.get(position).getTitle());
        holder.coinvalue.setText(listcoin.get(position).getCoin());
        holder.price.setText("₹"+listcoin.get(position).getPrice());
        holder.price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onClick(position,listcoin.get(position).getPrice(),listcoin.get(position).getCoin());
            }
        });
        Glide.with(context).load(listcoin.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return listcoin.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,price,coinvalue;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.iv_imageCoin);
            title=itemView.findViewById(R.id.tv_title);
            price=itemView.findViewById(R.id.bt_coinPrice);
            coinvalue=itemView.findViewById(R.id.tv_coinValue);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onClick(getLayoutPosition(),listcoin.get(getLayoutPosition()).getPrice(),listcoin.get(getLayoutPosition()).getCoin());
                }
            });
        }
    }
    public interface Click{
        void onClick(int position,String price,String coin);
    }
}

