package in.pureway.cinemaflix.activity.videoEditor.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.videoEditor.api.ModelContentsResponse;
import in.pureway.cinemaflix.activity.videoEditor.activity.MyVideoEditorActivity;
import in.pureway.cinemaflix.activity.videoEditor.adapters.MyFilterAdapter;
import in.pureway.cinemaflix.activity.videoEditor.viewmodel.ContentsViewModel;

import java.util.ArrayList;
import java.util.List;

public class EffectsFragment extends Fragment{
    private View view;
    private ContentsViewModel mContentsViewModel;
    private MyFilterAdapter myStickerAdapter;
    private RecyclerView recyclerViewSticker;
    private List<ModelContentsResponse.Category.Item> list = new ArrayList<>();

    public EffectsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_effects, container, false);
        mContentsViewModel = ViewModelProviders.of(EffectsFragment.this).get(ContentsViewModel.class);
//        findIds();
        return view;
    }
}
//    private void findIds() {
//        view.findViewById(R.id.btn_clear_effects).setOnClickListener(this);
//        view.findViewById(R.id.btn_add_effect).setOnClickListener(this);
//        recyclerViewSticker = view.findViewById(R.id.recyclerViewSticker);
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
//                        if (modelContentsResponse.getCategories().get(i).getTitle().equalsIgnoreCase("effects")) {
//                            for (int j = 0; j < modelContentsResponse.getCategories().get(i).getItems().size(); j++) {
//                                list.add(modelContentsResponse.getCategories().get(i).getItems().get(j));
//                            }
//                        }
//                    }
//                    myStickerAdapter = new MyFilterAdapter(list, getActivity(), new MyFilterAdapter.Listener() {
//                        @Override
//                        public void onFilterSelected(int position, ModelContentsResponse.Category.Item item) {
//                            ((MyVideoEditorActivity) getActivity()).setSticker(item);
//                        }
//                    });
//                    recyclerViewSticker.setAdapter(myStickerAdapter);
//                }
//            });
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_clear_effects:
//                ((MyVideoEditorActivity) getActivity()).clearStickers();
//                break;
//            case R.id.btn_add_effect:
////                getActivity().onBackPressed();
//                ((MyVideoEditorActivity) getActivity()).hideFragmnent();
//                break;
//        }
//    }
//}