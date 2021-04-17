package in.pureway.cinemaflix.mvvm;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import in.pureway.cinemaflix.models.GetCoinModel;
import in.pureway.cinemaflix.models.ModelBlock;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.models.ModelReport;
import in.pureway.cinemaflix.models.OtherUserDataModel;
import in.pureway.cinemaflix.models.SearchAllPojo;
import in.pureway.cinemaflix.retrofit.ApiClient;
import in.pureway.cinemaflix.retrofit.ApiInterface;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileMvvm extends ViewModel {

    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    //Update Profile
    private MutableLiveData<ModelLoginRegister> updateProfiledata;
    private MutableLiveData<GetCoinModel> coinz;


    public LiveData<GetCoinModel> getCoinModelLiveData(final Activity activity, String userId, String transaction, String coin){
        coinz=new MutableLiveData<>();
        CommonUtils.showProgress(activity,"Loading...");
        if(CommonUtils.isNetworkConnected(activity)){
            apiInterface.getCoinWallet(userId,transaction,coin).enqueue(new Callback<GetCoinModel>() {
                @Override
                public void onResponse(Call<GetCoinModel> call, Response<GetCoinModel> response) {
                    if(response.body()!=null){
                        CommonUtils.dismissProgress();
                        coinz.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<GetCoinModel> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return coinz;
    }

    public LiveData<ModelLoginRegister> upDateProfile(final Activity activity, String name, String username, String bio, String userId,String phone) {
        updateProfiledata = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Updating...");
            apiInterface.updateUserInfo(name, username, bio, userId,phone).enqueue(new Callback<ModelLoginRegister>() {
                @Override
                public void onResponse(Call<ModelLoginRegister> call, Response<ModelLoginRegister> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        updateProfiledata.postValue(response.body());
                    } else {
                        Toast.makeText(activity, "Error Occured", Toast.LENGTH_SHORT).show();
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
        return updateProfiledata;
    }

    //Upload Image-Video
    private MutableLiveData<ModelLoginRegister> uploadImageVideoData;

    public LiveData<ModelLoginRegister> uploadImageVideo(final Activity activity, RequestBody userId, MultipartBody.Part image, MultipartBody.Part video) {
        uploadImageVideoData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.uploadImageVideo(userId, image, video).enqueue(new Callback<ModelLoginRegister>() {
                @Override
                public void onResponse(Call<ModelLoginRegister> call, Response<ModelLoginRegister> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        uploadImageVideoData.postValue(response.body());
                    } else {
                        Toast.makeText(activity, "Error Occured", Toast.LENGTH_SHORT).show();
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

        return uploadImageVideoData;
    }


    //Remove Image Video
    private MutableLiveData<ModelLoginRegister> removeImageVideoData;

    public LiveData<ModelLoginRegister> removeImageVideo(final Activity activity, String type, String userId) {
        removeImageVideoData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Removing....");
            apiInterface.removeImageVideo(type, userId).enqueue(new Callback<ModelLoginRegister>() {
                @Override
                public void onResponse(Call<ModelLoginRegister> call, Response<ModelLoginRegister> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        removeImageVideoData.postValue(response.body());
                    } else {
                        Toast.makeText(activity, "Error Occured", Toast.LENGTH_SHORT).show();
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
        return removeImageVideoData;
    }

    //Private Account
    private MutableLiveData<Map> privateAccountData;

    public LiveData<Map> privateAccount(final Activity activity, String userId) {
        privateAccountData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.privateAccount(userId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    if (response.body() != null) {
                        privateAccountData.postValue(response.body());
                        Log.i("privateAccount", response.body().get("status").toString());
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    Log.i("privateAccount", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return privateAccountData;
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

    //User Profile data
    private MutableLiveData<OtherUserDataModel> otherUserData;

    public LiveData<OtherUserDataModel> otherUserProfile(final Activity activity, String userId, String loginId) {
        otherUserData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.userProfileData(userId, loginId).enqueue(new Callback<OtherUserDataModel>() {
                @Override
                public void onResponse(Call<OtherUserDataModel> call, Response<OtherUserDataModel> response) {
                    if (response.body() != null) {
                        otherUserData.postValue(response.body());
                        Log.i("profile data", response.body().getMessage());
                    } else {
                        Log.i("profile data", " response body null");
                    }
                }

                @Override
                public void onFailure(Call<OtherUserDataModel> call, Throwable t) {
                    Log.i("profile data", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return otherUserData;
    }

    //update Email Phone
    private MutableLiveData<ModelLoginRegister> updateEmailPhoneData;

    public LiveData<ModelLoginRegister> updateEmailPhone(final Activity activity, String userId, String type, String emailPhone) {

        updateEmailPhoneData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.updateEmailPhone(userId, type, emailPhone).enqueue(new Callback<ModelLoginRegister>() {
                @Override
                public void onResponse(Call<ModelLoginRegister> call, Response<ModelLoginRegister> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        Log.i("updateEmailPhone", response.body().getMessage());
                        updateEmailPhoneData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelLoginRegister> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("updateEmailPhone", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return updateEmailPhoneData;
    }

    //block user
    private MutableLiveData<Map> blockUserData;

    public LiveData<Map> blockUser(final Activity activity, String userId, String blockId) {
        blockUserData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.blockUser(userId, blockId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        if (response.isSuccessful()) {
                            Log.i("blockuser", response.body().get("message").toString());
                            blockUserData.postValue(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("blockuser", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return blockUserData;
    }

    //block unblock user
    private MutableLiveData<ModelBlock> blockUserList;

    public LiveData<ModelBlock> blockList(final Activity activity, String userId) {
        blockUserList = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.getBlockList(userId).enqueue(new Callback<ModelBlock>() {
                @Override
                public void onResponse(Call<ModelBlock> call, Response<ModelBlock> response) {

                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        if (response.isSuccessful()) {
                            Log.i("blockList", response.body().getMessage());
                            blockUserList.postValue(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ModelBlock> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("blockList", t.getMessage());

                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return blockUserList;
    }

    //search all api
    private MutableLiveData<SearchAllPojo> searchAllDetails;

    public LiveData<SearchAllPojo> searchAppResults(final Activity activity, String search, String tagId, String userId) {
        searchAllDetails = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {

            apiInterface.searchAll(search, tagId, userId).enqueue(new Callback<SearchAllPojo>() {
                @Override
                public void onResponse(Call<SearchAllPojo> call, Response<SearchAllPojo> response) {
                    if (response.body() != null) {
                        searchAllDetails.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<SearchAllPojo> call, Throwable t) {
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }

        return searchAllDetails;

    }

    //get report user list
    private MutableLiveData<ModelReport> reportListData;

    public LiveData<ModelReport> reportList(final Activity activity) {
        reportListData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.reportList().enqueue(new Callback<ModelReport>() {
                @Override
                public void onResponse(Call<ModelReport> call, Response<ModelReport> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        Log.i("reportList", response.body().getMessage());
                        reportListData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelReport> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("reportList", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return reportListData;
    }

    //report user
    private MutableLiveData<ModelReport> reportUserData;

    public LiveData<ModelReport> reportUser(final Activity activity, String userId, String otherId, String reportId) {
        reportUserData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.reportUser(userId, otherId, reportId).enqueue(new Callback<ModelReport>() {
                @Override
                public void onResponse(Call<ModelReport> call, Response<ModelReport> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        Log.i("reportUser", response.body().getMessage());
                        reportUserData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelReport> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("reportUser", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return reportUserData;
    }

    //mute notifications
    private MutableLiveData<Map> muteNotificationsData;

    public LiveData<Map> muteNotifications(final Activity activity, String userId, String muteId) {
        muteNotificationsData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.muteNotification(userId, muteId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        Log.i("muteNotifications", response.body().get("message").toString());
                        muteNotificationsData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("muteNotifications", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return muteNotificationsData;
    }

    //delete account request
    private MutableLiveData<Map> deleteAccountData;

    public LiveData<Map> deleteAccount(final Activity activity, String userId) {
        deleteAccountData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.deleteAccountRequest(userId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        Log.i("deleteAccount", response.body().get("message").toString());
                        deleteAccountData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("deleteAccount", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return deleteAccountData;
    }

    //delete account request
    private MutableLiveData<Map> otpdeleteAccountData;

    public LiveData<Map> otpdeleteAccount(final Activity activity, String userId) {
        otpdeleteAccountData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.otpDeleteAccount(userId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        Log.i("deleteAccount", response.body().get("message").toString());
                        otpdeleteAccountData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("deleteAccount", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return otpdeleteAccountData;
    }

    //Aadhar Verify
    private MutableLiveData<Map> aadharVerifyData;

    public LiveData<Map> aadharVerify(final Activity activity, RequestBody userId,RequestBody type,RequestBody aadharPanNumber, MultipartBody.Part image) {
        aadharVerifyData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.aadharVerify(userId, type, aadharPanNumber, image).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        aadharVerifyData.postValue(response.body());
                    } else {
                        Toast.makeText(activity, "Error Occured", Toast.LENGTH_SHORT).show();
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

        return aadharVerifyData;
    }

    //Get Verify Status
    private MutableLiveData<Map> getVerifyData;

    public LiveData<Map> getVerifyStatus(final Activity activity, String userId) {
        getVerifyData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
//            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.getVerifyStatus(userId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
//                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        getVerifyData.postValue(response.body());
                    } else {
                        Toast.makeText(activity, "Error Occured", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
//                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }

        return getVerifyData;
    }

}
