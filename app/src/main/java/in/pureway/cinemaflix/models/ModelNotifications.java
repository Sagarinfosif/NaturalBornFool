package in.pureway.cinemaflix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ModelNotifications {

    @SerializedName("messageCount")
    @Expose
    private String messageCount;
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;

    public String getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(String messageCount) {
        this.messageCount = messageCount;
    }

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

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public class Detail {

        @SerializedName("day")
        @Expose
        private String day;
        @SerializedName("listdetails")
        @Expose
        private List<Listdetail> listdetails = null;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public List<Listdetail> getListdetails() {
            return listdetails;
        }

        public void setListdetails(List<Listdetail> listdetails) {
            this.listdetails = listdetails;
        }

        public class Listdetail  {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("loginId")
            @Expose
            private String loginId;
            @SerializedName("userId")
            @Expose
            private String userId;
            @SerializedName("adminId")
            @Expose
            private String adminId;
            @SerializedName("videoId")
            @Expose
            private String videoId;
            @SerializedName("commentId")
            @Expose
            private String commentId;
            @SerializedName("message")
            @Expose
            private String message;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("videoUrl")
            @Expose
            private String videoUrl;
            @SerializedName("notiDate")
            @Expose
            private String notiDate;
            @SerializedName("created")
            @Expose
            private String created;
            @SerializedName("followStatus")
            @Expose
            private Boolean followStatus;
            @SerializedName("time")
            @Expose
            private String time;
            @SerializedName("image")
            @Expose
            private String image;
            @SerializedName("video")
            @Expose
            private String video;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLoginId() {
                return loginId;
            }

            public void setLoginId(String loginId) {
                this.loginId = loginId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getAdminId() {
                return adminId;
            }

            public void setAdminId(String adminId) {
                this.adminId = adminId;
            }

            public String getVideoId() {
                return videoId;
            }

            public void setVideoId(String videoId) {
                this.videoId = videoId;
            }

            public String getCommentId() {
                return commentId;
            }

            public void setCommentId(String commentId) {
                this.commentId = commentId;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getVideoUrl() {
                return videoUrl;
            }

            public void setVideoUrl(String videoUrl) {
                this.videoUrl = videoUrl;
            }

            public String getNotiDate() {
                return notiDate;
            }

            public void setNotiDate(String notiDate) {
                this.notiDate = notiDate;
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }

            public Boolean getFollowStatus() {
                return followStatus;
            }

            public void setFollowStatus(Boolean followStatus) {
                this.followStatus = followStatus;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
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

        }
    }


}