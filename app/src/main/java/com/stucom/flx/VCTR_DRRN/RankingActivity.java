package com.stucom.flx.VCTR_DRRN;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.stucom.flx.VCTR_DRRN.model.MyVolley;
import com.stucom.flx.VCTR_DRRN.model.Player;
import com.stucom.flx.VCTR_DRRN.model.Players;
import com.stucom.flx.VCTR_DRRN.model.Prefs;


import org.json.JSONObject;

public class RankingActivity extends AppCompatActivity {

    TextView textView;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rankingtest);
        textView = findViewById(R.id.rankingTextView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadPlanets();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        downloadPlanets();
    }

    final String URL = "https://api.flx.cat/dam2game/ranking/"+ prefs.getToken();

    public void downloadPlanets() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String json = response.toString();
                        Gson gson = new Gson();
                        Players players = gson.fromJson(json, Players.class);
                        PlayersAdapter adapter = new PlayersAdapter(players);
                        recyclerView.setAdapter(adapter);

                        String message = "Downloaded " + players.getItems().size() + " players\n";
                        for (Player planet : players.getItems()) {
                            message += planet.getName() + ":" + planet.getAvatar() + "\n";
                        }
                        textView.setText(message);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = error.toString();
                        NetworkResponse response = error.networkResponse;
                        if (response != null) {
                            message = response.statusCode + " " + message;
                        }
                        textView.setText("ERROR " + message);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        MyVolley.getInstance(this).add(request);
    }

    class PlayersViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        PlayersViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.rankingTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    class PlayersAdapter extends RecyclerView.Adapter<PlayersViewHolder> {

        private Players players;

        PlayersAdapter(Players planets) {
            super();
            this.players = planets;
        }

        @NonNull @Override
        public PlayersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_item, parent, false);
            return new PlayersViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull PlayersViewHolder viewHolder, int position) {
            Player player = players.getItems().get(position);
            viewHolder.textView.setText(player.getName());
            Picasso.get().load(player.getAvatar()).into(viewHolder.imageView);
        }
        @Override
        public int getItemCount() {
            return players.getItems().size();
        }
    }
}
