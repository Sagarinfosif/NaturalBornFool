package in.pureway.cinemaflix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelHashTag {


    public ModelHashTag(String success, String message, List<Detail> details) {
        this.success = success;
        this.message = message;
        this.details = details;
    }

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

    public static class Detail {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("hashtag")
        @Expose
        private String hashtag;
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
