package com.example.messagesystem;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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

public class Message extends AppCompatActivity {
    private ListView textViewRecive;
    private ListView textViewSend;
    private EditText editTextMessage;
    private ArrayAdapter adapter;
    private ArrayAdapter adapter1;
    private Button buttonSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        textViewRecive = findViewById(R.id.text_view_msg);
        textViewSend = findViewById(R.id.text_view_msg2);
        adapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item);
        adapter1 = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item){

            @Override
            public View getView(int position,  View convertView,ViewGroup parent) {
              TextView textView =  (TextView) super.getView(position, convertView, parent);
                textView.setGravity(Gravity.RIGHT|Gravity.END|Gravity.CENTER_VERTICAL);
                return textView;
            }
        };
        editTextMessage = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.button_send);
        textViewSend.setAdapter(adapter);
        textViewRecive.setAdapter(adapter1);
        if(!buttonSend.isPressed()) {
            Intent intent = getIntent();
            String send = intent.getStringExtra("name");
            Network1 network1 = new Network1();
            network1.execute(send, Globals.name);

            Network2 network2 = new Network2();
            network2.execute(Globals.name, send);
        }
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString();
                Intent intent = getIntent();
                String sendTo = intent.getStringExtra("name");
                Network network = new Network();
                network.execute(sendTo,Globals.name,message);
                adapter.clear();
                adapter1.clear();

                Network1 network1 = new Network1();
                network1.execute(sendTo,Globals.name);

                Network2 network2 = new Network2();
                network2.execute(Globals.name,sendTo);

                editTextMessage.setText("");

            }
        });
    }
    class Network1 extends AsyncTask<String,Void,String>{
        @Override
        protected void onPostExecute(String s) {
            if(s!=null) {
                String[] result = s.split("<br>");
                for (int i = 0; i < result.length; i++) {
                    String res = "";
                    res = res + result[i];
                    adapter.add(res);
                }
            }
        }

        @Override
        protected String doInBackground(String... args) {
            String urlString = "http://192.168.0.108/app_select1_message_message.php";
            try {
                URL url = new URL(urlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("content-type","application/jason;charset=utf-8");
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("sendTo",args[0]);
                jsonObject.accumulate("sendFrom",args[1]);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(jsonObject.toString());
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s = bufferedReader.readLine();
                httpURLConnection.connect();
                return s;
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

    class Network2 extends AsyncTask<String,Void,String>{
        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                String[] result = s.split("<br>");
                for (int i = 0; i < result.length; i++) {
                    String res = "";
                     res = res + result[i];
                    adapter1.add(res);
                }
            }
        }

        @Override
        protected String doInBackground(String... args) {
            String urlString = "http://192.168.0.108/app_select2_message_message.php";
            try {
                URL url = new URL(urlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("content-type","application/jason;charset=utf-8");
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("sendTo",args[0]);
                jsonObject.accumulate("sendFrom",args[1]);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(jsonObject.toString());
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s = bufferedReader.readLine();
                httpURLConnection.connect();
                return s;
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


    class Network extends AsyncTask<String,Void,String>{
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(Message.this,s,Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... args) {
            String urlString = "http://192.168.0.108/app_insert_message_message.php";
            try {
                URL url = new URL(urlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("content-type","application/jason;charset=utf-8");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("sendTo",args[0]);
                jsonObject.accumulate("sendFrom",args[1]);
                jsonObject.accumulate("message",args[2]);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                //InputStream inputStream = httpURLConnection.getInputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

                bufferedWriter.write(jsonObject.toString());
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                //String status = bufferedReader.readLine();
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
