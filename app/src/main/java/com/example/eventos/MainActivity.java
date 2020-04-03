package com.example.eventos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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
    DatabaseReference databaseInscricoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseEventos = FirebaseDatabase.getInstance().getReference("eventos");
        ltEventos = (ListView) findViewById(R.id.ltEventos);
        eventos = new ArrayList<>();

        ltEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Evento evento = eventos.get(i);
                showInscricaoDialog(evento.getId());
            }
        });
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

    private void showInscricaoDialog(final String evento_id) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.inscricao_evento, null);
        dialogBuilder.setView(dialogView);

        final ImageButton btnInscricao = (ImageButton) dialogView.findViewById(R.id.btnInscricao);

        final EditText etNome = (EditText) dialogView.findViewById(R.id.etNomeInscricao);
        final EditText etEmail = (EditText) dialogView.findViewById(R.id.etEmailInscricao);


        final AlertDialog b = dialogBuilder.create();
        b.show();

        btnInscricao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = etNome.getText().toString().trim();
                String email = etEmail.getText().toString().trim();

                if (!TextUtils.isEmpty(nome) && !TextUtils.isEmpty(email)) {
                    inscricaoEvento(evento_id, nome, email);
                    b.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(), "Por favor, preencha todos os campos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean inscricaoEvento(String evento_id, String nome, String email) {
        databaseInscricoes = FirebaseDatabase.getInstance().getReference("inscricoes").child(evento_id);

        String id = databaseInscricoes.push().getKey();
        Inscricao inscricao = new Inscricao(id, nome, email);

        databaseInscricoes.child(id).setValue(inscricao);

        Toast.makeText(this, "Inscrito com sucesso.", Toast.LENGTH_LONG).show();

        return true;
    }
}
