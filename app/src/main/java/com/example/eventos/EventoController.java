package com.example.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class EventoController extends AppCompatActivity {
    private EditText etNome;
    private EditText etDescricao;
    private EditText etData;
    private EditText etValor;
    private EditText etQtdeVagas;
    private EditText etLocalRealizacao;
    private ImageButton btnSalvar;
    private ImageButton btnDelete;

    ListView ltEventos;
    List<Evento> eventos;

    DatabaseReference databaseEventos;

    private Evento eventoSelecionado;

    private ArrayAdapter<Evento> eventosAdaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_evento);

        databaseEventos = FirebaseDatabase.getInstance().getReference("eventos");

        etNome = findViewById(R.id.etNome);
        etDescricao = findViewById(R.id.etDescricao);
        etData = findViewById(R.id.etData);
        etValor = findViewById(R.id.etValor);
        etQtdeVagas = findViewById(R.id.etQtdeVagas);
        etLocalRealizacao = findViewById(R.id.etLocalRealizacao);

        btnSalvar = (ImageButton) findViewById(R.id.btnSalvar);
        btnDelete = (ImageButton) findViewById(R.id.btnDelete);

        ltEventos = (ListView) findViewById(R.id.ltEventos);

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

        ltEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Evento evento = eventos.get(i);
                showUpdateDeleteDialog(evento.getId(), evento.getNome(), evento.getDescricao(), evento.getData(), evento.getValor(), evento.getQtdeVagas(), evento.getLocalRealizacao());
            }
        });

        /*ltEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventoSelecionado = eventos.get(position);
                etNome.setText(eventoSelecionado.getNome());
                etDescricao.setText(eventoSelecionado.getDescricao());
                etData.setText(eventoSelecionado.getData());
                etValor.setText(eventoSelecionado.getValor().toString().trim());
                etQtdeVagas.setText(eventoSelecionado.getQtdeVagas());
                etLocalRealizacao.setText(eventoSelecionado.getLocalRealizacao());
            }
        });*/

        atualizaLista();
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

        atualizaLista();
    }

    private boolean updateEvento(String id, String nome, String descricao, String data, Double valor, int qtdeVagas, String localRealizacao) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("eventos").child(id);

        //updating artist
        Evento evento = new Evento(id, nome, descricao, data, valor, qtdeVagas, localRealizacao);
        dR.setValue(evento);
        Toast.makeText(getApplicationContext(), "Evento atualizado", Toast.LENGTH_LONG).show();
        return true;
    }

    private void showUpdateDeleteDialog(final String id, String nome, String descricao, String data, Double valor, int qtdeVagas, String localRealizacao) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.cadastro_evento, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.show();


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = etNome.getText().toString().trim();
                String descricao = etDescricao.getText().toString().trim();
                String data = etData.getText().toString().trim();
                String valor = etValor.getText().toString().trim();
                String qtdeVagas = etQtdeVagas.getText().toString().trim();
                String localRealizacao = etLocalRealizacao.getText().toString().trim();

                if (!TextUtils.isEmpty(nome) && !TextUtils.isEmpty(descricao) && !TextUtils.isEmpty(data) && !TextUtils.isEmpty(valor) && !TextUtils.isEmpty(qtdeVagas) && !TextUtils.isEmpty(localRealizacao)) {
                    double valorParse = Double.parseDouble(valor);
                    int qtdeVagasParse = Integer.parseInt(qtdeVagas);

                    updateEvento(id, nome, descricao, data, valorParse, qtdeVagasParse, localRealizacao);
                    b.dismiss();
                }
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                 * we will code this method to delete the artist
                 * */

            }
        });
    }

    public void listar(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void atualizaLista() {
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
                EventoList eventoAdapter = new EventoList(EventoController.this, eventos);
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
