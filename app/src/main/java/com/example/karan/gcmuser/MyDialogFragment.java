package com.example.karan.gcmuser;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class MyDialogFragment extends DialogFragment  {

    public interface EditDialogListener {
        void updateResult(int i);
    }







   EditText et;
    Button bt;
    public Context context;
    RequestQueue mQueue1;
    private ProgressDialog pDialog;
    private static final String LOGIN_URL = "http://kmodi4.net76.net/GcmMsg.php";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final String name = getArguments().getString("name");
        View view =  inflater.inflate(R.layout.fragment_my_dialog, container, false);
        mQueue1 = CustomVolleyRequestQueue.getInstance(this.getActivity())
                .getRequestQueue();
        getDialog().setTitle("GCM Service");

        et= (EditText) view.findViewById(R.id.sendEdit);
        bt = (Button) view.findViewById(R.id.sendbutton);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = et.getText().toString();
                startprogress("Sending msg...");
                 volleyRequest(name,msg);
                dismiss();
            }
        });
        return  view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        context = getActivity();
        //activityCommunicator =(ActivityCommunicator)context;
    }

    public void startprogress(String st){
        pDialog = new ProgressDialog(this.getActivity());

        pDialog.setMessage(st);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    public void volleyRequest(String username,String msg){

        JSONObject jo = new JSONObject();

        Log.i("name:", username);
        try {
            jo.put("name",username);
            jo.put("msg",msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(LOGIN_URL, jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        pDialog.dismiss();

                        try {
                            int i = response.getInt("success");
                            //String msg = response.getString("message");
                            EditDialogListener activity = (EditDialogListener) context;
                            if(activity!=null)
                                activity.updateResult(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Log.i("Eror:", error.toString());
                pDialog.dismiss();
                int i=-1;
                EditDialogListener activity = (EditDialogListener)context;
                if(activity!=null)
                    activity.updateResult(i);
                //Toast.makeText(getActivity(), "Unknown Error", Toast.LENGTH_SHORT).show();

            }
        });
        mQueue1.add(req);

    }



}
