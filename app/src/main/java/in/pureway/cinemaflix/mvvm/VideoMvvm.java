package in.pureway.cinemaflix.mvvm;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import in.pureway.cinemaflix.Live_Stream.GIftHistoryModel;
import in.pureway.cinemaflix.adapters.ModelVideoTesting;
import in.pureway.cinemaflix.models.CoinModel;
import in.pureway.cinemaflix.models.GiftModel;
import in.pureway.cinemaflix.models.ModelComments;
import in.pureway.cinemaflix.models.ModelHashTag;
import in.pureway.cinemaflix.models.ModelLikedVideos;
import in.pureway.cinemaflix.models.ModelMyUploadedVideos;
import in.pureway.cinemaflix.models.ModelVideoList;
import in.pureway.cinemaflix.models.UploadSingleVideoPojo;
import in.pureway.cinemaflix.retrofit.ApiClient;
import in.pureway.cinemaflix.retrofit.ApiInterface;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class VideoMvvm extends ViewModel {
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    //Upload Video
    private MutableLiveData<Map> uploadVideoData;
    private MutableLiveData<GiftModel> gift;
    private MutableLiveData<CoinModel> coin;
    private MutableLiveData<Map> giftSend;
    private MutableLiveData<GIftHistoryModel> sentgift;
    private MutableLiveData<GIftHistoryModel> receivedgift;

    public LiveData<Map> uploadVideo(final Activity activity, RequestBody userId, RequestBody hashTag, RequestBody description, RequestBody allowComment
            , RequestBody allowDuetReact, RequestBody allowDownloads, MultipartBody.Part videoPath, MultipartBody.Part soundFile, RequestBody viewVideo, RequestBody soundId, RequestBody soundTitle,RequestBody videolength, RequestBody videoType,RequestBody isDuet,RequestBody heightDuet,RequestBody widthDuet,RequestBody duetUrl,MultipartBody.Part imageThumb) {
        uploadVideoData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Uploading...");
            apiInterface.uploadVideo(userId, hashTag, description, allowComment, allowDuetReact, allowDownloads, videoPath, soundFile, viewVideo, soundId, soundTitle,videolength,videoType,isDuet,heightDuet,widthDuet,duetUrl,imageThumb).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        Log.i("upload:Video", response.body().get("message").toString());
                        uploadVideoData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("upload:Video", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return uploadVideoData;
    }


    private MutableLiveData<UploadSingleVideoPojo> uploadSingleVideoPojoMutableLiveData;
    public LiveData<UploadSingleVideoPojo> uploadSingleVideo(final Activity activity,MultipartBody.Part video) {
        uploadSingleVideoPojoMutableLiveData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Uploading...");
            apiInterface.uploadSingleVideo(video).enqueue(new Callback<UploadSingleVideoPojo>() {
                @Override
                public void onResponse(Call<UploadSingleVideoPojo> call, Response<UploadSingleVideoPojo> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        uploadSingleVideoPojoMutableLiveData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<UploadSingleVideoPojo> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("upload:Video", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return uploadSingleVideoPojoMutableLiveData;
    }

    //Get Video List
    private MutableLiveData<ModelVideoList> videoListData;

    public LiveData<ModelVideoList> videoList(final Activity activity, String userId, String startLimit, String videoType,int anuradha,RelativeLayout relativeLayout, RecyclerView recyclerView, ProgressBar progressBar) {
        videoListData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.getVideoList(userId, startLimit,videoType,anuradha).enqueue(new Callback<ModelVideoList>() {
                @Override
                public void onResponse(Call<ModelVideoList> call, Response<ModelVideoList> response) {
                    if (response.body() != null) {
                        Log.i("videoHome", response.body().getSuccess());
                        videoListData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelVideoList> call, Throwable t) {
                    Log.i("videoHome", t.getMessage());
//                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            relativeLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return videoListData;
    }


    //Single Video List
    private MutableLiveData<ModelMyUploadedVideos> singleVideoData;

    public LiveData<ModelMyUploadedVideos> singleVideo(final Activity activity, String userId, String videoId) {
        singleVideoData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.getSingleVideo(userId, videoId).enqueue(new Callback<ModelMyUploadedVideos>() {
                @Override
                public void onResponse(Call<ModelMyUploadedVideos> call, Response<ModelMyUploadedVideos> response) {
                    if (response.body() != null) {
                        Log.i("videoHome", response.body().getSuccess());
                        singleVideoData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelMyUploadedVideos> call, Throwable t) {
                    Log.i("videoHome", t.getMessage());
//                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
//            relativeLayout.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.GONE);
//            progressBar.setVisibility(View.GONE);
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return singleVideoData;
    }

    //Like Video
    private MutableLiveData<Map> likeVideoData;

    public LiveData<Map> likeVideo(final Activity activity, String userId, String videoId, String ownerId) {
        likeVideoData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.likeVideo(userId, videoId, ownerId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    if (response.body() != null) {
                        if (response.isSuccessful()) {
                            likeVideoData.postValue(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
//                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return likeVideoData;
    }

    //Profile/Uploaded Videos List
    private MutableLiveData<ModelMyUploadedVideos> uploadedVideosData;

    public MutableLiveData<ModelMyUploadedVideos> getUploadedVideos(final Activity activity, String userId, String loginId) {
        uploadedVideosData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.getProfileVideos(userId, loginId).enqueue(new Callback<ModelMyUploadedVideos>() {
                @Override
                public void onResponse(Call<ModelMyUploadedVideos> call, Response<ModelMyUploadedVideos> response) {
//                    Log.i("myVideos", response.body().toString());
                    if (response.body() != null) {
                        uploadedVideosData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelMyUploadedVideos> call, Throwable t) {
                    Log.i("myVideos", t.getMessage());
                }
            });
        } else {
            Log.i("myVideos", "else");
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return uploadedVideosData;
    }

    //Liked Video List
    private MutableLiveData<ModelLikedVideos> likedVideosData;

    public LiveData<ModelLikedVideos> getLikedVideos(final Activity activity, String userId) {
        likedVideosData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.getLikedVideos(userId).enqueue(new Callback<ModelLikedVideos>() {
                @Override
                public void onResponse(Call<ModelLikedVideos> call, Response<ModelLikedVideos> response) {
                    if (response.body() != null) {
                        likedVideosData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelLikedVideos> call, Throwable t) {
//                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return likedVideosData;
    }

    //VideoComments
    private MutableLiveData<ModelComments> commentsData;

    public LiveData<ModelComments> getComments(final Activity activity, String userId, String videoId) {
        commentsData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.comments(userId, videoId).enqueue(new Callback<ModelComments>() {
                @Override
                public void onResponse(Call<ModelComments> call, Response<ModelComments> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        commentsData.postValue(response.body());
                    } else {
                        Log.e("comments", "Response : null");
                    }
                }

                @Override
                public void onFailure(Call<ModelComments> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.e("comments:error", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return commentsData;

    }

    //Add comment
    private MutableLiveData<ModelComments> addcommentsData;

    public LiveData<ModelComments> addComments(final Activity activity, String userId, String videoId, String comment, String ownerId) {
        addcommentsData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.commentOnVideo(userId, videoId, comment, ownerId).enqueue(new Callback<ModelComments>() {
                @Override
                public void onResponse(Call<ModelComments> call, Response<ModelComments> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        addcommentsData.postValue(response.body());
                    } else {
                        Log.e("comments", "Response : null");
                    }
                }

                @Override
                public void onFailure(Call<ModelComments> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.e("comments:error", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return addcommentsData;

    }

    //like dislike comment
    private MutableLiveData<Map> likecommentData;

    public LiveData<Map> likeComment(final Activity activity, String commentID, String userId) {
        likecommentData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.likeComment(commentID, userId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {

                    if (response.body() != null) {
                        likecommentData.postValue(response.body());
                    } else {
                        Log.e("comments", "Response : null");
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    Log.e("comments:error", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return likecommentData;
    }

    //Delet Video
    private MutableLiveData<Map> videoDeletData;

    public LiveData<Map> videoDelete(final Activity activity, String videoId) {
        videoDeletData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Deleting...");
            apiInterface.videoDelet(videoId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    CommonUtils.dismissProgress();
                    Log.i("videoDelete", response.body().get("message").toString());
                    if (response.body() != null) {
                        videoDeletData.postValue(response.body());
                    } else {
                        Log.i("videoDelete", "response:null");
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("videoDelete", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return videoDeletData;
    }

    //Delete comment
    private MutableLiveData<Map> deleteCommentdata;

    public LiveData<Map> deleteComment(final Activity activity, String commentId) {
        deleteCommentdata = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.deleteComment(commentId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    CommonUtils.dismissProgress();
                    Log.i("comment", response.body().toString());
                    if (response.body() != null) {
                        deleteCommentdata.postValue(response.body());
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("comment", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return deleteCommentdata;
    }

    //commentReply
    private MutableLiveData<ModelComments> commentReplyData;

    public LiveData<ModelComments> commentReply(final Activity activity, String userId, String videoId, String commentId, String comment) {

        commentReplyData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.commentReply(userId, videoId, commentId, comment).enqueue(new Callback<ModelComments>() {
                @Override
                public void onResponse(Call<ModelComments> call, Response<ModelComments> response) {
                    if (response.body() != null) {
                        if (response.isSuccessful()) {
                            Log.i("otpForgotPass", response.body().getMessage());
                            commentReplyData.postValue(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ModelComments> call, Throwable t) {
                    Log.i("otpForgotPass", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return commentReplyData;
    }

    private MutableLiveData<ModelHashTag> searchHashtagsVideoData;

    public LiveData<ModelHashTag> searchHashtagsVideo(final Activity activity, String search) {
        searchHashtagsVideoData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.searchHashtags(search).enqueue(new Callback<ModelHashTag>() {
                @Override
                public void onResponse(Call<ModelHashTag> call, Response<ModelHashTag> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        Log.i("searchHashtags", response.body().getMessage());
                        searchHashtagsVideoData.postValue(response.body());
                    } else {
                        try {
//                            String error = response.errorBody().toString();
//                            StringBuilder message = new StringBuilder();
//                            error = message.append(JSONObject())
                        } catch (Exception e) {

                        }
                        ModelHashTag data = new ModelHashTag("1", "deatil", null);
                    }
                }

                @Override
                public void onFailure(Call<ModelHashTag> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("searchHashtags", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return searchHashtagsVideoData;
    }

    //view video
    private MutableLiveData<Map> viewVideoData;

    public LiveData<Map> viewVideo(final Activity activity, String userId, final String videoId) {
        viewVideoData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.viewVideo(userId, videoId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    try {
                        if (response.body() != null) {
                            if (response.isSuccessful()) {
                                Log.i("viewVideo", response.body().get("message").toString() + "videoId : " + videoId);
                            }
                        }
                    } catch (Exception e) {
                        Log.i("viewVideo", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    Log.i("viewVideo", t.getMessage());
                }
            });
        } else {

        }
        return viewVideoData;
    }

    public LiveData<GiftModel> sendGift(final Activity activity, String userId){
        gift=new MutableLiveData<>();
        CommonUtils.showProgress(activity,"Loading...");
        if(CommonUtils.isNetworkConnected(activity)){
            apiInterface.getGifts(userId).enqueue(new Callback<GiftModel>() {
                @Override
                public void onResponse(Call<GiftModel> call, Response<GiftModel> response) {
                    if(response.body()!=null){
                        CommonUtils.dismissProgress();
                        gift.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<GiftModel> call, Throwable t) {
                    CommonUtils.dismissProgress();
//                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return gift;
    }

    public LiveData<CoinModel> getCoin(final Activity activity){
        coin=new MutableLiveData<>();
        CommonUtils.showProgress(activity,"Loading...");
        if(CommonUtils.isNetworkConnected(activity)){
            apiInterface.getCoins().enqueue(new Callback<CoinModel>() {
                @Override
                public void onResponse(Call<CoinModel> call, Response<CoinModel> response) {
                    if(response.body()!=null){
                        CommonUtils.dismissProgress();
                        coin.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<CoinModel> call, Throwable t) {
                    CommonUtils.dismissProgress();
//                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return coin;
    }

    public LiveData<Map> giftSend(final Activity activity,String userId,String coin,String giftUserId,String giftId){
        giftSend=new MutableLiveData<>();
        CommonUtils.showProgress(activity,"Loading...");
        if(CommonUtils.isNetworkConnected(activity)){
            apiInterface.giftSend(userId, coin, giftUserId, giftId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    if(response.body()!=null){
                        CommonUtils.dismissProgress();
                        giftSend.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    CommonUtils.dismissProgress();
//                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return giftSend;
    }

    public LiveData<GIftHistoryModel> sentGift(final Activity activity,String userId,String type){
        sentgift=new MutableLiveData<>();
        CommonUtils.showProgress(activity,"Loading...");
        if(CommonUtils.isNetworkConnected(activity)){
            apiInterface.giftHistory(userId, type).enqueue(new Callback<GIftHistoryModel>() {
                @Override
                public void onResponse(Call<GIftHistoryModel> call, Response<GIftHistoryModel> response) {
                    CommonUtils.dismissProgress();
                    if(response.body()!=null){
                        sentgift.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<GIftHistoryModel> call, Throwable t) {
                    CommonUtils.dismissProgress();
//                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return sentgift;
    }

    public LiveData<GIftHistoryModel> receivedGift(final Activity activity,String userId,String type){
        receivedgift=new MutableLiveData<>();
        CommonUtils.showProgress(activity,"Loading...");
        if(CommonUtils.isNetworkConnected(activity)){
            apiInterface.giftHistory(userId, type).enqueue(new Callback<GIftHistoryModel>() {
                @Override
                public void onResponse(Call<GIftHistoryModel> call, Response<GIftHistoryModel> response) {
                    CommonUtils.dismissProgress();
                    if(response.body()!=null){
                        receivedgift.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<GIftHistoryModel> call, Throwable t) {
                    CommonUtils.dismissProgress();
//                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return receivedgift;
    }

    //Get Video List
    private MutableLiveData<ModelVideoTesting> videoTestingDAta;

    public LiveData<ModelVideoTesting> videoTesting(final Activity activity, String userId, String startLimit, String videoType, int anuradha, RelativeLayout relativeLayout, RecyclerView recyclerView, ProgressBar progressBar) {
        videoTestingDAta = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.videoTesting(userId, startLimit,videoType,anuradha).enqueue(new Callback<ModelVideoTesting>() {
                @Override
                public void onResponse(Call<ModelVideoTesting> call, Response<ModelVideoTesting> response) {
                    if (response.body() != null) {
                        Log.i("videoHome", response.body().getSuccess());
                        videoTestingDAta.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelVideoTesting> call, Throwable t) {
                    Log.i("videoHome", t.getMessage());
//                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            relativeLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }
        return videoTestingDAta;
    }


    //Login
    private MutableLiveData<Map> adminVideoDAta;

    public LiveData<Map> adminVideo(final Activity activity, String userId,String videoId,String type) {
        adminVideoDAta = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
//            CommonUtils.showProgress(activity, "Loading...");
            apiInterface.adminVideo(userId,videoId,type).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
//                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        adminVideoDAta.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
//                    CommonUtils.dismissProgress();
//                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show();
        }

        return adminVideoDAta;
    }

}
