package lecture.mobile.final_project.ma01_20150995.dto;

import java.io.Serializable;

/**
 * Created by user on 2017-11-30.
 */

public class DateListDTO implements Serializable {
    private int _id;
    private String title;
    private String address;
    private int year;
    private int month;
    private int day_of_month;
    private int hours;
    private int minit;
    private int flag;

    public DateListDTO(){}

    public DateListDTO(int _id, String title, String address, int year, int month, int day_of_month, int hours, int minit, int flag) {
        this._id = _id;
        this.title = title;
        this.address = address;
        this.year = year;
        this.month = month;
        this.day_of_month = day_of_month;
        this.hours = hours;
        this.minit = minit;
        this.flag = flag;
    }

    public int isFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay_of_month() {
        return day_of_month;
    }

    public void setDay_of_month(int day_of_month) {
        this.day_of_month = day_of_month;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinit() {
        return minit;
    }

    public void setMinit(int minit) {
        this.minit = minit;
    }
}
