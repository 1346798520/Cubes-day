package hk.hku.cs.cubesnote.entity;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

import hk.hku.cs.cubesnote.utils.Jsonfy;

public class CubeEvent implements Serializable{

    private String id;
    private java.util.Calendar selectedStartCalendar;
    private java.util.Calendar selectedEndCalendar;
    private Boolean isAllDay;
    private Boolean isCountingDays;
    private Boolean isShownInTreeMap;
    private String title;
    private String Description;
    private CubeEventTreemapConfig treemapConfig;

    public CubeEvent(Calendar start, Calendar end) {
        this.id = UUID.randomUUID().toString();
        this.isAllDay = false;
        this.isCountingDays = false;
        this.isShownInTreeMap = false;
        this.selectedStartCalendar = start;
        this.selectedEndCalendar = end;
    }

    protected CubeEvent(Parcel in) {
        id = in.readString();
        byte tmpIsAllDay = in.readByte();
        isAllDay = tmpIsAllDay == 0 ? null : tmpIsAllDay == 1;
        byte tmpIsCountingDays = in.readByte();
        isCountingDays = tmpIsCountingDays == 0 ? null : tmpIsCountingDays == 1;
        byte tmpIsShownInTreeMap = in.readByte();
        isShownInTreeMap = tmpIsShownInTreeMap == 0 ? null : tmpIsShownInTreeMap == 1;
        title = in.readString();
        Description = in.readString();
        treemapConfig = in.readParcelable(CubeEventTreemapConfig.class.getClassLoader());
    }

    public JSONObject toJson() {
        return Jsonfy.toJson(this);
    }

    //********************************* Start Getter & Setter *********************************//
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Calendar getSelectedStartCalendar() {
        return selectedStartCalendar;
    }

    public void setSelectedStartCalendar(Calendar selectedStartCalendar) {
        this.selectedStartCalendar = selectedStartCalendar;
    }

    public Calendar getSelectedEndCalendar() {
        return selectedEndCalendar;
    }

    public void setSelectedEndCalendar(Calendar selectedEndCalendar) {
        this.selectedEndCalendar = selectedEndCalendar;
    }

    public Boolean getAllDay() {
        return isAllDay;
    }

    public void setAllDay(Boolean allDay) {
        isAllDay = allDay;
    }

    public Boolean getCountingDays() {
        return isCountingDays;
    }

    public void setCountingDays(Boolean countingDays) {
        isCountingDays = countingDays;
    }

    public Boolean getShownInTreeMap() {
        return isShownInTreeMap;
    }

    public void setShownInTreeMap(Boolean shownInTreeMap) {
        isShownInTreeMap = shownInTreeMap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public CubeEventTreemapConfig getTreemapConfig() {
        return treemapConfig;
    }

    public void setTreemapConfig(CubeEventTreemapConfig treemapConfig) {
        this.treemapConfig = treemapConfig;
    }

}
