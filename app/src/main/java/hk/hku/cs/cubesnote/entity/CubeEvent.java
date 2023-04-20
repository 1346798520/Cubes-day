package hk.hku.cs.cubesnote.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.UUID;

import hk.hku.cs.cubesnote.utils.Jsonfy;

public class CubeEvent {

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

    public JSONObject toJson() throws JSONException {
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
