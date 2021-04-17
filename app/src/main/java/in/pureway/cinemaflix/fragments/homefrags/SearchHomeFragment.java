package in.pureway.cinemaflix.fragments.homefrags;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.HashTagVideoActivity;
import in.pureway.cinemaflix.activity.login_register.LoginActivity;
import in.pureway.cinemaflix.activity.OtherUserProfileActivity;
import in.pureway.cinemaflix.activity.login_register.RegisterActivity;
import in.pureway.cinemaflix.activity.SelectedVideoActivity;
import in.pureway.cinemaflix.activity.SoundVideoActivity;
import in.pureway.cinemaflix.adapters.AdapterSearchCategories;
import in.pureway.cinemaflix.adapters.AdapterSearchHashtagsRj;
import in.pureway.cinemaflix.adapters.AdapterSearchPeopleRj;
import in.pureway.cinemaflix.adapters.AdapterSearchSoundRj;
import in.pureway.cinemaflix.adapters.AdapterSearchVideosRj;
import in.pureway.cinemaflix.javaClasses.FacebookLogin;
import in.pureway.cinemaflix.javaClasses.FollowUnfollowUser;
import in.pureway.cinemaflix.javaClasses.GoogleLogin;
import in.pureway.cinemaflix.models.ModelFavoriteSounds;
import in.pureway.cinemaflix.models.SearchAllPojo;
import in.pureway.cinemaflix.mvvm.FollowMvvm;
import in.pureway.cinemaflix.mvvm.SoundsMvvm;
import in.pureway.cinemaflix.mvvm.VideoMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchHomeFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recycler_search_home;
    private View view;
    private RelativeLayout rl_not_logged_in;
    private LinearLayout ll_search_logged_in;
    private static final int RC_SIGN_IN = 007;
    private GoogleLogin googleLogin;
    private FacebookLogin facebookLogin;
    private CallbackManager callbackManager;
    private FollowMvvm followMvvm;
    public EditText et_search;
    private AdapterSearchCategories adapterSearchCategories;
    private RecyclerView rv_searchTags, rv_videos_search, rv_hashTags, rv_peoples, rv_sounds;
    private String searchTag = "0";
    private VideoMvvm videoMvvm;
    private NestedScrollView nestedSearch;
    private AdapterSearchPeopleRj adapterSearchPeopleRj;
    private AdapterSearchHashtagsRj adapterSearchHashtagsRj;
    private AdapterSearchVideosRj adapterSearchVideosRj;
    private AdapterSearchSoundRj adapterSearchSoundRj;
    private LinearLayout trendingLinearLayout, hashTagsLinearLayout, peopleLinearLayout, soundsLinearLayout;
    private String userId;
    private TextView totalVideoTV;
    private ShimmerFrameLayout shimmerSearch;

    public SearchHomeFragment() {
    // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_home, container, false);
        followMvvm = ViewModelProviders.of(getActivity()).get(FollowMvvm.class);
        findIds();
        if (App.getSharedpref().isLogin(getActivity())) {
            userId = CommonUtils.userId(getActivity());
        } else {
            userId = "0";
        }
        searchAll("");
        getSearchCate();
//        checkLogin();

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    searchAll(s.toString());
                } else {
                    searchAll("");
                }
            }
        });
        return view;
    }

    private void checkLogin() {
        if (App.getSharedpref().isLogin(getActivity())) {
            rl_not_logged_in.setVisibility(View.GONE);
            ll_search_logged_in.setVisibility(View.VISIBLE);
        } else {
            rl_not_logged_in.setVisibility(View.VISIBLE);
            ll_search_logged_in.setVisibility(View.GONE);
        }
    }

    private void findIds() {
        totalVideoTV = view.findViewById(R.id.totalVideoTV);

        rv_sounds = view.findViewById(R.id.rv_sounds);
        rv_peoples = view.findViewById(R.id.rv_peoples);
        rv_hashTags = view.findViewById(R.id.rv_hashTags);
        rv_videos_search = view.findViewById(R.id.rv_videos_search);
        nestedSearch = view.findViewById(R.id.nestedSearch);
        rv_searchTags = view.findViewById(R.id.rv_searchTags);
        et_search = view.findViewById(R.id.et_search);
        ll_search_logged_in = view.findViewById(R.id.ll_search_logged_in);
        shimmerSearch = view.findViewById(R.id.shimmerSearch);
        rl_not_logged_in = view.findViewById(R.id.rl_not_logged_in);
        recycler_search_home = view.findViewById(R.id.recycler_search_home);
        view.findViewById(R.id.btn_sign_up_search).setOnClickListener(this);

        trendingLinearLayout = view.findViewById(R.id.ll_tradings);
        hashTagsLinearLayout = view.findViewById(R.id.ll_hashTags);
        peopleLinearLayout = view.findViewById(R.id.ll_people);
        soundsLinearLayout = view.findViewById(R.id.ll_sound);
    }

    private void openDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_signup, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(getActivity()).create();
        dailogbox.setCancelable(true);
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setView(confirmdailog);
        confirmdailog.findViewById(R.id.img_google_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getSingleton().setLoginType(AppConstants.LOGIN_SEARCH);
                googleLogin = new GoogleLogin(getActivity(),AppConstants.LOGIN_SEARCH);
                googleLogin.signIn();
            }
        });

        confirmdailog.findViewById(R.id.img_fb_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackManager = CallbackManager.Factory.create();
                facebookLogin = new FacebookLogin(getActivity(),AppConstants.LOGIN_SEARCH, getActivity().getApplication());
                facebookLogin.FBLogin();
            }
        });

        confirmdailog.findViewById(R.id.btn_signup_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });

        confirmdailog.findViewById(R.id.btn_signin_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        dailogbox.show();
    }

    public EditText getEt_search() {
        if (et_search != null) {
            return et_search;// returns instance of inflated textview
        }
        return null;// return null and check null
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_up_search:
                openDialog();
                break;
        }
    }

    private void getSearchCate() {
        String[] list = getActivity().getResources().getStringArray(R.array.search_list);
        String[] listTagIds = getActivity().getResources().getStringArray(R.array.search_tag_ids);

        adapterSearchCategories = new AdapterSearchCategories(getActivity(), list, listTagIds, new AdapterSearchCategories.Select() {
            @Override
            public void onClick(String tagId) {
                searchTag = tagId;
                if (tagId.equalsIgnoreCase("1")) {
                    peopleLinearLayout.setVisibility(View.VISIBLE);
                    rv_peoples.setLayoutManager(new LinearLayoutManager(getActivity()));
                    hashTagsLinearLayout.setVisibility(View.GONE);
                    trendingLinearLayout.setVisibility(View.GONE);
                    soundsLinearLayout.setVisibility(View.GONE);

                } else if (tagId.equalsIgnoreCase("2")) {
                    trendingLinearLayout.setVisibility(View.VISIBLE);
                    rv_videos_search.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    hashTagsLinearLayout.setVisibility(View.GONE);
                    peopleLinearLayout.setVisibility(View.GONE);
                    soundsLinearLayout.setVisibility(View.GONE);

                } else if (tagId.equalsIgnoreCase("3")) {
                    hashTagsLinearLayout.setVisibility(View.VISIBLE);
                    rv_hashTags.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    peopleLinearLayout.setVisibility(View.GONE);
                    trendingLinearLayout.setVisibility(View.GONE);
                    soundsLinearLayout.setVisibility(View.GONE);

                }
//                else if (tagId.equalsIgnoreCase("4")) {
//                    hashTagsLinearLayout.setVisibility(View.GONE);
//                    rv_sounds.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    peopleLinearLayout.setVisibility(View.GONE);
//                    trendingLinearLayout.setVisibility(View.GONE);
//                    soundsLinearLayout.setVisibility(View.VISIBLE);
//
//                }
                else {
                    peopleLinearLayout.setVisibility(View.VISIBLE);
                    trendingLinearLayout.setVisibility(View.VISIBLE);
                    hashTagsLinearLayout.setVisibility(View.VISIBLE);
                    soundsLinearLayout.setVisibility(View.VISIBLE);

                    rv_peoples.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rv_hashTags.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    rv_sounds.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    rv_videos_search.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                }


                if (!et_search.getText().toString().isEmpty()) {
                    searchAll(et_search.getText().toString());
                } else {
                    searchAll("");
                }
            }
        });
        rv_searchTags.setAdapter(adapterSearchCategories);
    }

    private void searchAll(final String search) {
        followMvvm.searchAppResults(getActivity(), search, searchTag, userId).observe(getViewLifecycleOwner(), new Observer<SearchAllPojo>() {
            @Override
            public void onChanged(final SearchAllPojo searchAllPojo) {
                if (searchAllPojo.getSuccess().equalsIgnoreCase("1")) {
                    shimmerSearch.stopShimmer();
                    shimmerSearch.setVisibility(View.GONE);
                    ll_search_logged_in.setVisibility(View.VISIBLE);
                    if (searchTag.equalsIgnoreCase("1")) {
                        adapterSearchPeopleRj = new AdapterSearchPeopleRj(getActivity(), searchAllPojo.getDetails().getPeopleList(), new AdapterSearchPeopleRj.Select() {
                            @Override
                            public void followUnfollow(int position) {

                                if (App.getSharedpref().isLogin(getActivity())) {

                                    followUser(searchAllPojo.getDetails().getPeopleList().get(position).getId(), position);
                                } else {
//                                    openDialog();
                                }

                            }

                            @Override
                            public void moveToProfile(int position) {
                                startActivity(new Intent(getActivity(), OtherUserProfileActivity.class).putExtra(AppConstants.OTHER_USER_ID, searchAllPojo.getDetails().getPeopleList().get(position).getId()));

                            }
                        });
                        rv_peoples.setAdapter(adapterSearchPeopleRj);

                    } else if (searchTag.equalsIgnoreCase("2")) {
                        totalVideoTV.setText(String.valueOf(searchAllPojo.getDetails().getVideoList().size()));
                        adapterSearchVideosRj = new AdapterSearchVideosRj(getActivity(), searchAllPojo.getDetails().getVideoList(), new AdapterSearchVideosRj.Select() {
                            @Override
                            public void moveTovideo(int position) {
//                                Intent intent = new Intent(getActivity(), SingleVideoPlayerActivity.class);
                                Intent intent = new Intent(getActivity(), SelectedVideoActivity.class);
                                App.getSingleton().setSearchVideoList(searchAllPojo.getDetails().getVideoList());
                                intent.putExtra(AppConstants.POSITION, position);
//                                intent.putExtra(AppConstants.CHECK_VIDEO, 1);
                                intent.putExtra(AppConstants.SINGLE_VIDEO_TYPE, AppConstants.SEARCH_VIDEO);
                                startActivity(intent);
                            }
                        });
                        rv_videos_search.setAdapter(adapterSearchVideosRj);
                    } else if (searchTag.equalsIgnoreCase("3")) {
                        adapterSearchHashtagsRj = new AdapterSearchHashtagsRj(getActivity(), searchAllPojo.getDetails().getHasTagList(), new AdapterSearchHashtagsRj.Select() {
                            @Override
                            public void hashtags(int position,String count) {
//                                startActivity(new Intent(getActivity(), HashTagVideoActivity.class).putExtra(AppConstants.HASHTAG_ID, searchAllPojo.getDetails().getHasTagList().get(position).getId()).putExtra(AppConstants.HASHTAG_COUNT, searchAllPojo.getDetails().getHasTagList().get(position).getVideoCount()));

                                Intent intent=new Intent(getActivity(),HashTagVideoActivity.class);

                                intent.putExtra(AppConstants.HASHTAG_ID,searchAllPojo.getDetails().getHasTagList().get(position).getId());
                                intent.putExtra(AppConstants.HASHTAG_COUNT,count);
                                startActivity(intent);

                            }
                        });
                        rv_hashTags.setAdapter(adapterSearchHashtagsRj);
                    } else if (searchTag.equalsIgnoreCase("4")) {
                        adapterSearchSoundRj = new AdapterSearchSoundRj(getActivity(), searchAllPojo.getDetails().getSoundList(), new AdapterSearchSoundRj.Select() {
                            @Override
                            public void sounds(String soundId) {
                                App.getSingleton().setSoundId(soundId);
                                gotoSoundDetails();
                            }

                            @Override
                            public void addToFavorites(int position) {
                                addSoundToFavorite(searchAllPojo.getDetails().getSoundList().get(position).getId(), position);
                            }
                        });
                        rv_sounds.setAdapter(adapterSearchSoundRj);
                    } else {
                        if (searchAllPojo.getDetails().getVideoList() != null) {
                            totalVideoTV.setText(String.valueOf(searchAllPojo.getDetails().getVideoList().size()));
                            adapterSearchVideosRj = new AdapterSearchVideosRj(getActivity(), searchAllPojo.getDetails().getVideoList(), new AdapterSearchVideosRj.Select() {
                                @Override
                                public void moveTovideo(int position) {
//                                    Intent intent = new Intent(getActivity(), SingleVideoPlayerActivity.class);
                                    Intent intent = new Intent(getActivity(), SelectedVideoActivity.class);
                                    App.getSingleton().setSearchVideoList(searchAllPojo.getDetails().getVideoList());
                                    intent.putExtra(AppConstants.POSITION, position);
//                                    intent.putExtra(AppConstants.CHECK_VIDEO, 1);
                                    intent.putExtra(AppConstants.SINGLE_VIDEO_TYPE, AppConstants.SEARCH_VIDEO);
                                    startActivity(intent);
                                }
                            });
                            rv_videos_search.setAdapter(adapterSearchVideosRj);
                        } else {
                            trendingLinearLayout.setVisibility(View.GONE);
                        }
                        if (searchAllPojo.getDetails().getHasTagList() != null) {
                            adapterSearchHashtagsRj = new AdapterSearchHashtagsRj(getActivity(), searchAllPojo.getDetails().getHasTagList(), new AdapterSearchHashtagsRj.Select() {
                                @Override
                                public void hashtags(int position,String count) {
//                                    startActivity(new Intent(getActivity(), HashTagVideoActivity.class).putExtra(AppConstants.HASHTAG_ID, searchAllPojo.getDetails().getHasTagList().get(position).getId()));
                                    Intent intent=new Intent(getActivity(),HashTagVideoActivity.class);

                                    intent.putExtra(AppConstants.HASHTAG_ID,searchAllPojo.getDetails().getHasTagList().get(position).getId());
                                    intent.putExtra(AppConstants.HASHTAG_COUNT,count);
                                    startActivity(intent);
                                }
                            });
                            rv_hashTags.setAdapter(adapterSearchHashtagsRj);
                        } else {
                            hashTagsLinearLayout.setVisibility(View.GONE);
                        }
                        if (searchAllPojo.getDetails().getHasTagList() != null) {
                            adapterSearchSoundRj = new AdapterSearchSoundRj(getActivity(), searchAllPojo.getDetails().getSoundList(), new AdapterSearchSoundRj.Select() {
                                @Override
                                public void sounds(String soundId) {
                                    App.getSingleton().setSoundId(soundId);
                                    gotoSoundDetails();
                                }

                                @Override
                                public void addToFavorites(int position) {
                                    addSoundToFavorite(searchAllPojo.getDetails().getSoundList().get(position).getId(), position);
                                }
                            });
                            rv_sounds.setAdapter(adapterSearchSoundRj);
                        } else {
                            hashTagsLinearLayout.setVisibility(View.GONE);
                        }


                        if (searchAllPojo.getDetails().getPeopleList() != null) {
                            adapterSearchPeopleRj = new AdapterSearchPeopleRj(getActivity(), searchAllPojo.getDetails().getPeopleList(), new AdapterSearchPeopleRj.Select() {
                                @Override
                                public void followUnfollow(int position) {
                                    followUser(searchAllPojo.getDetails().getPeopleList().get(position).getId(), position);
                                }

                                @Override
                                public void moveToProfile(int position) {
                                    startActivity(new Intent(getActivity(), OtherUserProfileActivity.class).putExtra(AppConstants.OTHER_USER_ID, searchAllPojo.getDetails().getPeopleList().get(position).getId()));
                                }
                            });
                            rv_peoples.setAdapter(adapterSearchPeopleRj);
                        } else {
                            peopleLinearLayout.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

    private void followUser(String otherId, final int position) {
        FollowUnfollowUser followUnfollowUser=new FollowUnfollowUser(getActivity(), new FollowUnfollowUser.followCallBack() {
            @Override
            public void followInterfaceCall(Map map) {
                if (map.get("success").equals("1")) {
                    if (map.get("following_status").equals(true)) {
                        Log.i("follow", String.valueOf(true));
                        broadcastFollow(AppConstants.FOLLOW, position);
                    } else if (map.get("following_status").equals(false)) {
                        Log.i("follow", String.valueOf(false));
                        broadcastFollow(AppConstants.UNFOLLOW, position);
                    }
                } else {
                    Log.i("follow", map.get("message").toString());
                }
            }
        });
        followUnfollowUser.followUnfollow(otherId);
    }

    private void addSoundToFavorite(String soundId, final int position) {
        SoundsMvvm soundsMvvm = ViewModelProviders.of(SearchHomeFragment.this).get(SoundsMvvm.class);
        soundsMvvm.addFavoriteSounds(getActivity(), CommonUtils.userId(getActivity()), soundId).observe(getActivity(), new Observer<ModelFavoriteSounds>() {
            @Override
            public void onChanged(ModelFavoriteSounds modelFavoriteSounds) {
                if (modelFavoriteSounds.getSuccess().equalsIgnoreCase("1")) {
                    Log.i("favoriteSounds", modelFavoriteSounds.getMessage());
                    Intent intent = new Intent(AppConstants.SOUNDS_FAVORITES);
                    intent.putExtra(AppConstants.POSITION, position);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                } else {
                    Log.i("favoriteSounds", modelFavoriteSounds.getMessage());
                }
            }
        });
    }

    private void broadcastFollow(String value, int position) {
        Intent intent = new Intent(AppConstants.FOLLOW_UNFOLLOW);
        intent.putExtra(AppConstants.POSITION, position);
        intent.putExtra(AppConstants.NOTI_FOLLOW, value);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    private void gotoSoundDetails() {
        startActivity(new Intent(getActivity(), SoundVideoActivity.class));
    }


}