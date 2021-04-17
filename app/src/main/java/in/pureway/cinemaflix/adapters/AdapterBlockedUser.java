package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelBlock;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterBlockedUser extends RecyclerView.Adapter<AdapterBlockedUser.ViewHolder> {

    Context context;
    List<ModelBlock.Detail> list;
    Select select;

    public interface Select {
        void blockUnblock(int position);
    }

    public AdapterBlockedUser(Context context, List<ModelBlock.Detail> list, Select select) {
        this.context = context;
        this.list = list;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_block, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getImage()).into(holder.img_user_block);
        holder.tv_block_name.setText(list.get(position).getName());
        holder.tv_block_username.setText(list.get(position).getUsername());
        holder.btn_block_unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.blockUnblock(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img_user_block;
        private TextView tv_block_username, tv_block_name;
        private Button btn_block_unblock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_user_block = itemView.findViewById(R.id.img_user_block);
            tv_block_username = itemView.findViewById(R.id.tv_block_username);
            tv_block_name = itemView.findViewById(R.id.tv_block_name);
            btn_block_unblock = itemView.findViewById(R.id.btn_block_unblock);

        }
    }
}
