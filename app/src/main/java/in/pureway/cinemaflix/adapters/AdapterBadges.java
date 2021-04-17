package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;

public class AdapterBadges extends RecyclerView.Adapter<AdapterBadges.ViewHolder> {

    Context context;
    Select select;
    int index = -1;

    public interface Select {
        void choose(int position);
    }

    public AdapterBadges(Context context, Select select) {
        this.context = context;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_badge, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.rl_main_badge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                notifyDataSetChanged();
            }
        });

        if (index == position) {
            if (holder.ll_description.getVisibility() == View.VISIBLE) {
                //TODO add animation to drop down and drop up

                holder.ll_description.setVisibility(View.GONE);
                Glide.with(context).load(context.getDrawable(R.drawable.ic_baseline_arrow_drop_down_24)).into(holder.img_drop_down);
            } else {
                holder.ll_description.setVisibility(View.VISIBLE);
                Glide.with(context).load(context.getDrawable(R.drawable.ic_baseline_arrow_drop_up_24)).into(holder.img_drop_down);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_description;
        private RelativeLayout rl_main_badge;
        private ImageView img_drop_down;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_drop_down = itemView.findViewById(R.id.img_drop_down);
            ll_description = itemView.findViewById(R.id.ll_description);
            rl_main_badge = itemView.findViewById(R.id.rl_main_badge);
        }
    }
}
