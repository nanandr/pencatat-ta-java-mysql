package com.nandanarafiardika.pencatatbukuta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {
    private EditText editTextNoInduk, editTextJudul, editTextNamaPemilik, editTextNamaPembimbing, editTextTempatPkl, editTextKelas;
    Button buttonAngkatan, buttonAdd;
    ImageView buttonBack;
    int currentAngkatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

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

        if(currentMonth < 6){
            currentAngkatan = currentYear - 2003;
        }
        else{
            currentAngkatan = currentYear - 2002;
        }

        buttonAngkatan = findViewById(R.id.angkatan);
        buttonAngkatan.setText(String.valueOf(currentAngkatan-2));
        buttonAngkatan.setOnClickListener(view -> {
            String[] options = new String[currentAngkatan + 1];
            for (int i = 1; i <= currentAngkatan; ++i) {
                options[i-1] = Integer.toString(i);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
            builder.setTitle("Pilih Angkatan");
            builder.setItems(options, (dialogInterface, which) -> {
                buttonAngkatan.setTextColor(Color.BLACK);
                buttonAngkatan.setText(options[which]);
            });
            builder.show();
        });

        buttonAdd = findViewById(R.id.add);
        buttonBack = findViewById(R.id.back);
        buttonAdd.setOnClickListener(view -> {

            final String noInduk = editTextNoInduk.getText().toString();
            final String judul = editTextJudul.getText().toString();
            final String namaPemilik = editTextNamaPemilik.getText().toString();
            final String namaPembimbing = editTextNamaPembimbing.getText().toString();
            final String angkatan = buttonAngkatan.getText().toString();
            final String kelas = editTextKelas.getText().toString();
            final String tempatPkl = editTextTempatPkl.getText().toString();

            class AddData extends AsyncTask<Void, Void, String>{
                ProgressDialog loading;

                @Override
                protected  void onPreExecute(){
                    super.onPreExecute();
                    loading = ProgressDialog.show(AddActivity.this, "Menambahkan Data..", "Mohon Tunggu..", false, false);
                }

                @Override
                protected void onPostExecute(String message){
                    super.onPostExecute(message);
                    loading.dismiss();
                    Toast.makeText(AddActivity.this, message, Toast.LENGTH_LONG).show();
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
                    hashMap.put(Config.NO_INDUK, noInduk);
                    hashMap.put(Config.JUDUL, judul);
                    hashMap.put(Config.NAMA_PEMILIK, namaPemilik);
                    hashMap.put(Config.NAMA_PEMBIMBING, namaPembimbing);
                    hashMap.put(Config.ANGKATAN, angkatan);
                    hashMap.put(Config.KELAS, kelas);
                    hashMap.put(Config.TEMPAT_PKL, tempatPkl);

                    RequestHandler requestHandler = new RequestHandler();
                    String postRequest = requestHandler.sendPostRequest(Config.URL_ADD, hashMap);
                    return postRequest;
                }
            }
            AddData addData = new AddData();
            addData.execute();
        });

        buttonBack.setOnClickListener(view -> {
            Intent back = new Intent(this, MainActivity.class);
            startActivity(back);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(this, MainActivity.class);
        startActivity(back);
        finish();
    }
}