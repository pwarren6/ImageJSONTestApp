package com.mobileappscompany.training.imagejsontestapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> imageList = new ArrayList<>();

    private final static String URL = "http://jsonplaceholder.typicode.com/photos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new FetchData().execute(URL);
    }

    private class FetchData extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... params) {

            StringBuilder result = new StringBuilder();

            try {

                URL url = new URL(URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                urlConnection.disconnect();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String dataFetched) {
            //parse the JSON data and then display
            parseJSON(dataFetched);
        }

        private void parseJSON(String data){

            try{
                JSONArray jsonMainNode = new JSONArray(data);

                int jsonArrLength = jsonMainNode.length();

                for(int i=0; i < jsonArrLength; i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String postTitle = jsonChildNode.getString("post_title");
                    imageList.add(postTitle);
                }

                // Get ListView object from xml
                listView = (ListView) findViewById(R.id.listview);

                // Define a new Adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, imageList);

                // Assign adapter to ListView
                listView.setAdapter(adapter);

            }catch(Exception e){
                Log.i("App", "Error parsing data" +e.getMessage());

            }
        }
    }
}
