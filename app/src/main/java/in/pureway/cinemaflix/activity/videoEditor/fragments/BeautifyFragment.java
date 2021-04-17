package in.pureway.cinemaflix.activity.videoEditor.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.seerslab.argear.session.ARGContents;
//import com.seerslab.argear.session.ARGFrame;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.videoEditor.adapters.BeautifyAdapter;
import in.pureway.cinemaflix.activity.videoEditor.util.AppConfig;
import in.pureway.cinemaflix.activity.videoEditor.activity.MyVideoEditorActivity;
import in.pureway.cinemaflix.activity.videoEditor.data.BeautyItemData;
import in.pureway.cinemaflix.activity.videoEditor.util.CustomSeekBar;

import java.util.Locale;

public class BeautifyFragment extends Fragment {
    private View view;
    private CustomSeekBar beauty_seekbar;
    private RecyclerView recycler_beauty_items;
    private BeautyItemData mBeautyItemData;
    //    private BeautyListAdapter mBeautyListAdapter;
    private BeautifyAdapter beautifyAdapter;
    private Button btn_beauty_comparison, btn_beauty_init, btn_add_effect;
    public static final String BEAUTY_PARAM1 = "bearuty_param1";
//    private ARGFrame.Ratio mScreenRatio = ARGFrame.Ratio.RATIO_FULL;
//    ;
//    private TextView tv_beauty_level_info;
//    private ARGContents.BeautyType mCurrentBeautyType = ARGContents.BeautyType.VLINE;

    public BeautifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_beautify, container, false);
//        findIds();
        recycler_beauty_items.setHasFixedSize(true);
        LinearLayoutManager beautyLayoutManager = new LinearLayoutManager(getContext());
        beautyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_beauty_items.setLayoutManager(beautyLayoutManager);

        return view;

    }
}
//        mBeautyListAdapter = new BeautyListAdapter(this);
//        beautifyAdapter = new BeautifyAdapter(getActivity(), this);
//        recycler_beauty_items.setAdapter(beautifyAdapter);
//
//
//        beauty_seekbar.setOnSeekBarChangeListener(BeautySeekBarListener);
//        return view;
//    }
//
//    private void updateBeautyInfoPosition(TextView view, int progress) {
//        if (view != null) {
//            int max = beauty_seekbar.getMaxValue() - beauty_seekbar.getMinValue();
//            view.setText(String.format(Locale.getDefault(), "%d", progress));
//
//            int paddingLeft = 0;
//            int paddingRight = 0;
//            int offset = -5;
//            int viewWidth = view.getWidth();
//            int x = (int) ((float) (beauty_seekbar.getRight() - beauty_seekbar.getLeft() - paddingLeft - paddingRight - viewWidth - 2 * offset)
//                    * (progress - beauty_seekbar.getMinValue()) / max)
//                    + beauty_seekbar.getLeft() + paddingLeft + offset;
//            view.setX(x);
//        }
//    }
//
//    private SeekBar.OnSeekBarChangeListener BeautySeekBarListener = new SeekBar.OnSeekBarChangeListener() {
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            if (fromUser) {
//                updateBeautyInfoPosition(tv_beauty_level_info, progress);
//                mBeautyItemData.setBeautyValue(mCurrentBeautyType, progress);
//                ((MyVideoEditorActivity) getActivity()).setBeauty(mBeautyItemData.getBeautyValues());
//            }
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//            tv_beauty_level_info.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//            tv_beauty_level_info.setVisibility(View.GONE);
//        }
//    };
//
//    private void findIds() {
//        btn_beauty_init = view.findViewById(R.id.btn_beauty_init);
//        btn_beauty_comparison = view.findViewById(R.id.btn_beauty_comparison);
//        beauty_seekbar = view.findViewById(R.id.beauty_seekbar);
//        tv_beauty_level_info = view.findViewById(R.id.tv_beauty_level_info);
//        recycler_beauty_items = view.findViewById(R.id.recycler_beauty_items);
//        btn_beauty_comparison.setOnTouchListener(BeautyComparisonBtnTouchListener);
//        btn_beauty_init.setOnClickListener(this);
//        view.findViewById(R.id.btn_add_effect).setOnClickListener(this);
//    }
//
//    public void updateUIStyle(ARGFrame.Ratio ratio) {
//        mScreenRatio = ratio;
//        if (ratio == ARGFrame.Ratio.RATIO_FULL) {
//            beauty_seekbar.setActivated(false);
//            tv_beauty_level_info.setActivated(false);
//            tv_beauty_level_info.setTextColor(Color.BLACK);
//        } else {
//            beauty_seekbar.setActivated(true);
//            tv_beauty_level_info.setActivated(true);
//            tv_beauty_level_info.setTextColor(Color.WHITE);
//        }
//
//        beautifyAdapter.notifyDataSetChanged();
////        mBeautyListAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        mBeautyItemData = ((MyVideoEditorActivity) getActivity()).getBeautyItemData();
//
//        beautifyAdapter.setData(mBeautyItemData.getItemInfoData());
//
//        beautifyAdapter.selectItem(ARGContents.BeautyType.VLINE);
//
//        updateUIStyle(mScreenRatio);
//
//        onBeautySelected(ARGContents.BeautyType.VLINE);
//
//        reloadBeauty();
//    }
//
//    private void reloadBeauty() {
//        ((MyVideoEditorActivity) getActivity()).setBeauty(mBeautyItemData.getBeautyValues());
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//    }
//
////    @Override
////    public void onBeautyItemSelected(ARGContents.BeautyType beautyType) {
////        int[] values = ARGContents.BEAUTY_RANGE.get(beautyType);
////        if (values == null) {
////            return;
////        }
////        mCurrentBeautyType = beautyType;
////        beauty_seekbar.setMinValue(values[0]);
////        beauty_seekbar.setMaxValue(values[1]);
////        beauty_seekbar.setProgress((int) mBeautyItemData.getBeautyValue(beautyType));
////    }
//
//    @Override
//    public void onBeautySelected(ARGContents.BeautyType beautyType) {
//        int[] values = ARGContents.BEAUTY_RANGE.get(beautyType);
//        if (values == null) {
//            return;
//        }
//        mCurrentBeautyType = beautyType;
//        beauty_seekbar.setMinValue(values[0]);
//        beauty_seekbar.setMaxValue(values[1]);
//        beauty_seekbar.setProgress((int) mBeautyItemData.getBeautyValue(beautyType));
//    }
//
//    @Override
//    public ARGFrame.Ratio getViewRatio() {
//        return null;
//    }
//
//    private void zeroBeautyParam() {
//        ((MyVideoEditorActivity) getActivity()).setBeauty(new float[ARGContents.BEAUTY_TYPE_NUM]);
//    }
//
//    private View.OnTouchListener BeautyComparisonBtnTouchListener = new View.OnTouchListener() {
//        public boolean onTouch(View v, MotionEvent event) {
//            if (MotionEvent.ACTION_DOWN == event.getAction()) {
//                zeroBeautyParam();
//            } else if (MotionEvent.ACTION_UP == event.getAction()) {
//                reloadBeauty();
//            }
//            return true;
//        }
//    };
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_beauty_init:
//                ((MyVideoEditorActivity) getActivity()).setBeauty(AppConfig.BEAUTY_TYPE_INIT_VALUE);
//                Toast.makeText(getActivity(), "cleared", Toast.LENGTH_SHORT).show();
//                mBeautyItemData.initBeautyValue();
//                beauty_seekbar.setProgress((int) mBeautyItemData.getBeautyValue(mCurrentBeautyType));
//                break;
//
//            case R.id.btn_add_effect:
////                getActivity().onBackPressed();
//                ((MyVideoEditorActivity) getActivity()).hideFragmnent();
//                break;
//        }
//    }
//}