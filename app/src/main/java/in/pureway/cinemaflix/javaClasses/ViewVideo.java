package in.pureway.cinemaflix.javaClasses;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import in.pureway.cinemaflix.mvvm.VideoMvvm;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Map;

public class ViewVideo {
    private FragmentActivity activity;
    private VideoMvvm videoMvvm;

    public interface followCallBack {
        void followInterfaceCall(Map map);
    }

    public ViewVideo(FragmentActivity activity) {
        this.activity = activity;
        videoMvvm = ViewModelProviders.of(activity).get(VideoMvvm.class);
    }

    public void viewVideoAPi(String videoId) {
        String userId = CommonUtils.userId(activity);
        videoMvvm.viewVideo(activity, userId, videoId).observe(activity, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {

            }
        });
    }

}
