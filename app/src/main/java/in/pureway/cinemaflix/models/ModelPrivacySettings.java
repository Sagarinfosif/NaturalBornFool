package in.pureway.cinemaflix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPrivacySettings {

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

        @SerializedName("privateAccount")
        @Expose
        private Boolean privateAccount;
        @SerializedName("followingViewStatus")
        @Expose
        private Boolean followingViewStatus;
        @SerializedName("profilePhotoStatus")
        @Expose
        private Boolean profilePhotoStatus;
        @SerializedName("likeVideo")
        @Expose
        private Boolean likeVideo;

        public Boolean getPrivateAccount() {
            return privateAccount;
        }

        public void setPrivateAccount(Boolean privateAccount) {
            this.privateAccount = privateAccount;
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

        public Boolean getLikeVideo() {
            return likeVideo;
        }

        public void setLikeVideo(Boolean likeVideo) {
            this.likeVideo = likeVideo;
        }

    }
}
