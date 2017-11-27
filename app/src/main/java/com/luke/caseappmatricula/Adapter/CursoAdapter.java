package com.luke.caseappmatricula.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luke.caseappmatricula.Entity.Curso;
import com.luke.caseappmatricula.R;

import java.util.List;

/**
 * Created by Lucas on 16/11/2017.
 */

public class CursoAdapter extends ArrayAdapter {

    private Activity context;
    private int resource;
    private List<Curso> cursos;

    public CursoAdapter(@NonNull Activity context, int resource, @NonNull List objects) {
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

        TextView txtItemCurso = linha.findViewById(R.id.txtItemCurso);
        TextView txtItemProfessor = linha. findViewById(R.id.txtItemProfessor);

        txtItemCurso.setText(cursos.get(position).getCurso());
        txtItemProfessor.setText(cursos.get(position).getProfessor());

        return linha;
    }
}
