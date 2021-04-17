package in.pureway.cinemaflix.adapters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelVideoTesting {
    @SerializedName("notificationCount")
    @Expose
    private String notificationCount;
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;

    public String getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(String notificationCount) {
        this.notificationCount = notificationCount;
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
        @SerializedName("viewCount")
        @Expose
        private String viewCount;
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
        @SerializedName("commentCount")
        @Expose
        private String commentCount;
        @SerializedName("downloadPath")
        @Expose
        private String downloadPath;
        @SerializedName("videolength")
        @Expose
        private String videolength;
        @SerializedName("videoType")
        @Expose
        private String videoType;
        @SerializedName("hashtagTitle")
        @Expose
        private String hashtagTitle;
        @SerializedName("hastagLists")
        @Expose
        private List<HastagList> hastagLists = null;
        @SerializedName("likeStatus")
        @Expose
        private Boolean likeStatus;
        @SerializedName("likeCount")
        @Expose
        private String likeCount;
        @SerializedName("followStatus")
        @Expose
        private String followStatus;
        @SerializedName("advertisementStatus")
        @Expose
        private String advertisementStatus;
        @SerializedName("videoTitle")
        @Expose
        private String videoTitle;
        @SerializedName("videoUrl")
        @Expose
        private String videoUrl;
        @SerializedName("buttonName")
        @Expose
        private String buttonName;
        @SerializedName("sponsored")
        @Expose
        private String sponsored;
        @SerializedName("setLimit")
        @Expose
        private String setLimit;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("hitCount")
        @Expose
        private String hitCount;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("updated")
        @Expose
        private String updated;

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

        public String getViewCount() {
            return viewCount;
        }

        public void setViewCount(String viewCount) {
            this.viewCount = viewCount;
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

        public String getVideolength() {
            return videolength;
        }

        public void setVideolength(String videolength) {
            this.videolength = videolength;
        }

        public String getVideoType() {
            return videoType;
        }

        public void setVideoType(String videoType) {
            this.videoType = videoType;
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

        public String getAdvertisementStatus() {
            return advertisementStatus;
        }

        public void setAdvertisementStatus(String advertisementStatus) {
            this.advertisementStatus = advertisementStatus;
        }

        public String getVideoTitle() {
            return videoTitle;
        }

        public void setVideoTitle(String videoTitle) {
            this.videoTitle = videoTitle;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getButtonName() {
            return buttonName;
        }

        public void setButtonName(String buttonName) {
            this.buttonName = buttonName;
        }

        public String getSponsored() {
            return sponsored;
        }

        public void setSponsored(String sponsored) {
            this.sponsored = sponsored;
        }

        public String getSetLimit() {
            return setLimit;
        }

        public void setSetLimit(String setLimit) {
            this.setLimit = setLimit;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getHitCount() {
            return hitCount;
        }

        public void setHitCount(String hitCount) {
            this.hitCount = hitCount;
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