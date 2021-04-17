package in.pureway.cinemaflix.javaClasses;

import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.ProfileMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

public class UpdateProfileApi {

    private FragmentActivity activity;
    private ProfileMvvm profileMvvm;

    public UpdateProfileApi(FragmentActivity activity) {
        this.activity = activity;
        profileMvvm = ViewModelProviders.of(activity).get(ProfileMvvm.class);
    }

    public void hitUpdateProfile(String name, String username, String bio,String phone) {
        String userId = CommonUtils.userId(activity);
        profileMvvm.upDateProfile(activity, name, username, bio, userId, phone).observe(activity, new Observer<ModelLoginRegister>() {
            @Override
            public void onChanged(ModelLoginRegister modelLoginRegister) {
                if (modelLoginRegister.getSuccess().equalsIgnoreCase("1")) {
                    App.getSharedpref().saveModel(AppConstants.REGISTER_LOGIN_DATA, modelLoginRegister);
                    activity.onBackPressed();
                } else {
                    Toast.makeText(activity, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
