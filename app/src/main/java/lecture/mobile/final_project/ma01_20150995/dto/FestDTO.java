package lecture.mobile.final_project.ma01_20150995.dto;

import java.io.Serializable;

/**
 * Created by user on 2017-11-24.
 */

public class FestDTO implements Serializable{
    private String title;
    private String place;
    private String startDay;
    private String EndDay;
    private String aboutFes;
    private String url;
    private String RoadAdd;
    private String Add;
    private int _id;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return EndDay;
    }

    public void setEndDay(String endDay) {
        EndDay = endDay;
    }

    public String getAboutFes() {
        return aboutFes;
    }

    public void setAboutFes(String aboutFes) {
        this.aboutFes = aboutFes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRoadAdd() {
        return RoadAdd;
    }

    public void setRoadAdd(String roadAdd) {
        RoadAdd = roadAdd;
    }

    public String getAdd() {
        return Add;
    }

    public void setAdd(String add) {
        Add = add;
    }
}
