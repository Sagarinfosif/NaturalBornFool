package in.pureway.cinemaflix.retrofit;

import in.pureway.cinemaflix.Live_Stream.GIftHistoryModel;
import in.pureway.cinemaflix.adapters.ModelVideoTesting;
import in.pureway.cinemaflix.models.CoinModel;
import in.pureway.cinemaflix.models.GetCoinModel;
import in.pureway.cinemaflix.models.GetSoundDetailsPojo;
import in.pureway.cinemaflix.models.GiftModel;
import in.pureway.cinemaflix.models.ModelBlock;
import in.pureway.cinemaflix.models.ModelComments;
import in.pureway.cinemaflix.models.ModelFavoriteHashTag;
import in.pureway.cinemaflix.models.ModelFavoriteSounds;
import in.pureway.cinemaflix.models.ModelFindContacts;
import in.pureway.cinemaflix.models.ModelFollowers;
import in.pureway.cinemaflix.models.ModelHashTag;
import in.pureway.cinemaflix.models.ModelHashTagsDetails;
import in.pureway.cinemaflix.models.ModelInbox;
import in.pureway.cinemaflix.models.ModelLikedVideos;
import in.pureway.cinemaflix.models.ModelLiveUsers;
import in.pureway.cinemaflix.models.ModelLoginPhone;
import in.pureway.cinemaflix.models.ModelLoginRegister;
import in.pureway.cinemaflix.models.ModelMyUploadedVideos;
import in.pureway.cinemaflix.models.ModelNotifications;
import in.pureway.cinemaflix.models.ModelPrivacySettings;
import in.pureway.cinemaflix.models.ModelReport;
import in.pureway.cinemaflix.models.ModelReportList;
import in.pureway.cinemaflix.models.ModelSearchFriend;
import in.pureway.cinemaflix.models.ModelSendMessage;
import in.pureway.cinemaflix.models.ModelSounds;
import in.pureway.cinemaflix.models.ModelVideoList;
import in.pureway.cinemaflix.models.ModelvideoReportList;
import in.pureway.cinemaflix.models.OtherUserDataModel;
import in.pureway.cinemaflix.models.PushNotificationSettingsPojo;
import in.pureway.cinemaflix.models.SearchAllPojo;

import java.util.Map;

import in.pureway.cinemaflix.models.UploadSingleVideoPojo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

//    @FormUrlEncoded
//    @POST("register")
//    Call<ModelLoginRegister> userRegister(@Field("type") String type,
//                                          @Field("emailPhone") String emailPhone,
//                                          @Field("password") String password,
//                                          @Field("dob") String dob,
//                                          @Field("reg_id") String reg_id,
//                                          @Field("device_type") String deviceType);

//    register natural born fool user
    @FormUrlEncoded
    @POST("register")
    Call<ModelLoginRegister> userRegister(@Field("password") String password,
                                          @Field("dob") String dob,
                                          @Field("email") String email,
                                          @Field("countryCode") String countryCode,
                                          @Field("phone") String phone,
                                          @Field("username") String username,
                                          @Field("reg_id") String reg_id,
                                          @Field("device_type") String device_type);


    //Check email and phone
//    @FormUrlEncoded
//    @POST("checkPhoneAndEmail")
//    Call<Map> checkPhoneEmail(@Field("type") String type,      //type => phone,email
//                              @Field("emailPhone") String emailPhone);

    //Check email phone and username natural born fool
    @FormUrlEncoded
    @POST("checkPhoneAndEmail")
    Call<Map> checkPhoneEmail(@Field("email") String email,
                              @Field("username") String username,
                              @Field("phone") String phone);

    //Login
//    @FormUrlEncoded
//    @POST("login")
//    Call<ModelLoginRegister> userlogin(@Field("emailPhone") String emailPhone,
//                                       @Field("password") String password,
//                                       @Field("reg_id") String regId,
//                                       @Field("device_type") String deviceType);

    //natural born fool Login
    @FormUrlEncoded
    @POST("login")
    Call<ModelLoginRegister> userlogin(@Field("username") String username,
                                       @Field("password") String password,
                                       @Field("reg_id") String reg_id,
                                       @Field("device_type") String device_type);

    //Social Login
    @FormUrlEncoded
    @POST("socialLogin")
    Call<ModelLoginRegister> socialLogin(@Field("name") String name,
                                         @Field("social_id") String socialId,
                                         @Field("email") String email,
                                         @Field("phone") String phone,
                                         @Field("reg_id") String regId,
                                         @Field("image") String image,
                                         @Field("device_type") String devicetype);

    //Update User Information
    @FormUrlEncoded
    @POST("updateUserInformation")
    Call<ModelLoginRegister> updateUserInfo(@Field("name") String name,
                                            @Field("username") String username,
                                            @Field("bio") String bio,
                                            @Field("userId") String userId,
                                            @Field("phone") String phone
                                            );

    //upload Profile video and image
    @Multipart
    @POST("imageVideo")
    Call<ModelLoginRegister> uploadImageVideo(@Part("userId") RequestBody userId,
                                              @Part MultipartBody.Part image,
                                              @Part MultipartBody.Part video);

    //DRemove Profile image-video
    @FormUrlEncoded
    @POST("imageVideoDelete")
    Call<ModelLoginRegister> removeImageVideo(@Field("type") String type,     //video, image
                                              @Field("userId") String userId);

    //Upload Video
    @Multipart
    @POST("uploadVideos")
    Call<Map> uploadVideo(@Part("userId") RequestBody userId,
                          @Part("hashTag") RequestBody hashTag,
                          @Part("description") RequestBody description,
                          @Part("allowComment") RequestBody allowComment,
                          @Part("allowDuetReact") RequestBody allowDuetReact,
                          @Part("allowDownloads") RequestBody allowDownloads,
                          @Part MultipartBody.Part video,
                          @Part MultipartBody.Part soundFile,
                          @Part("viewVideo") RequestBody viewVideo,
                          @Part("soundId") RequestBody soundId,
                          @Part("soundTitle") RequestBody soundTitle,
                          @Part("videolength") RequestBody videolength,
                          @Part("videoType") RequestBody videoType,

                          @Part("isDuet") RequestBody isDuet,
                          @Part("heightVideo") RequestBody heightVideo,
                          @Part("widthVideo") RequestBody widthVideo,
                          @Part("duetUrl") RequestBody duetUrl,
                          @Part MultipartBody.Part imageThumb
    );

    //Upload Video
    @Multipart
    @POST("uploadSingleVideo")
    Call<UploadSingleVideoPojo> uploadSingleVideo( @Part MultipartBody.Part video);

    //Get VideoList
    @FormUrlEncoded
    @POST("getVideo")
    Call<ModelVideoList> getVideoList(@Field("userId") String userId,
                                      @Field("startLimit") String startLimit,
                                      @Field("videoType") String videoType,
                                      @Field("anuradha") int anuradha);


    //Get VideoList
    @FormUrlEncoded
    @POST("videoTesting")
    Call<ModelVideoTesting> videoTesting(@Field("userId") String userId,
                                         @Field("startLimit") String startLimit,
                                         @Field("videoType") String videoType,
                                         @Field("anuradha") int anuradha);

    //    http://3.131.84.133/app/index.php/api/App/
    @FormUrlEncoded
    @POST("singleVideo")
    Call<ModelMyUploadedVideos> getSingleVideo(@Field("userId") String userId,
                                               @Field("videoId") String startLimit);


    //Like-dislike video
    @FormUrlEncoded
    @POST("likeAndDislikeVideo")
    Call<Map> likeVideo(@Field("userId") String userid,
                        @Field("videoId") String videoId,
                        @Field("ownerId") String ownerId);

    //My Uploaded/Profile Videos List
    @FormUrlEncoded
    @POST("myVideoList")
    Call<ModelMyUploadedVideos> getProfileVideos(
            @Field("userId") String userId,
            @Field("loginId") String loginId);

    //My Liked Videos List
    @FormUrlEncoded
    @POST("getLikeVideo")
    Call<ModelLikedVideos> getLikedVideos(@Field("userId") String userId);

    //Get Comments
    @FormUrlEncoded
    @POST("getVideoComments")
    Call<ModelComments> comments(@Field("userId") String userId,
                                 @Field("videoId") String videoId);


    //Comment on Video
    @FormUrlEncoded
    @POST("userCommentVideo")
    Call<ModelComments> commentOnVideo(@Field("userId") String userID,
                                       @Field("videoId") String videoId,
                                       @Field("comment") String comment,
                                       @Field("ownerId") String ownerId);

    //likeAndDislikeComments
    @FormUrlEncoded
    @POST("likeAndDislikeComments")
    Call<Map> likeComment(@Field("commentId") String commentId,
                          @Field("userId") String userId);

    //Privacy- private account
    @FormUrlEncoded
    @POST("accountType")
    Call<Map> privateAccount(@Field("userId") String userId);

    //Privacy-show my liked videos
    @FormUrlEncoded
    @POST("likeVideoShow")
    Call<Map> showLikedVideos(@Field("userId") String userId);

    //uSer profile data
    @FormUrlEncoded
    @POST("userInfo")
    Call<OtherUserDataModel> userProfileData(@Field("userId") String userId,
                                             @Field("loginId") String loginId);

    //Follow user
    @FormUrlEncoded
    @POST("userFollow")
    Call<Map> followUser(@Field("userId") String userId,
                         @Field("followingUserId") String followingUserId);

    //logout
    @FormUrlEncoded
    @POST("logout")
    Call<Map> logout(@Field("userId") String userId);

    //notifications list
    @FormUrlEncoded
    @POST("userNotification")
    Call<ModelNotifications> getNotifications(@Field("userId") String userId,
                                              @Field("startLimit") String startLimit);

    //Delete video
    @FormUrlEncoded
    @POST("videoDelete")
    Call<Map> videoDelet(@Field("videoId") String videoId);

    //delete comment
    @FormUrlEncoded
    @POST("commentDelete")
    Call<Map> deleteComment(@Field("commentId") String commentId);

    //followerList
    @FormUrlEncoded
    @POST("followerList")
    Call<ModelFollowers> getFollowersList(@Field("userId") String userId, @Field("search") String search);

    //folowingList
    @FormUrlEncoded
    @POST("followingList")
    Call<ModelFollowers> getFollowingList(@Field("userId") String userId, @Field("search") String search);

    //followerDelete
    @FormUrlEncoded
    @POST("followerDelete")
    Call<Map> deleteFollower(@Field("id") String id);

    //otherfollowingList
    @FormUrlEncoded
    @POST("otherFollowingList")
    Call<ModelFollowers> otherFollowingList(@Field("userId") String otherUserId,
                                            @Field("ownerId") String myId);

    //otherFollowersList
    @FormUrlEncoded
    @POST("otherFollowerList")
    Call<ModelFollowers> otherFollowersList(@Field("userId") String otherUserId,
                                            @Field("ownerId") String myId);

    //  Search Users List
    @FormUrlEncoded
    @POST("getSearchUsersList")
    Call<ModelSearchFriend> getSearchList(@Field("userId") String userId,
                                          @Field("search") String search);

    //Get Sounds List
    @FormUrlEncoded
    @POST("getSoundsList")
    Call<ModelSounds> getSoundsList(@Field("userId") String userId,
                                    @Field("search") String search);

    //add sound favorites
    @FormUrlEncoded
    @POST("addFavoriteSounds")
    Call<ModelFavoriteSounds> addSoundfavorites(@Field("userId") String userId,
                                                @Field("soundId") String soundId);

    //get favorite soundsList
    @FormUrlEncoded
    @POST("getUserSoundFavoriteList")
    Call<ModelFavoriteSounds> favoriteSoundList(@Field("userId") String userId);

    //updateEmailPhone
    @FormUrlEncoded
    @POST("updadtePhoneAndEmail")
    Call<ModelLoginRegister> updateEmailPhone(@Field("userId") String userId,
                                              @Field("type") String type,
                                              @Field("emailPhone") String emailPhone);

    //change password
    @FormUrlEncoded
    @POST("changePassword")
    Call<ModelLoginRegister> changePassword(@Field("userId") String userId,
                                            @Field("old_password") String old_password,
                                            @Field("new_password") String new_password);

    //forgot password
    @FormUrlEncoded
    @POST("updatePassword")
    Call<ModelLoginRegister> forgotPass(@Field("type") String type,
                                        @Field("emailPhone") String emailPhone,
                                        @Field("password") String password);

    //forgot password otp
    @FormUrlEncoded
    @POST("forgotPass")
    Call<Map> otpForgotPass(@Field("type") String type,
                            @Field("emailPhone") String emailPhone);

    //block user
    @FormUrlEncoded
    @POST("userBlock")
    Call<Map> blockUser(@Field("userId") String userId,
                        @Field("blockUserId") String blockId);

    //blockList
    @FormUrlEncoded
    @POST("blockList")
    Call<ModelBlock> getBlockList(@Field("userId") String userId);

    //show my following
    @FormUrlEncoded
    @POST("showFollowingUserStatus")
    Call<Map> showMyFollowing(@Field("userId") String userId);

    //show my profilevideo
    @FormUrlEncoded
    @POST("showProfilePhotoStatus")
    Call<Map> showProfileVideo(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("search")
    Call<SearchAllPojo> searchAll(@Field("search") String search,
                                  @Field("type") String tagId,
                                  @Field("userId") String userId);

    //commentReply
    @FormUrlEncoded
    @POST("commentReply")
    Call<ModelComments> commentReply(@Field("userId") String userId,
                                     @Field("videoId") String videoId,
                                     @Field("commentId") String commentId,
                                     @Field("comment") String comment);

    //search hashtags
    @FormUrlEncoded
    @POST("hashtag")
    Call<ModelHashTag> searchHashtags(@Field("search") String search);

    @FormUrlEncoded
    @POST("soundVideos")
    Call<GetSoundDetailsPojo> getSoundDetails(@Field("userId") String userId,
                                              @Field("soundId") String soundId,
                                              @Field("startLimit") String startLimit);

    //find contacts
    @FormUrlEncoded
    @POST("contactList")
    Call<ModelFindContacts> findContacts(@Field("userId") String userId,
                                         @Field("phoneNumber") String contactNumbers);

    //notification settings
    @FormUrlEncoded
    @POST("userNotificationSetting")
    Call<PushNotificationSettingsPojo> getPushNotiSettings(@Field("userId") String userId);

    //update notification settings
    @FormUrlEncoded
    @POST("updateNotificationSetting")
    Call<PushNotificationSettingsPojo> updatePushNotiSettings(@Field("userId") String userId,
                                                              @Field("likeNotifaction") String likeNotifaction,
                                                              @Field("commentNotification") String commentNotification,
                                                              @Field("followersNotification") String followersNotification,
                                                              @Field("messageNotification") String messageNotification,
                                                              @Field("videoNotification") String videoNotification);

    //hashTagVideos
    @FormUrlEncoded
    @POST("hashTagVideos")
    Call<ModelHashTagsDetails> getHashTagDetails(@Field("hashtagId") String hashtagId,
                                                 @Field("userId") String userId,
                                                 @Field("startLimit") String startLimit);

    //get report list
    @GET("report")
    Call<ModelReport> reportList();

    //report user
    @FormUrlEncoded
    @POST("reportUser")
    Call<ModelReport> reportUser(@Field("userId") String userId,
                                 @Field("reportUserId") String otherId,
                                 @Field("report") String report);

    //mute notifications
    @FormUrlEncoded
    @POST("muteNotification")
    Call<Map> muteNotification(@Field("userId") String userId,
                               @Field("muteId") String muteId);

    //add/remove hashtag from favorites
    @FormUrlEncoded
    @POST("addFavoriteHahtag")
    Call<Map> addRemoveHashFav(@Field("userId") String userId,
                               @Field("hashtagId") String hashtagId);

    //hashtag favorites list
    @FormUrlEncoded
    @POST("getFavoriteHahtag")
    Call<ModelFavoriteHashTag> hashTagFavList(@Field("userId") String userId);

    //get privacy settings
    @FormUrlEncoded
    @POST("privateAccountStatus")
    Call<ModelPrivacySettings> getPrivacySettings(@Field("userId") String userId);

    //delete account
    @FormUrlEncoded
    @POST("deleteAccountRequest")
    Call<Map> deleteAccountRequest(@Field("userId") String userId);

    //delete account otp
    @FormUrlEncoded
    @POST("deleteAccountOtp")
    Call<Map> otpDeleteAccount(@Field("userId") String userId);

    //view Video count send
    @FormUrlEncoded
    @POST("viewVideo")
    Call<Map> viewVideo(@Field("userId") String userId,
                        @Field("videoId") String videoId);

    //Problem report list
    @GET("problemReport")
    Call<ModelReportList> getReportProblemList();

    //report problem
    @FormUrlEncoded
    @POST("userProblemReport")
    Call<ModelReportList> reportProblem(@Field("userId") String userId,
                                        @Field("report") String report);


    @Multipart
    @POST("sendMessage")
    Call<ModelSendMessage> sendMessage(@Part("sender_id") RequestBody sender_id,
                                       @Part("reciver_id") RequestBody reciver_id,
                                       @Part("message") RequestBody message,
                                       @Part("messageType") RequestBody messageType,
                                       @Part MultipartBody.Part image);


    // Conversation Message
    @FormUrlEncoded
    @POST("conversationMessage")
    Call<ModelSendMessage> conversation(@Field("sender_id") String sender_id,
                                        @Field("reciver_id") String reciver_id);

    // Inbox
    @FormUrlEncoded
    @POST("inbox")
    Call<ModelInbox> inbox(@Field("sender_id") String sender_id);

    //delete chat
    @FormUrlEncoded
    @POST("deleteChat")
    Call<ModelInbox> deleteChat(@Field("userId") String senderId,
                                @Field("inboxId") String receiverId);

    //delete message
    @FormUrlEncoded
    @POST("singleMessageDelete")
    Call<ModelInbox> deleteMessage(@Field("userId") String userId,
                                   @Field("conversationId") String conversationId);


    //set live broadcasting
    @FormUrlEncoded
    @POST("storeLiveBrodcasting")
    Call<Map> sendBroad(@Field("broadcast_id") String broadcastId,
                        @Field("userId") String userId);

    //get hot live users
    @FormUrlEncoded
    @POST("getLiveBroadcast")
    Call<ModelLiveUsers> liveUsersList(@Field("userId") String userId);

    //
    //get foloowed live users
    @FormUrlEncoded
    @POST("followerBroadCast")
    Call<ModelLiveUsers> followersLiveBroad(@Field("userId") String userId);

    //stop broadcast
    @FormUrlEncoded
    @POST("stopLiveBroadcast")
    Call<Map> stopBroadcasting(@Field("broadcast_id") String broadcast_id);

    //comment on live
    @GET("firebase.php")
    Call<Map> commentOnLive(@Query("broadcastId") String broadcastId,
                            @Query("userId") String userId,
                            @Query("username") String username,
                            @Query("userImage") String userImage,
                            @Query("comment") String comment);


    //delete message
    @FormUrlEncoded
    @POST("gift")
    Call<GiftModel> getGifts(@Field("userId") String userId);


    //Problem report list
    @GET("coinList")
    Call<CoinModel> getCoins();


    //delete message
    @FormUrlEncoded
    @POST("purchaseCoin")
    Call<GetCoinModel> getCoinWallet(@Field("userId") String userId,
                                     @Field("transactionId") String transactionId,
                                     @Field("coin") String coin);


    //delete message
    @FormUrlEncoded
    @POST("sendGift")
    Call<Map> giftSend(@Field("userId") String userId,
                       @Field("coin") String coin,
                       @Field("giftUserId") String giftUserId,
                       @Field("giftId") String giftId);

    //delete message
    @FormUrlEncoded
    @POST("giftHistory")
    Call<GIftHistoryModel> giftHistory(@Field("userId") String userId,
                                       @Field("type") String type);


    // Profile Verify
    @Multipart
    @POST("uploadPanAndAadhar")
    Call<Map> aadharVerify(@Part("userId") RequestBody userId,
                           @Part("type") RequestBody type,
                           @Part("panAadharNumber") RequestBody aadharPanNumber,
                           @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("getVerifyStatus")
    Call<Map> getVerifyStatus(@Field("userId") String userId);






    @FormUrlEncoded
    @POST("loginWithPhone")
    Call<ModelLoginPhone> loginWithPhone(@Field("phone") String phone,
                                         @Field("reg_id") String reg_id,
                                         @Field("device_type") String device_type);


    @FormUrlEncoded
    @POST("matchVerificationToken")
    Call<ModelLoginPhone> matchVerificationToken(@Field("phone") String phone,
                                                 @Field("otp") String otp);



    @FormUrlEncoded
    @POST("resendOTP")
    Call<ModelLoginPhone> resendOTP(@Field("phone") String phone);


//    videoReportList

    @GET("videoReportList")
    Call<ModelvideoReportList> videoReportList();



    @FormUrlEncoded
    @POST("userReportVideo")
        Call<Map> userReportVideo(
            @Field("userId") String userId,
            @Field("videoId") String videoId,
            @Field("report") String report
            );
    @FormUrlEncoded
    @POST("adminVideo")
    Call<Map> adminVideo(
            @Field("userId") String userId,
            @Field("videoId") String videoId,
            @Field("type") String type
    );


//    userReportVideo
}
