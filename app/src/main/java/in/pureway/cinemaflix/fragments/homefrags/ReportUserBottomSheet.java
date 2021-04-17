package in.pureway.cinemaflix.fragments.homefrags;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.AdapterReport;
import in.pureway.cinemaflix.models.ModelReport;
import in.pureway.cinemaflix.mvvm.ProfileMvvm;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class ReportUserBottomSheet extends BottomSheetDialogFragment {

    private View view;
    private TextView tv_why_reporting;
    private RecyclerView recycler_report;
    List<ModelReport.Detail> list = new ArrayList<>();
    private ProfileMvvm profileMvvm;
    private String otherUserId = "", username = "";

    public ReportUserBottomSheet() {
    }

    public ReportUserBottomSheet(String otherUserId, String username) {
        this.otherUserId = otherUserId;
        this.username = username;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_report_user_bottom_sheet, container, false);
        profileMvvm = ViewModelProviders.of(ReportUserBottomSheet.this).get(ProfileMvvm.class);
        findIds();
        setRecycler();
        return view;
    }

    private void setRecycler() {
        profileMvvm.reportList(getActivity()).observe(getActivity(), new Observer<ModelReport>() {
            @Override
            public void onChanged(ModelReport modelReport) {
                if (modelReport.getSuccess().equalsIgnoreCase("1")) {
                    Log.i("reportList", modelReport.getMessage());
                    list = modelReport.getDetails();
                    AdapterReport adapterReport = new AdapterReport(list, getActivity(), new AdapterReport.Select() {
                        @Override
                        public void selectReport(int position, String title) {
                            reportUser(title);
                        }
                    });
                    recycler_report.setAdapter(adapterReport);
                } else {
                    Log.i("reportList", modelReport.getMessage());
                }
            }
        });
    }

    private void reportUser(String reportTitle) {
        String userId = CommonUtils.userId(getActivity());
        profileMvvm.reportUser(getActivity(), userId, otherUserId, reportTitle).observe(getActivity(), new Observer<ModelReport>() {
            @Override
            public void onChanged(ModelReport modelReport) {
                if (modelReport.getSuccess().equalsIgnoreCase("1")) {
                    Toast.makeText(getActivity(), modelReport.getMessage(), Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), modelReport.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findIds() {
        tv_why_reporting = view.findViewById(R.id.tv_why_reporting);
        recycler_report = view.findViewById(R.id.recycler_report);
        tv_why_reporting.setText(getActivity().getResources().getString(R.string.why_are_you_reporting) + " " + username + "?");
    }
}