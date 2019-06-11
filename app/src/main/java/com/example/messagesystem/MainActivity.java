package com.example.messagesystem;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private TextInputEditText editTextPassword;
    private TextView textViewRegister;
    private Button buttonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextUsername = findViewById(R.id.edit_text_username);
        editTextPassword = findViewById(R.id.edit_text_password);
        textViewRegister = findViewById(R.id.text_view_register);
        buttonLogin = findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                Network network = new Network();
                network.execute(username,password);
            }
        });
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Registration.class);
                startActivity(intent);
            }
        });

    }
    class Network extends AsyncTask<String,Void ,String> {
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("0")){
                Toast.makeText(MainActivity.this,"Invalid login",Toast.LENGTH_LONG).show();
            }else {
                Intent intent = new Intent(MainActivity.this,NameList.class);
                startActivity(intent);
        }   }

        @Override
        protected String doInBackground(String... args) {

            String urlString = "http://192.168.0.108/app_login_message.php";
            try {
                URL url = new URL(urlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("content-type","application/jason;charset=utf-8");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("username",args[0]);
                jsonObject.accumulate("password",args[1]);

                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

                bufferedWriter.write(jsonObject.toString());
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String s = br.readLine();
                String result[] = s.split("<br>");
                httpURLConnection.connect();
                Globals.name = result[1];
                return result[0];
                //return httpURLConnection.getResponseMessage();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
