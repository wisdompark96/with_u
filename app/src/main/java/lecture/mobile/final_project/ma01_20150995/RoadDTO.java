package lecture.mobile.final_project.ma01_20150995;

import java.io.Serializable;

/**
 * Created by user on 2017-11-24.
 */

public class RoadDTO implements Serializable{
    private int _id;
    private String start;

    private String title;
    private String intro;
    private int totalLength;

    private String totalTime;
    private String startAd;
    private String endAd;
    private String startRoad;
    private String EndRoad;
    private String aboutRoad;
    private String end;

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }


    public String getStartAd() {
        return startAd;
    }

    public void setStartAd(String startAd) {
        this.startAd = startAd;
    }

    public String getEndAd() {
        return endAd;
    }

    public void setEndAd(String endAd) {
        this.endAd = endAd;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getStartRoad() {
        return startRoad;
    }

    public void setStartRoad(String startRoad) {
        this.startRoad = startRoad;
    }

    public String getEndRoad() {
        return EndRoad;
    }

    public void setEndRoad(String endRoad) {
        EndRoad = endRoad;
    }

    public String getAboutRoad() {
        return aboutRoad;
    }

    public void setAboutRoad(String aboutRoad) {
        this.aboutRoad = aboutRoad;
    }


}
