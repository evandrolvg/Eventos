package com.example.eventos;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class InscricaoList extends ArrayAdapter<Inscricao> {
    private Activity context;
    List<Inscricao> inscricoes;

    public InscricaoList(Activity context, List<Inscricao> inscricoes) {
        super(context, R.layout.inscricoes_list, inscricoes);
        this.context = context;
        this.inscricoes = inscricoes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.inscricoes_list, null, true);

        TextView tvNome = (TextView) listViewItem.findViewById(R.id.tvNome);
        TextView tvEmail = (TextView) listViewItem.findViewById(R.id.tvEmail);

        Inscricao Inscricao = inscricoes.get(position);
        tvNome.setText(Inscricao.getNome());
        tvEmail.setText(Inscricao.getEmail());

        return listViewItem;
    }
}
