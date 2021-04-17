package in.pureway.cinemaflix.mvvm;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import in.pureway.cinemaflix.models.ModelInbox;
import in.pureway.cinemaflix.models.ModelSendMessage;
import in.pureway.cinemaflix.retrofit.ApiClient;
import in.pureway.cinemaflix.retrofit.ApiInterface;
import in.pureway.cinemaflix.utils.CommonUtils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageMvvm extends ViewModel {

    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    // Send Message
    private MutableLiveData<ModelSendMessage> sendMessageData;

    public LiveData<ModelSendMessage> sendMessage(final Activity activity, RequestBody sender_id, RequestBody reciver_id, RequestBody message, RequestBody messageType, MultipartBody.Part image) {
        sendMessageData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.sendMessage(sender_id, reciver_id, message, messageType, image).enqueue(new Callback<ModelSendMessage>() {
                @Override
                public void onResponse(Call<ModelSendMessage> call, Response<ModelSendMessage> response) {
                    if (response.body() != null) {
                        Log.i("sendMessage", response.body().getMessage());
                        sendMessageData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelSendMessage> call, Throwable t) {
                    Log.i("sendMessage", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "Please Check your internet connection", Toast.LENGTH_SHORT).show();
        }
        return sendMessageData;
    }


    // Conversation
    private MutableLiveData<ModelSendMessage> conversationData;

    public LiveData<ModelSendMessage> getConversation(final Activity activity, String sender_id, final String reciver_id) {
        conversationData = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.conversation(sender_id, reciver_id).enqueue(new Callback<ModelSendMessage>() {
                @Override
                public void onResponse(Call<ModelSendMessage> call, Response<ModelSendMessage> response) {
                    if (response.body() != null) {
                        Log.i("conversationData", response.body().getMessage());
                        conversationData.postValue(response.body());
                    } else {
                        Toast.makeText(activity, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ModelSendMessage> call, Throwable t) {
                    Log.i("conversationData", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "Please Check your internet connection", Toast.LENGTH_SHORT).show();
        }

        return conversationData;
    }


    // Inbox
    private MutableLiveData<ModelInbox> inboxData;

    public LiveData<ModelInbox> inbox(final Activity activity, String sender_id) {
        inboxData = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.inbox(sender_id).enqueue(new Callback<ModelInbox>() {
                @Override
                public void onResponse(Call<ModelInbox> call, Response<ModelInbox> response) {
                    if (response.body() != null) {
                        Log.i("inboxData", response.body().getMessage());
                        inboxData.postValue(response.body());

                    } else {
                        Toast.makeText(activity, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ModelInbox> call, Throwable t) {
                    Log.i("inboxData", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "Please Check your internet connection", Toast.LENGTH_SHORT).show();
        }

        return inboxData;
    }

    //delete chat
    private MutableLiveData<ModelInbox> deleteChatData;

    public LiveData<ModelInbox> deleteChat(final Activity activity, String userId, String inboxId) {
        deleteChatData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "Deleting...");
            apiInterface.deleteChat(userId, inboxId).enqueue(new Callback<ModelInbox>() {
                @Override
                public void onResponse(Call<ModelInbox> call, Response<ModelInbox> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        Log.i("deletechat", response.body().getMessage());
                        deleteChatData.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ModelInbox> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Log.i("deletechat", t.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, "no Internet", Toast.LENGTH_SHORT).show();
        }
        return deleteChatData;
    }

    //delete message
    private MutableLiveData<ModelInbox> deleteMessageData;

    public LiveData<ModelInbox> deleteMessage(final Activity activity, final String userId,String conversationId) {
        deleteMessageData = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {
            apiInterface.deleteMessage(userId,conversationId).enqueue(new Callback<ModelInbox>() {
                @Override
                public void onResponse(Call<ModelInbox> call, Response<ModelInbox> response) {
                    if (response.body() != null) {
                        deleteMessageData.postValue(response.body());
                        Log.i("deleteMessage", response.body().getMessage() + " messageId:" + conversationId);
                    }
                }

                @Override
                public void onFailure(Call<ModelInbox> call, Throwable t) {
                    Log.i("deleteMessage", t.getMessage() + " messageId:" + conversationId);
                }
            });
        } else {
            Toast.makeText(activity, "no Internet", Toast.LENGTH_SHORT).show();
        }
        return deleteMessageData;
    }
}
