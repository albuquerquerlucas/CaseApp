package com.luke.caseappmatricula.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luke.caseappmatricula.Adapter.CursoAdapter;
import com.luke.caseappmatricula.Entity.Curso;
import com.luke.caseappmatricula.R;
import com.luke.caseappmatricula.Util.ConfigFirebase;
import com.luke.caseappmatricula.Util.Mensagens;
import com.luke.caseappmatricula.Util.RotasFirebase;

import java.util.ArrayList;
import java.util.List;

public class CursosActivity extends AppCompatActivity {

    private ListView cursosLista;
    private List<Curso> cursos;
    private CursoAdapter cursoAdapter;
    private AlertDialog alertDialog, alertDialogConfirExclusao;
    private DatabaseReference dbCursos, dbAlunos;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private List<String> idCursosLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        auth = ConfigFirebase.getFirebaseNAuth();
        user = ConfigFirebase.getFirebaseCurrentUser();
        dbAlunos = ConfigFirebase.getFirebaseRef(RotasFirebase.DB_REFERENCE_ALUNOS);
        dbCursos = ConfigFirebase.getFirebaseRef(RotasFirebase.DB_REFERENCE_CURSOS);
        idCursosLista = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Checando os cursos disponíveis, aguarde...");
        progressDialog.show();

        cursosLista = findViewById(R.id.cursosLista);
        cursos = new ArrayList<>();

        dbCursos.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cursos.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Curso c = snapshot.getValue(Curso.class);
                    cursos.add(c);
                }
                cursoAdapter = new CursoAdapter(CursosActivity.this, R.layout.item_curso_lista, cursos);
                cursosLista.setAdapter(cursoAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cursosLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Curso c = cursos.get(position);
                exibirDetalhes(c);
            }
        });

        cursosLista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Curso cc = cursos.get(position);
                efetuarMatricula(cc.getId());
                return true;
            }
        });
    }

    public void efetuarMatricula(final String id){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.confirm_matricula, null);
        dialogBuilder.setView(dialogView);

        TextView txtTextoMatricula = dialogView.findViewById(R.id.txtTextoMatricula);
        Button btnEfetuarMatricula = dialogView.findViewById(R.id.btnEfetuarMatricula);

        txtTextoMatricula.setText("Deseja efetuar matrícula para este curso?");

        alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnEfetuarMatricula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idCursosLista.add(id);
                dbAlunos.child(user.getUid()).child("cursos").setValue(idCursosLista);
                alertDialog.dismiss();
            }
        });
    }

    public void exibirDetalhes(Curso curso){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.detalhes_curso, null);
        dialogBuilder.setView(dialogView);

        TextView detalhesNomeCurso = dialogView.findViewById(R.id.detalhesNomeCurso);
        TextView detalhesNomeProfessor = dialogView.findViewById(R.id.detalhesNomeProfessor);
        TextView txtDetalhesDescricao = dialogView.findViewById(R.id.txtDetalhesDescricao);
        Button btnDetalhesFechar = dialogView.findViewById(R.id.btnDetalhesFechar);

        detalhesNomeCurso.setText(curso.getCurso());
        detalhesNomeProfessor.setText(curso.getProfessor());
        if(!txtDetalhesDescricao.equals("")){
            txtDetalhesDescricao.setText(curso.getDescricao());
        }else{
            txtDetalhesDescricao.setText("informações não disponíveis");
        }

        alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnDetalhesFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void goToMenuPrincipal(){
        Intent it = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMenuPrincipal();
    }
}
