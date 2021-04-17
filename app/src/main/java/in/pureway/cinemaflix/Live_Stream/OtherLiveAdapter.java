package in.pureway.cinemaflix.Live_Stream;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelLiveUsers;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherLiveAdapter extends RecyclerView.Adapter<OtherLiveAdapter.ViewHolder> {
    Context context;
    List<ModelLiveUsers.Detail> list;
    Select select;

    public interface Select {
        void position(int pose);
    }

    public OtherLiveAdapter(Context context, List<ModelLiveUsers.Detail> list, Select select) {
        this.context = context;
        this.list = list;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_live_people, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_username.setText(list.get(position).getName());
        Glide.with(context).load(list.get(position).getImage()).into(holder.civ_user);
        Glide.with(context).load(list.get(position).getPreview()).placeholder(context.getDrawable(R.drawable.poat)).into(holder.img_live_user_preview);

        holder.relative_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select.position(position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relative_main;
        private RoundedImageView img_live_user_preview;
        private CircleImageView civ_user;
        private TextView tv_username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_username = itemView.findViewById(R.id.tv_username);
            civ_user = itemView.findViewById(R.id.civ_user);
            img_live_user_preview = itemView.findViewById(R.id.img_live_user_preview);
            relative_main = itemView.findViewById(R.id.relative_main);
        }
    }
}