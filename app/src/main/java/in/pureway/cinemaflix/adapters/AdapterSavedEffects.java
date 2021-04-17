package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.pureway.cinemaflix.R;

public class AdapterSavedEffects extends RecyclerView.Adapter<AdapterSavedEffects.ViewHolder> {

    Context context;
    SelectEffect selectEffect;

    public interface SelectEffect{
        void chooseEffect(int position);
    }


    public AdapterSavedEffects(Context context, SelectEffect selectEffect) {
        this.context = context;
        this.selectEffect = selectEffect;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_saved_effects, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (position == 0) {
            holder.btn_shoot_with_this.setVisibility(View.VISIBLE);
        }
        else {
            holder.btn_shoot_with_this.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Button btn_shoot_with_this;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_shoot_with_this=itemView.findViewById(R.id.btn_shoot_with_this);
        }
    }
}
