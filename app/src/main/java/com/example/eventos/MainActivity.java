package com.example.eventos;

import androidx.appcompat.app.AppCompatActivity;

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
    private EditText etNome;
    private EditText etDescricao;
    private EditText etData;
    private EditText etValor;
    private EditText etQtdeVagas;
    private EditText etLocalRealizacao;
    private Button btnSalvar;

    ListView ltEventos;

    List<Evento> eventos;

    public static final String ARTIST_NAME = "com.example.eventos.artistname";
    public static final String ARTIST_ID = "com.example.eventos.artistid";

    DatabaseReference databaseEventos;
    /*private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference eventoReferencia = FirebaseDatabase.getInstance().getReference("eventos");*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseEventos = FirebaseDatabase.getInstance().getReference("eventos");

        etNome = findViewById(R.id.etNome);
        etDescricao = findViewById(R.id.etDescricao);
        etData = findViewById(R.id.etData);
        etValor = findViewById(R.id.etValor);
        etQtdeVagas = findViewById(R.id.etQtdeVagas);
        etLocalRealizacao = findViewById(R.id.etLocalRealizacao);
        ltEventos = (ListView) findViewById(R.id.ltEventos);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);

        eventos = new ArrayList<>();

        //criar a mascara
        SimpleMaskFormatter simpleMaskFormatterData =
                new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher maskTextWatcherData =
                new MaskTextWatcher(etData, simpleMaskFormatterData);
        etData.addTextChangedListener(maskTextWatcherData);

        SimpleMaskFormatter simpleMaskFormatterValor =
                new SimpleMaskFormatter("N,N");
        MaskTextWatcher maskTextWatcherValor =
                new MaskTextWatcher(etValor, simpleMaskFormatterValor);
        etValor.addTextChangedListener(maskTextWatcherValor);

        //adding an onclicklistener to button
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addEvento();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void addEvento() throws ParseException {
        String nome = etNome.getText().toString().trim();
        String descricao = etDescricao.getText().toString().trim();
        String data = etData.getText().toString().trim();
        String valor = etValor.getText().toString().trim();
        String qtdeVagas = etQtdeVagas.getText().toString().trim();
        String localRealizacao = etLocalRealizacao.getText().toString().trim();

        if (!TextUtils.isEmpty(nome) && !TextUtils.isEmpty(descricao) && !TextUtils.isEmpty(data) && !TextUtils.isEmpty(valor) && !TextUtils.isEmpty(qtdeVagas) && !TextUtils.isEmpty(localRealizacao)) {
            String id = databaseEventos.push().getKey();

            //Date dataParse = new SimpleDateFormat("dd/MM/yyyy").parse(data);
            double valorParse = Double.parseDouble(valor);
            int qtdeVagasParse = Integer.parseInt(qtdeVagas);
            Evento evento = new Evento(id, nome, descricao, data, valorParse, qtdeVagasParse, localRealizacao);

            databaseEventos.child(id).setValue(evento);
            //etNome.setText("");

            Toast.makeText(this, "Evento cadastrado com sucesso.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_LONG).show();
        }

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
}
