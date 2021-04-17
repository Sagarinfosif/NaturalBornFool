package in.pureway.cinemaflix.javaClasses;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import in.pureway.cinemaflix.mvvm.FollowMvvm;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Map;

public class FollowUnfollowUser {

    private FragmentActivity activity;
    private FollowMvvm followMvvm;
    followCallBack followCallBack;

    public interface followCallBack {
        void followInterfaceCall(Map map);
    }

    public FollowUnfollowUser(FragmentActivity activity,followCallBack followCallBack) {
        this.activity = activity;
        followMvvm = ViewModelProviders.of(activity).get(FollowMvvm.class);
        this.followCallBack=followCallBack;
    }

    public void followUnfollow(String otheruserId) {
        String myId = CommonUtils.userId(activity);
            followMvvm.userFollow(activity, myId, otheruserId).observe(activity, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                followCallBack.followInterfaceCall(map);
            }
        });
    }
}