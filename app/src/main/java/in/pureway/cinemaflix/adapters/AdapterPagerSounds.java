package in.pureway.cinemaflix.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import in.pureway.cinemaflix.activity.sounds.fragments.DiscoverSoundsFragment;

public class AdapterPagerSounds extends FragmentStatePagerAdapter {
    public AdapterPagerSounds(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new DiscoverSoundsFragment();
                break;
            case 1:
                fragment = new DiscoverSoundsFragment();
                break;

        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
