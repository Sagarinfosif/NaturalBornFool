package in.pureway.cinemaflix.activity.videoEditor.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import in.pureway.cinemaflix.activity.videoEditor.api.ModelContentsResponse;

public class ContentsViewModel extends AndroidViewModel {
    private MutableLiveData<ModelContentsResponse> mutableLiveData;
    private ContentsRepository contentsRepository;
    public ContentsViewModel(Application application) {
        super(application);
        contentsRepository = ContentsRepository.getInstance();
        mutableLiveData = contentsRepository.getContents(application);
    }

    public LiveData<ModelContentsResponse> getContents() {
        return mutableLiveData;
    }
}
