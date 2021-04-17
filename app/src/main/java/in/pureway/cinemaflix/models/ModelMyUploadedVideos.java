package in.pureway.cinemaflix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelMyUploadedVideos {
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

        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("followers")
        @Expose
        private String followers;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("soundTitle")
        @Expose
        private String soundTitle;
        @SerializedName("soundId")
        @Expose
        private String soundId;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("hashtag")
        @Expose
        private String hashtag;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("videoPath")
        @Expose
        private String videoPath;
        @SerializedName("allowComment")
        @Expose
        private String allowComment;
        @SerializedName("allowDownloads")
        @Expose
        private String allowDownloads;
        @SerializedName("viewVideo")
        @Expose
        private String viewVideo;
        @SerializedName("imageThumb")
        @Expose
        private String imageThumb;
        @SerializedName("rejectStatus")
        @Expose
        private String rejectStatus;
        @SerializedName("videoType")
        @Expose
        private String videoType;
        @SerializedName("viewCount")
        @Expose
        private String viewCount;
        @SerializedName("commentCount")
        @Expose
        private String commentCount;
        @SerializedName("downloadPath")
        @Expose
        private String downloadPath;
        @SerializedName("downloadCount")
        @Expose
        private String downloadCount;
        @SerializedName("categoryId")
        @Expose
        private String categoryId;
        @SerializedName("allowDuetReact")
        @Expose
        private String allowDuetReact;
        @SerializedName("likeStatus")
        @Expose
        private Boolean likeStatus;
        @SerializedName("likeCount")
        @Expose
        private String likeCount;
        @SerializedName("followStatus")
        @Expose
        private String followStatus;
        @SerializedName("hashtagTitle")
        @Expose
        private String hashtagTitle;
        @SerializedName("hastagLists")
        @Expose
        private List<HastagList> hastagLists = null;

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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getSoundTitle() {
            return soundTitle;
        }

        public void setSoundTitle(String soundTitle) {
            this.soundTitle = soundTitle;
        }

        public String getSoundId() {
            return soundId;
        }

        public void setSoundId(String soundId) {
            this.soundId = soundId;
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

        public String getHashtag() {
            return hashtag;
        }

        public void setHashtag(String hashtag) {
            this.hashtag = hashtag;
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

        public String getAllowComment() {
            return allowComment;
        }

        public void setAllowComment(String allowComment) {
            this.allowComment = allowComment;
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

        public String getImageThumb() {
            return imageThumb;
        }

        public void setImageThumb(String imageThumb) {
            this.imageThumb = imageThumb;
        }

        public String getRejectStatus() {
            return rejectStatus;
        }

        public void setRejectStatus(String rejectStatus) {
            this.rejectStatus = rejectStatus;
        }

        public String getVideoType() {
            return videoType;
        }

        public void setVideoType(String videoType) {
            this.videoType = videoType;
        }

        public String getViewCount() {
            return viewCount;
        }

        public void setViewCount(String viewCount) {
            this.viewCount = viewCount;
        }

        public String getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(String commentCount) {
            this.commentCount = commentCount;
        }

        public String getDownloadPath() {
            return downloadPath;
        }

        public void setDownloadPath(String downloadPath) {
            this.downloadPath = downloadPath;
        }

        public String getDownloadCount() {
            return downloadCount;
        }

        public void setDownloadCount(String downloadCount) {
            this.downloadCount = downloadCount;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getAllowDuetReact() {
            return allowDuetReact;
        }

        public void setAllowDuetReact(String allowDuetReact) {
            this.allowDuetReact = allowDuetReact;
        }

        public Boolean getLikeStatus() {
            return likeStatus;
        }

        public void setLikeStatus(Boolean likeStatus) {
            this.likeStatus = likeStatus;
        }

        public String getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(String likeCount) {
            this.likeCount = likeCount;
        }

        public String getFollowStatus() {
            return followStatus;
        }

        public void setFollowStatus(String followStatus) {
            this.followStatus = followStatus;
        }

        public String getHashtagTitle() {
            return hashtagTitle;
        }

        public void setHashtagTitle(String hashtagTitle) {
            this.hashtagTitle = hashtagTitle;
        }

        public List<HastagList> getHastagLists() {
            return hastagLists;
        }

        public void setHastagLists(List<HastagList> hastagLists) {
            this.hastagLists = hastagLists;
        }
        public class HastagList {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("hashtag")
            @Expose
            private String hashtag;
            @SerializedName("userId")
            @Expose
            private String userId;
            @SerializedName("videoCount")
            @Expose
            private String videoCount;
            @SerializedName("created")
            @Expose
            private String created;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getHashtag() {
                return hashtag;
            }

            public void setHashtag(String hashtag) {
                this.hashtag = hashtag;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getVideoCount() {
                return videoCount;
            }

            public void setVideoCount(String videoCount) {
                this.videoCount = videoCount;
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }

        }
    }

}