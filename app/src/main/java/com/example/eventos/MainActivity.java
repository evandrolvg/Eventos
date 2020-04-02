package com.example.eventos;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView ltEventos;
    List<Evento> eventos;
    DatabaseReference databaseEventos;

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
        databaseEventos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventos.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Evento evento = postSnapshot.getValue(Evento.class);
                    eventos.add(evento);
                }
                EventoList eventoAdapter = new EventoList(MainActivity.this, eventos);
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
