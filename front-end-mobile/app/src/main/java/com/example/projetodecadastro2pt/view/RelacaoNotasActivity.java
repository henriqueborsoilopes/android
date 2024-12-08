package com.example.projetodecadastro2pt.view;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projetodecadastro2pt.R;
import com.example.projetodecadastro2pt.adapter.NotasAdapter;
import com.example.projetodecadastro2pt.helper.DatabaseHelper;

import java.util.List;

public class RelacaoNotasActivity extends AppCompatActivity {

    private ListView listViewNotas;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relacao_notas_alunos);

        listViewNotas = findViewById(R.id.listViewNotas);
        databaseHelper = new DatabaseHelper(this);

        List<String> listaNotas = databaseHelper.getNotas();

        NotasAdapter adapter = new NotasAdapter(this, listaNotas);
        listViewNotas.setAdapter(adapter);
    }
}
