package com.luke.caseappmatricula.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.luke.caseappmatricula.Entity.Curso;
import com.luke.caseappmatricula.R;

import java.util.List;

/**
 * Created by Lucas on 19/11/2017.
 */

public class MeusCursosAdapter extends ArrayAdapter {

    private Activity context;
    private int resource;
    private List<Curso> cursos;

    public MeusCursosAdapter(@NonNull Activity context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.cursos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View linha = inflater.inflate(resource, null);

        ViewHolder vHolder = new ViewHolder();
        //System.out.println("Professor" + cursos.get(position).getProfessor());
        vHolder.txtItemCursoSpinnerM = (TextView) linha.findViewById(R.id.txtItemCursoSpinnerM);
        vHolder.txtItemCursoSpinnerM.setText((String) cursos.get(position).getCurso().toString());

        return linha;
    }

    static class ViewHolder{

        TextView txtItemCursoSpinnerM;
    }
}
