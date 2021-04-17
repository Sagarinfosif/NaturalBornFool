package in.pureway.cinemaflix.mvvm;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import in.pureway.cinemaflix.models.ModelPrivacySettings;
import in.pureway.cinemaflix.models.ModelReportList;
import in.pureway.cinemaflix.models.PushNotificationSettingsPojo;
import in.pureway.cinemaflix.retrofit.ApiClient;
import in.pureway.cinemaflix.retrofit.ApiInterface;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsMvvm extends ViewModel {

    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    //show my following
    private MutableLiveData<Map> showFollowingData;

    public LiveData<Map> showFollowing(final Activity activity, String userId) {
        showFollowingData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.showMyFollowing(userId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    if (response.body() != null) {
                        if (response.isSuccessful()) {
                            showFollowingData.postValue(response.body());
                            Log.i("showFollowing", response.body().get("message").toString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    Log.i("showFollowing", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return showFollowingData;
    }

    //show my following
    private MutableLiveData<Map> showProfileVideoData;

    public LiveData<Map> showProfileVideo(final Activity activity, String userId) {
        showProfileVideoData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.showProfileVideo(userId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    if (response.body() != null) {
                        if (response.isSuccessful()) {
                            showProfileVideoData.postValue(response.body());
                            Log.i("showProfileVideoData", response.body().get("message").toString());
                        }
                    } else {
                        Log.i("showProfileVideoData", "null");
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    Log.i("showProfileVideoData", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return showProfileVideoData;
    }

//getNotiPush settings

    private MutableLiveData<PushNotificationSettingsPojo> getPushSettigsDetails;

    public LiveData<PushNotificationSettingsPojo> getPushSettingsResults(final Activity activity, String userId) {

        getPushSettigsDetails = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.getPushNotiSettings(userId).enqueue(new Callback<PushNotificationSettingsPojo>() {
                @Override
                public void onResponse(Call<PushNotificationSettingsPojo> call, Response<PushNotificationSettingsPojo> response) {
                    if (response.body() != null) {
                        getPushSettigsDetails.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<PushNotificationSettingsPojo> call, Throwable t) {
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return getPushSettigsDetails;
    }

    public LiveData<PushNotificationSettingsPojo> updatePushSettingsResults(final Activity activity, String userId, String likeNotifaction, String commentNotification, String followersNotification, String messageNotification, String videoNotification) {

        getPushSettigsDetails = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.updatePushNotiSettings(userId, likeNotifaction, commentNotification, followersNotification, messageNotification, videoNotification).enqueue(new Callback<PushNotificationSettingsPojo>() {
                @Override
                public void onResponse(Call<PushNotificationSettingsPojo> call, Response<PushNotificationSettingsPojo> response) {
                    if (response.body() != null) {
                        getPushSettigsDetails.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<PushNotificationSettingsPojo> call, Throwable t) {
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }

        return getPushSettigsDetails;
    }

    // get privacy settings
    private MutableLiveData<ModelPrivacySettings> privacySettingsMutableLiveData;

    public LiveData<ModelPrivacySettings> privacySettings(final Activity activity, String userId) {
        privacySettingsMutableLiveData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            privacySettingsMutableLiveData = new MutableLiveData<>();
            apiInterface.getPrivacySettings(userId).enqueue(new Callback<ModelPrivacySettings>() {
                @Override
                public void onResponse(Call<ModelPrivacySettings> call, Response<ModelPrivacySettings> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        Log.i("privacySettings", response.body().getMessage());
                        privacySettingsMutableLiveData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelPrivacySettings> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("privacySettings", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return privacySettingsMutableLiveData;
    }

    //Show liked videos
    private MutableLiveData<Map> showLikedvideosData;

    public LiveData<Map> showLikedvideos(final Activity activity, String userId) {
        showLikedvideosData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.showLikedVideos(userId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    if (response.body() != null) {
                        showLikedvideosData.postValue(response.body());
                        Log.i("likedVideos", response.body().get("status").toString());
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    Log.i("likedVideos", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return showLikedvideosData;
    }

    //report problem list
    private MutableLiveData<ModelReportList> reportProblemListdata;

    public LiveData<ModelReportList> reportProblemList(final Activity activity) {
        reportProblemListdata = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.getReportProblemList().enqueue(new Callback<ModelReportList>() {
                @Override
                public void onResponse(Call<ModelReportList> call, Response<ModelReportList> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        reportProblemListdata.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelReportList> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return reportProblemListdata;
    }

    //report problem list
    private MutableLiveData<ModelReportList> reportProblemdata;

    public LiveData<ModelReportList> reportProblem(final Activity activity, String userId, String report) {
        reportProblemdata = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.reportProblem(userId, report).enqueue(new Callback<ModelReportList>() {
                @Override
                public void onResponse(Call<ModelReportList> call, Response<ModelReportList> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        Log.i("reportProblem", response.body().getMessage());
                        reportProblemdata.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelReportList> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("reportProblem", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return reportProblemdata;
    }
}