package in.pureway.cinemaflix.javaClasses;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.iid.FirebaseInstanceId;
import in.pureway.cinemaflix.activity.HomeActivity;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.LoginRegisterMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

public class GoogleLogin {

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 007;
    FragmentActivity activity;
    String socialId = "", userName = "", email = "", userImage = "", phone = "";
    private LoginRegisterMvvm loginRegisterMvvm;
    private int loginTypee;

    public GoogleLogin(FragmentActivity activity, int loginType) {
        this.activity = activity;
        this.loginTypee = loginType;
        Log.i("loginType", String.valueOf(loginType));
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        loginRegisterMvvm = ViewModelProviders.of(activity).get(LoginRegisterMvvm.class);
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void activityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult task = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        CommonUtils.showProgress(activity, "Checking Social Profile..");
        if (result.isSuccess()) {
            CommonUtils.dismissProgress();
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("Account: ", acct.getDisplayName());
            Log.d("Account: ", acct.getId());
            Log.d("Account: ", acct.getEmail());
            socialId = acct.getId();
            userName = acct.getDisplayName();

            email = acct.getEmail();

            if (acct.getPhotoUrl() != null) {
                userImage = String.valueOf(acct.getPhotoUrl());
            } else {
                userImage = "";
            }

            //Login
            socialLoginFun();
        } else {
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            CommonUtils.dismissProgress();

        }
    }

    public void googlesignOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient;

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        if (account == null) {

        } else {
            mGoogleSignInClient.signOut();
        }
    }

    private void socialLoginFun() {
        String regId = FirebaseInstanceId.getInstance().getToken();
        String devieType = "Android";
        loginRegisterMvvm.userSocialLogin(activity, userName, socialId, email, phone, regId, userImage, devieType).observe(activity, new Observer<ModelLoginRegister>() {
            @Override
            public void onChanged(ModelLoginRegister modelLoginRegister) {
                if (modelLoginRegister.getSuccess().equalsIgnoreCase("1")) {
                    Log.i("google", modelLoginRegister.getMessage());
                    Log.i("google", modelLoginRegister.getDetails().getId());
                    Toast.makeText(activity, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
//                    SQLiteDatabaseHandler sqLiteDatabaseHandler=SQLiteDatabaseHandler.getInstance(activity);
//                    sqLiteDatabaseHandler.addModel(modelLoginRegister.getDetails());
                    App.getSharedpref().saveModel(AppConstants.REGISTER_LOGIN_DATA, modelLoginRegister);
                    App.getSharedpref().saveString(AppConstants.LOGIN_STATUS, "1");
                    App.getSharedpref().login(activity, true);
                    Log.i("loginType", String.valueOf(loginTypee));
                    activity.startActivity(new Intent(activity, HomeActivity.class).putExtra(AppConstants.LOGIN_TYPE, loginTypee));
                    activity.finishAffinity();
                } else {
                    Toast.makeText(activity, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
