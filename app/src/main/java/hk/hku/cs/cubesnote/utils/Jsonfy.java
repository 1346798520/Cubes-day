package hk.hku.cs.cubesnote.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hk.hku.cs.cubesnote.entity.CubeEvent;
import hk.hku.cs.cubesnote.entity.CubeEventTreemapConfig;

public class Jsonfy {
    public static String calenderToString(Calendar calendar) {
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        return dateFormat.format(date);
    }

    public static JSONObject toJson(CubeEventTreemapConfig c) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("importance", c.getImportance());
        jsonObject.put("emergency", c.getEmergency());
        jsonObject.put("start", calenderToString(c.getStart()));
        jsonObject.put("end", calenderToString(c.getEnd()));
        jsonObject.put("linearEmergency", c.getLinearEmergency());
        if (c.getLinearEmergency())
            jsonObject.put("linearEmergencyBegin", c.getLinearEmergencyBegin());
        return jsonObject;
    }

    public static JSONObject toJson(CubeEvent c) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", c.getId());
        jsonObject.put("selectedStartCalendar", calenderToString(c.getSelectedStartCalendar()));
        jsonObject.put("selectedEndCalendar", calenderToString(c.getSelectedEndCalendar()));
        jsonObject.put("isAllDay", c.getAllDay());
        jsonObject.put("isCountingDays", c.getCountingDays());
        jsonObject.put("title", c.getTitle());
        jsonObject.put("Description", c.getDescription());
        jsonObject.put("isShownInTreeMap", c.getShownInTreeMap());
        if (c.getShownInTreeMap())
            jsonObject.put("treemapConfig", toJson(c.getTreemapConfig()));
        return jsonObject;
    }
}
