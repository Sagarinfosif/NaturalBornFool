package in.pureway.cinemaflix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtherUserDataModel {

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("notificationCount")
    @Expose
    private String notificationCount;

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

    public String getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(String notificationCount) {
        this.notificationCount = notificationCount;
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

        @SerializedName("muteStatus")
        @Expose
        private Boolean muteStatus;
        @SerializedName("coin")
        @Expose
        private String coin;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("bio")
        @Expose
        private String bio;
        @SerializedName("followingViewStatus")
        @Expose
        private Boolean followingViewStatus;
        @SerializedName("profilePhotoStatus")
        @Expose
        private Boolean profilePhotoStatus;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("video")
        @Expose
        private String video;
        @SerializedName("privateAccount")
        @Expose
        private Boolean privateAccount;
        @SerializedName("likeVideo")
        @Expose
        private Boolean likeVideo;
        @SerializedName("followers")
        @Expose
        private String followers;
        @SerializedName("likes")
        @Expose
        private Object likes;
        @SerializedName("videoCount")
        @Expose
        private String videoCount;
        @SerializedName("followStatus")
        @Expose
        private String followStatus;
        @SerializedName("following")
        @Expose
        private String following;

        public Boolean getMuteStatus() {
            return muteStatus;
        }

        public void setMuteStatus(Boolean muteStatus) {
            this.muteStatus = muteStatus;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public Boolean getFollowingViewStatus() {
            return followingViewStatus;
        }

        public void setFollowingViewStatus(Boolean followingViewStatus) {
            this.followingViewStatus = followingViewStatus;
        }

        public Boolean getProfilePhotoStatus() {
            return profilePhotoStatus;
        }

        public void setProfilePhotoStatus(Boolean profilePhotoStatus) {
            this.profilePhotoStatus = profilePhotoStatus;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public Boolean getPrivateAccount() {
            return privateAccount;
        }

        public void setPrivateAccount(Boolean privateAccount) {
            this.privateAccount = privateAccount;
        }

        public Boolean getLikeVideo() {
            return likeVideo;
        }

        public void setLikeVideo(Boolean likeVideo) {
            this.likeVideo = likeVideo;
        }

        public String getFollowers() {
            return followers;
        }

        public void setFollowers(String followers) {
            this.followers = followers;
        }

        public Object getLikes() {
            return likes;
        }

        public void setLikes(Object likes) {
            this.likes = likes;
        }

        public String getVideoCount() {
            return videoCount;
        }

        public void setVideoCount(String videoCount) {
            this.videoCount = videoCount;
        }

        public String getFollowStatus() {
            return followStatus;
        }

        public void setFollowStatus(String followStatus) {
            this.followStatus = followStatus;
        }

        public String getFollowing() {
            return following;
        }

        public void setFollowing(String following) {
            this.following = following;
        }

    }
}