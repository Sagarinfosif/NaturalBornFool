package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelvideoReportList;

import java.util.List;

public class AdapterReportList extends RecyclerView.Adapter<AdapterReportList.ViewHolder> {

    Context context;
    List<ModelvideoReportList.Detail> list;
    String ReportType;
    Select select;

    int index = -1;

    public interface Select {
        void click(int position);
    }

    public AdapterReportList(Context context, List<ModelvideoReportList.Detail> list, String ReportType, Select select) {
        this.context = context;
        this.list = list;
        this.ReportType = ReportType;
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_reposrt_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_report_name.setText(list.get(position).getTitle());


        holder.ll_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                index = position;
                select.click(position);
                notifyDataSetChanged();


            }
        });

//        holder.check_box.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                index = position;
//                select.click(position);
//
////                notifyDataSetChanged();
//
//
//            }
//        });
//        holder.check_box.setChecked(false);


        if (ReportType.equalsIgnoreCase("0")){
            if (index == position) {
                holder.img_check_box.setBackgroundResource(R.drawable.ic_check_box_with_check_sign);


            } else {
                holder.img_check_box.setBackgroundResource(R.drawable.ic_blank_check_box);


            }

        }else {
            holder.img_check_box.setBackgroundResource(R.drawable.ic_blank_check_box);

        }

        if (ReportType.equalsIgnoreCase("1")) {
            holder.img_check_box.setBackgroundResource(R.drawable.ic_blank_check_box);
//            notifyDataSetChanged();
        }
//        if (index == position) {
//            if (holder.check_box.isChecked()) {
//                holder.check_box.setChecked(false);
//
//            } else {
//                holder.check_box.setChecked(true);
//
//            }
//
//        } else {
//            holder.check_box.setChecked(false);
//
//
//        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView
                tv_report_name;
        private LinearLayout ll_report;
        private CheckBox check_box;
        private ImageView img_check_box;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_check_box = itemView.findViewById(R.id.img_check_box);
            ll_report = itemView.findViewById(R.id.ll_report);
            check_box = itemView.findViewById(R.id.check_box);
            tv_report_name = itemView.findViewById(R.id.tv_report_name);


        }
    }
}
