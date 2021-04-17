package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.utils.CommonUtils;

public class SwitchProAccountActiviy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(SwitchProAccountActiviy.this);
        setContentView(R.layout.activity_switch_pro_account_activiy);


        findViewById(R.id.btn_switch_pro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private void openDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(SwitchProAccountActiviy.this);
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_pro_acc, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(SwitchProAccountActiviy.this).create();
        dailogbox.setCancelable(true);
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setView(confirmdailog);

        confirmdailog.findViewById(R.id.tv_ok_dialog_pro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dailogbox.dismiss();
            }
        });

        dailogbox.show();
    }

    public void back(View view) {
        onBackPressed();
    }
}
