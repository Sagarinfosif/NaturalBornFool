package in.pureway.cinemaflix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetSoundDetailsPojo {

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

        @SerializedName("soundInfo")
        @Expose
        private SoundInfo soundInfo;
        @SerializedName("soundVideo")
        @Expose
        private List<SoundVideo> soundVideo = null;

        public SoundInfo getSoundInfo() {
            return soundInfo;
        }

        public void setSoundInfo(SoundInfo soundInfo) {
            this.soundInfo = soundInfo;
        }

        public List<SoundVideo> getSoundVideo() {
            return soundVideo;
        }

        public void setSoundVideo(List<SoundVideo> soundVideo) {
            this.soundVideo = soundVideo;
        }

        public class SoundInfo {

            @SerializedName("username")
            @Expose
            private String username;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("followers")
            @Expose
            private String followers;
            @SerializedName("image")
            @Expose
            private String image;
            @SerializedName("soundTitle")
            @Expose
            private String soundTitle;
            @SerializedName("soundPath")
            @Expose
            private String soundPath;
            @SerializedName("videoCount")
            @Expose
            private String videoCount;
            @SerializedName("description")
            @Expose
            private String description;
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
            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("userId")
            @Expose
            private String userId;
            @SerializedName("videoPath")
            @Expose
            private String videoPath;
            @SerializedName("favoritesStatus")
            @Expose
            private String favoritesStatus;
            @SerializedName("likeStatus")
            @Expose
            private Boolean likeStatus;
            @SerializedName("followStatus")
            @Expose
            private String followStatus;
            @SerializedName("hashTag")
            @Expose
            private String hashTag;
            @SerializedName("hashtagTitle")
            @Expose
            private String hashtagTitle;
            @SerializedName("countVideo")
            @Expose
            private String countVideo;

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

            public String getSoundPath() {
                return soundPath;
            }

            public void setSoundPath(String soundPath) {
                this.soundPath = soundPath;
            }

            public String getVideoCount() {
                return videoCount;
            }

            public void setVideoCount(String videoCount) {
                this.videoCount = videoCount;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
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

            public String getVideoPath() {
                return videoPath;
            }

            public void setVideoPath(String videoPath) {
                this.videoPath = videoPath;
            }

            public String getFavoritesStatus() {
                return favoritesStatus;
            }

            public void setFavoritesStatus(String favoritesStatus) {
                this.favoritesStatus = favoritesStatus;
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

            public String getHashTag() {
                return hashTag;
            }

            public void setHashTag(String hashTag) {
                this.hashTag = hashTag;
            }

            public String getHashtagTitle() {
                return hashtagTitle;
            }

            public void setHashtagTitle(String hashtagTitle) {
                this.hashtagTitle = hashtagTitle;
            }

            public String getCountVideo() {
                return countVideo;
            }

            public void setCountVideo(String countVideo) {
                this.countVideo = countVideo;
            }

        }

        public class SoundVideo {

            @SerializedName("username")
            @Expose
            private String username;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("followers")
            @Expose
            private String followers;
            @SerializedName("image")
            @Expose
            private String image;
            @SerializedName("soundTitle")
            @Expose
            private String soundTitle;
            @SerializedName("soundPath")
            @Expose
            private String soundPath;
            @SerializedName("videoCount")
            @Expose
            private String videoCount;
            @SerializedName("description")
            @Expose
            private String description;
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
            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("userId")
            @Expose
            private String userId;
            @SerializedName("videoPath")
            @Expose
            private String videoPath;
            @SerializedName("favoritesStatus")
            @Expose
            private String favoritesStatus;
            @SerializedName("likeStatus")
            @Expose
            private Boolean likeStatus;
            @SerializedName("followStatus")
            @Expose
            private String followStatus;
            @SerializedName("hashTag")
            @Expose
            private String hashTag;
            @SerializedName("hashtagTitle")
            @Expose
            private String hashtagTitle;
            @SerializedName("countVideo")
            @Expose
            private String countVideo;

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

            public String getSoundPath() {
                return soundPath;
            }

            public void setSoundPath(String soundPath) {
                this.soundPath = soundPath;
            }

            public String getVideoCount() {
                return videoCount;
            }

            public void setVideoCount(String videoCount) {
                this.videoCount = videoCount;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
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

            public String getVideoPath() {
                return videoPath;
            }

            public void setVideoPath(String videoPath) {
                this.videoPath = videoPath;
            }

            public String getFavoritesStatus() {
                return favoritesStatus;
            }

            public void setFavoritesStatus(String favoritesStatus) {
                this.favoritesStatus = favoritesStatus;
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

            public String getHashTag() {
                return hashTag;
            }

            public void setHashTag(String hashTag) {
                this.hashTag = hashTag;
            }

            public String getHashtagTitle() {
                return hashtagTitle;
            }

            public void setHashtagTitle(String hashtagTitle) {
                this.hashtagTitle = hashtagTitle;
            }

            public String getCountVideo() {
                return countVideo;
            }

            public void setCountVideo(String countVideo) {
                this.countVideo = countVideo;
            }

        }
    }

}