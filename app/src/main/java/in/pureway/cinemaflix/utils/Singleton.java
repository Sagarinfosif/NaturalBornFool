package in.pureway.cinemaflix.utils;

import in.pureway.cinemaflix.models.GetSoundDetailsPojo;
import in.pureway.cinemaflix.models.ModelHashTagsDetails;
import in.pureway.cinemaflix.models.ModelLikedVideos;
import in.pureway.cinemaflix.models.ModelMyUploadedVideos;
import in.pureway.cinemaflix.models.ModelNotifications;
import in.pureway.cinemaflix.models.SearchAllPojo;
import in.pureway.cinemaflix.models.UpdateListModel;

import java.util.ArrayList;
import java.util.List;

public class Singleton {

    List<ModelLikedVideos.Detail> likedVideoList;
    List<ModelMyUploadedVideos.Detail> listMyUploaded;
    List<UpdateListModel> listFollowUn=new ArrayList<>();

    public List<UpdateListModel> getListFollowUn() {
        return listFollowUn;
    }

    public void setListFollowUn(List<UpdateListModel> listFollowUn) {
        this.listFollowUn = listFollowUn;
    }

    public String getShootcheck() {
        return shootcheck;
    }

    public void setShootcheck(String shootcheck) {
        this.shootcheck = shootcheck;
    }

    String shootcheck="";


    public List<ModelNotifications.Detail.Listdetail> getListREjDelList() {
        return listREjDelList;
    }

    public void setListREjDelList(List<ModelNotifications.Detail.Listdetail> listREjDelList) {
        this.listREjDelList = listREjDelList;
    }

    List<ModelNotifications.Detail.Listdetail> listREjDelList;
    List<SearchAllPojo.Details.VideoList> searchVideoList;
    List<GetSoundDetailsPojo.Details.SoundVideo> soundDetailList;
    List<ModelHashTagsDetails.Details.HashtagVideo> hashTagDetailList;
    List<String> blockedUserList;
    String soundName;
    String soundId;
    String idType;
    String myId;
    String otherUserId;
    String emailPhone;
    String RegisterType;
    String password;
    String notificationVideoId;
    String  galleryStatus;
    String  isWidth,isHeight;

    public String getIsWidth() {
        return isWidth;
    }

    public void setIsWidth(String isWidth) {
        this.isWidth = isWidth;
    }

    public String getIsHeight() {
        return isHeight;
    }

    public void setIsHeight(String isHeight) {
        this.isHeight = isHeight;
    }

    public String getGalleryStatus() {
        return galleryStatus;
    }

    public void setGalleryStatus(String galleryStatus) {
        this.galleryStatus = galleryStatus;
    }

    String viewId;

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getUpdateNumber() {
        return updateNumber;
    }

    public void setUpdateNumber(String updateNumber) {
        this.updateNumber = updateNumber;
    }

    String updateNumber;

    public String getBackVideoStatus() {
        return BackVideoStatus;
    }

    public void setBackVideoStatus(String backVideoStatus) {
        BackVideoStatus = backVideoStatus;
    }

    String BackVideoStatus;

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    String videoType;
    public String getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(String videoLength) {
        this.videoLength = videoLength;
    }

    String videoLength;

    public String getUpdateNumberStatus() {
        return UpdateNumberStatus;
    }

    public void setUpdateNumberStatus(String updateNumberStatus) {
        UpdateNumberStatus = updateNumberStatus;
    }

    String UpdateNumberStatus;
    static int loginType;
    boolean isGalleryVideo=false;

    public boolean isGalleryVideo() {
        return isGalleryVideo;
    }

    public void setGalleryVideo(boolean galleryVideo) {
        isGalleryVideo = galleryVideo;
    }

    public List<String> getBlockedUserList() {
        return blockedUserList;
    }

    public void setBlockedUserList(List<String> blockedUserList) {
        this.blockedUserList = blockedUserList;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public List<ModelHashTagsDetails.Details.HashtagVideo> getHashTagDetailList() {
        return hashTagDetailList;
    }

    public void setHashTagDetailList(List<ModelHashTagsDetails.Details.HashtagVideo> hashTagDetailList) {
        this.hashTagDetailList = hashTagDetailList;
    }

    public List<GetSoundDetailsPojo.Details.SoundVideo> getSoundDetailList() {
        return soundDetailList;
    }

    public void setSoundDetailList(List<GetSoundDetailsPojo.Details.SoundVideo> soundDetailList) {
        this.soundDetailList = soundDetailList;
    }

    public List<SearchAllPojo.Details.VideoList> getSearchVideoList() {
        return searchVideoList;
    }

    public void setSearchVideoList(List<SearchAllPojo.Details.VideoList> searchVideoList) {
        this.searchVideoList = searchVideoList;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public String getSoundId() {
        return soundId;
    }

    public void setSoundId(String soundId) {
        this.soundId = soundId;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public List<ModelMyUploadedVideos.Detail> getListMyUploaded() {
        return listMyUploaded;
    }

    public void setListMyUploaded(List<ModelMyUploadedVideos.Detail> listMyUploaded) {
        this.listMyUploaded = listMyUploaded;
    }

    public List<ModelLikedVideos.Detail> getLikedVideoList() {
        return likedVideoList;
    }

    public void setLikedVideoList(List<ModelLikedVideos.Detail> likedVideoList) {
        this.likedVideoList = likedVideoList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailPhone() {
        return emailPhone;
    }

    public void setEmailPhone(String emailPhone) {
        this.emailPhone = emailPhone;
    }

    public String getRegisterType() {
        return RegisterType;
    }

    public void setRegisterType(String registerType) {
        RegisterType = registerType;
    }

    public String getNotificationVideoId() {
        return notificationVideoId;
    }

    public void setNotificationVideoId(String notificationVideoId) {
        this.notificationVideoId = notificationVideoId;
    }

    public void setListREjDelList(String notiRejDelList, ModelNotifications.Detail.Listdetail listdetail) {
    }
}
