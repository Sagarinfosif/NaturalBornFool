package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelReport;

import java.util.List;

public class AdapterReport extends RecyclerView.Adapter<AdapterReport.ViewHolder> {

    List<ModelReport.Detail> list;
    Context context;
    Select select;

    public interface Select {
        void selectReport(int position, String title);
    }

    public AdapterReport(List<ModelReport.Detail> list, Context context, Select select) {
        this.list = list;
        this.context = context;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.selectReport(position, list.get(position).getTitle());
            }
        });
        holder.tv_report.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_report;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_report = itemView.findViewById(R.id.tv_report);
        }
    }
}
