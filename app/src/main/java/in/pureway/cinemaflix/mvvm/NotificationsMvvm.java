package in.pureway.cinemaflix.mvvm;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import in.pureway.cinemaflix.models.ModelNotifications;
import in.pureway.cinemaflix.retrofit.ApiClient;
import in.pureway.cinemaflix.retrofit.ApiInterface;
import in.pureway.cinemaflix.utils.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsMvvm extends ViewModel {

    private MutableLiveData<ModelNotifications> notificationsData;

    public LiveData<ModelNotifications> notifications(final Activity activity, String userid, String startLimit) {
        notificationsData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity,"Loading...");
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            apiInterface.getNotifications(userid, startLimit).enqueue(new Callback<ModelNotifications>() {
                @Override
                public void onResponse(Call<ModelNotifications> call, Response<ModelNotifications> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        Log.i("notifications",response.body().toString());
                        notificationsData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelNotifications> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("notifications",t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return notificationsData;
    }

}
