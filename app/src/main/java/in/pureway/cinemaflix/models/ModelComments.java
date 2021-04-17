package in.pureway.cinemaflix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelComments {


    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("commentCount")
    @Expose
    private String commentCount;
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

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public class Detail {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("videoId")
        @Expose
        private String videoId;
        @SerializedName("ownerId")
        @Expose
        private String ownerId;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("updated")
        @Expose
        private String updated;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("userImage")
        @Expose
        private String userImage;
        @SerializedName("likeStatus")
        @Expose
        private Boolean likeStatus;
        @SerializedName("likeCount")
        @Expose
        private String likeCount;
        @SerializedName("subComment")
        @Expose
        private List<SubComment> subComment = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
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

        public List<SubComment> getSubComment() {
            return subComment;
        }

        public void setSubComment(List<SubComment> subComment) {
            this.subComment = subComment;
        }

        public class SubComment {

            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("username")
            @Expose
            private String username;
            @SerializedName("image")
            @Expose
            private String image;
            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("userId")
            @Expose
            private String userId;
            @SerializedName("videoId")
            @Expose
            private String videoId;
            @SerializedName("commentId")
            @Expose
            private String commentId;
            @SerializedName("comment")
            @Expose
            private String comment;
            @SerializedName("created")
            @Expose
            private String created;
            @SerializedName("userImage")
            @Expose
            private String userImage;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }

            public String getUserImage() {
                return userImage;
            }

            public void setUserImage(String userImage) {
                this.userImage = userImage;
            }

        }
    }
}
