package com.luke.caseappmatricula.Activity;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.luke.caseappmatricula.Entity.Aluno;
import com.luke.caseappmatricula.R;
import com.luke.caseappmatricula.Util.ConfigFirebase;
import com.luke.caseappmatricula.Util.Mensagens;
import com.luke.caseappmatricula.Util.RotasFirebase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlterarCadastroActivity extends AppCompatActivity {

    private final int IMAGE_REQUEST = 2;

    private ImageView imgFotoAC;
    private Uri caminhoFoto;
    private String urlFoto, url;
    private EditText edtNomeAC, edtEmailAC, edtEnderecoAC, edtNumeroAC, edtTelFixoAC, edtCelularAC;
    private Button btnEntrarL;
    private Aluno aluno;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference dbAlunos;
    private StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_cadastro);

        Intent it = getIntent();
        aluno = (Aluno) it.getSerializableExtra("aluno");
        url = aluno.getUrlFoto();
        imgFotoAC = findViewById(R.id.imgFotoAC);
        Picasso.with(this).load(url).into(imgFotoAC);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        dbAlunos = FirebaseDatabase.getInstance().getReference("alunos");
        storage = ConfigFirebase.getFirebaseRefStorage();
    }

    @Override
    protected void onResume() {
        super.onResume();

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
                if(caminhoFoto != null){
                    atualizarDadosDoAluno();
                }
            }
        });

        imgFotoAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturaFoto();
            }
        });
    }

    private void capturaFoto(){
        Intent it = new Intent();
        it.setType("image/*");
        it.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(it, Mensagens.SELECIONE_FOTO), IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            caminhoFoto = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), caminhoFoto);
                imgFotoAC.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void atualizarDadosDoAluno(){

        final String id = user.getUid();
        final String nome = edtNomeAC.getText().toString();
        //final String email = edtEmailAC.getText().toString();
        final String endereco = edtEnderecoAC.getText().toString();
        final String numero = edtNumeroAC.getText().toString();
        final String telFixo = edtTelFixoAC.getText().toString();
        final String celular = edtCelularAC.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(Mensagens.FAZENDO_UPLOAD_AGUARDE);
        progressDialog.show();

        StorageReference ref = storage.child("imagens/" + System.currentTimeMillis() + "." + caminhoFoto.getLastPathSegment());
        ref.putFile(caminhoFoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                urlFoto = taskSnapshot.getDownloadUrl().toString();
                imgFotoAC.setImageDrawable(getResources().getDrawable(R.drawable.icnewfoto));

                List<String> iid = new ArrayList<>();
                for(String ssid : aluno.getCursos()){
                    iid.add(ssid);
                    Log.i("ARRAY DO KARAI DA LISTA", "" + iid.toArray());
                }

                Aluno a = new Aluno(id, nome, aluno.getEmail(), aluno.getSenha(), endereco, numero, telFixo, celular, urlFoto, aluno.getCursos());
                dbAlunos.child(user.getUid()).setValue(a);
                //Toast.makeText(getApplicationContext(), Mensagens.ATUALIZACAO_SUCESSO, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                returnToMeuCadastro();
                //Toast.makeText(AlterarCadastroActivity.this, "Alteração efetuada com sucesso!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AlterarCadastroActivity.this, Mensagens.FALHA_UPLOAD, Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setMessage(Mensagens.CARREGANDO + (int) progress + "%");
            }
        });
    }

    private void returnToMeuCadastro(){
        Intent it = new Intent(AlterarCadastroActivity.this, MeuCadastroActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        returnToMeuCadastro();
    }
}
