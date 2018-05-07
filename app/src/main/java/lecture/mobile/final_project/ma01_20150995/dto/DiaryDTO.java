package lecture.mobile.final_project.ma01_20150995.dto;

import java.io.Serializable;

/**
 * Created by user on 2017-12-18.
 */

public class DiaryDTO implements Serializable{
    private int _id;
    private String image;
    private String title;
    private String date;
    private String memo;
    private String address;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
