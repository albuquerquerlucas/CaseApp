package com.luke.caseappmatricula.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.luke.caseappmatricula.R;
import com.luke.caseappmatricula.Util.ConfigFirebase;
import com.luke.caseappmatricula.Util.Mensagens;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgBtnFacebook, imgBtnGoogle;
    private EditText edtEmailL, edtSenhaL;
    private Button btnEntrarL;
    private TextView txtLinkCadastro;
    private FirebaseAuth auth;
    private static final int RC_SGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = ConfigFirebase.getFirebaseNAuth();
    }

    @Override
    protected void onResume() {
        super.onResume();

        imgBtnFacebook = findViewById(R.id.imgBtnFacebook);
        imgBtnGoogle = findViewById(R.id.imgBtnGoogle);
        edtEmailL = findViewById(R.id.edtEmailL);
        edtSenhaL = findViewById(R.id.edtSenhaL);
        btnEntrarL = findViewById(R.id.btnEntrarL);
        txtLinkCadastro = findViewById(R.id.txtLinkCadastro);

        imgBtnFacebook.setOnClickListener(this);
        imgBtnGoogle.setOnClickListener(this);
        btnEntrarL.setOnClickListener(this);
        txtLinkCadastro.setOnClickListener(this);

    }

    private void validarCampos(){

        String email = edtEmailL.getText().toString();
        String senha = edtSenhaL.getText().toString();

        if(!email.equals("") && !senha.equals("")){
            efetuarLogin(email, senha);
        }else{
            Toast.makeText(getApplicationContext(), Mensagens.CAMPOS_VAZIOS, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.imgBtnFacebook:
                Toast.makeText(getApplicationContext(), Mensagens.SERVICO_TEMPORARIAMENTE_INDISPONIVEL, Toast.LENGTH_SHORT).show();
                break;

            case R.id.imgBtnGoogle:
                validarLoginComGoogle();
                googleSignIn();
                //Toast.makeText(getApplicationContext(), Mensagens.SERVICO_TEMPORARIAMENTE_INDISPONIVEL, Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnEntrarL:
                validarCampos();
                break;

            case R.id.txtLinkCadastro:
                goToCadastro();
                break;

        }
    }

    private void efetuarLogin(String email, String senha){

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle(Mensagens.VALIDANDO_DADOS);
        progressDialog.show();

        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    goToMenu();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, Mensagens.USUARIO_OU_SENHA_INVALIDOS, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void validarLoginComGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void googleSignIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                efetuarLoginComGoogle(account);
            }else{
                Toast.makeText(LoginActivity.this, Mensagens.ERRO_TENTE_NOVAMENTE, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void efetuarLoginComGoogle(GoogleSignInAccount account) {

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle(Mensagens.VALIDANDO_DADOS);
        progressDialog.show();

        AuthCredential credencial = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credencial)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        progressDialog.dismiss();
                        Intent it = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(it);
                        finish();
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, Mensagens.ERRO_TENTE_NOVAMENTE, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void goToMenu(){
        Intent it = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(it);
        finish();
    }

    private void goToCadastro(){
        Intent it = new Intent(getApplicationContext(), CadastroActivity.class);
        startActivity(it);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
