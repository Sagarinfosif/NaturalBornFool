package in.pureway.cinemaflix.activity.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.AdapterNotiMessages;
import in.pureway.cinemaflix.models.ModelInbox;
import in.pureway.cinemaflix.mvvm.MessageMvvm;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificationMessagesActivity extends AppCompatActivity {
    private RecyclerView rv_notifications_messages;
    private String senderId;
    private TextView noMessagesTV;
    private MessageMvvm messageMvvm;
    private List<ModelInbox.Detail> list = new ArrayList<>();
    private AdapterNotiMessages adapterNotiMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(NotificationMessagesActivity.this);
        setContentView(R.layout.activity_notification_messages);
        messageMvvm = ViewModelProviders.of(this).get(MessageMvvm.class);
        senderId = CommonUtils.userId(this);
        findIds();
        getInboxAPI();
    }

    private void getInboxAPI() {
        messageMvvm.inbox(this, senderId).observe(this, new Observer<ModelInbox>() {
            @Override
            public void onChanged(final ModelInbox modelInbox) {
                if (modelInbox.getSuccess().equalsIgnoreCase("1")) {
                    list = modelInbox.getDetails();
                    adapterNotiMessages = new AdapterNotiMessages(NotificationMessagesActivity.this, list, new AdapterNotiMessages.Select() {
                        @Override
                        public void goToMesssage(int position) {
                            Intent intent = new Intent(NotificationMessagesActivity.this, MessageActivity.class);
                            if (CommonUtils.userId(NotificationMessagesActivity.this).equalsIgnoreCase(modelInbox.getDetails().get(position).getReciverId())) {
                                intent.putExtra(AppConstants.OTHER_USER_ID, modelInbox.getDetails().get(position).getSenderId());
                            } else if (CommonUtils.userId(NotificationMessagesActivity.this).equalsIgnoreCase(modelInbox.getDetails().get(position).getSenderId())) {
                                intent.putExtra(AppConstants.OTHER_USER_ID, modelInbox.getDetails().get(position).getReciverId());
                            }
                            if (modelInbox.getDetails().get(position).getName().equalsIgnoreCase("")) {
                                intent.putExtra(AppConstants.OTHER_USER_NAME, modelInbox.getDetails().get(position).getUsername());
                            } else {
                                intent.putExtra(AppConstants.OTHER_USER_NAME, modelInbox.getDetails().get(position).getName());
                            }
                            intent.putExtra(AppConstants.OTHER_USER_IMAGE, modelInbox.getDetails().get(position).getImage());
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                        }

                        @Override
                        public void deleteChat(String receiverId,String chatId ,int position, View view) {
                            deleteMessageMenu(position, view, receiverId,chatId);
                        }
                    });
                    rv_notifications_messages.setAdapter(adapterNotiMessages);
                } else {
                    noMessagesTV.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void deleteChatApi(final String chatId, final int position) {
        String userId = CommonUtils.userId(NotificationMessagesActivity.this);
        messageMvvm.deleteChat(NotificationMessagesActivity.this, userId, chatId).observe(NotificationMessagesActivity.this, new Observer<ModelInbox>() {
            @Override
            public void onChanged(ModelInbox modelInbox) {
                if (modelInbox.getSuccess().equalsIgnoreCase("1")) {
                    list.remove(position);
                    adapterNotiMessages.notifyDataSetChanged();
                    Log.i("deleteMessage", "message Deleted:" + chatId);
                } else {
                    Log.i("deleteMessage", modelInbox.getMessage() + chatId);
                }
            }
        });
    }

    private void findIds() {
        rv_notifications_messages = findViewById(R.id.rv_notifications_messages);
        noMessagesTV = findViewById(R.id.noMessagesTV);
    }

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    //TODO replace with popupWIndow
    private void deleteMessageMenu(final int position, View view, final String receiverId,String chatId) {
        PopupMenu popupMenu = new PopupMenu(NotificationMessagesActivity.this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_delete_comment, popupMenu.getMenu());
        MenuItem tv_delete = popupMenu.getMenu().findItem(R.id.delete_comment);
        tv_delete.setTitle(R.string.delete_chat);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_comment:
                        deleteChatApi(chatId, position);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }
}