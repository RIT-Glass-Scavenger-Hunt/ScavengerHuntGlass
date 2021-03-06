package edu.rit.scavengerhuntglass;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.transform.Result;


public class NewTeam {

    static int code;
    String new_data;


    public String createTeam() {

        new Team().execute();
        while(new_data == null){

        }
        System.out.println("create team" + new_data);

        return new_data;
    }


    private class Team extends AsyncTask<String, String, String> {

        String result = null;
        String data;
        InputStream is = null;
        String line;


        @Override
        protected String doInBackground(String... params) {

//            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
//
//            nameValuePairs.add(new BasicNameValuePair("score", "0"));
//            nameValuePairs.add(new BasicNameValuePair("no_of_clues", "3"));


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://www.noella.kolash.org/hcin722/create_team.php");
//                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                is = httpEntity.getContent();
                Log.i("TAG", "Connection Successful");
            } catch (Exception e) {
                Log.i("TAG", e.toString());
            }

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();

                result = sb.toString();
                Log.i("TAG", "Result Retrieved");
            } catch (Exception e) {
                Log.i("TAG", e.toString());
            }

            try {
                JSONObject json = new JSONObject(result);
                code = (json.getInt("code"));
                data = (json.getString("data"));

                JSONObject jsono = new JSONObject(data);

                new_data = (jsono.getString("LAST_INSERT_ID()"));

                System.out.println("JSON" + " " + code);
                System.out.println("JSON" + " " + data);
                System.out.println("JSON" + " " + new_data);

                if (code == 1) {
                   // array = data;
                    Log.i("msg", "Data Successfully Inserted");
                } else {
                    Log.i("msg", "Error inserting data");
                }

            } catch (Exception e) {
                Log.i("TAG", e.toString());
            }

            return null;
        }

    }

}
