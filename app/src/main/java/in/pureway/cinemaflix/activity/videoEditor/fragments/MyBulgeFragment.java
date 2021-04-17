package in.pureway.cinemaflix.activity.videoEditor.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.videoEditor.activity.MyVideoEditorActivity;
import in.pureway.cinemaflix.activity.videoEditor.adapters.MyBulgeAdapter;

public class MyBulgeFragment extends Fragment {
    private View view;
    private RecyclerView recycler_bulge;

    public MyBulgeFragment() {
// Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_bulge, container, false);
//        findIds();
        return view;
    }

//    private void findIds() {
//        view.findViewById(R.id.btn_clear_bulge).setOnClickListener(this);
//        view.findViewById(R.id.btn_add_bulge).setOnClickListener(this);
//        recycler_bulge = view.findViewById(R.id.recycler_bulge);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if (getActivity() != null) {
//            MyBulgeAdapter myBulgeAdapter = new MyBulgeAdapter(getActivity(), new MyBulgeAdapter.BulgeListener() {
//                @Override
//                public void onBulgeSelected(int bulgeValue) {
//                    ((MyVideoEditorActivity) getActivity()).setBulgeFunType(bulgeValue);
//                }
//            });
//            recycler_bulge.setAdapter(myBulgeAdapter);
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_clear_bulge:
//                ((MyVideoEditorActivity) getActivity()).clearBulge();
//                break;
//
//            case R.id.btn_add_bulge:
//// getActivity().onBackPressed();
//                ((MyVideoEditorActivity) getActivity()).hideFragmnent();
//                break;
//        }
//    }
}