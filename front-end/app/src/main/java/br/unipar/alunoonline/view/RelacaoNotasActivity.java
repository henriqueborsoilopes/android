package br.unipar.alunoonline.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import br.unipar.alunoonline.R;
import br.unipar.alunoonline.adapter.NotasAdapter;
import br.unipar.alunoonline.helper.DatabaseHelper;
import br.unipar.alunoonline.model.Aluno;
import br.unipar.alunoonline.model.Disciplina;

public class RelacaoNotasActivity extends AppCompatActivity {

    private Spinner spinnerAluno;
    private ListView listViewNotas;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relacao_notas_alunos);

        spinnerAluno = findViewById(R.id.spinner_aluno);
        listViewNotas = findViewById(R.id.listViewNotas);

        databaseHelper = new DatabaseHelper(this);

        carregarAlunos();

        spinnerAluno.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String alunoSelecionado = (String) spinnerAluno.getSelectedItem();
                carregarNotas(alunoSelecionado);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {

            }
        });
    }

    private void carregarAlunos() {
        List<Aluno> listaAlunos = databaseHelper.getAllAlunos();
        String[] listaAlunoNomes = listaAlunos.stream()
                .map(Aluno::getNome)
                .toArray(String[]::new);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaAlunoNomes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAluno.setAdapter(adapter);
    }

    private void carregarNotas(String alunoNome) {
        Aluno aluno = databaseHelper.getAlunoByNome(alunoNome);
        List<Disciplina> listaDisciplinas = databaseHelper.getDisciplinasPorAluno(aluno.getId());

        if (listaDisciplinas.isEmpty()) {
            Toast.makeText(this, "Nenhuma nota encontrada para o aluno selecionado.", Toast.LENGTH_SHORT).show();
        }

        NotasAdapter adapter = new NotasAdapter(this, listaDisciplinas);
        listViewNotas.setAdapter(adapter);
    }
}