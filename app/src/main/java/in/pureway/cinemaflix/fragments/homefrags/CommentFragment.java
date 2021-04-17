package in.pureway.cinemaflix.fragments.homefrags;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;

import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.HomeActivity;
import in.pureway.cinemaflix.activity.OtherUserProfileActivity;
import in.pureway.cinemaflix.adapters.AdapterComments;
import in.pureway.cinemaflix.models.ModelComments;
import in.pureway.cinemaflix.mvvm.VideoMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CommentFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private View view;
    String videoId;
    private VideoMvvm videoMvvm;
    private List<ModelComments.Detail> list = new ArrayList<>();
    private RecyclerView rv_comments;
    private EditText et_add_comment;
    private ImageView img_send_comment;
    private AdapterComments adapterComments;
    String comments = "0";
    String comments1;
    private RelativeLayout rl_comments;
    private MaterialCardView card_no_comments;
    private String commentId, videoID, ownerId;
    private int position;
    private CardView card_private;
    private BottomClick bottomClick;
    private LinearLayout ll_bottom_sheet;
private BottomSheetBehavior bottomSheetBehavior;
    public CommentFragment() {

    }


    public interface BottomClick {
        void position(String status);
    }


    public CommentFragment(String videoId, String comments1, String ownerId, int position, BottomClick bottomClick) {
        this.videoId = videoId;
        this.comments = comments;
        this.comments1 = comments1;
        this.ownerId = ownerId;
        this.position = position;
        this.bottomClick = bottomClick;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_comment, container, false);
        videoMvvm = ViewModelProviders.of(CommentFragment.this).get(VideoMvvm.class);
        findIDs();
        // displaySize(getActivity());
        getComments();

        if (!comments1.equalsIgnoreCase("1")) {
            rl_comments.setVisibility(View.GONE);

//            tv_private.setVisibility(View.VISIBLE);
            card_private.setVisibility(View.VISIBLE);
        }

//        if (!App.getSharedpref().isLogin(getActivity())) {
//            rl_comments.setVisibility(View.GONE);
//        }

        return view;
    }

    private void findIDs() {
        ll_bottom_sheet = view.findViewById(R.id.ll_bottom_sheet);
        card_private = view.findViewById(R.id.card_private);
        card_no_comments = view.findViewById(R.id.card_no_comments);
        img_send_comment = view.findViewById(R.id.img_send_comment);
        rl_comments = view.findViewById(R.id.rl_comments);
        et_add_comment = view.findViewById(R.id.et_add_comment);
        rv_comments = view.findViewById(R.id.rv_comments);

        img_send_comment.setOnClickListener(this);


    }

    private void getComments() {
        videoMvvm.getComments(getActivity(), CommonUtils.userId(getActivity()), videoId).observe(getActivity(), new Observer<ModelComments>() {
            @Override
            public void onChanged(ModelComments modelComments) {
                if (modelComments.getSuccess().equalsIgnoreCase("1")) {
                    list = modelComments.getDetails();
                    if (list.size() == 0) {
                        rv_comments.setVisibility(View.GONE);
                        card_no_comments.setVisibility(View.VISIBLE);
                        return;
                    }
                } else {
                    Log.i("comments", modelComments.getMessage());
                }

                adapterComments = new AdapterComments(getActivity(), list, new AdapterComments.SelectComment() {
                    @Override
                    public void choose(int position) {
                        if (list.get(position).getUserId().equalsIgnoreCase(CommonUtils.userId(getActivity()))) {
                            startActivity(new Intent(getActivity(), HomeActivity.class).putExtra("fragment", AppConstants.VIDEO_POST));
                        } else {
                            startActivity(new Intent(getActivity(), OtherUserProfileActivity.class).putExtra(AppConstants.OTHER_USER_ID, list.get(position).getUserId()));
                            getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                        }
                    }

                    @Override
                    public void likeComment(int position) {
                        likeuserComment(position);
                    }

                    @Override
                    public void deleteCommentLongPress(int position, View view) {
                        if (list.get(position).getUserId().equalsIgnoreCase(CommonUtils.userId(getActivity()))) {
                            deletePopMenu(position, view);
                        } else {

                        }

                    }

                    @Override
                    public void commentReplyClick(int position) {
                        et_add_comment.requestFocus();
                        rv_comments.scrollToPosition(position);
                    }

                    @Override
                    public void commentReplyVisible(int position, String comment) {
                        videoID = list.get(position).getVideoId();
                        commentId = list.get(position).getId();
                        subComment(commentId, comment, videoID, position);
                    }

                    @Override
                    public void viewAll(int position) {
                        // Intent intent = new Intent(getActivity(), ViewAllCommentActivity.class);
                        // startActivity(intent);
                    }
                });
                rv_comments.setVisibility(View.VISIBLE);
                et_add_comment.requestFocus();
//                try {
//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.showSoftInput(et_add_comment, InputMethodManager.SHOW_IMPLICIT);
                rv_comments.setAdapter(adapterComments);

//                }catch (Exception e){
//                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
//                }


            }
        });
    }

    private void subComment(String commentId, String comment, String videoID, final int position) {
        videoMvvm.commentReply(getActivity(), CommonUtils.userId(getActivity()), videoID, commentId, comment).observe(CommentFragment.this, new Observer<ModelComments>() {
            @Override
            public void onChanged(ModelComments modelComments) {
                if (modelComments.getSuccess().equalsIgnoreCase("1")) {
                    Toast.makeText(getContext(), modelComments.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("subComment", "commented");
                    list.set(position, modelComments.getDetails().get(0));
                    adapterComments.notifyDataSetChanged();
                    sendCommentCountBroadcast(modelComments.getCommentCount());
                    hideKeyboard();
                } else {
                    Toast.makeText(getContext(), modelComments.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_send_comment:
                addComment();
                img_send_comment.setEnabled(false);
                disableButton();
                break;
        }
    }

    private void addComment() {

        if (App.getSharedpref().isLogin(getActivity())) {
            String comment = et_add_comment.getText().toString();
            if (TextUtils.isEmpty(comment)) {
                Toast.makeText(getActivity(), "Comment null", Toast.LENGTH_SHORT).show();
            } else {
                videoMvvm.addComments(getActivity(), CommonUtils.userId(getActivity()), videoId, comment, ownerId).observe(getActivity(), new Observer<ModelComments>() {
                    @Override
                    public void onChanged(ModelComments modelComments) {
                        if (modelComments.getSuccess().equalsIgnoreCase("1")) {
                            Log.i("comments : added", modelComments.getMessage());
                            list.add(modelComments.getDetails().get(0));
                            adapterComments.notifyDataSetChanged();
                            et_add_comment.setText(null);
                            et_add_comment.clearFocus();
                            et_add_comment.setCursorVisible(false);
                            rv_comments.scrollToPosition(list.size());
                            sendCommentCountBroadcast(modelComments.getCommentCount());
                            hideKeyboard();
                        } else {
                            Log.i("comments", modelComments.getMessage());
                        }
                    }
                });
            }
        } else {
//            ll_bottom_sheet.setVisibility(View.GONE);
            hideKeyboard();
            bottomClick.position("1");
        }

    }

    private void disableButton() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                img_send_comment.setEnabled(true);
            }
        }, 1500);
    }

    private void likeuserComment(final int videoPosition) {
        videoMvvm.likeComment(getActivity(), list.get(videoPosition).getId(), CommonUtils.userId(getActivity())).observe(getActivity(), new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    Log.i("comments", "comment liked");
                    Intent intent = new Intent(AppConstants.COMMENTS);
                    intent.putExtra(AppConstants.COMMENT_LIKE_COUNT, map.get("likeCount").toString());
                    intent.putExtra("position", videoPosition);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                } else {
                    Log.i("comments", "comment not liked");
                }
            }
        });
    }

    private static String displaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Log.i("height", String.valueOf(height));
        Log.i("width", String.valueOf(width));
        return "{" + width + "," + height + "}";
    }

    private void deletePopMenu(final int position, View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_delete_comment, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_comment:
                        deleteComment(position);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void deleteComment(final int position) {
        String commentId = list.get(position).getId();
        videoMvvm.deleteComment(getActivity(), commentId).observe(getActivity(), new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").toString().equalsIgnoreCase("1")) {
                    Log.i("comment", map.get("message").toString());
                    list.remove(position);
                    adapterComments.notifyDataSetChanged();
                } else {
                    Log.i("comment", map.get("message").toString());
                }
            }
        });
    }

    private void sendCommentCountBroadcast(String commentCount) {
        Intent intent = new Intent(AppConstants.COMMENT_COUNT_ACTION);
        intent.putExtra(AppConstants.POSITION, position);
        intent.putExtra(AppConstants.COMMENT_COUNT, commentCount);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}