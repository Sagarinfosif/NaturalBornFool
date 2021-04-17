package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.AdapterBalance;
import in.pureway.cinemaflix.utils.CommonUtils;

public class BalanceActivity extends AppCompatActivity {
    private RecyclerView rv_balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(BalanceActivity.this);
        setContentView(R.layout.activity_balance);

        rv_balance = findViewById(R.id.rv_balance);
        rv_balance.setAdapter(new AdapterBalance(BalanceActivity.this));
    }

    public void back(View view) {
        onBackPressed();
    }
}
