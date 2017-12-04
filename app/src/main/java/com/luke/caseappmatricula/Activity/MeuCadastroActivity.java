package com.luke.caseappmatricula.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.luke.caseappmatricula.Entity.Aluno;
import com.luke.caseappmatricula.R;
import com.luke.caseappmatricula.Util.ConfigFirebase;
import com.luke.caseappmatricula.Util.Mensagens;
import com.luke.caseappmatricula.Util.RotasFirebase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MeuCadastroActivity extends AppCompatActivity {

    private ImageView imgFotoMC;
    private TextView txtMatriculaMC, txtNomeMC, txtEmailMC, txtEnderecoMC, txtNumeroMC, txtTefFixoMC, txtCelMC;
    private Button btnAlterarC;
    private ListView meusCursosList;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference dbAlunos, dbCursos;
    private Aluno mAluno;
    private String keyPass, urlFoto;
    private List<String> idCursos;
    private ProgressDialog progressDialog;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_cadastro);

        auth = ConfigFirebase.getFirebaseNAuth();
        user = ConfigFirebase.getFirebaseCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Carregando suas informações, aguarde...");
        progressDialog.show();

        imgFotoMC = findViewById(R.id.imgFotoMC);
        txtMatriculaMC = findViewById(R.id.txtMatriculaMC);
        txtNomeMC = findViewById(R.id.txtNomeMC);
        txtEmailMC = findViewById(R.id.txtEmailMC);
        txtEnderecoMC = findViewById(R.id.txtEnderecoMC);
        txtNumeroMC = findViewById(R.id.txtNumeroMC);
        txtTefFixoMC = findViewById(R.id.txtTefFixoMC);
        txtCelMC = findViewById(R.id.txtCelMC);
        btnAlterarC = findViewById(R.id.btnAlterarC);

        resgataDadosDoBanco();

        btnAlterarC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtNomeMC.getText().equals("") && !txtEmailMC.getText().equals("")){

                    String id = user.getUid();
                    String nome = txtNomeMC.getText().toString();
                    String email = txtEmailMC.getText().toString();
                    String senha = keyPass;
                    String endereco = txtEnderecoMC.getText().toString();
                    String numero = txtNumeroMC.getText().toString();
                    String telFixo = txtTefFixoMC.getText().toString();
                    String celular = txtCelMC.getText().toString();
                    list = new ArrayList<>();

                    mAluno = new Aluno(id, nome, email, senha, endereco, numero, telFixo, celular, urlFoto, list);
                    goToAlterarCadastro(mAluno);
                }else{
                    Toast.makeText(getApplicationContext(), Mensagens.CARREGAMENTO_DADOS, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resgataDadosDoBanco(){

        idCursos = new ArrayList<>();

        dbAlunos = ConfigFirebase.getFirebaseRef(RotasFirebase.DB_REFERENCE_ALUNOS);
        dbAlunos.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Aluno a = dataSnapshot.getValue(Aluno.class);
                txtNomeMC.setText(a.getNome());
                txtEmailMC.setText(a.getEmail());
                keyPass = a.getSenha();
                txtEnderecoMC.setText(a.getEndereço());
                txtNumeroMC.setText(a.getNumero());
                txtTefFixoMC.setText(a.getTelFixo());
                txtCelMC.setText(a.getCelular());
                urlFoto = a.getUrlFoto();
                list = a.getCursos();
                Picasso.with(MeuCadastroActivity.this).load(a.getUrlFoto()).into(imgFotoMC);
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void goToAlterarCadastro(Aluno aluno){
        Intent it = new Intent(MeuCadastroActivity.this, AlterarCadastroActivity.class);
        it.putExtra("aluno", aluno);
        startActivity(it);
        finish();
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
