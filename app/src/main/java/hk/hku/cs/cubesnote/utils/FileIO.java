package hk.hku.cs.cubesnote.utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileIO {
    public static void writeJson(Context context, String fname, JSONObject json) throws IOException {
        File file = new File(context.getFilesDir(), fname);
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(json.toString());
        bufferedWriter.close();
        System.out.println(context.getFilesDir());
        System.out.println(fname);
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


}
