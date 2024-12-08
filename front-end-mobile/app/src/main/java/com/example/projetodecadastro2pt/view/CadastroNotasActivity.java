package com.example.projetodecadastro2pt.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetodecadastro2pt.R;
import com.example.projetodecadastro2pt.helper.DatabaseHelper;

public class CadastroNotasActivity extends AppCompatActivity {

    private EditText editRA, editNome, editNota;
    private Spinner spinnerBimestre, spinnerDisciplina;
    private Button btnAdicionar, btnVerNotas, btnVerMedias;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editRA = findViewById(R.id.editTextRA);
        editNome = findViewById(R.id.editTextNome);
        spinnerDisciplina = findViewById(R.id.spinnerDisciplina);
        editNota = findViewById(R.id.editTextNota);
        spinnerBimestre = findViewById(R.id.spinnerBimestre);
        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnVerNotas = findViewById(R.id.btnNotas);
        btnVerMedias = findViewById(R.id.btnMedia);

        databaseHelper = new DatabaseHelper(this);

        btnAdicionar.setOnClickListener(v -> {
            String ra = editRA.getText().toString();
            String nome = editNome.getText().toString();
            String disciplina = spinnerDisciplina.getSelectedItem().toString();
            String bimestre = spinnerBimestre.getSelectedItem().toString();
            double nota = Double.parseDouble(editNota.getText().toString());

            if (databaseHelper.addNota(ra, nome, disciplina, nota, bimestre)) {
                Toast.makeText(CadastroNotasActivity.this, "Nota adicionada com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CadastroNotasActivity.this, "Erro ao adicionar nota.", Toast.LENGTH_SHORT).show();
            }
        });

        btnVerNotas.setOnClickListener(v -> startActivity(new Intent(this, RelacaoNotasActivity.class)));
        btnVerMedias.setOnClickListener(v -> startActivity(new Intent(this, RelacaoMediasActivity.class)));
    }
}
