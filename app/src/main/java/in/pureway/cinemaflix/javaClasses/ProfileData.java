package in.pureway.cinemaflix.javaClasses;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import in.pureway.cinemaflix.models.ModelMyUploadedVideos;
import in.pureway.cinemaflix.models.OtherUserDataModel;
import in.pureway.cinemaflix.mvvm.ProfileMvvm;
import in.pureway.cinemaflix.mvvm.VideoMvvm;
import in.pureway.cinemaflix.utils.CommonUtils;

public class ProfileData {

    private FragmentActivity activity;
    private ProfileMvvm profileMvvm;
    private VideoMvvm videoMvvm;
    followCallBack followCallBack;

    public interface followCallBack {
        void followInterfaceCall(OtherUserDataModel otherUserDataModel);
        void uploadedVideosCall(ModelMyUploadedVideos modelMyUploadedVideos);
    }

    public ProfileData(FragmentActivity activity, followCallBack followCallBack) {
        this.activity = activity;
        profileMvvm = ViewModelProviders.of(activity).get(ProfileMvvm.class);
        videoMvvm = ViewModelProviders.of(activity).get(VideoMvvm.class);
        this.followCallBack = followCallBack;
    }

    public void getProfileData(String otherUserId) {
        String myId = CommonUtils.userId(activity);
        profileMvvm.otherUserProfile(activity, otherUserId, myId).observe(activity, new Observer<OtherUserDataModel>() {
            @Override
            public void onChanged(OtherUserDataModel otherUserDataModel) {
                followCallBack.followInterfaceCall(otherUserDataModel);
            }
        });
    }

    public void getUploadedVideo(String userId, String loginId){
        videoMvvm.getUploadedVideos(activity, userId, loginId).observe(activity, new Observer<ModelMyUploadedVideos>() {
            @Override
            public void onChanged(ModelMyUploadedVideos modelMyUploadedVideos) {
                followCallBack.uploadedVideosCall(modelMyUploadedVideos);
            }
        });

    }
}
