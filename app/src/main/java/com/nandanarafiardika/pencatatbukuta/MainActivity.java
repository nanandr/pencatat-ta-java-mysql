package com.nandanarafiardika.pencatatbukuta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String JSON_STRING;
    FloatingActionButton buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //listview
        getJSON();

        buttonAdd = findViewById(R.id.add);
        buttonAdd.setOnClickListener(view -> {
            Intent add = new Intent(this, AddActivity.class);
            startActivity(add);
        });
    }

    private void showData(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

        try{
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray jsonArray = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject result = jsonArray.getJSONObject(i);
                //id juga?
                String noInduk = result.getString(Config.NO_INDUK);
                String judul = result.getString(Config.JUDUL);
                String namaPemilik = result.getString(Config.NAMA_PEMILIK);
                String namaPembimbing = result.getString(Config.NAMA_PEMBIMBING);
                String angkatan = result.getString(Config.ANGKATAN);
                String kelas = result.getString(Config.KELAS);
                String tempatPkl = result.getString(Config.TEMPAT_PKL);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.NO_INDUK, noInduk);
                hashMap.put(Config.JUDUL, judul);
                hashMap.put(Config.NAMA_PEMILIK, namaPemilik);
                hashMap.put(Config.NAMA_PEMBIMBING, namaPembimbing);
                hashMap.put(Config.ANGKATAN, angkatan);
                hashMap.put(Config.KELAS, kelas);
                hashMap.put(Config.TEMPAT_PKL, tempatPkl);
                arrayList.add(hashMap);
            }
        }
        catch (JSONException jsonException){
            jsonException.printStackTrace();
        }
    }

    //adapter

    private void getJSON(){
        class GetJSON extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Mengambil Data..", "Mohon Tunggu..", false, false);
            }

            @Override
            protected void onPostExecute(String message){
                super.onPostExecute(message);
                loading.dismiss();
                JSON_STRING = message;
                showData();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler requestHandler = new RequestHandler();
                String getRequest = requestHandler.sendGetRequest(Config.URL_VIEW);
                return getRequest;
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    //adapter on click

    //backpress
}