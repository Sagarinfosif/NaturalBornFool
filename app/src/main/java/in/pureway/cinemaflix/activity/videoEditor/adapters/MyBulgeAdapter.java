package in.pureway.cinemaflix.activity.videoEditor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.pureway.cinemaflix.R;

public class MyBulgeAdapter extends RecyclerView.Adapter<MyBulgeAdapter.ViewHolder> {

    Context context;
    BulgeListener bulgeListener;

    public interface BulgeListener {
        void onBulgeSelected(int bulgeValue);
    }

    public MyBulgeAdapter(Context context, BulgeListener bulgeListener) {
        this.context = context;
        this.bulgeListener = bulgeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bulge, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.btn_bulge.setText("Bulge " + (position + 1));
        holder.btn_bulge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bulgeListener.onBulgeSelected(position + 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Button btn_bulge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_bulge = itemView.findViewById(R.id.btn_bulge);
        }
    }
}
