package com.example.karan.gcmuser;

import com.example.karan.gcmuser.MyDialogFragment.EditDialogListener;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Userlist extends AppCompatActivity implements EditDialogListener {

    RequestQueue mQueue1;
    private ProgressDialog pDialog;
    private static final String LOGIN_URL = "http://kmodi4.net76.net/userlist.php";
    private static final String url = "http://kmodi4.net76.net/Gcmsend.php";
    ListView l1;
    String[] name;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        l1=(ListView)findViewById(R.id.listView);

        mQueue1 = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Bundle bundle = new Bundle();

                String username = String.valueOf(parent.getItemAtPosition(position));
                bundle.putString("name", username);
                FragmentManager fm = getFragmentManager();
                MyDialogFragment dialogFragment = new MyDialogFragment();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, "Sample Fragment");
                //startprogress("Sending msg...");
                //    volleyRequest(username);
            }
        });


        startprogress("Fetching List...");
        volleyconnect();
    }

    public void startprogress(String st){
        pDialog = new ProgressDialog(Userlist.this);

        pDialog.setMessage(st);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    public void volleyconnect(){

        JsonArrayRequest getRequest = new JsonArrayRequest(LOGIN_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        name = new String[response.length()];
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jresponse = null;
                            try {
                                jresponse = response.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            try {

                                if (jresponse != null) {
                                    name[i] = jresponse.getString("name");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }




                        // mTextView.setText(nickname);
                        pDialog.dismiss();
                        adapter = new ArrayAdapter<String>(Userlist.this,
                                android.R.layout.simple_list_item_1, name);
                        l1.setAdapter(adapter);
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("Error.Response", error.toString());
                        //mTextView.setText("Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),"could not fetch data",Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                }
        );
        mQueue1.add(getRequest);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_userlist, menu);
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
    public void updateResult(int i) {

        if(i==0)
            Toast.makeText(getApplicationContext(), "invalid Registration", Toast.LENGTH_SHORT).show();
        else if (i==1)
            Toast.makeText(getApplicationContext(),"Successfully sent", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"Unknown Error", Toast.LENGTH_SHORT).show();
    }
}
