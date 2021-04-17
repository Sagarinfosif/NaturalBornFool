package in.pureway.cinemaflix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelLikedVideos {
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;

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

        @SerializedName("videoId")
        @Expose
        private String videoId;
        @SerializedName("ownerId")
        @Expose
        private String ownerId;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("followers")
        @Expose
        private String followers;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("userImage")
        @Expose
        private String userImage;
        @SerializedName("userVideo")
        @Expose
        private String userVideo;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("hashTag")
        @Expose
        private String hashTag;
        @SerializedName("downloadCount")
        @Expose
        private String downloadCount;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("videoPath")
        @Expose
        private String videoPath;
        @SerializedName("videolength")
        @Expose
        private String videolength;
        @SerializedName("allowComment")
        @Expose
        private String allowComment;
        @SerializedName("allowDuetReact")
        @Expose
        private String allowDuetReact;
        @SerializedName("allowDownloads")
        @Expose
        private String allowDownloads;
        @SerializedName("viewVideo")
        @Expose
        private String viewVideo;
        @SerializedName("soundId")
        @Expose
        private String soundId;
        @SerializedName("commentCount")
        @Expose
        private String commentCount;
        @SerializedName("viewCount")
        @Expose
        private String viewCount;
        @SerializedName("likeCount")
        @Expose
        private String likeCount;
        @SerializedName("categoryId")
        @Expose
        private String categoryId;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("videoType")
        @Expose
        private String videoType;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("downloadPath")
        @Expose
        private String downloadPath;
        @SerializedName("soundTitle")
        @Expose
        private String soundTitle;
        @SerializedName("hashtagTitle")
        @Expose
        private String hashtagTitle;
        @SerializedName("likeStatus")
        @Expose
        private Boolean likeStatus;
        @SerializedName("followStatus")
        @Expose
        private String followStatus;
        @SerializedName("shareCount")
        @Expose
        private String shareCount;

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFollowers() {
            return followers;
        }

        public void setFollowers(String followers) {
            this.followers = followers;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public String getUserVideo() {
            return userVideo;
        }

        public void setUserVideo(String userVideo) {
            this.userVideo = userVideo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getHashTag() {
            return hashTag;
        }

        public void setHashTag(String hashTag) {
            this.hashTag = hashTag;
        }

        public String getDownloadCount() {
            return downloadCount;
        }

        public void setDownloadCount(String downloadCount) {
            this.downloadCount = downloadCount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVideoPath() {
            return videoPath;
        }

        public void setVideoPath(String videoPath) {
            this.videoPath = videoPath;
        }

        public String getVideolength() {
            return videolength;
        }

        public void setVideolength(String videolength) {
            this.videolength = videolength;
        }

        public String getAllowComment() {
            return allowComment;
        }

        public void setAllowComment(String allowComment) {
            this.allowComment = allowComment;
        }

        public String getAllowDuetReact() {
            return allowDuetReact;
        }

        public void setAllowDuetReact(String allowDuetReact) {
            this.allowDuetReact = allowDuetReact;
        }

        public String getAllowDownloads() {
            return allowDownloads;
        }

        public void setAllowDownloads(String allowDownloads) {
            this.allowDownloads = allowDownloads;
        }

        public String getViewVideo() {
            return viewVideo;
        }

        public void setViewVideo(String viewVideo) {
            this.viewVideo = viewVideo;
        }

        public String getSoundId() {
            return soundId;
        }

        public void setSoundId(String soundId) {
            this.soundId = soundId;
        }

        public String getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(String commentCount) {
            this.commentCount = commentCount;
        }

        public String getViewCount() {
            return viewCount;
        }

        public void setViewCount(String viewCount) {
            this.viewCount = viewCount;
        }

        public String getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(String likeCount) {
            this.likeCount = likeCount;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getVideoType() {
            return videoType;
        }

        public void setVideoType(String videoType) {
            this.videoType = videoType;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getDownloadPath() {
            return downloadPath;
        }

        public void setDownloadPath(String downloadPath) {
            this.downloadPath = downloadPath;
        }

        public String getSoundTitle() {
            return soundTitle;
        }

        public void setSoundTitle(String soundTitle) {
            this.soundTitle = soundTitle;
        }

        public String getHashtagTitle() {
            return hashtagTitle;
        }

        public void setHashtagTitle(String hashtagTitle) {
            this.hashtagTitle = hashtagTitle;
        }

        public Boolean getLikeStatus() {
            return likeStatus;
        }

        public void setLikeStatus(Boolean likeStatus) {
            this.likeStatus = likeStatus;
        }

        public String getFollowStatus() {
            return followStatus;
        }

        public void setFollowStatus(String followStatus) {
            this.followStatus = followStatus;
        }

        public String getShareCount() {
            return shareCount;
        }

        public void setShareCount(String shareCount) {
            this.shareCount = shareCount;
        }

    }
}