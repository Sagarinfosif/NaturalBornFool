package in.pureway.cinemaflix.Live_Stream;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.PurchaseCoinAdapter;
import in.pureway.cinemaflix.models.CoinModel;
import in.pureway.cinemaflix.models.GetCoinModel;
import in.pureway.cinemaflix.mvvm.ProfileMvvm;
import in.pureway.cinemaflix.mvvm.VideoMvvm;
import in.pureway.cinemaflix.utils.CommonUtils;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PurchaseCoinActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultWithDataListener {

    private List<CoinModel.Detail> list=new ArrayList<>();
    private VideoMvvm videoMvvm;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private Checkout checkout=new Checkout();
    private static final String TAG = PurchaseCoinActivity.class.getSimpleName();
    private ProfileMvvm profileMvvm;
    private String getCoins="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_coin);
        videoMvvm= ViewModelProviders.of(this).get(VideoMvvm.class);
        profileMvvm=ViewModelProviders.of(this).get(ProfileMvvm.class);
        Checkout.preload(getApplicationContext());
        findId();
        getCoins();
    }

    private void findId() {
        recyclerView=findViewById(R.id.rv_coinsList);
        imageView=findViewById(R.id.iv_close);
        imageView.setOnClickListener(this);
    }

    private void getCoins() {
        videoMvvm.getCoin(this).observe(PurchaseCoinActivity.this, new Observer<CoinModel>() {
            @Override
            public void onChanged(CoinModel coinModel) {
                if(coinModel.getSuccess().equalsIgnoreCase("1")){
                    list=coinModel.getDetails();
                    PurchaseCoinAdapter purchaseCoinAdapter=new PurchaseCoinAdapter(PurchaseCoinActivity.this, list, new PurchaseCoinAdapter.Click() {
                        @Override
                        public void onClick(int position,String price,String coins) {
                            getCoins=coins;
                            startPayment(price,coinModel.getKey(),coins);
                        }
                    });
                    recyclerView.setAdapter(purchaseCoinAdapter);
                }else {
                    Toast.makeText(PurchaseCoinActivity.this, coinModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void startPayment(String price,String key,String coins){
        checkout.setKeyID(key);
        /**
         * Instantiate Checkout
         */

        /**
         * Set your logo here
         */
//            checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", getString(R.string.app_name));

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Coin Purchase");
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//                options.put("order_id", "order_9A33XWu170gUtm");

            checkout.setImage(R.drawable.app_icon);

            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */

            double total = Double.parseDouble(price);
            total=total*100;
            options.put("amount", total);

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                onBackPressed();
                break;
        }
    }





    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        getCoinWallet(paymentData.getPaymentId());
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(PurchaseCoinActivity.this,"Payment Failed", Toast.LENGTH_SHORT).show();
    }

    private void getCoinWallet(String paymentId) {
        profileMvvm.getCoinModelLiveData(this, CommonUtils.userId(this),paymentId,getCoins).observe(PurchaseCoinActivity.this, new Observer<GetCoinModel>() {
            @Override
            public void onChanged(GetCoinModel getCoinModel) {
                if(getCoinModel.getSuccess().equalsIgnoreCase("1")){
                    onBackPressed();
                    Toast.makeText(PurchaseCoinActivity.this, getCoinModel.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(PurchaseCoinActivity.this, getCoinModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}