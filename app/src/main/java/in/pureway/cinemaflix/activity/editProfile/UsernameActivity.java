package in.pureway.cinemaflix.activity.editProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.javaClasses.UpdateProfileApi;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

public class UsernameActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_username;
    String username = "";
    Pattern space = Pattern.compile("\\s+");
    boolean containsSpace;
    Pattern p = Pattern.compile("[^A-Za-z0-9]");//replace this with your needs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(UsernameActivity.this);
        setContentView(R.layout.activity_username);
        findIds();
        checkusername();
    }

    private void checkusername() {
        String username = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getUsername();
        if (username.contains("@")) {
            et_username.setText(charRemoveAt(username, 0));
        } else {
            et_username.setText(username);
        }
    }

    public static String charRemoveAt(String str, int p) {
        return str.substring(0, p) + str.substring(p + 1);
    }

    private void findIds() {
        findViewById(R.id.tv_save_username).setOnClickListener(this);
        et_username = findViewById(R.id.et_username);
    }

    public void backPress(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save_username:
                username = et_username.getText().toString().trim();
                Matcher matcherSpace = space.matcher(username);
                containsSpace = matcherSpace.find();
                Matcher m = p.matcher(username);

                boolean b = m.find();


                if (username.contains("@")) {
                    username = charRemoveAt(username, 0);
                }
                if (username.equalsIgnoreCase("") || username.isEmpty()) {
                    et_username.setError("Username cannot be empty");
                }  else if (containsSpace == true) {

                    et_username.setError("Username cannot be contain space");

                    //string contains space
                }
                else if (b == true) {

                    et_username.setError("Username cannot be contain special Character");

                }

                else {
                    UpdateProfileApi updateProfileApi = new UpdateProfileApi(UsernameActivity.this);
                    updateProfileApi.hitUpdateProfile("", username, "", "");
                }
                break;
        }
    }
}
