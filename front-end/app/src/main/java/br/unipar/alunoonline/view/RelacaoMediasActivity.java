package br.unipar.alunoonline.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import br.unipar.alunoonline.R;
import br.unipar.alunoonline.adapter.DisciplinaAdapter;
import br.unipar.alunoonline.helper.DatabaseHelper;
import br.unipar.alunoonline.model.Disciplina;

public class RelacaoMediasActivity extends AppCompatActivity {

    private Spinner spinnerDisciplina;
    private ListView listViewAlunos;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relacao_media_disciplina);

        spinnerDisciplina = findViewById(R.id.spinner_disciplina);
        listViewAlunos = findViewById(R.id.listViewAlunos);

        databaseHelper = new DatabaseHelper(this);

        carregarDisciplinas();

        spinnerDisciplina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getCount() > 0 && position >= 0) {
                    String disciplinaSelecionada = parent.getItemAtPosition(position).toString();
                    carregarMedias(disciplinaSelecionada);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void carregarDisciplinas() {
        List<Disciplina> disciplinas = databaseHelper.getAllDisciplinas();
        String[] disciplinaNomes = disciplinas.stream()
                .map(Disciplina::getNome)
                .toArray(String[]::new);
        if (disciplinas != null && !disciplinas.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, disciplinaNomes);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDisciplina.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Nenhuma disciplina encontrada.", Toast.LENGTH_SHORT).show();
        }
    }

    private void carregarMedias(String disciplinaNome) {
        Disciplina disciplina = databaseHelper.getDisciplinaByNome(disciplinaNome);
        disciplina.getAlunos().addAll(databaseHelper.getAlunosPorDisciplina(disciplina.getId()));

        if (disciplina == null) {
            Toast.makeText(this, "Nenhuma média encontrada para a disciplina: " + disciplinaNome, Toast.LENGTH_SHORT).show();
        }

        DisciplinaAdapter adapter = new DisciplinaAdapter(this, disciplina);
        listViewAlunos.setAdapter(adapter);
    }
}
