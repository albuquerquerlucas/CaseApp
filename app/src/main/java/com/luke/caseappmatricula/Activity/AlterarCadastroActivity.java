package com.luke.caseappmatricula.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luke.caseappmatricula.Entity.Aluno;
import com.luke.caseappmatricula.R;
import com.luke.caseappmatricula.Util.Mensagens;

public class AlterarCadastroActivity extends AppCompatActivity {

    private ImageView imgFotoAC;
    private EditText edtNomeAC, edtEmailAC, edtEnderecoAC, edtNumeroAC, edtTelFixoAC, edtCelularAC;
    private Button btnEntrarL;
    private Aluno aluno;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference dbAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_cadastro);

        Intent it = getIntent();
        aluno = (Aluno) it.getSerializableExtra("aluno");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        dbAlunos = FirebaseDatabase.getInstance().getReference("alunos");
    }

    @Override
    protected void onResume() {
        super.onResume();

        imgFotoAC = findViewById(R.id.imgFotoAC);
        edtNomeAC = findViewById(R.id.edtNomeAC);
        edtEmailAC = findViewById(R.id.edtEmailAC);
        edtEnderecoAC = findViewById(R.id.edtEnderecoAC);
        edtNumeroAC = findViewById(R.id.edtNumeroAC);
        edtTelFixoAC = findViewById(R.id.edtTelFixoAC);
        edtCelularAC = findViewById(R.id.edtCelularAC);
        btnEntrarL = findViewById(R.id.btnEntrarL);

        edtNomeAC.setText(aluno.getNome());
        edtEmailAC.setText(aluno.getEmail());
        edtEnderecoAC.setText(aluno.getEndereço());
        edtNumeroAC.setText(aluno.getNumero());
        edtTelFixoAC.setText(aluno.getTelFixo());
        edtCelularAC.setText(aluno.getCelular());

        btnEntrarL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarDadosDoAluno();
            }
        });
    }

    private void atualizarDadosDoAluno(){

        String id = user.getUid();
        String nome = edtNomeAC.getText().toString();
        String email = edtEmailAC.getText().toString();
        String endereco = edtEnderecoAC.getText().toString();
        String numero = edtNumeroAC.getText().toString();
        String telFixo = edtTelFixoAC.getText().toString();
        String celular = edtCelularAC.getText().toString();
        String urlFoto = "";

        Aluno a = new Aluno(id, nome, email, aluno.getSenha(), endereco, numero, telFixo, celular, urlFoto, aluno.getCursos());

        dbAlunos.child(user.getUid()).setValue(a);
        Toast.makeText(getApplicationContext(), Mensagens.ATUALIZACAO_SUCESSO, Toast.LENGTH_SHORT).show();
        returnToMeuCadastro();
    }

    private void returnToMeuCadastro(){
        Intent it = new Intent(getApplicationContext(), MeuCadastroActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnToMeuCadastro();
    }
}
