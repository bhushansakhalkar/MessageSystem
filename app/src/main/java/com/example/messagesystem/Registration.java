package com.example.messagesystem;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Registration extends AppCompatActivity {
    private Button buttonRegister;
    private EditText editTextName;
    private EditText editTextContact;
    private EditText editTextUsername;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextCPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        editTextName = findViewById(R.id.edit_text_name);
        editTextContact = findViewById(R.id.edit_text_contact);
        editTextUsername = findViewById(R.id.edit_text_user);
        editTextPassword = (TextInputEditText) findViewById(R.id.edit_text_pass);
        editTextCPassword = (TextInputEditText)findViewById(R.id.edit_text_cpassword);
        buttonRegister = findViewById(R.id.button_register);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String contact = editTextContact.getText().toString();
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String cpassword = editTextCPassword.getText().toString();

                if(cpassword!=null  && contact!=null &&name!=null &&username!=null &&password!=null){
                    if(cpassword.equals(password)){
                        Network network = new Network();
                        network.execute(name,contact,username,password);
                    }else
                        Toast.makeText(Registration.this,"Please enter the Correct Password",Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(Registration.this,"Please enter all the fields",Toast.LENGTH_LONG).show();

            }
        });


    }

    class Network extends AsyncTask<String,Void ,String>{
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(Registration.this,s,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Registration.this,MainActivity.class);
            startActivity(intent);
        }

        @Override
        protected String doInBackground(String... args) {
            String urlString = "http://192.168.0.108/app_register_message.php";
            try {
                URL url = new URL(urlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("content-type","application/jason;charset=utf-8");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("name",args[0]);
                jsonObject.accumulate("contact",args[1]);
                jsonObject.accumulate("username",args[2]);
                jsonObject.accumulate("password",args[3]);

                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

                bufferedWriter.write(jsonObject.toString());
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                httpURLConnection.connect();
                return httpURLConnection.getResponseMessage();
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
