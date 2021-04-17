package in.pureway.cinemaflix.mvvm;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import in.pureway.cinemaflix.models.GetSoundDetailsPojo;
import in.pureway.cinemaflix.models.ModelFavoriteHashTag;
import in.pureway.cinemaflix.models.ModelFavoriteSounds;
import in.pureway.cinemaflix.models.ModelHashTagsDetails;
import in.pureway.cinemaflix.models.ModelSounds;
import in.pureway.cinemaflix.retrofit.ApiClient;
import in.pureway.cinemaflix.retrofit.ApiInterface;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoundsMvvm extends ViewModel {

    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    private MutableLiveData<ModelSounds> soundsData;

    public LiveData<ModelSounds> getSoundsList(final Activity activity, String userId, String search) {
        soundsData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
//            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.getSoundsList(userId, search).enqueue(new Callback<ModelSounds>() {
                @Override
                public void onResponse(Call<ModelSounds> call, Response<ModelSounds> response) {
//                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        soundsData.postValue(response.body());
//                        Log.i("soundsList", response.body().getDetails().get(0).getId());
                    } else {
                        Log.i("soundsList", "null");
                    }
                }

                @Override
                public void onFailure(Call<ModelSounds> call, Throwable t) {
                   // CommonUtils.dismissProgress();
                    Log.i("soundsList", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return soundsData;
    }


    //add sound to favorites
    private MutableLiveData<ModelFavoriteSounds> addfavoriteSoundData;

    public LiveData<ModelFavoriteSounds> addFavoriteSounds(final Activity activity, String userId, String soundId) {
        addfavoriteSoundData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.addSoundfavorites(userId, soundId).enqueue(new Callback<ModelFavoriteSounds>() {
                @Override
                public void onResponse(Call<ModelFavoriteSounds> call, Response<ModelFavoriteSounds> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        Log.i("favoriteSounds", response.body().getMessage());
                        addfavoriteSoundData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelFavoriteSounds> call, Throwable t) {
                    Log.i("favoriteSounds", t.getMessage());
                    CommonUtils.dismissProgress();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return addfavoriteSoundData;
    }

    //get Favorites sounds list
    private MutableLiveData<ModelFavoriteSounds> favoriteSoundsData;

    public LiveData<ModelFavoriteSounds> getFavoriteSoundsList(final Activity activity, String userId) {
        favoriteSoundsData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.favoriteSoundList(userId).enqueue(new Callback<ModelFavoriteSounds>() {
                @Override
                public void onResponse(Call<ModelFavoriteSounds> call, Response<ModelFavoriteSounds> response) {
                    if (response.body() != null) {
                        Log.i("favoriteSounds", response.body().getMessage());
                        favoriteSoundsData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelFavoriteSounds> call, Throwable t) {
                    Log.i("favoriteSounds", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return favoriteSoundsData;
    }

    //sound detail api
    private MutableLiveData<GetSoundDetailsPojo> getSoundDetails;

    public LiveData<GetSoundDetailsPojo> getSoundResults(final Activity activity, String userId, String soundId, String startLimit) {
        getSoundDetails = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
//            CommonUtils.showProgress(activity, "");
            apiInterface.getSoundDetails(userId, soundId, startLimit).enqueue(new Callback<GetSoundDetailsPojo>() {
                @Override
                public void onResponse(Call<GetSoundDetailsPojo> call, Response<GetSoundDetailsPojo> response) {
//                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        getSoundDetails.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<GetSoundDetailsPojo> call, Throwable t) {
//                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }

        return getSoundDetails;
    }

    //hashtag detail api
    private MutableLiveData<ModelHashTagsDetails> hashTagDetailsData;

    public LiveData<ModelHashTagsDetails> hashTagDetails(final Activity activity, String hashTagId, String userId, String startLimit) {
        hashTagDetailsData = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
//            CommonUtils.showProgress(activity, "");
            apiInterface.getHashTagDetails(hashTagId, userId, startLimit).enqueue(new Callback<ModelHashTagsDetails>() {
                @Override
                public void onResponse(Call<ModelHashTagsDetails> call, Response<ModelHashTagsDetails> response) {
//                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        hashTagDetailsData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelHashTagsDetails> call, Throwable t) {
//                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }

        return hashTagDetailsData;
    }

    //add/remove hashtag from favorites
    private MutableLiveData<Map> addhashTagFavData;

    public LiveData<Map> addhashTagFav(final Activity activity, String userId, String hashTagId) {
        addhashTagFavData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.addRemoveHashFav(userId, hashTagId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        Log.i("hashTagFav", response.body().get("message").toString());
                        addhashTagFavData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("hashTagFav", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return addhashTagFavData;
    }

    //favorite hashtag list
    private MutableLiveData<ModelFavoriteHashTag> favHashListData;

    public LiveData<ModelFavoriteHashTag> favHashList(final Activity activity, String userId) {
        favHashListData=new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.hashTagFavList(userId).enqueue(new Callback<ModelFavoriteHashTag>() {
                @Override
                public void onResponse(Call<ModelFavoriteHashTag> call, Response<ModelFavoriteHashTag> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        Log.i("favHashList",response.body().getMessage());
                        favHashListData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelFavoriteHashTag> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("favHashList",t.getMessage());
                }
            });
        }else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return favHashListData;
    }
}
