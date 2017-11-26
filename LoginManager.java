package com.example.abhay.biet_login;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhay on 21/11/17.
 */

public class LoginManager extends AsyncTask<String,String,String> {
    private Context context;
    private  String type;
    private Exception e;
    private String s="";
    private int status;
    private String username,password;
    public LoginManager(Context context,String type) {
        this.context=context;
        this.type=type;
        getData();
    }

    public void getData() {
        FileInputStream fin;
        try {
            fin= context.openFileInput("configuration.dat");
            ObjectInputStream ois= new ObjectInputStream(fin);
            UserData userData;
            while(fin.available()>0)
            {
                userData=(UserData) ois.readObject();
                username=userData.getUsername();
                password=userData.getPassword();
            }
            ois.close();
            fin.close();
        } catch (Exception e) {
            Toast.makeText(context,"Error occurred \n Please clear your app data and restart application",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            //String login_url = "http://www.google.com";
            String login_url="http://172.16.40.5:8090/httpclient.html";
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data= URLEncoder.encode("mode","UTF-8")+"="+ URLEncoder.encode(type,"UTF-8")+"&"+
                    URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                    URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"+
                    URLEncoder.encode("a","UTF-8")+"="+URLEncoder.encode("1510598306463","UTF-8")+"&"+
                    URLEncoder.encode("producttype","UTF-8")+"="+URLEncoder.encode("1","UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();



            InputStream inputStream=httpURLConnection.getInputStream();
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG,null ,"requestresponse");

            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();

                if(name.equals("message")) {
                    parser.require(XmlPullParser.START_TAG,null ,"message");
                    parser.next();
                    s=parser.getText();
                    parser.nextTag();
                    parser.require(XmlPullParser.END_TAG,null ,"message");
                }

            }
            inputStream.close();
            status=1;
        }
        catch (Exception e) {
            this.e=e;
            status=0;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
       if(status==1)
           Toast.makeText(context,this.s,Toast.LENGTH_SHORT).show();
       else
           Toast.makeText(context,"Please check your internet connection",Toast.LENGTH_SHORT).show();
    }


}
