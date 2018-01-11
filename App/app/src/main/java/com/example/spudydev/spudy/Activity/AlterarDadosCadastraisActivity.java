package com.example.spudydev.spudy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.spudydev.spudy.DAO.AcessoFirebase;
import com.example.spudydev.spudy.R;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class AlterarDadosCadastraisActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText nomeAlterar;
    private EditText dataNascimentoAlterar;
    private EditText instituicaoAlterar;
    private Button btn_confirmaAlteracao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_dados_cadastrais);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.sp_navigation_drawer_open, R.string.sp_navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Instanciando os EditTexts
        nomeAlterar = (EditText) findViewById(R.id.edt_conta_nomeAlterar);
        dataNascimentoAlterar = (EditText) findViewById(R.id.edt_conta_dataNascimentoAlterar);
        instituicaoAlterar = (EditText) findViewById(R.id.edt_conta_instituicaoAlterar);
        btn_confirmaAlteracao = (Button) findViewById(R.id.btn_content_confirmar_alteracao);
        //fim instâncias

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();

        final String email = dados.getString("email");


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        btn_confirmaAlteracao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference referencia = AcessoFirebase.getFirebase().child("usuario").child(MD5.HASH(email));
                Map<String, Object> atualizarDados = new HashMap<String, Object>();

                atualizarDados.put("nome", nomeAlterar.getText().toString());
                atualizarDados.put("dataNasc", dataNascimentoAlterar.getText().toString());
                atualizarDados.put("instituicao", instituicaoAlterar.getText().toString());

                referencia.updateChildren(atualizarDados);

                abrirTelaMeuPerfilActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.alterar_dados_cadastrais, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_turmas) {
            // Handle the camera action
        } else if (id == R.id.nav_meu_perfil) {

        } else if (id == R.id.nav_sair) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void abrirTelaMeuPerfilActivity(){
        Intent intentAbrirTelaMeuPerfilActivity = new Intent(AlterarDadosCadastraisActivity.this, MeuPerfilAlunoActivity.class);
        startActivity(intentAbrirTelaMeuPerfilActivity);
        finish();
    }

}
