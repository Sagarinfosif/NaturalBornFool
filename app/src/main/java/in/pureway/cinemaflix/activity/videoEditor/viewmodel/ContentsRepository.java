package in.pureway.cinemaflix.activity.videoEditor.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import in.pureway.cinemaflix.activity.videoEditor.util.AppConfig;
import in.pureway.cinemaflix.activity.videoEditor.api.CmsService;
import in.pureway.cinemaflix.activity.videoEditor.api.ContentsApi;
import in.pureway.cinemaflix.activity.videoEditor.api.ModelContentsResponse;
import in.pureway.cinemaflix.utils.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ContentsRepository {

    @NonNull
    static ContentsRepository getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final ContentsRepository INSTANCE = new ContentsRepository();
    }

    private ContentsApi contentsApi;

    private ContentsRepository() {
        contentsApi = CmsService.createContentsService(AppConfig.API_URL);
    }

    MutableLiveData<ModelContentsResponse> getContents(Application application) {
        MutableLiveData<ModelContentsResponse> contents = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(application.getApplicationContext())) {
            contentsApi.getContents1().enqueue(new Callback<ModelContentsResponse>() {
                @Override
                public void onResponse(@Nullable Call<ModelContentsResponse> call, @NonNull Response<ModelContentsResponse> response) {

                    if (response.isSuccessful()) {
                        Log.i("contentsGet", response.body().getDescription());
                        contents.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(@Nullable Call<ModelContentsResponse> call, @NonNull Throwable t) {
                    contents.setValue(null);
                    Log.i("contentsGet", t.getMessage());
                }
            });
        } else {
            Toast.makeText(application, "Internet Required", Toast.LENGTH_SHORT).show();
        }
        return contents;
    }
}
