package hk.hku.cs.cubesnote.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import hk.hku.cs.cubesnote.utils.Jsonfy;

public class CubeEventTreemapConfig  implements Parcelable {
    private int importance;
    private int emergency;
    private java.util.Calendar start;
    private java.util.Calendar end;
    private Boolean linearEmergency;
    private java.util.Calendar linearEmergencyBegin;
    // private final CubeEvent parent;

    public JSONObject toJson() throws JSONException {
        return Jsonfy.toJson(this);
    }

    //********************************* Start Getter & Setter *********************************//
    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getEmergency() {
        return emergency;
    }

    public void setEmergency(int emergency) {
        this.emergency = emergency;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public Boolean getLinearEmergency() {
        return linearEmergency;
    }

    public void setLinearEmergency(Boolean linearEmergency) {
        this.linearEmergency = linearEmergency;
    }

    public Calendar getLinearEmergencyBegin() {
        return linearEmergencyBegin;
    }

    public void setLinearEmergencyBegin(Calendar linearEmergencyBegin) {
        this.linearEmergencyBegin = linearEmergencyBegin;
    }
}
