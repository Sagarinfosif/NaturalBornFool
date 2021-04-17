package in.pureway.cinemaflix.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.pureway.cinemaflix.Live_Stream.PurchaseCoinActivity;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.GiftHistoryAdapter;
import com.google.android.material.tabs.TabLayout;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener{

    private TabLayout tabLayout;
    private ViewPager pager;
    private TextView coinBalance,tv_getCoins;
    private GiftHistoryAdapter giftHistoryAdapter;
    private String getCoin="";
    private ImageView iv_backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        getCoin=getIntent().getStringExtra("coin");
        findId();
        setAdapter();
    }

    private void findId() {
        iv_backArrow=findViewById(R.id.iv_backArrow);
        iv_backArrow.setOnClickListener(this);
        tv_getCoins=findViewById(R.id.tv_getCoins);
        tv_getCoins.setOnClickListener(this);
        coinBalance=findViewById(R.id.tv_coinBalance);
        coinBalance.setText(getCoin);
        tabLayout=findViewById(R.id.tablayoutGift);
        pager=findViewById(R.id.viewpagerGift);
    }


    private void setAdapter() {
        giftHistoryAdapter=new GiftHistoryAdapter(getSupportFragmentManager(),this);
        pager.setAdapter(giftHistoryAdapter);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                onBackPressed();
                break;
            case R.id.tv_getCoins:
                startActivity(new Intent(WalletActivity.this, PurchaseCoinActivity.class));
                break;
            case R.id.iv_backArrow:
                onBackPressed();
                break;
        }
    }
}