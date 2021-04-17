package in.pureway.cinemaflix.activity.findFriends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.adapters.AdapterInviteFriends;
import in.pureway.cinemaflix.models.ModelInviteFriends;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class InviteFriendsActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rv_invite_friends;
    List<ModelInviteFriends> list = new ArrayList<>();
    private ImageView img_share;
    private EditText et_search_contacts;
    private List<ModelInviteFriends> searchList = new ArrayList<>();
    private AdapterInviteFriends adapterInviteFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(InviteFriendsActivity.this);
        setContentView(R.layout.activity_invite_friends);

        findIds();
        setRecycler();
        textWatcher();
    }

    private void textWatcher() {
        et_search_contacts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    searchContact(s.toString());
                } else {
                    setRecycler();
                }
            }
        });
    }

    private void searchContact(String toString) {
        if (searchList.size() > 0) {
            searchList.clear();
            if (adapterInviteFriends != null) {
                adapterInviteFriends.notifyDataSetChanged();
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getContactNo().contains(toString) || list.get(i).getContactName().toLowerCase().contains(toString)) {
                searchList.add(list.get(i));
            }
        }
        adapterInviteFriends = new AdapterInviteFriends(searchList, InviteFriendsActivity.this, new AdapterInviteFriends.Select() {
            @Override
            public void inviteFriend(int position) {
                CommonUtils.sendSmsInvite(searchList.get(position).getContactNo(), App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getUsername(), InviteFriendsActivity.this);
            }
        });
        rv_invite_friends.setAdapter(adapterInviteFriends);

    }

    private void setRecycler() {
        if (list.size() > 0) {
            list.clear();
            adapterInviteFriends.notifyDataSetChanged();
        }

        Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phone.moveToNext()) {
            String name = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactImage = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
            ModelInviteFriends contact = new ModelInviteFriends(name, phoneNumber, contactImage);
            list.add(contact);
        }
        phone.close();
        adapterInviteFriends = new AdapterInviteFriends(list, InviteFriendsActivity.this, new AdapterInviteFriends.Select() {
            @Override
            public void inviteFriend(int position) {
                CommonUtils.sendSmsInvite(list.get(position).getContactNo(), App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getUsername(), InviteFriendsActivity.this);
            }
        });
        rv_invite_friends.setAdapter(adapterInviteFriends);
    }

    private void findIds() {
        rv_invite_friends = findViewById(R.id.rv_invite_friends);
        et_search_contacts = findViewById(R.id.et_search_contacts);

    }

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}