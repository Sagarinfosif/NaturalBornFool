package in.pureway.cinemaflix.activity.privacysettings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.models.ModelReportList;
import in.pureway.cinemaflix.mvvm.SettingsMvvm;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class ReportProblemActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spinner_report_problem;
    private List<ModelReportList.Detail> list = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private EditText et_manual_report;
    private SettingsMvvm settingsMvvm;
    private Activity activity;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);
        activity = ReportProblemActivity.this;
        settingsMvvm = ViewModelProviders.of(ReportProblemActivity.this).get(SettingsMvvm.class);
        findIds();
//        setSpinner();
    }

    private void setSpinner() {
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        settingsMvvm.reportProblemList(activity).observe(ReportProblemActivity.this, new Observer<ModelReportList>() {
            @Override
            public void onChanged(ModelReportList modelReportList) {
                if (modelReportList.getSuccess().equalsIgnoreCase("1")) {
                    list = modelReportList.getDetails();
                    for (int i = 0; i < modelReportList.getDetails().size(); i++) {
                        arrayAdapter.add(modelReportList.getDetails().get(i).getTitle());
                    }
                    spinner_report_problem.setAdapter(arrayAdapter);

                    spinner_report_problem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == list.size() - 1) {
                                et_manual_report.setVisibility(View.VISIBLE);
                            } else {
                                et_manual_report.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {
                    Toast.makeText(activity, modelReportList.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void reportProblem() {
        String problem = et_manual_report.getText().toString();
        if (problem.isEmpty()) {
            et_manual_report.setError(getString(R.string.problem_empty));
            return;
        }
        settingsMvvm.reportProblem(activity, CommonUtils.userId(activity), problem).observe(ReportProblemActivity.this, new Observer<ModelReportList>() {
            @Override
            public void onChanged(ModelReportList modelReportList) {
                if (modelReportList.getSuccess().equalsIgnoreCase("1")) {
                    Toast.makeText(activity, modelReportList.getMessage(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(activity, modelReportList.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findIds() {
        et_manual_report = findViewById(R.id.et_manual_report);
        spinner_report_problem = findViewById(R.id.spinner_report_problem);
        findViewById(R.id.btn_report).setOnClickListener(this);
    }

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_report:
                reportProblem();
                break;
        }
    }
}