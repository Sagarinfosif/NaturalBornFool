package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.pureway.cinemaflix.R;

public class AdapterSearchCategories extends RecyclerView.Adapter<AdapterSearchCategories.SearchCate> {

    private Context context;
    private String[] list;
    private String[] listTagIds;
    private Select select;

    private int index = 0;


    public AdapterSearchCategories(Context context, String[] list, String[] listTagIds, Select select) {
        this.context = context;
        this.list = list;
        this.listTagIds = listTagIds;
        this.select = select;
    }

    @NonNull
    @Override
    public SearchCate onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_search_categories, parent, false);
        return new SearchCate(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchCate holder, final int position) {

        holder.searchCateNAme.setText(list[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                select.onClick(listTagIds[position]);
                notifyDataSetChanged();
            }
        });
        if (index == position) {
            holder.ll_cateClick.setBackgroundResource(R.drawable.btn_bg);
            holder.searchCateNAme.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.ll_cateClick.setBackgroundResource(R.drawable.stroke_primary);
            holder.searchCateNAme.setTextColor(context.getResources().getColor(R.color.purple));
        }
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class SearchCate extends RecyclerView.ViewHolder {

        private LinearLayout ll_cateClick;
        private TextView searchCateNAme;

        public SearchCate(@NonNull View itemView) {
            super(itemView);
            ll_cateClick = itemView.findViewById(R.id.ll_cateClick);
            searchCateNAme = itemView.findViewById(R.id.tv_searchCat);
        }
    }

    public interface Select {
        void onClick(String tagId);
    }
}