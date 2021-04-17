package in.pureway.cinemaflix.fragments.homefrags;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import in.pureway.cinemaflix.Live_Stream.LiveMainActivity;

import com.facebook.CallbackManager;
import com.github.ybq.android.spinkit.SpinKitView;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.AdapterVideoHome;
import in.pureway.cinemaflix.javaClasses.FacebookLogin;
import in.pureway.cinemaflix.javaClasses.GoogleLogin;
import in.pureway.cinemaflix.javaClasses.ViewVideo;
import in.pureway.cinemaflix.models.ModelVideoList;
import in.pureway.cinemaflix.mvvm.VideoMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoHomeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private RecyclerView recycler_video;
    private GoogleLogin googleLogin;
    private FacebookLogin facebookLogin;
    private CallbackManager callbackManager;
    private List<ModelVideoList.Detail> list = new ArrayList<>();
    private List<ModelVideoList.Detail> listNew = new ArrayList<>();
    VideoView videoView;
    private VideoMvvm videoMvvm;
    int currentPage = -1;
    LinearLayoutManager layoutManager;
    boolean is_visible_to_user = true;
    boolean is_user_stop_video = false;
    SpinKitView p_bar;
    ImageView img_loader;
    private RelativeLayout rl_no_internet;
    boolean isScrolling = false;
    int currentItems, totalItem, scrollOutItems, page = 0, followPage = 0;
    private AdapterVideoHome adapterVideoHome;
    private ViewVideo viewVideo;
    private TextView tv_noti_badge_count, tvCount, tv_hot_videos, tv_follow_videos, tv_live_videos;
    private RelativeLayout rl_not_badge;
    private ImageView img_logo_anime;
    // private List<GiftModel.Detail> listgift = new ArrayList<>();
    private VideoHotFragment videoHotFragment;
    private VideoLongFragment videoLongFragment;
    private VideoFollowFragment videoFollowFragment;
    private boolean back = false;
    private TextView tv_live;
    private View view123;

    public VideoHomeFragment() {
// Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video_home, container, false);

        initids();


        if (!App.getSharedpref().isLogin(getActivity())) {
            tv_follow_videos.setVisibility(View.GONE);
            view123.setVisibility(View.GONE);
        } else {

            view123.setVisibility(View.VISIBLE);
        }

        tv_follow_videos.setTextSize(16);
        tv_live_videos.setTextSize(16);
        tv_live.setTextSize(16);
        tv_hot_videos.setTextSize(18);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tv_follow_videos.setTextColor(getResources().getColor(R.color.grey));
            tv_live_videos.setTextColor(getResources().getColor(R.color.grey));
            tv_live.setTextColor(getResources().getColor(R.color.grey));
            tv_hot_videos.setTextColor(getResources().getColor(R.color.red1));
//        }


        changeFrag(getVideoHotFragment());


//        changeFrag(getVideoHotFragment());
// CacheUti

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                tv_follow_videos.setTextSize(16);
                tv_live_videos.setTextSize(16);
                tv_live.setTextSize(16);
                tv_hot_videos.setTextSize(18);

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tv_follow_videos.setTextColor(getResources().getColor(R.color.grey));
                    tv_live.setTextColor(getResources().getColor(R.color.grey));
                    tv_live_videos.setTextColor(getResources().getColor(R.color.grey));
                    tv_hot_videos.setTextColor(getResources().getColor(R.color.red1));
//                }


                backPress();
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), onBackPressedCallback);
        return view;
    }

    private void backPress() {

//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//        alertDialog.setTitle("Are you sure you want to exit?");
//        LayoutInflater layoutInflater = this.getLayoutInflater();
//        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                System.exit(0);
//            }
//        });
//        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.cancel();
//            }
//        });
//        alertDialog.show();


        if (back) {

            System.exit(0);

        } else {
            App.getSingleton().setBackVideoStatus("1");
            changeFrag(getVideoHotFragment());

            back = true;
            Toast.makeText(getActivity(), "Please click BACK again to Home exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    back = false;
                    App.getSingleton().setBackVideoStatus("0");

                }
            }, 1500);
        }


    }

    private void initids() {
// tvCount = getActivity().findViewById(R.id.tvCount);
        view123 = view.findViewById(R.id.view123);
        rl_not_badge = getActivity().findViewById(R.id.rl_not_badge);
// tv_noti_badge_count = getActivity().findViewById(R.id.tvCount);
        tv_follow_videos = view.findViewById(R.id.tv_follow_videos);
        tv_hot_videos = view.findViewById(R.id.tv_hot_videos);
        img_logo_anime = view.findViewById(R.id.img_logo_anime);
        tv_live_videos = view.findViewById(R.id.tv_live_videos);
        tv_live = view.findViewById(R.id.tv_live);

        tv_live.setOnClickListener(this);
        tv_hot_videos.setOnClickListener(this);
        tv_follow_videos.setOnClickListener(this);
        tv_live_videos.setOnClickListener(this);
// Glide.with(getActivity()).asGif().load(R.raw.dots).into(img_loader);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_follow_videos:
                tv_follow_videos.setTextSize(18);
                tv_hot_videos.setTextSize(16);
                tv_live_videos.setTextSize(16);
                tv_live.setTextSize(16);

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    tv_follow_videos.setTextColor(getActivity().getColor(R.color.purple));
                tv_follow_videos.setTextColor(getResources().getColor(R.color.red1));
                tv_hot_videos.setTextColor(getResources().getColor(R.color.grey));
//                    tv_hot_videos.setTextColor(getActivity().getColor(R.color.grey));

                tv_live.setTextColor(getResources().getColor(R.color.grey));
                tv_live_videos.setTextColor(getResources().getColor(R.color.grey));
//                }

                changeFrag(getFollowFragment());
                break;

            case R.id.tv_hot_videos:
                tv_follow_videos.setTextSize(16);
                tv_live.setTextSize(16);
                tv_live_videos.setTextSize(16);
                tv_hot_videos.setTextSize(18);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tv_follow_videos.setTextColor(getResources().getColor(R.color.grey));
                tv_live.setTextColor(getResources().getColor(R.color.grey));
                tv_live_videos.setTextColor(getResources().getColor(R.color.grey));
                tv_hot_videos.setTextColor(getResources().getColor(R.color.red1));
//                }


                changeFrag(getVideoHotFragment());
                break;

            case R.id.tv_live_videos:
                tv_follow_videos.setTextSize(16);
                tv_hot_videos.setTextSize(16);
                tv_live_videos.setTextSize(18);
                tv_live.setTextSize(16);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tv_live.setTextColor(getResources().getColor(R.color.grey));
                tv_follow_videos.setTextColor(getResources().getColor(R.color.grey));
                tv_hot_videos.setTextColor(getResources().getColor(R.color.grey));
                tv_live_videos.setTextColor(getResources().getColor(R.color.red1));
//                }

// changeFrag(getVideoHotFragment());
//                startActivity(new Intent(getActivity(), LiveMainActivity.class));
                changeFrag(getvideoLongFragment());

//TODO live fragment
                break;


            case R.id.tv_live:
                tv_follow_videos.setTextSize(16);
                tv_hot_videos.setTextSize(16);
                tv_live_videos.setTextSize(16);
                tv_live.setTextSize(18);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tv_follow_videos.setTextColor(getResources().getColor(R.color.grey));
                tv_hot_videos.setTextColor(getResources().getColor(R.color.grey));
                tv_live_videos.setTextColor(getResources().getColor(R.color.grey));
                tv_live.setTextColor(getResources().getColor(R.color.red1));
//                }

// changeFrag(getVideoHotFragment());
                startActivity(new Intent(getActivity(), LiveMainActivity.class));

//TODO live fragment
                break;


        }
    }

    private void changeFrag(Fragment fragment) {
        getChildFragmentManager().beginTransaction().replace(R.id.frag_container_video_home, fragment).commit();
    }

    private VideoFollowFragment getFollowFragment() {
        if (videoFollowFragment == null) {
            videoFollowFragment = new VideoFollowFragment();
        }
        return videoFollowFragment;
    }

    private VideoHotFragment getVideoHotFragment() {
        if (videoHotFragment == null) {
            videoHotFragment = new VideoHotFragment();
        }
        return videoHotFragment;
    }

    private VideoLongFragment getvideoLongFragment() {
        if (videoLongFragment == null) {
            videoLongFragment = new VideoLongFragment();
        }
        return videoLongFragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
//        tv_follow_videos.setTextSize(16);
//        tv_live_videos.setTextSize(16);
//        tv_live.setTextSize(16);
//        tv_hot_videos.setTextSize(18);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            tv_follow_videos.setTextColor(getActivity().getColor(R.color.grey));
//            tv_live_videos.setTextColor(getActivity().getColor(R.color.grey));
//            tv_live.setTextColor(getActivity().getColor(R.color.grey));
//            tv_hot_videos.setTextColor(getActivity().getColor(R.color.purple));
//        }
//
//
//        changeFrag(getVideoHotFragment());
    }

}
