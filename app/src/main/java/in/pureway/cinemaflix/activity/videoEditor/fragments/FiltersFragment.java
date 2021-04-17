package in.pureway.cinemaflix.activity.videoEditor.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.videoEditor.activity.MyVideoEditorActivity;
import in.pureway.cinemaflix.activity.videoEditor.adapters.MyFilterAdapter;
import in.pureway.cinemaflix.activity.videoEditor.api.ModelContentsResponse;
import in.pureway.cinemaflix.activity.videoEditor.viewmodel.ContentsViewModel;

import java.util.ArrayList;
import java.util.List;

public class FiltersFragment extends Fragment{
    private View view;
    private ContentsViewModel mContentsViewModel;
    private List<ModelContentsResponse.Category.Item> list = new ArrayList<>();
    private RecyclerView sticker_recycler;
    private Button btn_clear_stickers;
    private MyFilterAdapter myFilterAdapter;
    private SeekBar filter_seek_bar;

    public FiltersFragment() {
// Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_filters, container, false);

                return view;

    }
}
//        findIds();
//        filter_seek_bar.setProgress(((MyVideoEditorActivity) getActivity()).getFilterStrength());
//        filter_seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Log.i("progressFilter", String.valueOf(progress));
//                ((MyVideoEditorActivity) getActivity()).setFilterStrength(progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//        return view;
//    }
//
//    private void findIds() {
//        filter_seek_bar = view.findViewById(R.id.filter_seek_bar);
//        sticker_recycler = view.findViewById(R.id.sticker_recycler);
//        view.findViewById(R.id.btn_vignette).setOnClickListener(this);
//        view.findViewById(R.id.btn_blur).setOnClickListener(this);
//        view.findViewById(R.id.btn_clear_filters).setOnClickListener(this);
//        view.findViewById(R.id.btn_add_filters).setOnClickListener(this);
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
//                        if (modelContentsResponse.getCategories().get(i).getTitle().equalsIgnoreCase("filters")) {
//                            for (int j = 0; j < modelContentsResponse.getCategories().get(i).getItems().size(); j++) {
//                                list.add(modelContentsResponse.getCategories().get(i).getItems().get(j));
//                            }
//                        }
//                    }
//                    myFilterAdapter = new MyFilterAdapter(list, getActivity(), new MyFilterAdapter.Listener() {
//                        @Override
//                        public void onFilterSelected(int position, ModelContentsResponse.Category.Item item) {
//                            ((MyVideoEditorActivity) getActivity()).setFilter(item);
//                        }
//                    });
//                    sticker_recycler.setAdapter(myFilterAdapter);
//                }
//            });
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_clear_filters:
//                ((MyVideoEditorActivity) getActivity()).clearFilter();
//                break;
//            case R.id.btn_vignette:
//                ((MyVideoEditorActivity) getActivity()).setVignette();
//                break;
//            case R.id.btn_blur:
//                ((MyVideoEditorActivity) getActivity()).setBlurVignette();
//                break;
//            case R.id.btn_add_filters:
//// getActivity().onBackPressed();
//                ((MyVideoEditorActivity) getActivity()).hideFragmnent();
//                break;
//        }
//    }
//}