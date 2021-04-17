package in.pureway.cinemaflix.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.AdapterReportList;
import in.pureway.cinemaflix.models.ModelvideoReportList;
import in.pureway.cinemaflix.mvvm.LoginRegisterMvvm;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportProblemActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rc_repert_list;
    private AdapterReportList adapterReportList;
    List<ModelvideoReportList.Detail> list;
    private LoginRegisterMvvm loginRegisterMvvm;
    private String reportVideoID, report, userId;
    private Button btn_submit;

    private LinearLayout ll_other;
    private EditText ed_otherReport;
    private String checkOther = "0";
    private CheckBox checkOther1;
    private ImageView img_other;
    private String ReportType = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem2);


        loginRegisterMvvm = ViewModelProviders.of(ReportProblemActivity.this).get(LoginRegisterMvvm.class);


        userId = CommonUtils.userId(this);


        findIds();

        reportList();


    }

    private void findIds() {


        img_other = findViewById(R.id.img_other);
        checkOther1 = findViewById(R.id.checkOther);
        ed_otherReport = findViewById(R.id.ed_otherReport);
        ll_other = findViewById(R.id.ll_other);
        rc_repert_list = findViewById(R.id.rc_repert_list);
        btn_submit = findViewById(R.id.btn_submit);
        reportVideoID = getIntent().getExtras().getString("ReportVideoID");
        btn_submit.setOnClickListener(this);
        ll_other.setOnClickListener(this);
        checkOther1.setOnClickListener(this);
        img_other.setOnClickListener(this);


    }

    private void reportList() {

        if (ReportType.equalsIgnoreCase("0")) {
            ed_otherReport.setVisibility(View.GONE);
        } else {
            ed_otherReport.setVisibility(View.VISIBLE);

        }
        loginRegisterMvvm.videoReportList(this).observe(this, new Observer<ModelvideoReportList>() {
            @Override
            public void onChanged(ModelvideoReportList modelvideoReportList) {
                if (modelvideoReportList.getSuccess().equalsIgnoreCase("1")) {


                    list = modelvideoReportList.getDetails();
                    adapterReportList = new AdapterReportList(ReportProblemActivity.this, list, ReportType, new AdapterReportList.Select() {
                        @Override
                        public void click(int position) {
                            ed_otherReport.setVisibility(View.GONE);
                            ed_otherReport.setText("");

                            img_other.setBackgroundResource(R.drawable.ic_blank_check_box);
                            ReportType = "0";
                            report = list.get(position).getTitle().toString();
                        }
                    });
                    rc_repert_list.setAdapter(adapterReportList);
                    adapterReportList.notifyDataSetChanged();

//                    Toast.makeText(ReportProblemActivity.this, modelvideoReportList.getMessage(), Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(ReportProblemActivity.this, modelvideoReportList.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_submit:


                valReport();


                break;


            case R.id.ll_other:
            case R.id.checkOther:
            case R.id.img_other:
                ed_otherReport.setText("");

                ed_otherReport.setVisibility(View.VISIBLE);

                if (ReportType.equalsIgnoreCase("1")) {
                    img_other.setBackgroundResource(R.drawable.ic_check_box_with_check_sign);
                    ReportType = "1";
                    reportList();
                    ReportType = "0";

                } else {

                    ReportType = "0";
                    ed_otherReport.setVisibility(View.GONE);
                    img_other.setBackgroundResource(R.drawable.ic_blank_check_box);
                    reportList();
                    ReportType = "1";

                }


                break;


        }

    }

    private void valReport() {

        if (checkOther.equalsIgnoreCase("1")) {
            report = ed_otherReport.getText().toString();
        }

        if (report.isEmpty()) {
            Toast.makeText(this, "Please Select appropriate Option ", Toast.LENGTH_SHORT).show();
        } else {

            loginRegisterMvvm.userReportVideo(this, userId, reportVideoID, report).observe(this, new Observer<Map>() {
                @Override
                public void onChanged(Map map) {
                    if (map.get("success").toString().equalsIgnoreCase("1")) {
                        ed_otherReport.setText("");
                        onBackPressed();
                        Toast.makeText(ReportProblemActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(ReportProblemActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
}