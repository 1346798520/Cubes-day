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

public class CubeEvent implements Parcelable, Serializable{

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
        title = in.readString();
        Description = in.readString();
        isAllDay = in.readByte() == 1;
        isCountingDays = in.readByte() == 1;
        String start = in.readString();
        selectedStartCalendar = Jsonfy.stringToCalendar(start);
        String end = in.readString();
        selectedEndCalendar = Jsonfy.stringToCalendar(end);
        isShownInTreeMap = in.readByte() == 1;
        if (isShownInTreeMap)
            treemapConfig = in.readParcelable(CubeEventTreemapConfig.class.getClassLoader());
    }

    public static final Creator<CubeEvent> CREATOR = new Creator<CubeEvent>() {
        @Override
        public CubeEvent createFromParcel(Parcel in) {
            return new CubeEvent(in);
        }

        @Override
        public CubeEvent[] newArray(int size) {
            return new CubeEvent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(Description);
        parcel.writeByte((byte) (isAllDay ? 1 : 0));
        parcel.writeByte((byte) (isCountingDays ? 1 : 0));
        parcel.writeString(Jsonfy.calenderToString(selectedStartCalendar));
        parcel.writeString(Jsonfy.calenderToString(selectedEndCalendar));
        parcel.writeByte((byte) (isShownInTreeMap ? 1 : 0));
        if (isShownInTreeMap)
            parcel.writeParcelable(treemapConfig, i);
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
