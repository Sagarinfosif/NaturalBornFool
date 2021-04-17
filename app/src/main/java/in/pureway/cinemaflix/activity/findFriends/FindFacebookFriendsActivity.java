package in.pureway.cinemaflix.activity.findFriends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.javaClasses.FacebookLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FindFacebookFriendsActivity extends AppCompatActivity {
    private RecyclerView recyler_fb_friends;
    private List<String> fbList = new ArrayList<>();
    private CallbackManager callbackManager;
    FacebookLogin facebookLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_facebook_friends);
//        recyler_fb_friends = findViewById(R.id.recyler_fb_friends);
    }

//    private List<String> getFriendsList() {
//        final List<String> friendslist = new ArrayList<String>();
//        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/friends", null, HttpMethod.GET, new GraphRequest.Callback() {
//            public void onCompleted(GraphResponse response) {
//                /* handle the result */
////                Log.e(“Friends List: 1”, response.toString());
//                try {
//                    JSONObject responseObject = response.getJSONObject();
//                    JSONArray dataArray = responseObject.getJSONArray("data");
//
//                    for (int i = 0; i < dataArray.length(); i++) {
//                        JSONObject dataObject = dataArray.getJSONObject(i);
//                        String fbId = dataObject.getString("id");
//                        String fbName = dataObject.getString("name");
//                        Log.e("FbId", fbId);
//                        Log.e("FbName", fbName);
////                        friendslist.add(fbId);
//                        friendslist.add(fbName);
//                    }
//                    Log.e("fbfriendList", friendslist.toString());
//                    List<String> list = friendslist;
//                    String friends = "";
//                    if (list != null && list.size() > 0) {
//                        friends = list.toString();
//                        if (friends.contains("[")) {
//                            friends = (friends.substring(1, friends.length() - 1));
//                        }
//                    }
//                } catch (JSONException e) {
//                    Log.i("fbfriendList", e.getMessage());
//                    e.printStackTrace();
//                } finally {
////                    hideLoadingProgress();
//                }
//            }
//        }).executeAsync();
//        return friendslist;
//    }
//
    public class AdapterFacebookFriends extends RecyclerView.Adapter<AdapterFacebookFriends.ViewHolder> {

        List<String> list;

        public AdapterFacebookFriends(List<String> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(FindFacebookFriendsActivity.this).inflate(R.layout.list_fb_friends, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tv_fb_name.setText(list.get(position));
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_fb_name;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_fb_name = itemView.findViewById(R.id.tv_fb_name);
            }
        }
    }

}