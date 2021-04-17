package in.pureway.cinemaflix.fragments.homefrags;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.videoEditor.activity.DraftsActivity;
import in.pureway.cinemaflix.activity.SelectedVideoActivity;
import in.pureway.cinemaflix.activity.videoEditor.util.Variables;
import in.pureway.cinemaflix.adapters.AdapterVideoProfile;
import in.pureway.cinemaflix.models.ModelMyUploadedVideos;
import in.pureway.cinemaflix.mvvm.VideoMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileVideosFragment extends Fragment {
    private RecyclerView recycler_videos_profile;
    private View view;
    private VideoMvvm videoMvvm;
    private List<ModelMyUploadedVideos.Detail> list = new ArrayList<>();
    private String userId, loginId;
    private MaterialCardView card_no_videos;
    private AdapterVideoProfile adapterVideoProfile;

    public ProfileVideosFragment() {
        // Required empty public constructor
    }

    public ProfileVideosFragment(String userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_videos, container, false);
        videoMvvm = ViewModelProviders.of(ProfileVideosFragment.this).get(VideoMvvm.class);
        findIds();
        setRecycler();
        return view;
    }

    private void setRecycler() {
        videoMvvm.getUploadedVideos(getActivity(), userId, CommonUtils.userId(getActivity()) ).observe(getActivity(), new Observer<ModelMyUploadedVideos>() {
            @Override
            public void onChanged(ModelMyUploadedVideos modelMyUploadedVideos) {
                if (modelMyUploadedVideos.getSuccess().equalsIgnoreCase("1")) {
                    list = modelMyUploadedVideos.getDetails();
                    adapterVideoProfile = new AdapterVideoProfile(getActivity(), list, new AdapterVideoProfile.Select() {
                        @Override
                        public void drafts(int position) {
                            if (position == 0) {
                                //check if drafts has files in it
                                if (getListFiles(new File(Variables.draft_app_folder)).size() != 0) {
                                    startActivity(new Intent(getActivity(), DraftsActivity.class));
                                } else {
                                    Toast.makeText(getActivity(), "Drafts are Empty", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void videoPlayList(int position) {
                            Intent intent = new Intent(getActivity(), SelectedVideoActivity.class);
                            App.getSingleton().setListMyUploaded(list);
                            intent.putExtra(AppConstants.POSITION, position);
                            intent.putExtra(AppConstants.SINGLE_VIDEO_TYPE, AppConstants.TYPE_UPLOADED);
                            startActivity(intent);
                        }

                        @Override
                        public void onLongClick(int position) {
                            showDeleteDialog(list.get(position).getId(),position,list.get(position).getVideoPath());
                        }

//                        @Override
//                        public void onLongClick(int position) {
//
//                        }

                    }, userId, CommonUtils.userId(getActivity()));
                    recycler_videos_profile.setAdapter(adapterVideoProfile);
                } else {
                    recycler_videos_profile.setVisibility(View.GONE);
                    card_no_videos.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showDeleteDialog(final String id, final int position, final String path) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View confirmdailog = layoutInflater.inflate(R.layout.dialog_delete_video, null);
        final AlertDialog dailogbox = new AlertDialog.Builder(getActivity()).create();
        dailogbox.setCancelable(true);
        dailogbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dailogbox.setView(confirmdailog);
        confirmdailog.findViewById(R.id.tv_delete_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVideo(id, position, dailogbox);
            }
        });
        confirmdailog.findViewById(R.id.tv_share_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CommonUtils.shareIntent();
            }
        });
        confirmdailog.findViewById(R.id.tv_cancel_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogbox.dismiss();
            }
        });
        dailogbox.show();
    }

    private void deleteVideo(String id, final int position, final Dialog dialog) {
        videoMvvm.videoDelete(getActivity(), id).observe(getActivity(), new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").equals("1")) {
                    Toast.makeText(getActivity(), "Video Deleted", Toast.LENGTH_SHORT).show();
                    list.remove(position);
                    adapterVideoProfile.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), map.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //get files from drafts folder
    public static ArrayList<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files;
        files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".mp4")) {
                    if (!inFiles.contains(file)) inFiles.add(file);
                    Log.i("draftsFile", file.toString());
                }
            }
        }
        return inFiles;
    }

    @Override
    public void onResume() {
//        setRecycler();
        super.onResume();
    }

    private void findIds() {
        card_no_videos = view.findViewById(R.id.card_no_videos);
        recycler_videos_profile = view.findViewById(R.id.recycler_videos_profile);
    }
}
