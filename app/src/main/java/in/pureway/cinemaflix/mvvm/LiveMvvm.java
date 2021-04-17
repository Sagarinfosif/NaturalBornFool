package in.pureway.cinemaflix.mvvm;


import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import in.pureway.cinemaflix.models.ModelLiveUsers;
import in.pureway.cinemaflix.retrofit.ApiClient;
import in.pureway.cinemaflix.retrofit.ApiInterface;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveMvvm extends ViewModel {

    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    private MutableLiveData<Map> goliveData;

    public LiveData<Map> goLive(final Activity activity, String broadcastId, String userId) {
        goliveData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {

            apiInterface.sendBroad(broadcastId, userId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    if (response.body() != null) {
                        Log.i("LiveBroadcast", response.body().get("message").toString());
                        goliveData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    Log.i("LiveBroadcast", t.getMessage());
                }
            });

        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return goliveData;
    }

//    private MutableLiveData<ReceiveBroadModel> getLiveVideoData;
//
//    public LiveData<ReceiveBroadModel> getLiveVideo(final Activity activity) {
//        getLiveVideoData = new MutableLiveData<>();
//        if (CommonUtils.isNetworkConnected(activity)) {
//            apiInterface.getBroad().enqueue(new Callback<ReceiveBroadModel>() {
//                @Override
//                public void onResponse(Call<ReceiveBroadModel> call, Response<ReceiveBroadModel> response) {
//
//                    if (response.body() != null) {
//                        getLiveVideoData.postValue(response.body());
//                        Log.i("getBroadcast", response.body().getMessage());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ReceiveBroadModel> call, Throwable t) {
//                    Log.i("getBroadcast", t.getMessage());
//                }
//            });
//        } else {
//            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
//        }
//        return getLiveVideoData;
//    }
//
    private MutableLiveData<ModelLiveUsers> getLiveUsersData;

    public LiveData<ModelLiveUsers> getLiveusers(final Activity activity, String userId) {
        getLiveUsersData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Getting live users...");
            apiInterface.liveUsersList(userId).enqueue(new Callback<ModelLiveUsers>() {
                @Override
                public void onResponse(Call<ModelLiveUsers> call, Response<ModelLiveUsers> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        getLiveUsersData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelLiveUsers> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("liveuserslist", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return getLiveUsersData;
    }
//
    private MutableLiveData<ModelLiveUsers> getFollowedLiveUsersData;
    public LiveData<ModelLiveUsers> getFollowedLive(final Activity activity, String userId) {
        getFollowedLiveUsersData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Getting live users...");
            apiInterface.followersLiveBroad(userId).enqueue(new Callback<ModelLiveUsers>() {
                @Override
                public void onResponse(Call<ModelLiveUsers> call, Response<ModelLiveUsers> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        getFollowedLiveUsersData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelLiveUsers> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("liveuserslist", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return getFollowedLiveUsersData;
    }

    private MutableLiveData<Map> commentLiveData;

    public LiveData<Map> commentOnLive(final Activity activity, String broadcastId, String userId, String username, String userImage, String comment) {
        commentLiveData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            ApiInterface apiInterface = ApiClient.commentClient().create(ApiInterface.class);
            apiInterface.commentOnLive(broadcastId, userId, username, userImage, comment).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.i("commentOnVideo", t.getMessage());
                }
            });
        } else {

        }
        return commentLiveData;
    }

    private MutableLiveData<Map> stopBroadcastdata;

    public LiveData<Map> stopBroadcasting(final Activity activity, String broadcast_id) {
        stopBroadcastdata = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.stopBroadcasting(broadcast_id).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    if (response.body() != null) {
                        stopBroadcastdata.postValue(response.body());
                        Log.i("stopBroadcasting", response.body().get("message").toString());
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    Log.i("stopBroadcasting", t.getMessage());
                }
            });

        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return stopBroadcastdata;
    }
}

