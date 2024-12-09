package br.unipar.alunoonline.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.unipar.alunoonline.R;
import br.unipar.alunoonline.model.Aluno;
import br.unipar.alunoonline.model.Disciplina;
import br.unipar.alunoonline.model.Nota;

public class NotasAdapter extends BaseAdapter {
    private Context context;
    private List<Disciplina> disciplinas;

    public NotasAdapter(Context context, List<Disciplina> disciplinas) {
        this.context = context;
        this.disciplinas = disciplinas;
    }

    @Override
    public int getCount() {
        return disciplinas.size();
    }

    @Override
    public Object getItem(int position) {
        return disciplinas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false);
        }

        Disciplina disciplina = disciplinas.get(position);

        TextView textDisciplina = convertView.findViewById(R.id.textDisciplina);
        textDisciplina.setText(disciplina.getNome());
        TextView media = convertView.findViewById(R.id.media);
        media.setText(disciplina.getMedia().toString());

        Aluno aluno = disciplina.getAlunos().get(position);
        TextView bimestre1Nota = convertView.findViewById(R.id.bimestre1Nota);
        bimestre1Nota.setText(aluno.getNotas().get(1).getBimestre() + " - " + aluno.getNotas().get(1).getNota());
        TextView bimestre2Nota = convertView.findViewById(R.id.bimestre2Nota);
        bimestre2Nota.setText(aluno.getNotas().get(2).getBimestre() + " - " + aluno.getNotas().get(2).getNota());
        TextView bimestre3Nota = convertView.findViewById(R.id.bimestre3Nota);
        bimestre3Nota.setText(aluno.getNotas().get(3).getBimestre() + " - " + aluno.getNotas().get(3).getNota());
        TextView bimestre4Nota = convertView.findViewById(R.id.bimestre4Nota);
        bimestre4Nota.setText(aluno.getNotas().get(4).getBimestre() + " - " + aluno.getNotas().get(4).getNota());
        return convertView;
    }
}
