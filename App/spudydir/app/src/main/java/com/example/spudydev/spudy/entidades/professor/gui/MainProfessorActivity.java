package com.example.spudydev.spudy.entidades.professor.gui;

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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.gui.LoginActivity;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.entidades.chat.gui.ChatActivity;
import com.example.spudydev.spudy.entidades.turma.gui.CriarTurmaActivity;
import com.example.spudydev.spudy.perfil.gui.MeuPerfilProfessorActivity;
import com.example.spudydev.spudy.perfil.negocio.MenuLateralServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The type Main professor activity.
 */
public class MainProfessorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private  AlertDialog alerta;
    private List<HashMap<String, String>> listaTurmasMinistradas = new ArrayList<>();
    private ListView lvTurmaProfessor;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_professor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.sp_navigation_drawer_open, R.string.sp_navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        //Chamando a classe para setar nome e email do nav_header_menu_professor
        MenuLateralServices.setNomeEmailView(navigationView, FirebaseAuth.getInstance().getCurrentUser());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_meu_perfil:
                        abrirTelaMeuPerfilProfessorActivity();
                        return true;
                    case R.id.nav_turmas:
                        //Activity de turmas
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_sair:
                        //sair
                        sair();
                        return true;
                    default:
                        return false;
                }
            }
        });
        FloatingActionButton btnFlutuanteCriarTurma = findViewById(R.id.btnFlutuanteCriarTurma);
        btnFlutuanteCriarTurma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainProfessorActivity.this, CriarTurmaActivity.class);
                startActivity(intent);
            }
        });

        lvTurmaProfessor = findViewById(R.id.lvTurmasProfessor);
        carregarTurmasMinistradas();

        lvTurmaProfessor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String codigoTurma = ((TextView) view.findViewById(R.id.txtCodigoTurma)).getText().toString();
                String nomeTurma = ((TextView) view.findViewById(R.id.txtNomeTurma)).getText().toString();
                abrirTelaChat(codigoTurma, nomeTurma);
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
        getMenuInflater().inflate(R.menu.main_professor, menu);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void sair() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle(getString(R.string.sp_dialogo_titulo));
        //define a mensagem
        builder.setMessage(getString(R.string.sp_dialogo_mensagem_sair_conta));
        //define um botão como positivo
        builder.setPositiveButton(getString(R.string.sp_dialogo_sim), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1) {
                FirebaseAuth.getInstance().signOut();
                abrirTelaLoginActivity();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton(getString(R.string.sp_dialogo_nao), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alerta.dismiss();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
    //Método que carrega as turmas que o professor ministra
    private void carregarTurmasMinistradas(){

        //Lista para salvar as turmas que o professor é responsável
        AcessoFirebase.getFirebase().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getTurmasMinistradas(dataSnapshot);
                setListViewTurmas();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Método que adiciona ao RetornoLista listaTurmasMinistradas as informações das turmas ministradas pelo professor
    private void getTurmasMinistradas(DataSnapshot dataSnapshot) {

        listaTurmasMinistradas.clear();

        Iterable<DataSnapshot> turmasMinistradas = dataSnapshot.child("professor").child(AcessoFirebase.getUidUsuario()).child("turmasMinistradas").getChildren();

        for (DataSnapshot dataSnapshotChild: turmasMinistradas){
            if (!dataSnapshotChild.getKey().equals("SENTINELA")){
                montandoDicionarioTurmasProfessor(dataSnapshot, dataSnapshotChild);
            }
        }
    }
    private void montandoDicionarioTurmasProfessor(DataSnapshot dataSnapshot, DataSnapshot dataSnapshotChild) {
        HashMap<String, String> dicionarioTurmasProfessor = new HashMap<>();

        String nomeTurma = dataSnapshot.child("turma").child(dataSnapshotChild.getKey()).child("nomeTurma").getValue(String.class).toUpperCase();
        String codigoTurma = dataSnapshotChild.getKey();

        dicionarioTurmasProfessor.put("nomeTurma",nomeTurma);
        dicionarioTurmasProfessor.put("codigoTurma", codigoTurma);
        //Aqui ele esta com todas as turmas do professor
        listaTurmasMinistradas.add(dicionarioTurmasProfessor);
    }

    //Setando o ListView com o auxílio de um ArrayAdapter
    private void setListViewTurmas(){
        SimpleAdapter adapter = new SimpleAdapter(this, listaTurmasMinistradas,R.layout.modelo_list_view_turmas
                , new String[]{"nomeTurma","codigoTurma"}, new int[]{R.id.txtNomeTurma, R.id.txtCodigoTurma});
        lvTurmaProfessor.setAdapter(adapter);
    }

    //Intent perfil professor
    private void abrirTelaMeuPerfilProfessorActivity(){
        Intent intentAbrirTelaMeuPerfilProfessorAcitivty = new Intent(MainProfessorActivity.this, MeuPerfilProfessorActivity.class);
        startActivity(intentAbrirTelaMeuPerfilProfessorAcitivty);
    }
    //Intent singOut
    private void abrirTelaLoginActivity(){
        Intent intentAbrirTelaLoginAcitivty = new Intent(MainProfessorActivity.this, LoginActivity.class);
        startActivity(intentAbrirTelaLoginAcitivty);
        finish();
    }
    private void abrirTelaChat(String codigoTurma, String nomeTurma){
        Intent intent = new Intent(MainProfessorActivity.this, ChatActivity.class);
        intent.putExtra("codigoTurma", codigoTurma);
        intent.putExtra("nomeTurma", nomeTurma);
        startActivity(intent);
    }
}

