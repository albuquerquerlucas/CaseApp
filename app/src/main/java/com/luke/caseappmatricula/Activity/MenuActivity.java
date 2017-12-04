package com.luke.caseappmatricula.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.luke.caseappmatricula.R;
import com.luke.caseappmatricula.Util.ConfigFirebase;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView txtEmailUsuarioLogado;
    private ImageView imgGoToMeuCadastro, imgGoToMatricula, imgGoToCursos, imgGoToSobre;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        auth = ConfigFirebase.getFirebaseNAuth();
        user = ConfigFirebase.getFirebaseCurrentUser();
    }

    @Override
    protected void onResume() {
        super.onResume();

        txtEmailUsuarioLogado = findViewById(R.id.txtEmailUsuarioLogado);
        txtEmailUsuarioLogado.setText(user.getEmail());

        imgGoToMeuCadastro = findViewById(R.id.imgGoToMeuCadastro);
        imgGoToMatricula = findViewById(R.id.imgGoToMatricula);
        imgGoToCursos = findViewById(R.id.imgGoToCursos);
        imgGoToSobre = findViewById(R.id.imgGoToSobre);

        imgGoToMeuCadastro.setOnClickListener(this);
        imgGoToMatricula.setOnClickListener(this);
        imgGoToCursos.setOnClickListener(this);
        imgGoToSobre.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgGoToMeuCadastro:
                selecaoMenu(1);
                break;
            case R.id.imgGoToMatricula:
                selecaoMenu(2);
                break;
            case R.id.imgGoToCursos:
                selecaoMenu(3);
                break;
            case R.id.imgGoToSobre:
                selecaoMenu(4);
                break;
        }
    }

    private void selecaoMenu(int opcao){
        if(opcao == 1){
            Intent goMeuCadastro = new Intent(MenuActivity.this, MeuCadastroActivity.class);
            startActivity(goMeuCadastro);
            finish();
        }else if(opcao == 2){
            Intent goMatricula = new Intent(MenuActivity.this, MeusCursosActivity.class);
            startActivity(goMatricula);
            finish();
        }else if(opcao == 3){
            Intent goCursos = new Intent(getApplicationContext(), CursosActivity.class);
            startActivity(goCursos);
            finish();
        }else{
            Intent goSobre = new Intent(getApplicationContext(), SobreActivity.class);
            startActivity(goSobre);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.itemSair) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        auth.signOut();
        Intent it = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logout();
    }
}
