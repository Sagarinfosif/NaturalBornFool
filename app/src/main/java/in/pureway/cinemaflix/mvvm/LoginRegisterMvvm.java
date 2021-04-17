package in.pureway.cinemaflix.mvvm;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import in.pureway.cinemaflix.models.ModelLoginPhone;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.models.ModelvideoReportList;
import in.pureway.cinemaflix.retrofit.ApiClient;
import in.pureway.cinemaflix.retrofit.ApiInterface;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRegisterMvvm extends ViewModel {

    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);


    //Check Email Phone
    private MutableLiveData<Map> phoneEmailData;

    public LiveData<Map> checkEmailPhone(final Activity activity, String email, String username, String phone) {
        phoneEmailData = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.checkPhoneEmail(email, username, phone).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        phoneEmailData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "No Internet!", Toast.LENGTH_SHORT).show();
        }
        return phoneEmailData;

    }


    //Register
    private MutableLiveData<ModelLoginRegister> registerdata;

    public LiveData<ModelLoginRegister> registerUser(final Activity activity, String password, String dob, String email, String countryCode, String phone, String username, String reg_id, String device_type) {

        registerdata = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.userRegister(password, dob, email, countryCode, phone, username, reg_id, device_type).enqueue(new Callback<ModelLoginRegister>() {
                @Override
                public void onResponse(Call<ModelLoginRegister> call, Response<ModelLoginRegister> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        registerdata.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelLoginRegister> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return registerdata;
    }

    //Login
    private MutableLiveData<ModelLoginRegister> logindata;

    public LiveData<ModelLoginRegister> userLogin(final Activity activity, String username, String password, String reg_id, String device_type) {
        logindata = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.userlogin(username, password, reg_id, device_type).enqueue(new Callback<ModelLoginRegister>() {
                @Override
                public void onResponse(Call<ModelLoginRegister> call, Response<ModelLoginRegister> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        logindata.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelLoginRegister> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return logindata;
    }

    //Social Login
    private MutableLiveData<ModelLoginRegister> socialLogindata;

    public LiveData<ModelLoginRegister> userSocialLogin(final Activity activity, String name, String socialId, String email, String phone, String regid, String image, String
            devicetype) {

        socialLogindata = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading....");
            apiInterface.socialLogin(name, socialId, email, phone, regid, image, devicetype).enqueue(new Callback<ModelLoginRegister>() {
                @Override
                public void onResponse(Call<ModelLoginRegister> call, Response<ModelLoginRegister> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        socialLogindata.postValue(response.body());
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<ModelLoginRegister> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.e("socialLogin", t.getMessage());
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return socialLogindata;

    }

    //logout
    private MutableLiveData<Map> logoutData;

    public LiveData<Map> logout(final Activity activity, String userId) {
        logoutData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.logout(userId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    CommonUtils.dismissProgress();
                    Log.i("logout", response.body().get("success").toString());
                    if (response.body() != null) {
                        logoutData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("logout", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return logoutData;
    }

    //change password
    private MutableLiveData<ModelLoginRegister> changePasswordData;

    public LiveData<ModelLoginRegister> changePassword(final Activity activity, String useriD, String old_password, String new_password) {
        changePasswordData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.changePassword(useriD, old_password, new_password).enqueue(new Callback<ModelLoginRegister>() {
                @Override
                public void onResponse(Call<ModelLoginRegister> call, Response<ModelLoginRegister> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        if (response.isSuccessful()) {
                            changePasswordData.postValue(response.body());
                            Log.i("changePassword", response.body().getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ModelLoginRegister> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("changePassword", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return changePasswordData;
    }

    //otp forgot pass
    private MutableLiveData<Map> otpforgotPassdata;

    public LiveData<Map> otpforgotPass(final Activity activity, String type, String emailPhone) {
        otpforgotPassdata = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            otpforgotPassdata = new MutableLiveData<>();
            apiInterface.otpForgotPass(type, emailPhone).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        if (response.body() != null) {
//                            Log.i("otpForgotPass", response.body().get("otp").toString());
                            otpforgotPassdata.postValue(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("otpForgotPass", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return otpforgotPassdata;

    }

    private MutableLiveData<ModelLoginRegister> forgotPassData;

    public LiveData<ModelLoginRegister> forgotPass(final Activity activity, String type, String emailPhone, String password) {
        forgotPassData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.forgotPass(type, emailPhone, password).enqueue(new Callback<ModelLoginRegister>() {
                @Override
                public void onResponse(Call<ModelLoginRegister> call, Response<ModelLoginRegister> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        if (response.isSuccessful()) {
                            Log.i("otpForgotPass", response.body().getMessage());
                            forgotPassData.postValue(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ModelLoginRegister> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("otpForgotPass", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return forgotPassData;
    }


    //Login
    private MutableLiveData<ModelLoginPhone> loginWithPhonedata;

    public LiveData<ModelLoginPhone> loginWithPhone(final Activity activity, String userId, String reg_id, String device_type) {
        loginWithPhonedata = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.loginWithPhone(userId, reg_id, device_type).enqueue(new Callback<ModelLoginPhone>() {
                @Override
                public void onResponse(Call<ModelLoginPhone> call, Response<ModelLoginPhone> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        loginWithPhonedata.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelLoginPhone> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }

        return loginWithPhonedata;
    }

    //Login
    private MutableLiveData<ModelLoginPhone> matchVerificationToken;

    public LiveData<ModelLoginPhone> matchVerificationToken(final Activity activity, String phone,String otp) {
        matchVerificationToken = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.matchVerificationToken(phone,otp).enqueue(new Callback<ModelLoginPhone>() {
                @Override
                public void onResponse(Call<ModelLoginPhone> call, Response<ModelLoginPhone> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        matchVerificationToken.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelLoginPhone> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }

        return matchVerificationToken;
    }


    //Login
    private MutableLiveData<ModelLoginPhone> resendOTP;

    public LiveData<ModelLoginPhone> resendOTP(final Activity activity, String phone) {
        matchVerificationToken = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.resendOTP(phone).enqueue(new Callback<ModelLoginPhone>() {
                @Override
                public void onResponse(Call<ModelLoginPhone> call, Response<ModelLoginPhone> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        matchVerificationToken.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelLoginPhone> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }

        return matchVerificationToken;
    }


    //Login
    private MutableLiveData<ModelvideoReportList> videoReportListData;

    public LiveData<ModelvideoReportList> videoReportList(final Activity activity) {
        videoReportListData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.videoReportList().enqueue(new Callback<ModelvideoReportList>() {
                @Override
                public void onResponse(Call<ModelvideoReportList> call, Response<ModelvideoReportList> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        videoReportListData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelvideoReportList> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }

        return videoReportListData;
    }

    //Login
    private MutableLiveData<Map> userReportVideoData;

    public LiveData<Map> userReportVideo(final Activity activity, String userId,String videoId,String report) {
        userReportVideoData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.userReportVideo(userId,videoId,report).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        userReportVideoData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }

        return userReportVideoData;
    }




}
