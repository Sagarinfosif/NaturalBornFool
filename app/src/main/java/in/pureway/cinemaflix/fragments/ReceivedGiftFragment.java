package in.pureway.cinemaflix.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import in.pureway.cinemaflix.Live_Stream.GIftHistoryModel;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.ReceivedAdapter;
import in.pureway.cinemaflix.mvvm.VideoMvvm;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class ReceivedGiftFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReceivedAdapter receivedAdapter;
    private VideoMvvm videoMvvm;
    private List<GIftHistoryModel.Detail> list=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_received_gift, container, false);
        videoMvvm= ViewModelProviders.of(getActivity()).get(VideoMvvm.class);
        findId(view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            getReceived();
        }
    }

    @Override
    public void onResume() {
        getUserVisibleHint();
        super.onResume();
    }

    private void getReceived() {
        videoMvvm.receivedGift(getActivity(), CommonUtils.userId(getActivity()),"recive").observe(getActivity(), new Observer<GIftHistoryModel>() {
            @Override
            public void onChanged(GIftHistoryModel gIftHistoryModel) {
                if(gIftHistoryModel.getSuccess().equalsIgnoreCase("1")){
                    list=gIftHistoryModel.getDetails();
                    setAdapter(list);
                }else {
                    Toast.makeText(getActivity(), gIftHistoryModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAdapter(List<GIftHistoryModel.Detail> list) {
        receivedAdapter=new ReceivedAdapter(getActivity(),list);
        recyclerView.setAdapter(receivedAdapter);
    }

    private void findId(View view) {
        recyclerView=view.findViewById(R.id.rv_received);
    }
}