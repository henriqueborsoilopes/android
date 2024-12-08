package com.example.projetodecadastro2pt.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetodecadastro2pt.R;
import com.example.projetodecadastro2pt.helper.DatabaseHelper;

import java.util.List;

public class RelacaoMediasActivity extends AppCompatActivity {

    private Spinner spinnerDisciplina;
    private TextView mediaResultados;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relacao_media_disciplina);

        spinnerDisciplina = findViewById(R.id.spinner_disciplina);
        mediaResultados = findViewById(R.id.title);

        databaseHelper = new DatabaseHelper(this);

        // Carregar as disciplinas no spinner
        carregarDisciplinas();

        // Configurar o ouvinte de seleção do spinner
        spinnerDisciplina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Verifica se a lista não está vazia e se a posição é válida
                if (parent.getCount() > 0 && position >= 0) {
                    String disciplinaSelecionada = parent.getItemAtPosition(position).toString();
                    carregarMedias(disciplinaSelecionada);
                } else {
                    mediaResultados.setText("Selecione uma disciplina válida.");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Caso nada seja selecionado, pode exibir uma mensagem ou realizar alguma outra ação
                mediaResultados.setText("Nenhuma disciplina selecionada.");
            }
        });
    }

    // Método para carregar as disciplinas no Spinner
    private void carregarDisciplinas() {
        List<String> disciplinas = databaseHelper.getDisciplinas();

        // Verifica se a lista de disciplinas não está vazia
        if (disciplinas != null && !disciplinas.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, disciplinas);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDisciplina.setAdapter(adapter);
        } else {
            mediaResultados.setText("Nenhuma disciplina encontrada.");
        }
    }

    // Método para carregar as médias de acordo com a disciplina selecionada
    private void carregarMedias(String disciplina) {
        List<String> medias = databaseHelper.getMediasByDisciplina(disciplina);

        // Verifica se as médias foram encontradas
        if (medias != null && !medias.isEmpty()) {
            StringBuilder resultados = new StringBuilder();
            for (String media : medias) {
                resultados.append(media).append("\n");
            }
            mediaResultados.setText(resultados.toString());
        } else {
            mediaResultados.setText("Nenhuma média encontrada para a disciplina: " + disciplina);
        }
    }
}
