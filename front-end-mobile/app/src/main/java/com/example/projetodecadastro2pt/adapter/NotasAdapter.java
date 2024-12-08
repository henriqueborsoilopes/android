package com.example.projetodecadastro2pt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.projetodecadastro2pt.R;

import java.util.List;

public class NotasAdapter extends BaseAdapter {
    private Context context;
    private List<String> notas;

    public NotasAdapter(Context context, List<String> notas) {
        this.context = context;
        this.notas = notas;
    }

    @Override
    public int getCount() {
        return notas.size();
    }

    @Override
    public Object getItem(int position) {
        return notas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_main, parent, false);
        }


        String nota = notas.get(position);


        TextView textNota = convertView.findViewById(R.id.textNota);
        textNota.setText(nota);

        return convertView;
    }
}
