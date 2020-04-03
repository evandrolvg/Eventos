package com.example.eventos;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class EventoController extends AppCompatActivity {
    private EditText etNome;
    private EditText etDescricao;
    private EditText etData;
    private EditText etValor;
    private EditText etQtdeVagas;
    private EditText etLocalRealizacao;
    private ImageButton btnSalvar;
    private ImageButton btnDelete;
    private ImageButton btnInscritos;

    private FloatingActionButton btnAddEvento;

    ListView ltEventos;
    List<Evento> eventos;

    ListView ltInscricoes;
    List<Inscricao> inscricoes;

    DatabaseReference databaseEventos;
    DatabaseReference databaseInscricoes;

    private Evento eventoSelecionado;

    private ArrayAdapter<Evento> eventosAdaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_evento);

        databaseEventos = FirebaseDatabase.getInstance().getReference("eventos");

        etNome = findViewById(R.id.etNome);
        etDescricao = findViewById(R.id.etDescricao);
        etData = findViewById(R.id.etData);
        etValor = findViewById(R.id.etValor);
        etQtdeVagas = findViewById(R.id.etQtdeVagas);
        etLocalRealizacao = findViewById(R.id.etLocalRealizacao);

        btnSalvar = (ImageButton) findViewById(R.id.btnSalvar);
        btnDelete = (ImageButton) findViewById(R.id.btnDelete);
        btnInscritos = (ImageButton) findViewById(R.id.btnInscritos);

        btnAddEvento = (FloatingActionButton) findViewById(R.id.btnAddEvento);

        ltEventos = (ListView) findViewById(R.id.ltEventos);

        eventos = new ArrayList<>();

        ltInscricoes = (ListView) findViewById(R.id.ltInscricoes);

        inscricoes = new ArrayList<>();

        ltEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Evento evento = eventos.get(i);
                showUpdateDeleteDialog(evento.getId(), evento.getNome(), evento.getDescricao(), evento.getData(), evento.getValor(), evento.getQtdeVagas(), evento.getLocalRealizacao());
            }
        });

        btnAddEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

        atualizaLista();
    }

    private void showAddDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_evento, null);
        dialogBuilder.setView(dialogView);

        final ImageButton btnSalvar = (ImageButton) dialogView.findViewById(R.id.btnSalvar);

        final EditText etNome = (EditText) dialogView.findViewById(R.id.etNome);
        final EditText etDescricao = (EditText) dialogView.findViewById(R.id.etDescricao);
        final EditText etData = (EditText) dialogView.findViewById(R.id.etData);
        final EditText etValor = (EditText) dialogView.findViewById(R.id.etValor);
        final EditText etQtdeVagas = (EditText) dialogView.findViewById(R.id.etQtdeVagas);
        final EditText etLocalRealizacao = (EditText) dialogView.findViewById(R.id.etLocalRealizacao);

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

                    addUpdateEvento(NULL, nome, descricao, data, valorParse, qtdeVagasParse, localRealizacao);
                    b.dismiss();
                    atualizaLista();
                }else{
                    Toast.makeText(getApplicationContext(), "Por favor, preencha todos os campos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showUpdateDeleteDialog(final String id, String nome, String descricao, String data, Double valor, int qtdeVagas, String localRealizacao) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_evento, null);
        dialogBuilder.setView(dialogView);

        final ImageButton btnSalvar = (ImageButton) dialogView.findViewById(R.id.btnSalvar);
        final ImageButton btnDelete = (ImageButton) dialogView.findViewById(R.id.btnDelete);
        final ImageButton btnInscritos = (ImageButton) dialogView.findViewById(R.id.btnInscritos);

        final EditText etNome = (EditText) dialogView.findViewById(R.id.etNome);
        final EditText etDescricao = (EditText) dialogView.findViewById(R.id.etDescricao);
        final EditText etData = (EditText) dialogView.findViewById(R.id.etData);
        final EditText etValor = (EditText) dialogView.findViewById(R.id.etValor);
        final EditText etQtdeVagas = (EditText) dialogView.findViewById(R.id.etQtdeVagas);
        final EditText etLocalRealizacao = (EditText) dialogView.findViewById(R.id.etLocalRealizacao);
        final ListView ltInscricoes = (ListView) dialogView.findViewById(R.id.ltInscricoes);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        etNome.setText(nome);
        etDescricao.setText(descricao);
        etData.setText(data);
        etValor.setText(valor.toString());
        etQtdeVagas.setText(String.valueOf(qtdeVagas));
        etLocalRealizacao.setText(localRealizacao);

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

                    addUpdateEvento(id, nome, descricao, data, valorParse, qtdeVagasParse, localRealizacao);
                    b.dismiss();
                    atualizaLista();
                }else{
                    Toast.makeText(getApplicationContext(), "Por favor, preencha todos os campos", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeEvento(id);
                b.dismiss();
            }
        });

        btnInscritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseInscricoes = FirebaseDatabase.getInstance().getReference("inscricoes").child(id);
                databaseInscricoes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        inscricoes.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Inscricao inscricao = postSnapshot.getValue(Inscricao.class);
                            inscricoes.add(inscricao);
                        }

                        if (inscricoes.size() > 0) {
                            InscricaoList inscricaoAdapter = new InscricaoList(EventoController.this, inscricoes);

                            ltInscricoes.setAdapter(inscricaoAdapter);
                        }else{
                            Toast.makeText(getApplicationContext(), "Nenhum inscrito", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("databaseError", String.valueOf(databaseError));
                    }
                });
            }
        });

    }

    private boolean addUpdateEvento(String id, String nome, String descricao, String data, Double valor, int qtdeVagas, String localRealizacao) {
        //Log.d("ID", "ID:" + id);
        if (!TextUtils.isEmpty(id) && !id.equals(NULL)){
            Evento evento = new Evento(id, nome, descricao, data, valor, qtdeVagas, localRealizacao);

            databaseEventos.child(id).setValue(evento);
            Toast.makeText(getApplicationContext(), "Evento atualizado", Toast.LENGTH_LONG).show();
        }else{
            String idAdd = databaseEventos.push().getKey();
            Evento evento = new Evento(idAdd, nome, descricao, data, valor, qtdeVagas, localRealizacao);

            databaseEventos.child(idAdd).setValue(evento);
            //etNome.setText("");

            Toast.makeText(this, "Evento cadastrado com sucesso.", Toast.LENGTH_LONG).show();
        }

        return true;
    }

    private boolean removeEvento(String id) {
        if (!TextUtils.isEmpty(id)){
            databaseEventos = FirebaseDatabase.getInstance().getReference("eventos").child(id);
            databaseEventos.removeValue();
            Toast.makeText(getApplicationContext(), "Evento removido", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Ocorreu algum erro", Toast.LENGTH_LONG).show();
        }

        return true;
    }

    public void atualizaLista() {
        databaseEventos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventos.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Evento evento = postSnapshot.getValue(Evento.class);
                    eventos.add(evento);
                }

                EventoList eventoAdapter = new EventoList(EventoController.this, eventos);
                ltEventos.setAdapter(eventoAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //a
            }
        });
    }
}
