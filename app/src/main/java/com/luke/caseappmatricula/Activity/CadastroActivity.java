package com.luke.caseappmatricula.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.luke.caseappmatricula.Entity.Aluno;
import com.luke.caseappmatricula.R;
import com.luke.caseappmatricula.Util.ConfigFirebase;
import com.luke.caseappmatricula.Util.Mensagens;
import com.luke.caseappmatricula.Util.RotasFirebase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CadastroActivity extends Activity implements View.OnClickListener {

    private final int IMAGE_REQUEST = 1;

    private ImageView imgNewFotoC;
    private Uri caminhoFoto;
    private String urlFoto;
    private EditText edtNomeC, edtEmailC, edtSenhaC;
    private Button btnCadastrarC;
    private TextView txtLinkLogin;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference dbCadastroUsuario;
    private FirebaseStorage firebaseStorage;
    private StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        auth = ConfigFirebase.getFirebaseNAuth();
        //firebaseStorage = FirebaseStorage.getInstance();
        //storage = firebaseStorage.getReference();
    }

    @Override
    protected void onResume() {
        super.onResume();

        imgNewFotoC = findViewById(R.id.imgNewFotoC);
        edtNomeC = findViewById(R.id.edtNomeC);
        edtEmailC = findViewById(R.id.edtEmailC);
        edtSenhaC = findViewById(R.id.edtSenhaC);
        btnCadastrarC = findViewById(R.id.btnCadastrarC);
        txtLinkLogin = findViewById(R.id.txtLinkLogin);

        imgNewFotoC.setOnClickListener(this);
        btnCadastrarC.setOnClickListener(this);
        txtLinkLogin.setOnClickListener(this);
    }

    private void validaCampos(){

        String nome = edtNomeC.getText().toString();
        String email = edtEmailC.getText().toString();
        String senha = edtSenhaC.getText().toString();

        if(!nome.equals("") && !email.equals("") && !senha.equals("")){
            cadastrarUsuario(nome, email, senha);
        }else{
            Toast.makeText(getApplicationContext(), "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgNewFotoC:
                capturaFoto();
                break;
            case R.id.btnCadastrarC:
                validaCampos();
                break;
            case R.id.txtLinkLogin:
                returnToLogin();
                break;
        }
    }

    private void capturaFoto(){
        Intent it = new Intent();
        it.setType("image/*");
        it.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(it, "Selecione uma imagem"), IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            caminhoFoto = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), caminhoFoto);
                imgNewFotoC.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cadastrarUsuario(final String nome, final String email, final String senha){
        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            String id = task.getResult().getUser().getUid();
                            urlFoto = "";
                            List<String> listaIdCursos = new ArrayList<>();
                            Aluno aluno = new Aluno(id, nome, email, senha,"","","","", urlFoto, listaIdCursos);
                            dbCadastroUsuario = ConfigFirebase.getFirebaseRef(RotasFirebase.DB_REFERENCE_ALUNOS);
                            dbCadastroUsuario.child(id).setValue(aluno);
                            Toast.makeText(CadastroActivity.this, Mensagens.CADASTRO_SUCESSO, Toast.LENGTH_SHORT).show();
                            returnToLogin();
                        }else{
                            String erroExcecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erroExcecao = Mensagens.SENHA_FRACA;
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erroExcecao = Mensagens.EMAIL_INVALIDO;
                            } catch (FirebaseAuthUserCollisionException e) {
                                erroExcecao = Mensagens.EMAIL_JA_CADASTRADO;
                            } catch (Exception e) {
                                erroExcecao = Mensagens.ERRO_AO_EFETUAR_CADASTRO;
                                e.printStackTrace();
                            }
                            Toast.makeText(CadastroActivity.this, "" + erroExcecao, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void returnToLogin(){
        Intent it = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnToLogin();
    }
}
