package in.pureway.cinemaflix.activity.videoEditor.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.videoEditor.activity.MyVideoEditorActivity;
import in.pureway.cinemaflix.activity.videoEditor.adapters.MyFilterAdapter;
import in.pureway.cinemaflix.activity.videoEditor.api.ModelContentsResponse;
import in.pureway.cinemaflix.activity.videoEditor.viewmodel.ContentsViewModel;

import java.util.ArrayList;
import java.util.List;

public class VirtualBackGroundFragment extends Fragment {
    private View view;
    private RecyclerView recycler_segment;
    private ContentsViewModel mContentsViewModel;
    private List<ModelContentsResponse.Category.Item> list = new ArrayList<>();
    private MyFilterAdapter myFilterAdapter;
    private Button btn_add_background;

    public VirtualBackGroundFragment() {
// Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_virtual_back_ground, container, false);
//        findIds();
        return view;
    }
}
//    private void findIds() {
//        view.findViewById(R.id.btn_clear_background).setOnClickListener(this);
//        view.findViewById(R.id.btn_add_background).setOnClickListener(this);
//        recycler_segment = view.findViewById(R.id.recycler_segment);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if (getActivity() != null) {
//            mContentsViewModel = new ViewModelProvider(getActivity()).get(ContentsViewModel.class);
//            mContentsViewModel.getContents().observe(getViewLifecycleOwner(), new Observer<ModelContentsResponse>() {
//                @Override
//                public void onChanged(ModelContentsResponse modelContentsResponse) {
//                    for (int i = 0; i < modelContentsResponse.getCategories().size(); i++) {
//                        if (modelContentsResponse.getCategories().get(i).getTitle().equalsIgnoreCase("virtual_backgrounds")) {
//                            for (int j = 0; j < modelContentsResponse.getCategories().get(i).getItems().size(); j++) {
//                                list.add(modelContentsResponse.getCategories().get(i).getItems().get(j));
//                            }
//                        }
//                    }
//                    myFilterAdapter = new MyFilterAdapter(list, getActivity(), new MyFilterAdapter.Listener() {
//                        @Override
//                        public void onFilterSelected(int position, ModelContentsResponse.Category.Item item) {
//                            ((MyVideoEditorActivity) getActivity()).setSticker(item);
//                        }
//                    });
//                    recycler_segment.setAdapter(myFilterAdapter);
//                }
//            });
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_clear_background:
//                ((MyVideoEditorActivity) getActivity()).clearStickers();
//                break;
//
//            case R.id.btn_add_background:
//// getActivity().onBackPressed();
//                ((MyVideoEditorActivity) getActivity()).hideFragmnent();
//                break;
//        }
//    }
//}