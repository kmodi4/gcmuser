package com.example.karan.gcmuser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import co.mobiwise.fastgcm.GCMListener;
import co.mobiwise.fastgcm.GCMManager;

public class MainActivity extends AppCompatActivity implements GCMListener {

    RequestQueue mQueue1;
    private ProgressDialog pDialog;
    Context applicationContext;
    Button b,b2;
    EditText et;
    String rid="";
    String name;
    private static final String LOGIN_URL = "http://kmodi4.net76.net/reg.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et= (EditText) findViewById(R.id.editText);
        b= (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        mQueue1 = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();


        applicationContext = getApplicationContext();


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GCMManager.getInstance(applicationContext).registerListener(MainActivity.this);

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GCMManager.getInstance(applicationContext).unRegisterListener();
                SharedPreferences prefs = getSharedPreferences("UserDetails",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(MainActivity.this, "DeRegister Successfully", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void storeRegIdinSharedPref(Context context, String regid,
                                        String name) {

        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Log.i("regis:",regid);
        editor.putString("Regid", regid);
        editor.putString("Name", name);
        editor.apply();



    }
    public void startprogress(){
        pDialog = new ProgressDialog(MainActivity.this);

        pDialog.setMessage("Attempting for Registrating...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    public void volleyconnect(String regid, String name){

       /* Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password",password);*/
        JSONObject jo = new JSONObject();
        try {
            jo.put("regid", regid);
            jo.put("name", name);
            Log.i("json:",jo.getString("regid"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(LOGIN_URL, jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //VolleyLog.v("Response:%n %s", response.toString(4));
                            pDialog.dismiss();
                            int i = response.getInt("success");
                            String msg = response.getString("message");
                            if (i == 1) {
                                /*Intent ii = new Intent(register.this, MainActivity.class);
                                finish();
                                // this finish() method is used to tell android os that we are done with current //activity now! Moving to other activity
                                startActivity(ii);*/
                            }


                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Log.i("Eror:",error.toString());
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_SHORT).show();

            }
        });
        mQueue1.add(req);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeviceRegisted(String s) {
        Log.i("id:",s);
        int len = s.length();
        Log.i("len:",String.valueOf(len));
        rid=s;
        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        String registrationId = prefs.getString("Regid", "");
        name = et.getText().toString();
        Log.d("reg1", registrationId);
        Log.d("name",name);
        if(registrationId.equals("")){
            startprogress();
            storeRegIdinSharedPref(applicationContext, rid, name);
            volleyconnect(rid, name);
        }
        else{
            Toast.makeText(getApplicationContext(),"Already Have Registation Id",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onMessage(String s, Bundle bundle) {

    }

    @Override
    public void onPlayServiceError() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}