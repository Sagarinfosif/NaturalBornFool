package in.pureway.cinemaflix.javaClasses;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;
import in.pureway.cinemaflix.activity.HomeActivity;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.LoginRegisterMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;

public class FacebookLogin {

    public static CallbackManager callbackManager;
    private FragmentActivity activity;
    String fbId = "", fbFirstName = "", fbLastName = "", fbPhoneNumber = "", fbEmail = "", fbGender = "", fbDateOfBirth = "", fbCountry = "", fbProfilePicture = "";
    private URL fbProfilePicturenew;
    private LoginRegisterMvvm loginRegisterMvvm;
    private int loginType = AppConstants.LOGIN_VIDEO;

    public FacebookLogin(FragmentActivity activity, int loginType, Application application) {
        this.activity = activity;
        FacebookSdk.sdkInitialize(application);
        AppEventsLogger.activateApp(application);
        this.loginType = loginType;
        callbackManager = CallbackManager.Factory.create();
        loginRegisterMvvm = ViewModelProviders.of(activity).get(LoginRegisterMvvm.class);

    }

    public void FBLogin() {
        if (CommonUtils.isNetworkConnected(activity)) {

            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    CommonUtils.dismissProgress();
                    Log.e("facebook", loginResult.getAccessToken().getToken());
                    getFacebookData(loginResult);
                    // Toast.makeText(activity, ""+userStringEmail, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    CommonUtils.dismissProgress();
                    Log.e("facebook", "cancel");
                    Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    CommonUtils.dismissProgress();
                    Log.e("facebook", error.getMessage());
                    if (error instanceof FacebookAuthorizationException) {
                        if (AccessToken.getCurrentAccessToken() != null) {
                            LoginManager.getInstance().logOut();
                        }
                    }
                }
            });
        } else {
            Toast.makeText(activity, "Network Issue", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFacebookData(LoginResult loginResult) {

        CommonUtils.showProgress(activity, "");

        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                Log.e("facebook", "completed");
                CommonUtils.dismissProgress();

                try {
                    if (object.has("id")) {
                        fbId = object.getString("id");
                        Log.e("LoginActivity", "id" + fbId);
                    }

                    //check permission first userName
                    if (object.has("first_name")) {
                        fbFirstName = object.getString("first_name");
                        Log.e("LoginActivity", "first_name" + fbFirstName);
                    }

                    //check permisson last userName
                    if (object.has("last_name")) {
                        fbLastName = object.getString("last_name");
                        Log.e("LoginActivity", "last_name" + fbLastName);
                    }

                    //check permisson email
                    if (object.has("email")) {
                        fbEmail = object.getString("email");
                        Log.e("LoginActivity", "email" + fbEmail);
                    }
                    if (object.has("phoneNumber")) {
                        fbPhoneNumber = object.getString("phoneNumber");
                        Log.e("LoginActivity", "email" + fbPhoneNumber);
                    }

                    if (object.has("gender")) {
                        fbGender = object.getString("gender");
                        Log.e("LoginActivity", "email" + fbGender);
                    }

                    if (object.has("dateofbirth")) {
                        fbDateOfBirth = object.getString("dateofbirth");
                        Log.e("LoginActivity", "email" + fbDateOfBirth);
                    }

                    if (object.has("country")) {
                        fbCountry = object.getString("country");
                        Log.e("LoginActivity", "email" + fbCountry);
                    }
                    JSONObject jsonObject = new JSONObject(object.getString("picture"));
                    if (jsonObject != null) {
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        Log.e("LoginActivity", "json oject get picture" + dataObject);
                        fbProfilePicturenew = new URL("https://graph.facebook.com/" + fbId + "/picture?width=500&height=500");
                        Log.e("LoginActivity", "json object=>" + object.toString());
                    }

                    if (fbProfilePicturenew != null) {
                        fbProfilePicture = String.valueOf(fbProfilePicturenew);
                    } else {
                        fbProfilePicture = "";
                    }

                    //Login
                    socialLoginFun();

                } catch (Exception e) {
                    CommonUtils.dismissProgress();
                    Log.e("Exception", e.getMessage());
                }
            }
        });

        Bundle bundle = new Bundle();
        Log.e("LoginActivity", "bundle set");
        bundle.putString("fields", "id, first_name, last_name,email,picture,gender,location");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }

    private void socialLoginFun() {
        Log.e("facebook", "functioncalled");
        String deviceType = "Android";
        final String regId = FirebaseInstanceId.getInstance().getToken();
        loginRegisterMvvm.userSocialLogin(activity, fbFirstName, fbId, fbEmail, fbPhoneNumber, regId, fbProfilePicture, deviceType).observe(activity, new Observer<ModelLoginRegister>() {
            @Override
            public void onChanged(ModelLoginRegister modelLoginRegister) {
                if (modelLoginRegister.getSuccess().equalsIgnoreCase("1")) {
                    Log.i("facebook", modelLoginRegister.getMessage());
                    Log.i("facebook", modelLoginRegister.getDetails().getId());
                    Toast.makeText(activity, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
//                    SQLiteDatabaseHandler sqLiteDatabaseHandler=SQLiteDatabaseHandler.getInstance(activity);
//                    sqLiteDatabaseHandler.addModel(modelLoginRegister.getDetails());
                    App.getSharedpref().saveModel(AppConstants.REGISTER_LOGIN_DATA, modelLoginRegister);
                    App.getSharedpref().saveString(AppConstants.LOGIN_STATUS, "1");
                    App.getSharedpref().login(activity, true);
                    activity.startActivity(new Intent(activity, HomeActivity.class).putExtra(AppConstants.LOGIN_TYPE, loginType));
                    activity.finishAffinity();
                } else {
                    Toast.makeText(activity, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void fbLogout() {
        FacebookSdk.sdkInitialize(activity.getApplicationContext());
        if (LoginManager.getInstance().getLoginBehavior() != null) {
            LoginManager.getInstance().logOut();
        }
    }
}
