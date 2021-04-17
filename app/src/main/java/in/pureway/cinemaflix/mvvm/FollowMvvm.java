package in.pureway.cinemaflix.mvvm;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import in.pureway.cinemaflix.models.ModelFindContacts;
import in.pureway.cinemaflix.models.ModelFollowers;
import in.pureway.cinemaflix.models.ModelSearchFriend;
import in.pureway.cinemaflix.models.SearchAllPojo;
import in.pureway.cinemaflix.retrofit.ApiClient;
import in.pureway.cinemaflix.retrofit.ApiInterface;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowMvvm extends ViewModel {

    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    //Follow User
    private MutableLiveData<Map> userfollowdata;

    public LiveData<Map> userFollow(final Activity activity, String userId, String followingUserId) {
        userfollowdata = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.followUser(userId, followingUserId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {

                    if (response.body() != null) {
                        userfollowdata.postValue(response.body());
                        Log.i("userfollow", response.body().get("message").toString());
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    Log.i("userfollow", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return userfollowdata;
    }

    //followersList
    private MutableLiveData<ModelFollowers> followersData;

    public LiveData<ModelFollowers> getFollowers(final Activity activity, String userid,String search) {
        followersData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
//            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.getFollowersList(userid,search).enqueue(new Callback<ModelFollowers>() {
                @Override
                public void onResponse(Call<ModelFollowers> call, Response<ModelFollowers> response) {
                    CommonUtils.dismissProgress();
                    Log.i("followers", response.body().toString());
                    if (response.body() != null) {
                        followersData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelFollowers> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("followers", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return followersData;
    }

    //followingList
    private MutableLiveData<ModelFollowers> followingData;

    public LiveData<ModelFollowers> getfollowing(final Activity activity, String userid,String search) {
        followingData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.getFollowingList(userid,search).enqueue(new Callback<ModelFollowers>() {
                @Override
                public void onResponse(Call<ModelFollowers> call, Response<ModelFollowers> response) {
                    CommonUtils.dismissProgress();
                    Log.i("followers", response.body().toString());
                    if (response.body() != null) {
                        followingData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelFollowers> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("followers", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return followingData;
    }

    //delete follower
    private MutableLiveData<Map> deleteFollowerData;

    public LiveData<Map> deleteFollower(final Activity activity, String id) {
        deleteFollowerData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.deleteFollower(id).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    CommonUtils.dismissProgress();
                    Log.i("deleteFollower", response.body().toString());
                    if (response.body() != null) {
                        deleteFollowerData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("deleteFollower", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return deleteFollowerData;
    }

    //otherFollowingList
    private MutableLiveData<ModelFollowers> otherFollowingData;

    public LiveData<ModelFollowers> otherFollowingList(final Activity activity, String otherUserid, String myId) {
        otherFollowingData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.otherFollowingList(otherUserid, myId).enqueue(new Callback<ModelFollowers>() {
                @Override
                public void onResponse(Call<ModelFollowers> call, Response<ModelFollowers> response) {
                    Log.i("otherFollowingList", response.body().toString());
                    if (response.body() != null) {
                        otherFollowingData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelFollowers> call, Throwable t) {
                    Log.i("otherFollowingList", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return otherFollowingData;
    }

    //otherfoloowersList
    private MutableLiveData<ModelFollowers> otherFollowersData;

    public LiveData<ModelFollowers> otherFollowersList(final Activity activity, String otherUserid, String myId) {
        otherFollowersData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.otherFollowersList(otherUserid, myId).enqueue(new Callback<ModelFollowers>() {
                @Override
                public void onResponse(Call<ModelFollowers> call, Response<ModelFollowers> response) {
                    Log.i("otherFollowerList", response.body().toString());
                    if (response.body() != null) {
                        otherFollowersData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelFollowers> call, Throwable t) {
                    Log.i("otherFollowerList", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return otherFollowersData;
    }

    //Search Friends
    private MutableLiveData<ModelSearchFriend> searchFriendsdata;

    public LiveData<ModelSearchFriend> searchFriends(final Activity activity, String userId, String search) {
        searchFriendsdata = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.getSearchList(userId, search).enqueue(new Callback<ModelSearchFriend>() {
                @Override
                public void onResponse(Call<ModelSearchFriend> call, Response<ModelSearchFriend> response) {
                    Log.i("searchFriends", response.body().toString());
                    if (response.body() != null) {
                        searchFriendsdata.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelSearchFriend> call, Throwable t) {
                    Log.i("searchFriends", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return searchFriendsdata;
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



    private MutableLiveData<ModelFindContacts> findContactsData;

    public LiveData<ModelFindContacts> findContacts(final Activity activity, String userId, String contactsList) {
        findContactsData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity,"Loading...");
            apiInterface.findContacts(userId, contactsList).enqueue(new Callback<ModelFindContacts>() {
                @Override
                public void onResponse(Call<ModelFindContacts> call, Response<ModelFindContacts> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        Log.i("findContacts", response.body().getMessage());
                        findContactsData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelFindContacts> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("findContacts", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return findContactsData;
    }
}
