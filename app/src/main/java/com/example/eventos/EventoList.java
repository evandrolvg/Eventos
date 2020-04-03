package com.example.eventos;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class EventoList extends ArrayAdapter<Evento> {
    private Activity context;
    List<Evento> eventos;

    public EventoList(Activity context, List<Evento> eventos) {
        super(context, R.layout.eventos_list, eventos);
        this.context = context;
        this.eventos = eventos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.eventos_list, null, true);

        TextView tvNome = (TextView) listViewItem.findViewById(R.id.tvNome);
        TextView tvDescricao = (TextView) listViewItem.findViewById(R.id.tvEmail);
        TextView tvData = (TextView) listViewItem.findViewById(R.id.tvData);
        TextView tvValor = (TextView) listViewItem.findViewById(R.id.tvValor);
        TextView tvQtdeVagas = (TextView) listViewItem.findViewById(R.id.tvQtdeVagas);
        TextView tvLocalRealizacao = (TextView) listViewItem.findViewById(R.id.tvLocalRealizacao);

        Evento evento = eventos.get(position);
        tvNome.setText(evento.getNome());
        tvDescricao.setText(evento.getDescricao());
        tvData.setText(evento.getData());
        tvValor.setText(String.valueOf(evento.getValor()));
        tvQtdeVagas.setText(String.valueOf(evento.getQtdeVagas()));
        tvLocalRealizacao.setText(evento.getLocalRealizacao());

        //Log.d("listview", "Evento");
        return listViewItem;
    }
}
