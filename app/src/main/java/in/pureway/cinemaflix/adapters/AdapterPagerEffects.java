package in.pureway.cinemaflix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import in.pureway.cinemaflix.R;

public class AdapterPagerEffects extends PagerAdapter {

    LayoutInflater mLayoutInflater;
    Context context;
    public AdapterPagerEffects(Context context) {
        this.context=context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 6;
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
        View itemView = mLayoutInflater.inflate(R.layout.pager_effects_bottom_item, container, false);

        RecyclerView recycler_pager_effects = (RecyclerView) itemView.findViewById(R.id.recycler_pager_effects);
        recycler_pager_effects.setAdapter(new AdapterPagerRecycerEffects(context));

        container.addView(itemView);

        return itemView;
    }
}
