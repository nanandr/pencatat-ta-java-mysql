package com.nandanarafiardika.pencatatbukuta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private String JSON_STRING;
    private Button buttonSearch;
    private EditText editTextSearch;
    private ImageView buttonLogout;
    FloatingActionButton buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener((AdapterView.OnItemClickListener) this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras == null){
            getJSON(null);
        }
        else{
            getJSON(extras.getString("keyword"));
        }

        editTextSearch = findViewById(R.id.search_box);
        buttonSearch = findViewById(R.id.search);
        buttonSearch.setOnClickListener(view -> {
            Intent search = new Intent(this, MainActivity.class);
            search.putExtra("keyword", editTextSearch.getText().toString());
            startActivity(search);
            finish();
        });

        buttonAdd = findViewById(R.id.add);
        buttonAdd.setOnClickListener(view -> {
            Intent add = new Intent(this, AddActivity.class);
            startActivity(add);
        });

        buttonLogout = findViewById(R.id.logout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_dialog();
            }
            public void delete_dialog(){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Apakah anda yakin akan logout?");
                builder.setPositiveButton("Ya", (dialogInterface1, i1) -> {
                    Intent logout = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(logout);
                    finish();
                });
                builder.setNegativeButton("Tidak", ((dialogInterface12, i12) -> dialogInterface12.dismiss()));
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
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
                String id = result.getString(Config.ID);
                String noInduk = result.getString(Config.NO_INDUK);
                String judul = result.getString(Config.JUDUL);
                String namaPemilik = result.getString(Config.NAMA_PEMILIK);
                String namaPembimbing = result.getString(Config.NAMA_PEMBIMBING);
                String tahun = result.getString(Config.TAHUN);
                String tempatPkl = result.getString(Config.TEMPAT_PKL);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.ID, id);
                hashMap.put(Config.NO_INDUK, noInduk);
                hashMap.put(Config.JUDUL, judul);
                hashMap.put(Config.NAMA_PEMILIK, namaPemilik);
                hashMap.put(Config.NAMA_PEMBIMBING, namaPembimbing);
                hashMap.put(Config.TAHUN, tahun);
                hashMap.put(Config.TEMPAT_PKL, tempatPkl);
                arrayList.add(hashMap);
            }
        }
        catch (JSONException jsonException){
            jsonException.printStackTrace();
        }
        ListAdapter listAdapter = new SimpleAdapter(
                MainActivity.this, arrayList, R.layout.list_item,
                new String[]{
                        Config.ID,
                        Config.NO_INDUK,
                        Config.JUDUL,
                        Config.NAMA_PEMILIK,
                        Config.NAMA_PEMBIMBING,
                        Config.TAHUN,
                        Config.TEMPAT_PKL
        },
                new int[]{
                        R.id.id,
                        R.id.no_induk,
                        R.id.judul,
                        R.id.nama_pemilik,
                        R.id.nama_pembimbing,
                        R.id.tahun,
                        R.id.tempat_pkl
                });
        listView.setAdapter(listAdapter);
    }


    private void getJSON(String keyword){
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
                String getRequest;
                if(keyword == null){
                    getRequest = requestHandler.sendGetRequest(Config.URL_VIEW);
                }
                else{
                    getRequest = requestHandler.sendGetRequestParam(Config.URL_SEARCH,keyword);
                }

                return getRequest;
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Intent intent = new Intent(this, EditActivity.class);
        HashMap<String, String> hashMap = (HashMap) parent.getItemAtPosition(position);
        String idString = hashMap.get(Config.ID).toString();
        intent.putExtra(Config.ID, idString);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }
}