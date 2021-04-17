package in.pureway.cinemaflix.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.fragments.ReceivedGiftFragment;
import in.pureway.cinemaflix.fragments.SentGiftFragment;

public class GiftHistoryAdapter extends FragmentPagerAdapter {

    Context context;

    public GiftHistoryAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SentGiftFragment sentGiftFragment = new SentGiftFragment();
                return sentGiftFragment;
            case 1:
                ReceivedGiftFragment receivedGiftFragment = new ReceivedGiftFragment();
                return receivedGiftFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.sent);
            case 1:
                return context.getResources().getString(R.string.received);
        }
        return null;
    }
}


