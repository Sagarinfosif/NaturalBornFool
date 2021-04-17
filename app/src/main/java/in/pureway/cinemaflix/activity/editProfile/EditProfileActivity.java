package in.pureway.cinemaflix.activity.editProfile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import in.pureway.cinemaflix.R;
import in.pureway.cinemaflix.activity.EnterOldNumberActivity;
import in.pureway.cinemaflix.activity.privacysettings.UpdatePhoneNumberActivity;
import in.pureway.cinemaflix.activity.videoEditor.util.VideoAudioFFMPEG;
import in.pureway.cinemaflix.activity.videoEditor.util.Variables;
import in.pureway.cinemaflix.javaClasses.instagram.AuthenticationDialog;
import in.pureway.cinemaflix.javaClasses.instagram.AuthenticationListener;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.mvvm.ProfileMvvm;
import in.pureway.cinemaflix.utils.App;
import in.pureway.cinemaflix.utils.AppConstants;
import in.pureway.cinemaflix.utils.CommonUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, AuthenticationListener {
    private static final int ICAMERA = 1;
    private static final int GALLERY = 2;
    private TextView tv_bio_edit_prof, tv_name_edit_prof, tv_username_edit_prof,tv_phonenumber;
    private CircleImageView img_photo;
    private CircleImageView img_video;
    private String imagepath = "";
    private boolean imageCheck = false;
    private boolean videoCheck = false;
    private ProfileMvvm profileMvvm;
    private Uri contentURI;
    private String videoPath;
    String type = "";
    public static final String INSTA_TOKEN = "0a986e726938502c03381474676517d1";
    private AuthenticationDialog authenticationDialog = null;
    String token = null;
    long maxDur;
    int startTrim = 1000, endTrim = 10000;
    MediaMetadataRetriever mediaMetadataRetriever = null;
    private RelativeLayout rl_phone_number;
    Long tsLong = System.currentTimeMillis() / 1000;
    String ts = tsLong.toString();
    String userId = CommonUtils.userId(EditProfileActivity.this);
    File gifFile = new File(Variables.app_folder, "/" + userId + ts + "myGif.gif");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.changeLanguage(EditProfileActivity.this);
        setContentView(R.layout.activity_edit_profile);
        findiDs();
        profileMvvm = ViewModelProviders.of(EditProfileActivity.this).get(ProfileMvvm.class);
        mediaMetadataRetriever = new MediaMetadataRetriever();
    }

    private void findiDs() {
        tv_phonenumber = findViewById(R.id.tv_phonenumber);
        rl_phone_number = findViewById(R.id.rl_phone_number);
        img_video = findViewById(R.id.img_video);
        img_photo = findViewById(R.id.img_photo);
        img_photo.setOnClickListener(this);
        img_video.setOnClickListener(this);
        rl_phone_number.setOnClickListener(this);

        tv_username_edit_prof = findViewById(R.id.tv_username_edit_prof);
        findViewById(R.id.rl_username).setOnClickListener(this);

        tv_name_edit_prof = findViewById(R.id.tv_name_edit_prof);
        findViewById(R.id.rl_name).setOnClickListener(this);

        tv_bio_edit_prof = findViewById(R.id.tv_bio_edit_prof);
        findViewById(R.id.rl_insta).setOnClickListener(this);
        findViewById(R.id.rl_bio).setOnClickListener(this);

        findViewById(R.id.img_back_edit).setOnClickListener(this);

        img_photo.setOnLongClickListener(this);
        img_video.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_insta:
                if (token != null) {
                    token = null;
                } else {
                    authenticationDialog = new AuthenticationDialog(this, this);
                    authenticationDialog.setCancelable(true);
                    authenticationDialog.show();
                }
                break;

            case R.id.img_back_edit:
                onBackPressed();
                break;

            case R.id.rl_username:

                startActivity(new Intent(EditProfileActivity.this, UsernameActivity.class));
                break;

            case R.id.rl_name:
                startActivity(new Intent(EditProfileActivity.this, NameActivity.class));
                break;

            case R.id.rl_bio:
                startActivity(new Intent(EditProfileActivity.this, BioActivity.class));
                break;

            case R.id.img_photo:
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
                break;

            case R.id.img_video:
                showPictureDialog();
                break;



            case R.id.rl_phone_number:
                if (App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getPhone().equalsIgnoreCase("")){
                    startActivity(new Intent(EditProfileActivity.this, UpdatePhoneNumberActivity.class));

                }else {
                    startActivity(new Intent(EditProfileActivity.this, EnterOldNumberActivity.class));

                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
                    Glide.with(EditProfileActivity.this).load(imagepath).into(img_photo);
                    if (!imagepath.equalsIgnoreCase("")) {
                        imageCheck = true;
                        uploadImage(imagepath);
                    }
                } else {
                    Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
                }
            }
            //Video
            else {
                if (requestCode == GALLERY) {
                    Log.d("what", "gale");
                    if (data != null) {
                        contentURI = data.getData();
                        videoPath = getPath(contentURI);
                        if (videoPath != null) {
                            videoCheck = true;
                            File videoFile = new File(videoPath);
                            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                            retriever.setDataSource(EditProfileActivity.this, Uri.fromFile(videoFile));
                            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                            long timeInMillisec = Long.parseLong(time);
                            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillisec);
                            retriever.release();
                            if (seconds > 5) {
                                Toast.makeText(this, "Video size cannot be greater than 5 seconds.", Toast.LENGTH_SHORT).show();
                            } else {
                                convertToGif(videoPath);
                            }
                        }
                        Log.d("path", videoPath);
                    }
                } else if (requestCode == ICAMERA) {
                    Uri contentURI = data.getData();
                    videoPath = getPath(contentURI);
                    if (videoPath != null) {
                        videoCheck = true;
                        convertToGif(videoPath);
                    }
                }
            }
        }
    }

    private void convertToGif(String videoPath) {
        String[] gifCommand = new String[]{"-i", videoPath, "-vf", "fps=5,scale=450:-1", gifFile.getAbsolutePath()};
        VideoAudioFFMPEG videoAudioFFMPEG = new VideoAudioFFMPEG(EditProfileActivity.this, new VideoAudioFFMPEG.GifCompleted() {
            @Override
            public void onGIFCompletedListener() {
                Glide.with(EditProfileActivity.this).asGif().load(gifFile.getAbsolutePath()).placeholder(getResources().getDrawable(R.drawable.ic_user1)).into(img_video);
                uploadVideo(gifFile.getAbsolutePath());
            }
        });
        videoAudioFFMPEG.executeGifCommand(gifCommand);
    }


    @Override
    protected void onResume() {

        String name = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getName();
        String username = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getUsername();
        String bio = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getBio();
        String image = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getImage();
        String video = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getVideo();
        String phone = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getPhone();

        if (!bio.equalsIgnoreCase("")) {
            tv_bio_edit_prof.setVisibility(View.VISIBLE);
            tv_bio_edit_prof.setText(bio);
        }
        if (!username.equalsIgnoreCase("")) {
            tv_username_edit_prof.setVisibility(View.VISIBLE);
            tv_username_edit_prof.setText(username);
        }
        if (!name.equalsIgnoreCase("")) {
            tv_name_edit_prof.setVisibility(View.VISIBLE);
            tv_name_edit_prof.setText(name);
        }

        if (!phone.equalsIgnoreCase("")) {
            tv_phonenumber.setVisibility(View.VISIBLE);
            tv_phonenumber.setText(phone);
        }

        if (!image.equalsIgnoreCase("")) {
            Glide.with(EditProfileActivity.this).load(image).placeholder(getResources().getDrawable(R.drawable.background_login)).into(img_photo);
        } else {
            Glide.with(EditProfileActivity.this).load(getResources().getDrawable(R.drawable.background_login)).into(img_photo);
        }

        if (!video.equalsIgnoreCase("")) {
            Glide.with(EditProfileActivity.this).load(video).placeholder(getResources().getDrawable(R.drawable.background_login)).into(img_video);
        } else {
            Glide.with(EditProfileActivity.this).load(getResources().getDrawable(R.drawable.background_login)).into(img_video);
        }
        super.onResume();
    }

    private void uploadImage(String ImagePath) {
        String userId = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getId();
        RequestBody rb_userId = RequestBody.create(MediaType.parse("text/plain"), userId);
        MultipartBody.Part rb_image, rb_video;

        if (imageCheck) {
            File file = new File(ImagePath);
            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            rb_image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        } else {
            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            rb_image = MultipartBody.Part.createFormData("image", "", requestFile);
        }

        final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        rb_video = MultipartBody.Part.createFormData("video", "", requestFile);

        profileMvvm.uploadImageVideo(EditProfileActivity.this, rb_userId, rb_image, rb_video).observe(EditProfileActivity.this, new Observer<ModelLoginRegister>() {
            @Override
            public void onChanged(ModelLoginRegister modelLoginRegister) {
                if (modelLoginRegister.getSuccess().equalsIgnoreCase("1")) {
                    App.getSharedpref().saveModel(AppConstants.REGISTER_LOGIN_DATA, modelLoginRegister);
                    Glide.with(EditProfileActivity.this).load(modelLoginRegister.getDetails().getImage()).into(img_photo);
                } else {
                    Toast.makeText(EditProfileActivity.this, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadVideo(String videoPath) {
        String userId = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getId();
        RequestBody rb_userId = RequestBody.create(MediaType.parse("text/plain"), userId);
        MultipartBody.Part rb_image, rb_video;

        if (videoCheck) {
            File file = new File(videoPath);
            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            rb_video = MultipartBody.Part.createFormData("video", file.getName(), requestFile);
        } else {
            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            rb_video = MultipartBody.Part.createFormData("video", "", requestFile);
        }

        final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        rb_image = MultipartBody.Part.createFormData("image", "", requestFile);


        profileMvvm.uploadImageVideo(EditProfileActivity.this, rb_userId, rb_image, rb_video).observe(EditProfileActivity.this, new Observer<ModelLoginRegister>() {
            @Override
            public void onChanged(ModelLoginRegister modelLoginRegister) {
                if (modelLoginRegister.getSuccess().equalsIgnoreCase("1")) {
                    App.getSharedpref().saveModel(AppConstants.REGISTER_LOGIN_DATA, modelLoginRegister);
                    Glide.with(EditProfileActivity.this).asGif().load(modelLoginRegister.getDetails().getVideo()).placeholder(getResources().getDrawable(R.drawable.ic_user1)).into(img_video);

                    gifFile.delete();
                    boolean success = gifFile.delete();
                    Log.i("giffiledeleted", String.valueOf(success));
                } else {
                    Toast.makeText(EditProfileActivity.this, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.img_video:
                type = "video";
                showMenu(v);
                break;
            case R.id.img_photo:
                type = "image";
                showMenu(v);
                break;
        }
        return true;
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_remove:
                        checkImageVideo(type);
                        break;
                }
                return true;
            }
        });
        popup.inflate(R.menu.menu_delete);
        popup.show();
    }

    private void checkImageVideo(String type) {
        String image = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getImage();
        String video = App.getSharedpref().getModel(AppConstants.REGISTER_LOGIN_DATA, ModelLoginRegister.class).getDetails().getVideo();

        if (type.equalsIgnoreCase("image")) {
            if (!image.equalsIgnoreCase("")) {
                removeImageVideo(type);
            } else {
                Toast.makeText(this, "No Image", Toast.LENGTH_SHORT).show();
            }
        } else if (type.equalsIgnoreCase("video")) {
            if (!video.equalsIgnoreCase("")) {
                removeImageVideo(type);
            } else {
                Toast.makeText(this, "No Video", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void removeImageVideo(final String type) {
        String userId = CommonUtils.userId(EditProfileActivity.this);
        profileMvvm.removeImageVideo(EditProfileActivity.this, type, userId).observe(EditProfileActivity.this, new Observer<ModelLoginRegister>() {
            @Override
            public void onChanged(ModelLoginRegister modelLoginRegister) {
                if (modelLoginRegister.getSuccess().equalsIgnoreCase("1")) {
                    App.getSharedpref().saveModel(AppConstants.REGISTER_LOGIN_DATA, modelLoginRegister);
                    if (type.equalsIgnoreCase("image")) {
                        Glide.with(EditProfileActivity.this).load(getResources().getDrawable(R.drawable.background_login)).into(img_photo);
                    } else {
                        Glide.with(EditProfileActivity.this).load(getResources().getDrawable(R.drawable.background_login)).into(img_video);
                    }

                } else {
                    Toast.makeText(EditProfileActivity.this, modelLoginRegister.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this, R.style.dialogStyle);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select video from gallery",
                "Record video from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                chooseVideoFromGallery();
                                break;
                            case 1:
                                takeVideoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void chooseVideoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takeVideoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);  //video limited to 5 seconds
        startActivityForResult(intent, ICAMERA);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    @Override
    public void onTokenReceived(String auth_token) {
        if (auth_token == null)
            return;
        token = auth_token;
        Log.i("instaToken", token);
//        appPreferences.putString(AppPreferences.TOKEN, auth_token);
//        token = auth_token;
//        getUserInfoByAccessToken(auth_token);
    }


//    private void getUserInfoByAccessToken(String token) {
//        new RequestInstagramAPI().execute();
//    }

//    private class RequestInstagramAPI extends AsyncTask<Void, String, String> {
//
//        @Override
//        protected String doInBackground(Void... params) {
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpGet httpGet = new HttpGet(getResources().getString(R.string.get_user_info_url) + token);
//            try {
//                HttpResponse response = httpClient.execute(httpGet);
//                HttpEntity httpEntity = response.getEntity();
//                return EntityUtils.toString(httpEntity);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String response) {
//            super.onPostExecute(response);
//            if (response != null) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    Log.e("response", jsonObject.toString());
//                    JSONObject jsonData = jsonObject.getJSONObject("data");
//                    if (jsonData.has("id")) {
//                        //сохранение данных пользователя
//                        Log.i("instaData", jsonData.getString("id") + ":" + jsonData.getString("username") + ":" + jsonData.getString("profile_picture"));
////                        appPreferences.putString(AppPreferences.USER_ID, jsonData.getString("id"));
////                        appPreferences.putString(AppPreferences.USER_NAME, jsonData.getString("username"));
////                        appPreferences.putString(AppPreferences.PROFILE_PIC, jsonData.getString("profile_picture"));
//                    }
//                    Log.i("instadata", jsonData.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                Toast toast = Toast.makeText(getApplicationContext(), "Ошибка входа!", Toast.LENGTH_LONG);
//                toast.show();
//            }
//        }
//    }
}
