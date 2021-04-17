package in.pureway.cinemaflix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelLoginPhone {


    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private Details details;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public class Details {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("badge")
        @Expose
        private String badge;
        @SerializedName("loginOtp")
        @Expose
        private String loginOtp;
        @SerializedName("privateAccount")
        @Expose
        private String privateAccount;
        @SerializedName("likeVideo")
        @Expose
        private String likeVideo;
        @SerializedName("followingUser")
        @Expose
        private String followingUser;
        @SerializedName("profilePhotoStatus")
        @Expose
        private String profilePhotoStatus;
        @SerializedName("onlineStatus")
        @Expose
        private String onlineStatus;
        @SerializedName("coin")
        @Expose
        private String coin;
        @SerializedName("purchasedCoin")
        @Expose
        private String purchasedCoin;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("bio")
        @Expose
        private String bio;
        @SerializedName("social_id")
        @Expose
        private String socialId;
        @SerializedName("dob")
        @Expose
        private Object dob;
        @SerializedName("video")
        @Expose
        private String video;
        @SerializedName("followerCount")
        @Expose
        private String followerCount;
        @SerializedName("likeNotifaction")
        @Expose
        private String likeNotifaction;
        @SerializedName("commentNotification")
        @Expose
        private String commentNotification;
        @SerializedName("followersNotification")
        @Expose
        private String followersNotification;
        @SerializedName("messageNotification")
        @Expose
        private String messageNotification;
        @SerializedName("videoNotification")
        @Expose
        private String videoNotification;
        @SerializedName("reg_id")
        @Expose
        private String regId;
        @SerializedName("device_type")
        @Expose
        private String deviceType;
        @SerializedName("login_type")
        @Expose
        private String loginType;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("updated")
        @Expose
        private String updated;
        @SerializedName("status")
        @Expose
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBadge() {
            return badge;
        }

        public void setBadge(String badge) {
            this.badge = badge;
        }

        public String getLoginOtp() {
            return loginOtp;
        }

        public void setLoginOtp(String loginOtp) {
            this.loginOtp = loginOtp;
        }

        public String getPrivateAccount() {
            return privateAccount;
        }

        public void setPrivateAccount(String privateAccount) {
            this.privateAccount = privateAccount;
        }

        public String getLikeVideo() {
            return likeVideo;
        }

        public void setLikeVideo(String likeVideo) {
            this.likeVideo = likeVideo;
        }

        public String getFollowingUser() {
            return followingUser;
        }

        public void setFollowingUser(String followingUser) {
            this.followingUser = followingUser;
        }

        public String getProfilePhotoStatus() {
            return profilePhotoStatus;
        }

        public void setProfilePhotoStatus(String profilePhotoStatus) {
            this.profilePhotoStatus = profilePhotoStatus;
        }

        public String getOnlineStatus() {
            return onlineStatus;
        }

        public void setOnlineStatus(String onlineStatus) {
            this.onlineStatus = onlineStatus;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getPurchasedCoin() {
            return purchasedCoin;
        }

        public void setPurchasedCoin(String purchasedCoin) {
            this.purchasedCoin = purchasedCoin;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getSocialId() {
            return socialId;
        }

        public void setSocialId(String socialId) {
            this.socialId = socialId;
        }

        public Object getDob() {
            return dob;
        }

        public void setDob(Object dob) {
            this.dob = dob;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getFollowerCount() {
            return followerCount;
        }

        public void setFollowerCount(String followerCount) {
            this.followerCount = followerCount;
        }

        public String getLikeNotifaction() {
            return likeNotifaction;
        }

        public void setLikeNotifaction(String likeNotifaction) {
            this.likeNotifaction = likeNotifaction;
        }

        public String getCommentNotification() {
            return commentNotification;
        }

        public void setCommentNotification(String commentNotification) {
            this.commentNotification = commentNotification;
        }

        public String getFollowersNotification() {
            return followersNotification;
        }

        public void setFollowersNotification(String followersNotification) {
            this.followersNotification = followersNotification;
        }

        public String getMessageNotification() {
            return messageNotification;
        }

        public void setMessageNotification(String messageNotification) {
            this.messageNotification = messageNotification;
        }

        public String getVideoNotification() {
            return videoNotification;
        }

        public void setVideoNotification(String videoNotification) {
            this.videoNotification = videoNotification;
        }

        public String getRegId() {
            return regId;
        }

        public void setRegId(String regId) {
            this.regId = regId;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getLoginType() {
            return loginType;
        }

        public void setLoginType(String loginType) {
            this.loginType = loginType;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }
}
