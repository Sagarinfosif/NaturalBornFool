package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.AdapterBadges;

public class PhotofitBadgeActivity extends AppCompatActivity {
    private RecyclerView recycler_badge;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photofit_badge);

        activity = PhotofitBadgeActivity.this;
        findiDs();
        setRecycler();
    }

    private void setRecycler() {
        AdapterBadges adapterBadges = new AdapterBadges(activity, new AdapterBadges.Select() {
            @Override
            public void choose(int position) {

            }
        });
        recycler_badge.setAdapter(adapterBadges);
    }

    private void findiDs() {
        recycler_badge = findViewById(R.id.recycler_badge);
    }

    public void backPress(View view) {
        onBackPressed();
    }
}