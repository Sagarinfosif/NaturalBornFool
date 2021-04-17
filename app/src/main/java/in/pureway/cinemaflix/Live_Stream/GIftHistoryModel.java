package in.pureway.cinemaflix.Live_Stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GIftHistoryModel {

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
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("giftTitle")
        @Expose
        private String giftTitle;
        @SerializedName("giftCoin")
        @Expose
        private String giftCoin;
        @SerializedName("giftImage")
        @Expose
        private String giftImage;
        @SerializedName("giftUserId")
        @Expose
        private String giftUserId;
        @SerializedName("created")
        @Expose
        private String created;

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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGiftTitle() {
            return giftTitle;
        }

        public void setGiftTitle(String giftTitle) {
            this.giftTitle = giftTitle;
        }

        public String getGiftCoin() {
            return giftCoin;
        }

        public void setGiftCoin(String giftCoin) {
            this.giftCoin = giftCoin;
        }

        public String getGiftImage() {
            return giftImage;
        }

        public void setGiftImage(String giftImage) {
            this.giftImage = giftImage;
        }

        public String getGiftUserId() {
            return giftUserId;
        }

        public void setGiftUserId(String giftUserId) {
            this.giftUserId = giftUserId;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

    }

}