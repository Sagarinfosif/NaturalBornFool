package in.pureway.cinemaflix.models;

public class ModelInviteFriends {
    String contactName,contactNo;
    String image;

    public ModelInviteFriends(String contactName, String contactNo, String image) {
        this.contactName = contactName;
        this.contactNo = contactNo;
        this.image = image;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
