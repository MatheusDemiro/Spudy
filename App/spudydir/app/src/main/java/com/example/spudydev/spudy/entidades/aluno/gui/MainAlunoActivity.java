package com.example.spudydev.spudy.entidades.aluno.gui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.gui.LoginActivity;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.entidades.turma.gui.AdicionarTurmaActivity;
import com.example.spudydev.spudy.entidades.turma.gui.ChatActivity;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilAlunoActivity;
import com.example.spudydev.spudy.perfil.negocio.MenuLateral;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainAlunoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MenuLateral dadosMenuDAO = new MenuLateral();
    //ListView
    private ArrayList<String> listaTurmaAluno = new ArrayList<>();
    private ListView lvTurmaAluno;
    //ListView
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_aluno);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.sp_navigation_drawer_open, R.string.sp_navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        //Chamando a classe para setar nome e email do nav_header_main_aluno
        dadosMenuDAO.setNomeEmailView(navigationView, AcessoFirebase.getFirebaseAutenticacao().getCurrentUser());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_meu_perfil:
                        abrirTelaMeuPerfilAlunoActivity();
                        return true;
                    case R.id.nav_turmas:
                        //Activity de turmas
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_sair:
                        sair();
                        //sair
                        return true;
                    default:
                        return false;
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.btnFlutuanteAdicionarTurma);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainAlunoActivity.this, AdicionarTurmaActivity.class);
                startActivity(intent);
            }
        });

        lvTurmaAluno = findViewById(R.id.lvTurmasAluno);
        //Carregar as turmas
        carregaTurmaAluno();

        lvTurmaAluno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String array[] = ((TextView) view).getText().toString().split("\n");
                abrirTelaChat(array[1].substring(17));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_aluno, menu);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_turmas) {
            // Handle the camera action
        } else if (id == R.id.nav_meu_perfil) {

        } else if (id == R.id.nav_sair) {
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void sair() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Sair");
        //define a mensagem
        builder.setMessage("Tem certeza que deseja sair?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1) {
                FirebaseAuth.getInstance().signOut();
                abrirTelaLoginActivity();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alerta.dismiss();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
    public void abrirTelaLoginActivity(){
        Intent intentAbrirTelaLoginAcitivty = new Intent(MainAlunoActivity.this, LoginActivity.class);
        startActivity(intentAbrirTelaLoginAcitivty);
        finish();
    }
    public void abrirTelaMeuPerfilAlunoActivity(){
        Intent intentAbrirTelaMeuPerfilAlunoAcitivty = new Intent(MainAlunoActivity.this, MeuPerfilAlunoActivity.class);
        startActivity(intentAbrirTelaMeuPerfilAlunoAcitivty);
    }
    private void carregaTurmaAluno(){
        //Lista para salvar as turmas que o professor é responsável
        AcessoFirebase.getFirebase().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getTurmasAluno(dataSnapshot);
                setListViewTurmas();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Método retorna as turmas do aluno
    private void getTurmasAluno(DataSnapshot dataSnapshot) {
        Iterable<DataSnapshot> turmas = dataSnapshot.child("aluno").child(AcessoFirebase.getUidUsuario()).child("turmas").getChildren();
        //For percorrendo e adicionando os UID's dos usuarios a listaTurmaAluno.
        for (DataSnapshot dataSnapshotChild: turmas){
            if (!dataSnapshotChild.getKey().equals("SENTINELA")){
                //Aqui ele esta com todas as turmas do professor, pegar o cardview e seta o texto
                listaTurmaAluno.add(dataSnapshot.child("turma").child(dataSnapshotChild.getKey()).child("nomeTurma").getValue(String.class).toUpperCase()+"\nCódigo da turma: "+dataSnapshotChild.getKey());
            }
        }
    }
    //Método que seta o ListView com o auxílio de ArrayAdapter
    public void setListViewTurmas(){
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, R.layout.modelo_list_view, listaTurmaAluno);
        lvTurmaAluno.setAdapter(itemsAdapter);
    }

    public void abrirTelaChat(String turma){
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("codigoTurma", turma);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
