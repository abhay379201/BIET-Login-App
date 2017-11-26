package com.example.abhay.biet_login;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    Button login_b,logout_b,save_b,clear_b;
    LoginManager loginManager;
    String username,password;
    EditText uid,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //setContentView(R.layout.registration);
       /*login_b=(Button) findViewById(R.id.loginbutton);
       logout_b=(Button)findViewById(R.id.logoutbutton);

       login_b.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               loginManager=new LoginManager(MainActivity.this,"191");
               loginManager.execute("1","1","1");
           }
       });
       logout_b.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               loginManager=new LoginManager(MainActivity.this,"193");
               loginManager.execute("1","1","1");
           }
       });*/
       checkUserStatus();



    }

    public void checkUserStatus() {
        FileInputStream fin;
        try {
            fin= openFileInput("configuration.dat");
            ObjectInputStream ois= new ObjectInputStream(fin);
            UserData userData;
            while(fin.available()>0)
            {
                userData=(UserData) ois.readObject();
                if(!userData.isStatus()) {
                    setContentView(R.layout.registration);
                    startRegistrationPage();
                }
                else {
                    setContentView(R.layout.activity_main);
                    startMainPage();
                }
            }
            ois.close();
            fin.close();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this,"Please enter your details",Toast.LENGTH_SHORT).show();
            setContentView(R.layout.registration);
            startRegistrationPage();
        }
    }

    public void startMainPage() {
        login_b=(Button) findViewById(R.id.loginbutton);
        logout_b=(Button)findViewById(R.id.logoutbutton);

        login_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager=new LoginManager(MainActivity.this,"191");
                loginManager.execute("1","1","1");
            }
        });
        logout_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager=new LoginManager(MainActivity.this,"193");
                loginManager.execute("1","1","1");
            }
        });

        clearData();
    }

    public void startRegistrationPage() {
        uid=(EditText)findViewById(R.id.username);
        pass=(EditText)findViewById(R.id.password);
        save_b=(Button)findViewById(R.id.register_button);
        save_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uid.setError(null);
                pass.setError(null);
                if(uid.getText().toString().trim().isEmpty())
                    uid.setError("Username can't be blank");
                if(pass.getText().toString().trim().isEmpty())
                    pass.setError("Password can't be blank");
                else {
                    uid.setError(null);
                    pass.setError(null);
                    FileOutputStream fos;
                    try {
                        File file = new File(MainActivity.this.getFilesDir(), "configuration.dat");
                        fos = new FileOutputStream(file);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(new UserData(uid.getText().toString(), pass.getText().toString(), true));
                        oos.flush();
                        oos.close();
                        fos.close();
                        Toast.makeText(MainActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();
                        setContentView(R.layout.activity_main);
                        startMainPage();

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
    public void clearData() {
        clear_b=(Button)findViewById(R.id.clear_button);
        clear_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"In clear data",Toast.LENGTH_SHORT).show();
                displayAlertDialog();

            }
        });
    }

    public void displayAlertDialog() {
        AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
        adb.setTitle("Alert!")
                .setMessage("Do you really want to reset your data")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(MainActivity.this.getFilesDir(),"configuration.dat");
                        if(file.delete()) {
                            Toast.makeText(MainActivity.this, "Reset successful", Toast.LENGTH_SHORT).show();
                            setContentView(R.layout.registration);
                            startRegistrationPage();
                            Toast.makeText(MainActivity.this,"Please enter your details",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog ad=adb.create();
        ad.show();
    }

}
