package br.unipar.alunoonline.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import br.unipar.alunoonline.R;
import br.unipar.alunoonline.helper.DatabaseHelper;
import br.unipar.alunoonline.model.Disciplina;

public class CadastroNotasActivity extends AppCompatActivity {

    private EditText editTextRA, editTextNome, editTextNota;
    private Spinner spinnerDisciplina, spinnerBimestre;
    private Button btnAdicionar, btnNotas, btnMedia;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_notas);

        // Inicializar componentes da interface
        editTextRA = findViewById(R.id.editTextRA);
        editTextNome = findViewById(R.id.editTextNome);
        editTextNota = findViewById(R.id.editTextNota);
        spinnerDisciplina = findViewById(R.id.spinnerDisciplina);
        spinnerBimestre = findViewById(R.id.spinnerBimestre);
        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnNotas = findViewById(R.id.btnNotas);
        btnMedia = findViewById(R.id.btnMedia);

        databaseHelper = new DatabaseHelper(this);

        configurarSpinners();

        btnAdicionar.setOnClickListener(v -> adicionarNota());

        btnNotas.setOnClickListener(v -> abrirRelacaoNotas());

        btnMedia.setOnClickListener(v -> abrirRelacaoMedias());
    }

    private void configurarSpinners() {
        List<Disciplina> disciplinas = databaseHelper.getAllDisciplinas();
        String[] disciplinaNomes = disciplinas.stream()
                .map(Disciplina::getNome)
                .toArray(String[]::new);
        ArrayAdapter<String> disciplinaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, disciplinaNomes);
        disciplinaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDisciplina.setAdapter(disciplinaAdapter);

        String[] bimestres = {"1º Bimestre", "2º Bimestre", "3º Bimestre", "4º Bimestre"};
        ArrayAdapter<String> bimestreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bimestres);
        bimestreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBimestre.setAdapter(bimestreAdapter);
    }

    private void adicionarNota() {
        String ra = editTextRA.getText().toString();
        String nome = editTextNome.getText().toString();
        String disciplina = spinnerDisciplina.getSelectedItem().toString();
        String bimestre = spinnerBimestre.getSelectedItem().toString();
        String notaStr = editTextNota.getText().toString();

        if (ra.isEmpty() || nome.isEmpty() || notaStr.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        double nota = Double.parseDouble(notaStr);

        boolean inserido = databaseHelper.addNota(nota, bimestre, disciplina, nome, ra);

        if (inserido) {
            Toast.makeText(this, "Nota adicionada com sucesso!", Toast.LENGTH_SHORT).show();
            limparCampos();
        } else {
            Toast.makeText(this, "Erro ao adicionar nota.", Toast.LENGTH_SHORT).show();
        }
    }

    private void limparCampos() {
        editTextRA.setText("");
        editTextNome.setText("");
        editTextNota.setText("");
        spinnerDisciplina.setSelection(0);
        spinnerBimestre.setSelection(0);
    }

    private void abrirRelacaoNotas() {
        Intent intent = new Intent(CadastroNotasActivity.this, RelacaoNotasActivity.class);
        startActivity(intent);
    }

    private void abrirRelacaoMedias() {
        Intent intent = new Intent(CadastroNotasActivity.this, RelacaoMediasActivity.class);
        startActivity(intent);
    }
}
