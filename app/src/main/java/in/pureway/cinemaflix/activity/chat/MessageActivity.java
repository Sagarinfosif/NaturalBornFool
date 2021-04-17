package in.pureway.cinemaflix.activity.chat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.OtherUserProfileActivity;
import in.pureway.cinemaflix.adapters.AdapterMessages;
import in.pureway.cinemaflix.models.ModelInbox;
import in.pureway.cinemaflix.models.ModelSendMessage;
import in.pureway.cinemaflix.mvvm.MessageMvvm;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rv_message;
    private String receiverId = "", message = "", senderId = "", otherUserName = "", path = "", messageType = "";
    private EditText messageET;
    private String imagepath = "";
    private TextView chatUserNameTV, noConversationFoundTV;
    private ImageView sendIV, imageIV;
    private MessageMvvm messageMvvm;
    private List<ModelSendMessage.Detail> list = new ArrayList<>();
    private List<ModelSendMessage.Detail> newList = new ArrayList<>();
    private AdapterMessages adapterMessages;
    final Handler handler = new Handler();
    private String userId = CommonUtils.userId(MessageActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(MessageActivity.this);
        setContentView(R.layout.activity_message);
        messageMvvm = ViewModelProviders.of(MessageActivity.this).get(MessageMvvm.class);
        receiverId = getIntent().getExtras().getString(AppConstants.OTHER_USER_ID);
        otherUserName = getIntent().getExtras().getString(AppConstants.OTHER_USER_NAME);
        path = getIntent().getExtras().getString(AppConstants.OTHER_USER_IMAGE);
        senderId = userId;
        Log.i("receiverId", receiverId);
        findIds();
        chatUserNameTV.setText(otherUserName);
        getConversationAPI();
// handler.postDelayed(runnable, 5000);
    }

    private void getConversationAPI() {
        messageMvvm.getConversation(this, senderId, receiverId).observe(this, new Observer<ModelSendMessage>() {
            @Override
            public void onChanged(ModelSendMessage modelSendMessage) {
                if (modelSendMessage.getSuccess().equalsIgnoreCase("1")) {
                    list = modelSendMessage.getDetails();
                    if (list.size() > 0) {
                        adapterMessages = new AdapterMessages(MessageActivity.this, list, path, new AdapterMessages.Select() {
                            @Override
                            public void deleteMessage(int position, String messageId, View view) {
                                deleteMessageMenu(position, view, messageId);
                            }

                            @Override
                            public void moveToProfile(int position) {
                                Intent intent = new Intent(MessageActivity.this, OtherUserProfileActivity.class);
                                String otherUserid = list.get(position).getReciverId();
                                if (list.get(position).getReciverId().equalsIgnoreCase(userId)) {
                                    otherUserid = list.get(position).getSenderId();
                                }
                                intent.putExtra(AppConstants.OTHER_USER_ID, otherUserid);
                                startActivity(intent);
                            }

                            @Override
                            public void showImage(Drawable drawable) {
                                openImageDialog(drawable);
                            }
                        });
                        rv_message.setAdapter(adapterMessages);
                        adapterMessages.notifyDataSetChanged();
                        rv_message.scrollToPosition(list.size() - 1);
                    } else {
                        noConversationFoundTV.setVisibility(View.VISIBLE);
                    }
                } else {
                    adapterMessages = new AdapterMessages(MessageActivity.this, list, path, new AdapterMessages.Select() {
                        @Override
                        public void deleteMessage(int position, String messageId, View view) {
                            deleteMessageMenu(position, view, messageId);
                        }

                        @Override
                        public void moveToProfile(int position) {
                            Intent intent = new Intent(MessageActivity.this, OtherUserProfileActivity.class);
                            String otherUserid = list.get(position).getReciverId();
                            if (list.get(position).getReciverId().equalsIgnoreCase(userId)) {
                                otherUserid = list.get(position).getSenderId();
                            }
                            intent.putExtra(AppConstants.OTHER_USER_ID, otherUserid);
                            startActivity(intent);
                        }

                        @Override
                        public void showImage(Drawable drawable) {
                            openImageDialog(drawable);
                        }
                    });
                    rv_message.setAdapter(adapterMessages);
                    adapterMessages.notifyDataSetChanged();
                }
            }
        });
    }

    private void deleteMessageApi(final int position, String messageId) {
        messageMvvm.deleteMessage(MessageActivity.this, userId, messageId).observe(MessageActivity.this, new Observer<ModelInbox>() {
            @Override
            public void onChanged(ModelInbox modelInbox) {
                if (modelInbox.getSuccess().equalsIgnoreCase("1")) {
                    list.remove(position);
                    adapterMessages.notifyDataSetChanged();
                    Log.i("conversationData", modelInbox.getMessage());
                } else {
                    Log.i("conversationData", modelInbox.getMessage());
                }
            }
        });
    }

    private void findIds() {
        messageET = findViewById(R.id.messageET);
        chatUserNameTV = findViewById(R.id.chatUserNameTV);
        rv_message = findViewById(R.id.rv_message);
        noConversationFoundTV = findViewById(R.id.noConversationFoundTV);
        sendIV = findViewById(R.id.sendIV);
        imageIV = findViewById(R.id.imageIV);
        chatUserNameTV.setOnClickListener(this);
        sendIV.setOnClickListener(this);
        imageIV.setOnClickListener(this);
        messageET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    validate();
                    handled = true;
                }
                return handled;
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void validate() {
        message = messageET.getText().toString().trim();
        if (message.isEmpty()) {
            Toast.makeText(this, "Please enter message", Toast.LENGTH_SHORT).show();
        } else {
            senMessageApi("1");
        }
    }

    private void senMessageApi(String messageType) {
        RequestBody rb_senderId = RequestBody.create(MediaType.parse("text/plain"), senderId);
        RequestBody rb_receiverId = RequestBody.create(MediaType.parse("text/plain"), receiverId);
        RequestBody rb_message = RequestBody.create(MediaType.parse("text/plain"), message);
        RequestBody rb_messageType = RequestBody.create(MediaType.parse("text/plain"), messageType);
        MultipartBody.Part rb_image;
        if (!imagepath.isEmpty()) {
            File file = new File(imagepath);
            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            rb_image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        } else {
            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            rb_image = MultipartBody.Part.createFormData("image", "", requestFile);
        }
        messageMvvm.sendMessage(this, rb_senderId, rb_receiverId, rb_message, rb_messageType, rb_image).observe(this, new Observer<ModelSendMessage>() {
            @Override
            public void onChanged(ModelSendMessage modelSendMessage) {
                if (modelSendMessage.getSuccess().equalsIgnoreCase("1")) {
                    Log.i("messages:before", String.valueOf(list.size()));
                    messageET.setText("");
                    ModelSendMessage.Detail modelSendMessage1 = new ModelSendMessage.Detail();
                    modelSendMessage1.setId(modelSendMessage.getDetails().get(0).getId());
                    modelSendMessage1.setMessageType(modelSendMessage.getDetails().get(0).getMessageType());
                    modelSendMessage1.setReciverId(modelSendMessage.getDetails().get(0).getReciverId());
                    modelSendMessage1.setSenderId(modelSendMessage.getDetails().get(0).getSenderId());
                    modelSendMessage1.setCreated(modelSendMessage.getDetails().get(0).getCreated());
                    modelSendMessage1.setTime(modelSendMessage.getDetails().get(0).getTime());
                    modelSendMessage1.setMessage(message);
                    modelSendMessage1.setImage(imagepath);
                    list.add(modelSendMessage1);
                    noConversationFoundTV.setVisibility(View.GONE);
                    adapterMessages.notifyItemInserted(list.size() - 1);
                    rv_message.scrollToPosition(list.size() - 1);
                    Log.i("messages:after", String.valueOf(list.size()));
                    if (modelSendMessage.getDetails().get(0).getMessageType().equalsIgnoreCase("1")) {
                        hideKeyboard(MessageActivity.this);
                    }
                } else {
                    Toast.makeText(MessageActivity.this, modelSendMessage.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String sender = intent.getExtras().getString(AppConstants.OTHER_USER_ID);
            if (sender.equalsIgnoreCase(receiverId)) {
                ModelSendMessage.Detail detail = (ModelSendMessage.Detail) intent.getExtras().getSerializable(AppConstants.MESSAGE_RESPOSNE);
                list.add(detail);
                adapterMessages.notifyItemInserted(list.size() - 1);
                rv_message.scrollToPosition(list.size() - 1);
                Log.i("notificationMessage:", "Received from:" + sender + " Message:" + detail.getMessage());
            }
            Log.i("messageNotification", "received");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(MessageActivity.this).unregisterReceiver(messageReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(MessageActivity.this).registerReceiver(messageReceiver, new IntentFilter(AppConstants.ACTION_MESSAGE_NOTIFICATION));
    }

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chatUserNameTV:

                break;
            case R.id.sendIV:
                messageType = "1";
                validate();
                break;

            case R.id.imageIV:
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//image
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    imagepath = resultUri.getPath();
                    if (!imagepath.equalsIgnoreCase("")) {
                        messageType = "2";
                        senMessageApi("2");
                    } else {
                        messageType = "1";
                    }
                } else {
                    Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LocalBroadcastManager.getInstance(MessageActivity.this).unregisterReceiver(messageReceiver);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    //TODO replace with popupWIndow
    private void deleteMessageMenu(final int position, View view, final String messageId) {
        PopupMenu popupMenu = new PopupMenu(MessageActivity.this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_delete_comment, popupMenu.getMenu());
        MenuItem tv_delete = popupMenu.getMenu().findItem(R.id.delete_comment);
        tv_delete.setTitle(R.string.delete_message);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_comment:
                        deleteMessageApi(position, messageId);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void openImageDialog(Drawable drawable) {
        final Dialog dialog;
        LayoutInflater layoutInflater = LayoutInflater.from(MessageActivity.this);
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_show_code, null);
        dialog = new Dialog(MessageActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setCancelable(true);
        dialog.setContentView(confirmdailog);
        ImageView img_user = confirmdailog.findViewById(R.id.img_user);
        ImageView img_gif_user = confirmdailog.findViewById(R.id.img_gif_user);

        Glide.with(MessageActivity.this).load(drawable).placeholder(R.drawable.poat).into(img_user);

        confirmdailog.findViewById(R.id.img_back_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}