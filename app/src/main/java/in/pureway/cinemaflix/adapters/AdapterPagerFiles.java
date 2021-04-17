package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import in.pureway.cinemaflix.R;

public class AdapterPagerFiles extends PagerAdapter {
Context context;
LayoutInflater mLayoutInflater;
    public AdapterPagerFiles(Context context) {
        this.context=context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
       return view == ((RecyclerView) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RecyclerView)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_files_uploads, container, false);

        RecyclerView rv_uploads = (RecyclerView) itemView.findViewById(R.id.rv_uploads);
        rv_uploads.setAdapter(new AdapterUplaodsFiles(context));

        container.addView(itemView);

        return itemView;
    }
}
