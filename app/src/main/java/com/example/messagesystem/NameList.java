
package com.example.messagesystem;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class NameList extends AppCompatActivity {
    private ListView listViewNames;
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_list);
        listViewNames = findViewById(R.id.list_view_name);
        adapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item){

            @Override
            public View getView(int position,  View convertView, ViewGroup parent) {
                View view =  super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextSize(20);
                return view;
            }
        };
        listViewNames.setAdapter(adapter);
        Network network = new Network();
        network.execute();


        listViewNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                Intent intent = new Intent(NameList.this,Message.class);
                intent.putExtra("name",name);
                startActivity(intent);

            }
        });
    }

    class Network extends AsyncTask<String,Void ,String> {
        @Override
        protected void onPostExecute(String s) {
            String names[] = s.split("<br>");
            for(int i=0;i<names.length;i++){
                adapter.add(names[i]);
            }



        }
        @Override
        protected String doInBackground(String... args) {
            String urlString = "http://192.168.0.108/app_names_message.php";
            try {
                URL url = new URL(urlString);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String name = bufferedReader.readLine();
                return name;
                //return httpURLConnection.getResponseMessage();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
