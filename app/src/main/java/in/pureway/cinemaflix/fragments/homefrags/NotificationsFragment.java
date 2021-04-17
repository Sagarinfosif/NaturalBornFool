package in.pureway.cinemaflix.fragments.homefrags;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.pureway.cinemaflix.activity.RejectedVideoShowActivity;
import in.pureway.cinemaflix.activity.SelectedVideoActivity;

import com.facebook.CallbackManager;
import com.google.android.material.card.MaterialCardView;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.WebLinkOpenActivity;
import in.pureway.cinemaflix.activity.login_register.LoginActivity;
import in.pureway.cinemaflix.activity.chat.NotificationMessagesActivity;
import in.pureway.cinemaflix.activity.OtherUserProfileActivity;
import in.pureway.cinemaflix.activity.login_register.RegisterActivity;
import in.pureway.cinemaflix.activity.videoEditor.DraftSelectedVideoActivity;
import in.pureway.cinemaflix.activity.videoEditor.activity.DraftsActivity;
import in.pureway.cinemaflix.adapters.AdapterNotificationsParent;
import in.pureway.cinemaflix.javaClasses.FacebookLogin;
import in.pureway.cinemaflix.javaClasses.FollowUnfollowUser;
import in.pureway.cinemaflix.javaClasses.GoogleLogin;
import in.pureway.cinemaflix.models.ModelNotifications;
import in.pureway.cinemaflix.mvvm.FollowMvvm;
import in.pureway.cinemaflix.mvvm.NotificationsMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment implements View.OnClickListener {
    private View view;
    private RecyclerView recycler_notifications;
    private ImageView img_speech, img_fb_login, img_google_login;
    private Button btn_signup, btn_signin;
    private LinearLayout ll_noti_logged_in;
    private RelativeLayout rl_not_logged_in, rl_not_badge, rl_msg_badge;
    private GoogleLogin googleLogin;
    private FacebookLogin facebookLogin;
    private CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 007;
    private NotificationsMvvm notificationsMvvm;
    private AdapterNotificationsParent adapterNotificationsParent;
    List<ModelNotifications.Detail> list = new ArrayList<>();
    private FollowMvvm followMvvm;
    private TextView tv_noti_badge_count, tv_msg_badge_count,tv_policyLink,tvTermLink;
    private int startLimit = 0;
    private MaterialCardView card_no_notifications;
    private String statusProgress = "0";

    public NotificationsFragment() {
// Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        notificationsMvvm = ViewModelProviders.of(NotificationsFragment.this).get(NotificationsMvvm.class);
        followMvvm = ViewModelProviders.of(NotificationsFragment.this).get(FollowMvvm.class);
        findids();
        checkLogin();
        return view;
    }

    private void dialogAdminMessage(String message) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setMessage("");
        builder.setMessage(message+" - From CINEMAFLIX.");
        builder.show();

    }

    private void checkLogin() {
        if (App.getSharedpref().isLogin(getActivity())) {
            ll_noti_logged_in.setVisibility(View.VISIBLE);
            rl_not_logged_in.setVisibility(View.GONE);
            setRecycler();
        } else {
            ll_noti_logged_in.setVisibility(View.GONE);
            rl_not_logged_in.setVisibility(View.VISIBLE);
        }
    }

    private void setRecycler() {


        notificationsMvvm.notifications(getActivity(), CommonUtils.userId(getActivity()), String.valueOf(startLimit)).observe(getActivity(), new Observer<ModelNotifications>() {
            @Override
            public void onChanged(ModelNotifications modelNotifications) {

                if (modelNotifications.getSuccess().equalsIgnoreCase("1")) {

                    recycler_notifications.setVisibility(View.VISIBLE);

                    Log.i("notifications", modelNotifications.getMessage());
                    list = modelNotifications.getDetails();
                    rl_not_badge.setVisibility(View.GONE);
                    String msgCount = modelNotifications.getMessageCount();
                    if (!msgCount.equalsIgnoreCase("0")) {
                        rl_msg_badge.setVisibility(View.VISIBLE);
                        tv_msg_badge_count.setText(msgCount);
                    }
                    adapterNotificationsParent = new AdapterNotificationsParent(getActivity(), list, new AdapterNotificationsParent.Select() {
                        @Override
                        public void followUser(int childposition, int parentPosition) {
                            followUnfollow(list.get(parentPosition).getListdetails().get(childposition).getLoginId(), childposition, parentPosition);
                        }

                        @Override
                        public void unfollowUser(int childposition, int parentPosition) {
                            followUnfollow(list.get(parentPosition).getListdetails().get(childposition).getLoginId(), childposition, parentPosition);
                        }

                        @Override
                        public void moveToProfile(int position, int childPosition) {
                            startActivity(new Intent(getActivity(), OtherUserProfileActivity.class).putExtra(AppConstants.OTHER_USER_ID, list.get(position).getListdetails().get(childPosition).getLoginId()));
                            getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                        }

                        @Override
                        public void moveToComment(int position, int childPosition, String videoId) {


                                Intent intent = new Intent(getActivity(), SelectedVideoActivity.class);
                                intent.putExtra(AppConstants.VIDEO, videoId);
//                            intent.putExtra(AppConstants.VIDEOPATH, videoPath);
                                intent.putExtra(AppConstants.POSITION, 0);
                                intent.putExtra(AppConstants.CHECK_VIDEO, 0);
                                intent.putExtra(AppConstants.NOTI_VIDEOS, true);
                                intent.putExtra(AppConstants.NOTI_COMMENT, true);
                                intent.putExtra(AppConstants.COMMENT_ID, "list.get(position).getListdetails().get(childPosition).getCommentId()");
                                intent.putExtra(AppConstants.SINGLE_VIDEO_TYPE, AppConstants.TYPE_NOTIFICATION_VIDEO);
                                intent.putExtra(AppConstants.VIDEOTYPENOTI, list.get(position).getListdetails().get(position).getType());
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

//                            startActivity(new Intent(getActivity(), OtherUserProfileActivity.class).putExtra(AppConstants.OTHER_USER_ID, list.get(position).getListdetails().get(childPosition).getLoginId()));
                                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);



                        }

                        @Override
                        public void moveToVideo(int position, int childPosition, String videoId){

                        String videoType = list.get(position).getListdetails().get(childPosition).getType().toString();

                        String videoPath = "";
                        if (videoType.equalsIgnoreCase(AppConstants.DELETEVIDEO) || videoType.equalsIgnoreCase(AppConstants.REJECTVIDEO)) {
//                                if (!list.get(position).getListdetails().get(position).getVideo().equalsIgnoreCase("")) {
                            videoPath = list.get(position).getListdetails().get(childPosition).getVideo();
//                            App.getSingleton().setListREjDelList(AppConstants.NotiRejDelList,list.get(position).getListdetails().get(childPosition));

                            Intent intent = new Intent(getActivity(), SelectedVideoActivity.class);
//                                    intent.putExtra(AppConstants.VIDEO, videoId);

                            intent.putExtra(AppConstants.VIDEOPATH, videoPath);
                            intent.putExtra(AppConstants.POSITION, 0);
                            intent.putExtra(AppConstants.CHECK_VIDEO, 0);
                            intent.putExtra(AppConstants.NOTI_VIDEOS, true);
//                            intent.putExtra("NotiRejDelList", list.get(position).getListdetails().get(childPosition));
//                                    intent.putExtra(AppConstants.NOTI_COMMENT, true);
//                                    intent.putExtra(AppConstants.COMMENT_ID, "list.get(position).getListdetails().get(childPosition).getCommentId()");
                                    intent.putExtra(AppConstants.SINGLE_VIDEO_TYPE, AppConstants.TYPE_NOTIFICATION_VIDEO);
                            intent.putExtra(AppConstants.VIDEOTYPENOTI, list.get(position).getListdetails().get(childPosition).getType());
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

//                            startActivity(new Intent(getActivity(), OtherUserProfileActivity.class).putExtra(AppConstants.OTHER_USER_ID, list.get(position).getListdetails().get(childPosition).getLoginId()));
                            getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

//                                }
                        } else {
                            Intent intent = new Intent(getActivity(), SelectedVideoActivity.class);
                            intent.putExtra(AppConstants.VIDEO, list.get(position).getListdetails().get(childPosition).getVideoId());
//                            intent.putExtra(AppConstants.VIDEOID, list.get(position).getListdetails().get(childPosition).getVideoId());
                            intent.putExtra(AppConstants.POSITION, 0);
//                            intent.putExtra(AppConstants.VIDEOPATH, list.get(position).getListdetails().get(childPosition).getVideo());
//                            intent.putExtra(AppConstants.VIDEOTYPENOTI, list.get(position).getListdetails().get(childPosition).getType());
                            intent.putExtra(AppConstants.VIDEOTYPENOTI, list.get(position).getListdetails().get(childPosition).getType());

                            intent.putExtra(AppConstants.CHECK_VIDEO, 0);
                            intent.putExtra(AppConstants.NOTI_VIDEOS, true);
                            intent.putExtra(AppConstants.SINGLE_VIDEO_TYPE, AppConstants.TYPE_NOTIFICATION_VIDEO);

                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

//                            startActivity(new Intent(getActivity(), SelectedVideoActivity.class).putExtra("notificationVideoId", videoId).putExtra(AppConstants.SINGLE_VIDEO_TYPE, AppConstants.TYPE_NOTIFICATION_VIDEO));
                        }
                    }

                        @Override
                        public void openDialog(int position, int childPosition) {
                            dialogAdminMessage(list.get(position).getListdetails().get(childPosition).getMessage());

                        }

                        @Override
                        public void RejectedVideoP(int position, String url) {
                            Log.i("draftUrl", "videoUrl: ==" + url);
                            startActivity(new Intent(getActivity(), RejectedVideoShowActivity.class).putExtra(AppConstants.REJECT_TATUS, "0")
                                    .putExtra(AppConstants.VIDEO_PATH, url));
                        }

                        @Override
                        public void DeletedVideoP(int position, String url) {
                            Log.i("draftUrl", "videoUrl: ==" + url);
                            startActivity(new Intent(getActivity(), RejectedVideoShowActivity.class)
                                    .putExtra(AppConstants.VIDEO_PATH, url));
                        }
                    });
                    recycler_notifications.setAdapter(adapterNotificationsParent);
                } else {
                    String msgCount = modelNotifications.getMessageCount();
                    if (!msgCount.equalsIgnoreCase("0")) {
                        rl_msg_badge.setVisibility(View.VISIBLE);
                        tv_msg_badge_count.setText(msgCount);
                    }
                    rl_not_badge.setVisibility(View.GONE);
                    card_no_notifications.setVisibility(View.VISIBLE);
//                    recycler_notifications.setVisibility(View.GONE);

                    Log.i("notifications", modelNotifications.getMessage());
                }
            }
        });
    }

    private void findids() {
        tvTermLink = view.findViewById(R.id.tvTermLink);


        tv_policyLink = view.findViewById(R.id.tv_policyLink);


        rl_msg_badge = view.findViewById(R.id.rl_msg_badge);
        tv_msg_badge_count = view.findViewById(R.id.tv_msg_badge_count);
        rl_not_badge = getActivity().findViewById(R.id.rl_not_badge);
        tv_noti_badge_count = getActivity().findViewById(R.id.tv_noti_badge_count);
        card_no_notifications = view.findViewById(R.id.card_no_notifications);
        rl_not_logged_in = view.findViewById(R.id.rl_not_logged_in);
        ll_noti_logged_in = view.findViewById(R.id.ll_noti_logged_in);
        img_speech = view.findViewById(R.id.img_speech);
        recycler_notifications = view.findViewById(R.id.recycler_notifications);
        view.findViewById(R.id.rl_message).setOnClickListener(this);
        view.findViewById(R.id.btn_signin).setOnClickListener(this);
        view.findViewById(R.id.btn_signup).setOnClickListener(this);
        view.findViewById(R.id.btn_SocialMail).setOnClickListener(this);
        view.findViewById(R.id.btn_facebook).setOnClickListener(this);
        img_speech.setOnClickListener(this);
        tvTermLink.setOnClickListener(this);
        tv_policyLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_message:
            case R.id.img_speech:
                startActivity(new Intent(getActivity(), NotificationMessagesActivity.class));
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;

            case R.id.btn_facebook:
                callbackManager = CallbackManager.Factory.create();
                facebookLogin = new FacebookLogin(getActivity(), AppConstants.LOGIN_NOTI, getActivity().getApplication());
                facebookLogin.FBLogin();
                break;

            case R.id.btn_SocialMail:
                App.getSingleton().setLoginType(AppConstants.LOGIN_NOTI);
                googleLogin = new GoogleLogin(getActivity(), AppConstants.LOGIN_NOTI);
                googleLogin.signIn();
                break;

            case R.id.btn_signup:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                break;

            case R.id.btn_signin:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
                case R.id.tv_policyLink:

                    Intent intent=new Intent(getActivity(), WebLinkOpenActivity.class);
                    intent.putExtra("key","1");
                    intent.putExtra("title","CINEMAFLIX Privacy Policy");
                    startActivity(intent);
                break;

                case R.id.tvTermLink:

                    Intent intent1=new Intent(getActivity(), WebLinkOpenActivity.class);
                    intent1.putExtra("key","0");
                    intent1.putExtra("title","Terms and Conditions");
                    startActivity(intent1);

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            googleLogin.activityResult(requestCode, resultCode, data);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void followUnfollow(String otherId, final int position, final int parentPosition) {
        FollowUnfollowUser followUnfollowUser = new FollowUnfollowUser(getActivity(), new FollowUnfollowUser.followCallBack() {
            @Override
            public void followInterfaceCall(Map map) {
                if (map.get("success").equals("1")) {
                    if (map.get("following_status").equals(true)) {
                        Log.i("follow", String.valueOf(true));
                        broadcastFollow(AppConstants.FOLLOW, position, parentPosition);
                    } else if (map.get("following_status").equals(false)) {
                        Log.i("follow", String.valueOf(false));
                        broadcastFollow(AppConstants.UNFOLLOW, position, parentPosition);
                    }
                } else {
                    Log.i("follow", map.get("message").toString());
                }
            }
        });
        followUnfollowUser.followUnfollow(otherId);
    }

    private void broadcastFollow(String value, int position, int parentPosition) {
        Intent intent = new Intent(AppConstants.FOLLOW_UNFOLLOW);
        intent.putExtra(AppConstants.POSITION, position);
        intent.putExtra(AppConstants.NOTI_FOLLOW, value);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }
}