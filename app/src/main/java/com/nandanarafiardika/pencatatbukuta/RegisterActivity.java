package com.nandanarafiardika.pencatatbukuta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextUsername, editTextPassword;
    Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);

        buttonRegister = findViewById(R.id.register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = editTextUsername.getText().toString();
                final String password = editTextPassword.getText().toString();

                class Register extends AsyncTask<Void, Void, String>{
                    ProgressDialog loading;

                    @Override
                    protected void onPreExecute(){
                        super.onPreExecute();
                        loading = ProgressDialog.show(RegisterActivity.this,
                                "Registering..", "Mohon Tunggu", false, false);
                    }

                    @Override
                    protected void onPostExecute(String response){
                        super.onPostExecute(response);
                        loading.dismiss();
                        if(response.equals("Proceed")){
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.putExtra("username", username);
                            intent.putExtra("password", password);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    protected String doInBackground(Void... voids) {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("password", password);

                        RequestHandler requestHandler = new RequestHandler();
                        String response = requestHandler.sendPostRequest(Config.URL_REGISTER, params);
                        return response;
                    }
                }
                Register register = new Register();
                register.execute();
            }
        });
    }
}