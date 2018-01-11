package com.example.spudydev.spudy.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spudydev.spudy.Activity.Alteracao.AlterarDataNascimentoActivity;
import com.example.spudydev.spudy.Activity.Alteracao.AlterarEmailActivity;
import com.example.spudydev.spudy.Activity.Alteracao.AlterarInstituicaoActivity;
import com.example.spudydev.spudy.Activity.Alteracao.AlterarNomeActivity;
import com.example.spudydev.spudy.Activity.Alteracao.AlterarSenhaActivity;
import com.example.spudydev.spudy.DAO.AcessoFirebase;
import com.example.spudydev.spudy.DAO.DadosMenuDAO;
import com.example.spudydev.spudy.Entidades.Pessoa;
import com.example.spudydev.spudy.Entidades.Usuario;
import com.example.spudydev.spudy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MeuPerfilAlunoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nomeConsulta;
    private TextView emailConsulta;
    private TextView dataNascimentoConsulta;
    private TextView instituicaoConsulta;
    private Button btn_informacao;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DadosMenuDAO dadosMenuDAO = new DadosMenuDAO();
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_perfil_aluno);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //instanciando os TextViews/Button
        nomeConsulta = (TextView) findViewById(R.id.edt_conta_aluno_nomeAlterar);
        emailConsulta = (TextView) findViewById(R.id.edt_conta_aluno_emailAlterar);
        dataNascimentoConsulta = (TextView) findViewById(R.id.edt_conta_aluno_dataNascimentoAlterar);
        instituicaoConsulta = (TextView) findViewById(R.id.edt_conta_aluno_instituicaoAlterar);
        btn_informacao = (Button) findViewById(R.id.btn_informacao_perfil_aluno);
        //fim instancias

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.sp_navigation_drawer_open, R.string.sp_navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setando email e nome do header
        dadosMenuDAO.resgatarUsuario(navigationView, user);

        //setando os TXT'S do meu perfil
        setTxtPerfil();

        //Informações sobre como trocar dados perfil
        btn_informacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MeuPerfilAlunoActivity.this, R.string.sp_click_informacao, Toast.LENGTH_SHORT).show();
            }
        });

        //Alterar Nome
        nomeConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAlteraNomeActivity();
            }
        });

        //Alterar Email
        emailConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MeuPerfilAlunoActivity.this, "Em construção...", Toast.LENGTH_SHORT).show();
            }
        });

        //Alterar datanascimento
        dataNascimentoConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAlteraDataNascimentoActivity();
            }
        });

        //Alterar Instituição
        instituicaoConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaAlteraInstituicaoActivity();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_meu_perfil:
                        abrirTelaAtualActivity();
                        return true;
                    case R.id.nav_turmas:
                        //Activity de turmas
                        Toast.makeText(MeuPerfilAlunoActivity.this, "Em construção", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.meu_perfil_aluno, menu);
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

    public void setTxtPerfil() {
        DatabaseReference referencia = AcessoFirebase.getFirebase();

        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Pessoa pessoa = dataSnapshot.child("pessoa").child(user.getUid()).getValue(Pessoa.class);
                Usuario usuario = dataSnapshot.child("usuario").child(user.getUid()).getValue(Usuario.class);
                nomeConsulta.setText(pessoa.getNome());
                emailConsulta.setText(usuario.getEmail());
                dataNascimentoConsulta.setText(pessoa.getData_nascimento());
                instituicaoConsulta.setText(usuario.getInstituicao());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Intent Alterar Nome
    public void abrirTelaAlteraNomeActivity(){
        Intent intentAbrirTelaAlteraNomeActivity = new Intent(MeuPerfilAlunoActivity.this, AlterarNomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email", emailConsulta.getText().toString());
        intentAbrirTelaAlteraNomeActivity.putExtras(bundle);
        startActivity(intentAbrirTelaAlteraNomeActivity);
    }

    //Intent Alterar Data
    public void abrirTelaAlteraDataNascimentoActivity(){
        Intent intentAbrirTelaAlteraDataNascimentoActivity = new Intent(MeuPerfilAlunoActivity.this, AlterarDataNascimentoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email", emailConsulta.getText().toString());
        intentAbrirTelaAlteraDataNascimentoActivity.putExtras(bundle);
        startActivity(intentAbrirTelaAlteraDataNascimentoActivity);
    }
    //Intent Alterar Instituicao
    public void abrirTelaAlteraInstituicaoActivity(){
        Intent intentAbrirTelaAlteraInstituicaoActivity = new Intent(MeuPerfilAlunoActivity.this, AlterarInstituicaoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email", emailConsulta.getText().toString());
        intentAbrirTelaAlteraInstituicaoActivity.putExtras(bundle);
        startActivity(intentAbrirTelaAlteraInstituicaoActivity);
    }
    //Intent Alterar Senha
    public void abrirTelaAlterarSenhaActivity(){
        Intent intentAbrirTelaAlteraSenhaActivity = new Intent(MeuPerfilAlunoActivity.this, AlterarSenhaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email", emailConsulta.getText().toString());
        intentAbrirTelaAlteraSenhaActivity.putExtras(bundle);
        startActivity(intentAbrirTelaAlteraSenhaActivity);
    }

    //Intent Alterar Email
    public void abrirTelaAlterarEmailActivity(){
        Intent intentAbrirTelaAlterarEmailActicity = new Intent(MeuPerfilAlunoActivity.this, AlterarEmailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email", emailConsulta.getText().toString());
        intentAbrirTelaAlterarEmailActicity.putExtras(bundle);
        startActivity(intentAbrirTelaAlterarEmailActicity);
    }

    //Intent Tela Atual
    public void abrirTelaAtualActivity(){
        Intent intentAbrirTelaAtualAcitivty = new Intent(MeuPerfilAlunoActivity.this, MeuPerfilAlunoActivity.class);
        startActivity(intentAbrirTelaAtualAcitivty);
        finish();
    }

    //Intent Tela Login
    public void abrirTelaLoginActivity(){
        Intent intentAbrirTelaLoginAcitivty = new Intent(MeuPerfilAlunoActivity.this, LoginActivity.class);
        startActivity(intentAbrirTelaLoginAcitivty);
        finish();
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
}
