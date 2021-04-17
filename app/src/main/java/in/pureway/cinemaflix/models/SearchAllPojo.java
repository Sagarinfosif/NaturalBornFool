package in.pureway.cinemaflix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchAllPojo {

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

        @SerializedName("peopleList")
        @Expose
        private List<PersonList> peopleList = null;
        @SerializedName("videoList")
        @Expose
        private List<VideoList> videoList = null;
        @SerializedName("hasTagList")
        @Expose
        private List<HasTagList> hasTagList = null;
        @SerializedName("soundList")
        @Expose
        private List<SoundList> soundList = null;

        public List<PersonList> getPeopleList() {
            return peopleList;
        }

        public void setPeopleList(List<PersonList> peopleList) {
            this.peopleList = peopleList;
        }

        public List<VideoList> getVideoList() {
            return videoList;
        }

        public void setVideoList(List<VideoList> videoList) {
            this.videoList = videoList;
        }

        public List<HasTagList> getHasTagList() {
            return hasTagList;
        }

        public void setHasTagList(List<HasTagList> hasTagList) {
            this.hasTagList = hasTagList;
        }

        public List<SoundList> getSoundList() {
            return soundList;
        }

        public void setSoundList(List<SoundList> soundList) {
            this.soundList = soundList;
        }
        public class PersonList {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("username")
            @Expose
            private String username;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("image")
            @Expose
            private String image;
            @SerializedName("followerCount")
            @Expose
            private String followerCount;
            @SerializedName("followStatus")
            @Expose
            private String followStatus;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getFollowerCount() {
                return followerCount;
            }

            public void setFollowerCount(String followerCount) {
                this.followerCount = followerCount;
            }

            public String getFollowStatus() {
                return followStatus;
            }

            public void setFollowStatus(String followStatus) {
                this.followStatus = followStatus;
            }

        }




        public class VideoList {

            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("followers")
            @Expose
            private String followers;
            @SerializedName("username")
            @Expose
            private String username;
            @SerializedName("image")
            @Expose
            private String image;
            @SerializedName("video")
            @Expose
            private String video;
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

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
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

        }



        public class HasTagList {

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


        public class SoundList {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("userId")
            @Expose
            private String userId;
            @SerializedName("sound")
            @Expose
            private String sound;
            @SerializedName("soundImg")
            @Expose
            private String soundImg;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("soundCount")
            @Expose
            private String soundCount;
            @SerializedName("created")
            @Expose
            private String created;
            @SerializedName("updated")
            @Expose
            private String updated;
            @SerializedName("favoritesStatus")
            @Expose
            private String favoritesStatus;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getSound() {
                return sound;
            }

            public void setSound(String sound) {
                this.sound = sound;
            }

            public String getSoundImg() {
                return soundImg;
            }

            public void setSoundImg(String soundImg) {
                this.soundImg = soundImg;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getSoundCount() {
                return soundCount;
            }

            public void setSoundCount(String soundCount) {
                this.soundCount = soundCount;
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

            public String getFavoritesStatus() {
                return favoritesStatus;
            }

            public void setFavoritesStatus(String favoritesStatus) {
                this.favoritesStatus = favoritesStatus;
            }

        }
    }
}