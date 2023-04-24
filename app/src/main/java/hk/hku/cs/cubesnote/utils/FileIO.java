package hk.hku.cs.cubesnote.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.SecureDirectoryStream;
import java.util.ArrayList;
import java.util.Calendar;

import hk.hku.cs.cubesnote.entity.CubeEvent;

public class FileIO implements Serializable {
    public static void writeJson(Context context, String fname, JSONObject json) throws IOException {
        File file = new File(context.getFilesDir(), fname);
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(json.toString());
        bufferedWriter.close();
    }

    public static JSONObject readJsonString(Context context, String fname) throws IOException, JSONException {
        File file = new File(context.getFilesDir(), fname);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null){
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        String response = stringBuilder.toString();
        return new JSONObject(response);
    }

    public static ArrayList<CubeEvent> readAllEvents(Context context) {
        ArrayList<CubeEvent> eventList = new ArrayList<>();
        File directory = new File(String.valueOf(context.getFilesDir()));
        File[] files = directory.listFiles();
        if (files == null)
            return eventList;
        for (File file : files) {
            try {
                JSONObject j = readJsonString(context, file.getName());
                eventList.add(Jsonfy.jsonToCubeEvent(j));
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return eventList;
    }

    public static ArrayList<JSONObject> readAllEventJsons(Context context) {
        ArrayList<JSONObject> eventList = new ArrayList<>();
        File directory = new File(String.valueOf(context.getFilesDir()));
        File[] files = directory.listFiles();
        if (files == null)
            return eventList;
        for (File file : files) {
            try {
                JSONObject j = readJsonString(context, file.getName());
                eventList.add(j);
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return eventList;
    }

    public static void clearAllEventFiles(Context context) {
        ArrayList<JSONObject> eventList = new ArrayList<>();
        File directory = new File(String.valueOf(context.getFilesDir()));
        File[] files = directory.listFiles();
        if (files != null)
            for (File file : files)
                file.delete();
    }

    public static void deleteEvent(Context context, String fname) {
        File file = new File(context.getFilesDir(), fname);
        if (file.exists())
            file.delete();
    }

    public static void deleteEvent(Context context, CubeEvent event) {
        deleteEvent(context, event.getId());
    }

    public static ArrayList<CubeEvent> readEventsOfDay(Context context, int year, int month, int day) {
        Log.i("readEventsOfDay", year + " " + month + " " + day);
        Calendar st = Calendar.getInstance();
        st.set(year, month-1, day);
        st.set(Calendar.HOUR_OF_DAY, 0);
        st.set(Calendar.MINUTE, 0);
        Calendar ed = (Calendar) st.clone();
        ed.add(Calendar.DATE, 1);
        return readEventsWithin(context, st, ed);
    }

    public static ArrayList<CubeEvent> readEventsWithin(Context context, Calendar st, Calendar ed) {
        Log.i("readEventsWithin","!!!st:"+st.getTime());
        Log.i("readEventsWithin","!!!ed:"+ed.getTime());
        ArrayList<CubeEvent> eventList = new ArrayList<>();
        File directory = new File(String.valueOf(context.getFilesDir()));
        File[] files = directory.listFiles();
        if (files == null)
            return eventList;
        for (File file : files) {
            try {
                JSONObject j = readJsonString(context, file.getName());
                CubeEvent event = Jsonfy.jsonToCubeEvent(j);
                Log.i("readEventsWithin","!!!Checking: "+event.getSelectedStartCalendar().getTime()+" "+event.getSelectedEndCalendar().getTime());
                if ( !(event.getSelectedStartCalendar().after(ed) || event.getSelectedStartCalendar().before(st)) ) {
                    eventList.add(event);
                    Log.i("readEventsWithin","!!!added");
                }
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return eventList;
    }
}
