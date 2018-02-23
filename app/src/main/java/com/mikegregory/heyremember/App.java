package com.mikegregory.heyremember;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.UUID;

/**
 * Created by mikegregory on 2/18/18.
 * TODO: handle global variables better: https://stackoverflow.com/questions/1944656/android-global-variable
 */

public class App {

    private static String idFileName = "idFile";
    private static File idFile;

    private String _id;

    private Context context;

    public App(Context context) {
        this.context = context;
        this.idFile = new File(context.getFilesDir(), idFileName);
    }

    public String id() {
        if (_id != null) {
            return _id;
        }
        if (_id == null) {
            _id = fetchId();
            Toast toast = Toast.makeText(context, "fetched", Toast.LENGTH_SHORT);
            toast.show();
            if (_id != null) { Log.d("fetched id", _id); }
        }
        if (_id == null) {
            _id = generateId();
            Toast toast = Toast.makeText(context, "generated", Toast.LENGTH_SHORT);
            toast.show();
            Log.d("generated id", _id);
        }
        return _id;
    }

    private String fetchId() {
        String fetchedId = null;
        FileInputStream inputStream;
        if (idFile.exists()) {
            try {
                String message;
                inputStream = context.openFileInput(idFileName);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();
                while ((message = bufferedReader.readLine()) != null) {
                    stringBuffer.append(message + "\n");
                }
                fetchedId = stringBuffer.toString();
                Toast.makeText(context.getApplicationContext(), "ID written: " + fetchedId, Toast.LENGTH_LONG).show();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
        return fetchedId;
    }

    private String generateId() {
        String generatedId = UUID.randomUUID().toString();
        try {
            FileOutputStream outputStream = context.openFileOutput(idFileName, Context.MODE_PRIVATE);
            outputStream.write(generatedId.getBytes());
            outputStream.close();
            Toast.makeText(context.getApplicationContext(), "ID written: " + generatedId, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedId;
    }

}
