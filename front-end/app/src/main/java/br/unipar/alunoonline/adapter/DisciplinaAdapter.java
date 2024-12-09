package br.unipar.alunoonline.adapter;

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

public class DisciplinaAdapter extends BaseAdapter {
    private Context context;
    private Disciplina disciplina;

    public DisciplinaAdapter(Context context, Disciplina disciplina) {
        this.context = context;
        this.disciplina = disciplina;
    }

    @Override
    public int getCount() {
        return disciplina.getAlunos().size();
    }

    @Override
    public Object getItem(int position) {
        return disciplina.getAlunos().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_disciplina, parent, false);
        }

        Aluno aluno = this.disciplina.getAlunos().get(position);
        Double media = aluno.mediaDasNotas();
        TextView ra = convertView.findViewById(R.id.ra);
        ra.setText(aluno.getRa());

        TextView mediaResult = convertView.findViewById(R.id.media);
        mediaResult.setText(media.toString());

        TextView nome = convertView.findViewById(R.id.nome);
        nome.setText(aluno.getNome());

        TextView resultado = convertView.findViewById(R.id.resultado);
        if (media < 6) {
            resultado.setText("Reprovado");
        } else {
            resultado.setText("Aprovado");
        }


        return convertView;
    }
}
