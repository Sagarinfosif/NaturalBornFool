package in.pureway.cinemaflix.javaClasses;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import in.pureway.cinemaflix.activity.SplashActivity;
import in.pureway.cinemaflix.mvvm.LoginRegisterMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Map;

public class Logout {

    private FragmentActivity activity;

    public Logout(FragmentActivity activity) {
        this.activity = activity;

    }

    public void logoutUser() {
        String userId = CommonUtils.userId(activity);
        LoginRegisterMvvm loginRegisterMvvm = ViewModelProviders.of(activity).get(LoginRegisterMvvm.class);
        loginRegisterMvvm.logout(activity, CommonUtils.userId(activity)).observe(activity, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    //GoogleLogout
                    GoogleLogin googleLogin = new GoogleLogin(activity, AppConstants.LOGIN_VIDEO);
                    googleLogin.googlesignOut();

                    //Facebook Logout
                    FacebookLogin facebookLogin = new FacebookLogin(activity, AppConstants.LOGIN_VIDEO, activity.getApplication());
                    facebookLogin.fbLogout();

                    App.getSharedpref().clearPreferences();
                    activity.startActivity(new Intent(activity, SplashActivity.class));
                    activity.finishAffinity();
                    Toast.makeText(activity, "Logged out", Toast.LENGTH_SHORT).show();
                    Log.i("logout", map.get("message").toString());
                } else {
                    Log.i("logout", map.get("message").toString());
                }
            }
        });
    }
}
