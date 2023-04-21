package hk.hku.cs.cubesnote.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import hk.hku.cs.cubesnote.entity.CubeEvent;
import hk.hku.cs.cubesnote.entity.CubeEventTreemapConfig;

public class Jsonfy {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String calenderToString(Calendar calendar) {
        if(calendar == null)
            return null;
        Date date = calendar.getTime();
        return dateFormat.format(date);
    }

    public static Calendar stringToCalendar(String s){
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(Objects.requireNonNull(dateFormat.parse(s)));
        } catch (ParseException e) {
            Log.e("Jsonfy", "Try parsing " + s + " to calendar error. Returned current time");
            return c;
        }
        return c;
    }

    public static JSONObject toJson(CubeEventTreemapConfig c) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("importance", c.getImportance());
            jsonObject.put("emergency", c.getEmergency());
            jsonObject.put("start", calenderToString(c.getStart()));
            jsonObject.put("end", calenderToString(c.getEnd()));
            jsonObject.put("linearEmergency", c.getLinearEmergency());
            if (c.getLinearEmergency())
                jsonObject.put("linearEmergencyBegin", calenderToString(c.getLinearEmergencyBegin()));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    public static JSONObject toJson(CubeEvent c) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", c.getId());
            jsonObject.put("title", c.getTitle());
            jsonObject.put("selectedStartCalendar", calenderToString(c.getSelectedStartCalendar()));
            jsonObject.put("selectedEndCalendar", calenderToString(c.getSelectedEndCalendar()));
            jsonObject.put("isAllDay", c.getAllDay());
            jsonObject.put("isCountingDays", c.getCountingDays());
            jsonObject.put("Description", c.getDescription());
            jsonObject.put("isShownInTreeMap", c.getShownInTreeMap());
            if (c.getShownInTreeMap())
                jsonObject.put("treemapConfig", toJson(c.getTreemapConfig()));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;
    }

    public static CubeEvent jsonToCubeEvent(JSONObject jsonObject) throws JSONException {
        CubeEvent cubeEvent = new CubeEvent(
                stringToCalendar(jsonObject.getString("selectedStartCalendar")),
                stringToCalendar(jsonObject.getString("selectedEndCalendar"))
        );
        cubeEvent.setId(jsonObject.getString("id"));
        cubeEvent.setAllDay(jsonObject.getBoolean("isAllDay"));
        cubeEvent.setCountingDays(jsonObject.getBoolean("isCountingDays"));
        cubeEvent.setTitle(jsonObject.getString("title"));
        cubeEvent.setDescription(jsonObject.getString("Description"));
        cubeEvent.setShownInTreeMap(jsonObject.getBoolean("isShownInTreeMap"));
        if(jsonObject.getBoolean("isShownInTreeMap")) {
            cubeEvent.setTreemapConfig(jsonToTreemapConfig(jsonObject.getJSONObject("treemapConfig")));
        } else {
            cubeEvent.setTreemapConfig(null);
        }
        return cubeEvent;
    }

    public static CubeEventTreemapConfig jsonToTreemapConfig(JSONObject jsonObject) throws JSONException {
        CubeEventTreemapConfig cfg = new CubeEventTreemapConfig(
                stringToCalendar(jsonObject.get("start").toString()),
                stringToCalendar(jsonObject.get("end").toString()),
                null
        );
        cfg.setImportance(jsonObject.getInt("importance"));
        cfg.setEmergency(jsonObject.getInt("emergency"));
        cfg.setLinearEmergency(jsonObject.getBoolean("linearEmergency"));
        if(jsonObject.getBoolean("linearEmergency"))
            cfg.setLinearEmergencyBegin(stringToCalendar(jsonObject.getString("linearEmergencyBegin")));
        return cfg;
    }
}
