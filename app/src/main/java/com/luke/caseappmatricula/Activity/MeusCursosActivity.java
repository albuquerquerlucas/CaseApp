package com.luke.caseappmatricula.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.luke.caseappmatricula.Adapter.CursoAdapter;
import com.luke.caseappmatricula.Entity.Aluno;
import com.luke.caseappmatricula.Entity.Curso;
import com.luke.caseappmatricula.R;
import com.luke.caseappmatricula.Util.ConfigFirebase;
import com.luke.caseappmatricula.Util.RotasFirebase;

import java.util.ArrayList;
import java.util.List;

public class MeusCursosActivity extends AppCompatActivity {

    private ListView listaMeusCursos;
    private List<Curso> cursos;
    private List<Aluno> alunos;
    private CursoAdapter cursoAdapter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference dbAlunos, dbCursos;
    private String itemSelecionado;
    private List<String> listaCursosAlunos, listaR;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_cursos);

        auth = ConfigFirebase.getFirebaseNAuth();
        user = ConfigFirebase.getFirebaseCurrentUser();
        dbAlunos = ConfigFirebase.getFirebaseRef(RotasFirebase.DB_REFERENCE_ALUNOS);
        dbCursos = ConfigFirebase.getFirebaseRef(RotasFirebase.DB_REFERENCE_CURSOS);
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Consultando seus cursos, aguarde...");
        progressDialog.show();

        listaMeusCursos = findViewById(R.id.listaMeusCursos);

        Query query = dbAlunos.orderByKey().equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        if(data!= null){
                            trazCursosMatriculados(data);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void trazCursosMatriculados(DataSnapshot data){

        Aluno a = data.getValue(Aluno.class);
        cursos = new ArrayList<>();
        List<String> idCursosLista = a.getCursos();

        if(a.getCursos() != null){
            for(int i = 0; i < idCursosLista.size(); i++){
                Query query = dbCursos.orderByKey().equalTo(idCursosLista.get(i));
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //cursos.clear();
                        if(dataSnapshot.exists()){
                            for(DataSnapshot dat : dataSnapshot.getChildren()){
                                Curso c = dat.getValue(Curso.class);
                                cursos.add(c);
                            }
                            cursoAdapter = new CursoAdapter(MeusCursosActivity.this, R.layout.item_curso_lista, cursos);
                            listaMeusCursos.setAdapter(cursoAdapter);
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }else{
            progressDialog.dismiss();
            //Toast.makeText(MeusCursosActivity.this, "Você não está matriculado em nenhum curso.", Toast.LENGTH_SHORT).show();
        }
    }


    private void goToMenuPrincipal(){
        Intent it = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        goToMenuPrincipal();
    }
}
