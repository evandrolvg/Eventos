package com.example.eventos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView ltEventos;

    List<Evento> eventos;

    /*public static final String ARTIST_NAME = "com.example.eventos.artistname";
    public static final String ARTIST_ID = "com.example.eventos.artistid";*/

    DatabaseReference databaseEventos;
    /*private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference eventoReferencia = FirebaseDatabase.getInstance().getReference("eventos");*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseEventos = FirebaseDatabase.getInstance().getReference("eventos");

        ltEventos = (ListView) findViewById(R.id.ltEventos);

        eventos = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseEventos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                eventos.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Evento evento = postSnapshot.getValue(Evento.class);
                    //adding artist to the list
                    eventos.add(evento);
                }

                //creating adapter
                EventoList eventoAdapter = new EventoList(MainActivity.this, eventos);
                //attaching adapter to the listview
                ltEventos.setAdapter(eventoAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            //a
            }
        });
    }

    public void logar(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
