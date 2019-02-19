package com.stucom.flx.VCTR_DRRN;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stucom.flx.VCTR_DRRN.api.APIResponse;
import com.stucom.flx.VCTR_DRRN.model.MyVolley;
import com.stucom.flx.VCTR_DRRN.model.Player;
import com.stucom.flx.VCTR_DRRN.model.Prefs;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class VerifyActivity extends AppCompatActivity {
    Prefs prefs;
    Player player;
    EditText verifyField;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        verifyField = findViewById(R.id.editText);
        button = findViewById(R.id.btn_verify);
        final String code = verifyField.getText().toString();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String URL = "https://api.flx.cat/dam2game/register";
                StringRequest request = new StringRequest
                        (Request.Method.POST, URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        String json = response;
                                        Gson gson = new Gson();
                                        Type typeToken = new TypeToken<APIResponse<String>>() {}.getType();
                                        APIResponse<String> apiResponse = gson.fromJson(json, typeToken);
                                        String userToken = apiResponse.getData();
                                        //Guardar el token el el SharedPreferences
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(VerifyActivity.this);
                                        Context context = getApplicationContext();

                                        //Si el token no esta vacio nos va a llevar al menu principal
                                        //Sino se va a quedar en la activity actual para pedir el registro
                                        if (!userToken.isEmpty()) {
                                            Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            //Toast.makeText(RegistrarCode.this, userToken, Toast.LENGTH_LONG).show();
                                            final SharedPreferences.Editor editor = prefs.edit();
                                            editor.putString("token", userToken);
                                            editor.apply();
                                        } else {
                                            Toast.makeText(VerifyActivity.this, "Token:" + userToken, Toast.LENGTH_LONG).show();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                String message = error.toString();
                                NetworkResponse response = error.networkResponse;
                                if (response != null) {
                                    Context context = getApplicationContext();
                                    CharSequence text = response.statusCode + " " + message;
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", verifyField.getText().toString());
                        params.put("verify", code);
                        return params;
                    }
                };
                MyVolley.getInstance(VerifyActivity.this).add(request);
            }

        });
    }

}
