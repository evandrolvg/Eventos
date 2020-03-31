package com.example.eventos;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.ArrayList;

public class EventoController extends AppCompatActivity {
    private EditText etNome;
    private EditText etDescricao;
    private EditText etData;
    private EditText etValor;
    private EditText etQtdeVagas;
    private EditText etLocalRealizacao;
    private Button btnSalvar;

    DatabaseReference databaseEventos;

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

        btnSalvar = (Button) findViewById(R.id.btnSalvar);

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
}
