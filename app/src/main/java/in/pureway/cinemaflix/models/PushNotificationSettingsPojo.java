package in.pureway.cinemaflix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PushNotificationSettingsPojo {

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

    }

}