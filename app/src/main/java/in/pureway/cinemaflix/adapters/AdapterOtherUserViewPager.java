package in.pureway.cinemaflix.adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterOtherUserViewPager extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    public AdapterOtherUserViewPager(FragmentManager fm) {
        super(fm);
    }

    public void addFrag(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }
    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    public void clearAdapter(){
        fragmentList.clear();
        titleList.clear();
        notifyDataSetChanged();
    }
}
