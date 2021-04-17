package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelHashTag;

import java.util.ArrayList;
import java.util.List;

public class HashTagListAdapter extends ArrayAdapter {

    private List<ModelHashTag.Detail> dataList = new ArrayList<ModelHashTag.Detail>();
    //    private List<String> dataList;
    private Context mContext;
    private int itemLayout;

    private ListFilter listFilter = new ListFilter();
    private List<ModelHashTag.Detail> dataListAllItems;


    public HashTagListAdapter(Context context, int resource, List<ModelHashTag.Detail> storeDataLst) {
        super(context, resource, storeDataLst);
        dataList = storeDataLst;
        mContext = context;
        itemLayout = resource;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getItem(int position) {
        Log.d("CustomListAdapter",
                dataList.get(position).getHashtag());
        return dataList.get(position).getHashtag();
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.autocomplete_custom_layout, parent, false);
        }

        TextView tv_hashtag_followers = (TextView) view.findViewById(R.id.tv_hashtag_followers);
        TextView tv_hashtag_name = (TextView) view.findViewById(R.id.tv_hashtag_name);
        tv_hashtag_name.setText(dataList.get(position).getHashtag());
        tv_hashtag_followers.setText(dataList.get(position).getVideoCount() + " posts.");

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<ModelHashTag.Detail>(dataList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<ModelHashTag.Detail> matchValues = new ArrayList<ModelHashTag.Detail>();

                for (ModelHashTag.Detail dataItem : dataListAllItems) {
                    if (dataItem.getHashtag().startsWith(searchStrLowerCase)) {
                        matchValues.add(dataItem);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<ModelHashTag.Detail>) results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}