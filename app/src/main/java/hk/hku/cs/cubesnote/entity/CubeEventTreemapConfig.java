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

    public CubeEventTreemapConfig(Calendar start, Calendar end, Calendar linear) {
        this.importance = 3;
        this.emergency = 3;
        this.start = start;
        this.end = end;
        this.linearEmergency = false;
        this.linearEmergencyBegin = linear;
    }

    protected CubeEventTreemapConfig(Parcel in) {
        importance = in.readInt();
        emergency = in.readInt();
        start = Jsonfy.stringToCalendar(in.readString());
        end = Jsonfy.stringToCalendar(in.readString());
        linearEmergency = in.readByte() != 0;
        linearEmergencyBegin = linearEmergency ? null : Jsonfy.stringToCalendar(in.readString());
    }

    public static final Creator<CubeEventTreemapConfig> CREATOR = new Creator<CubeEventTreemapConfig>() {
        @Override
        public CubeEventTreemapConfig createFromParcel(Parcel in) {
            return new CubeEventTreemapConfig(in);
        }

        @Override
        public CubeEventTreemapConfig[] newArray(int size) {
            return new CubeEventTreemapConfig[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(importance);
        parcel.writeInt(emergency);
        parcel.writeString(Jsonfy.calenderToString(start));
        parcel.writeString(Jsonfy.calenderToString(end));
        parcel.writeByte((byte) (linearEmergency ? 1 : 0));
        parcel.writeString(Jsonfy.calenderToString(linearEmergencyBegin));
    }

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
