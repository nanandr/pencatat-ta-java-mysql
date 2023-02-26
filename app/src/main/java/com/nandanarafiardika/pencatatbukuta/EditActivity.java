package com.nandanarafiardika.pencatatbukuta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextNoInduk, editTextJudul, editTextNamaPemilik, editTextNamaPembimbing, editTextTempatPkl, editTextKelas;
    private Button buttonAngkatan, buttonAdd, buttonDelete;
    private ImageView buttonBack;
    private String id;
    int currentAngkatan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        id = intent.getStringExtra(Config.ID);

        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);

        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(calendar.YEAR);

        editTextNoInduk = findViewById(R.id.no_induk);
        editTextJudul = findViewById(R.id.judul);
        editTextNamaPemilik = findViewById(R.id.nama_pemilik);
        editTextNamaPembimbing = findViewById(R.id.nama_pembimbing);
        editTextKelas = findViewById(R.id.kelas);
        editTextTempatPkl = findViewById(R.id.tempat_pkl);
        buttonAngkatan = findViewById(R.id.angkatan);
        buttonAdd = findViewById(R.id.add);
        buttonDelete = findViewById(R.id.delete);

        if(currentMonth < 6){
            currentAngkatan = currentYear - 2003;
        }
        else{
            currentAngkatan = currentYear - 2002;
        }

        buttonAngkatan.setOnClickListener(view -> {
            String[] options = new String[currentAngkatan + 1];
            for (int i = 1; i <= currentAngkatan; ++i) {
                options[i-1] = Integer.toString(i);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
            builder.setTitle("Pilih Angkatan");
            builder.setItems(options, (dialogInterface, which) -> {
                buttonAngkatan.setTextColor(Color.BLACK);
                buttonAngkatan.setText(options[which]);
            });
            builder.show();
        });

        buttonAdd.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        getData();
    }

    public void getData(){
        class GetData extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(EditActivity.this, "Fetching Data..", "Mohon Tunggu..", false, false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                showData(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler requestHandler = new RequestHandler();
                String s = requestHandler.sendGetRequestParam(Config.URL_GET, id);
                return s;
            }
        }
        GetData getData = new GetData();
        getData.execute();
    }

    private void showData(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);

//            String id = result.getString(Config.ID);
            String noInduk = c.getString(Config.NO_INDUK);
            String judul = c.getString(Config.JUDUL);
            String namaPemilik = c.getString(Config.NAMA_PEMILIK);
            String namaPembimbing = c.getString(Config.NAMA_PEMBIMBING);
            String angkatan = c.getString(Config.ANGKATAN);
            String kelas = c.getString(Config.KELAS);
            String tempatPkl = c.getString(Config.TEMPAT_PKL);

            editTextNoInduk.setText(noInduk);
            editTextJudul.setText(judul);
            editTextNamaPemilik.setText(namaPemilik);
            editTextNamaPembimbing.setText(namaPembimbing);
            editTextKelas.setText(kelas);
            editTextTempatPkl.setText(tempatPkl);
            buttonAngkatan.setText(angkatan);
        }
        catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    private void updateData(){
        final String noInduk = editTextNoInduk.getText().toString();
        final String judul = editTextJudul.getText().toString();
        final String namaPemilik = editTextNamaPemilik.getText().toString();
        final String namaPembimbing = editTextNamaPembimbing.getText().toString();
        final String angkatan = buttonAngkatan.getText().toString().trim();
        final String kelas = editTextKelas.getText().toString();
        final String tempatPkl = editTextTempatPkl.getText().toString();

        class UpdateData extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected  void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(EditActivity.this, "Memperbarui Data..", "Mohon Tunggu..", false, false);
            }

            @Override
            protected void onPostExecute(String message){
                super.onPostExecute(message);
                loading.dismiss();
                Toast.makeText(EditActivity.this, message, Toast.LENGTH_LONG).show();
                editTextNoInduk.setText("");
                editTextJudul.setText("");
                editTextNamaPemilik.setText("");
                editTextNamaPembimbing.setText("");
                editTextKelas.setText("");
                editTextTempatPkl.setText("");
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.ID, id);
                hashMap.put(Config.NO_INDUK, noInduk);
                hashMap.put(Config.JUDUL, judul);
                hashMap.put(Config.NAMA_PEMILIK, namaPemilik);
                hashMap.put(Config.NAMA_PEMBIMBING, namaPembimbing);
                hashMap.put(Config.ANGKATAN, angkatan);
                hashMap.put(Config.KELAS, kelas);
                hashMap.put(Config.TEMPAT_PKL, tempatPkl);

                RequestHandler requestHandler = new RequestHandler();
                String postRequest = requestHandler.sendPostRequest(Config.URL_EDIT, hashMap);
                return postRequest;
            }
        }
        UpdateData updateData = new UpdateData();
        updateData.execute();
    }

    private void deleteData(){
        class DeleteData extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(EditActivity.this, "Menghapus Data..", "Mohon Tunggu..", false, false);
            }

            @Override
            protected void onPostExecute(String message){
                super.onPostExecute(message);
                loading.dismiss();
                Toast.makeText(EditActivity.this, message, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler requestHandler = new RequestHandler();
                String getRequest = requestHandler.sendGetRequestParam(Config.URL_DELETE, id);
                return getRequest;
            }
        }
        DeleteData deleteData = new DeleteData();
        deleteData.execute();
    }

    private void confirmDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah anda yakin akan menghapus data ini?");
        builder.setPositiveButton("Ya",
                (dialogInterface, i) -> {
                    deleteData();
                    startActivity(new Intent(EditActivity.this, MainActivity.class));
                    finish();
                });
        builder.setNegativeButton("Tidak", (dialogInterface, i) -> {});
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonAdd){
            updateData();
        }
        if(view == buttonDelete){
            confirmDelete();
        }
        if(view == buttonAngkatan){

        }
    }
}