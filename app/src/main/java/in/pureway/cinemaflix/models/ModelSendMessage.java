package in.pureway.cinemaflix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ModelSendMessage  implements Serializable {
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public static class Detail implements Serializable {

        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("sender_id")
        @Expose
        private String senderId;
        @SerializedName("reciver_id")
        @Expose
        private String reciverId;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("messageType")
        @Expose
        private String messageType;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("time")
        @Expose
        private String time;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getReciverId() {
            return reciverId;
        }

        public void setReciverId(String reciverId) {
            this.reciverId = reciverId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

    }

}