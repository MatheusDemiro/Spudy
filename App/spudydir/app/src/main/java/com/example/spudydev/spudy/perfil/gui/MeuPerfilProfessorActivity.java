package com.example.spudydev.spudy.perfil.gui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.infraestrutura.gui.LoginActivity;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.perfil.negocio.MenuLateral;
import com.example.spudydev.spudy.entidades.endereco.dominio.Endereco;
import com.example.spudydev.spudy.entidades.pessoa.dominio.Pessoa;
import com.example.spudydev.spudy.entidades.pessoa.gui.AlterarDataNascimentoActivity;
import com.example.spudydev.spudy.entidades.usuario.gui.AlterarEmailActivity;
import com.example.spudydev.spudy.entidades.pessoa.gui.AlterarEnderecoActivity;
import com.example.spudydev.spudy.entidades.usuario.gui.AlterarInstituicaoActivity;
import com.example.spudydev.spudy.entidades.pessoa.gui.AlterarNomeActivity;
import com.example.spudydev.spudy.entidades.usuario.gui.AlterarSenhaActivity;
import com.example.spudydev.spudy.entidades.usuario.dominio.Usuario;
import com.example.spudydev.spudy.entidades.professor.gui.MainProfessorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class MeuPerfilProfessorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser user = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser();
    private MenuLateral menuLateral = new MenuLateral();
    private AlertDialog alertaSair;

    //TEXT VIEWS (PRIVATES PARA SEREM CHAMADOS NO ACESSO AO BANCO)

    private TextView tvNomePerfilProfessor;
    private TextView tvDataNascimentoPerfilProfessor;
    private TextView tvEnderecoPerfilProfessor;
    private TextView tvInstituicaoPerfilProfessor;
    private TextView tvEmailPerfilProfessor;
    private TextView tvSenhaPerfilProfessor;

    //variáveis utilizadas pelo método de escolher foto
    private ImageView imgPerfilProfessor;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private Bitmap minhaFoto;
    private StorageReference referenciaStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_perfil_professor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        //instanciando os TextViews/Button
        tvNomePerfilProfessor = findViewById(R.id.tvContaProfessorNomeAlterar);
        tvDataNascimentoPerfilProfessor = findViewById(R.id.tvDataNascimentoPerfilProfessor);
        tvEnderecoPerfilProfessor = findViewById(R.id.tvEnderecoPerfilProfessor);
        tvInstituicaoPerfilProfessor = findViewById(R.id.tvInstituicaoPerfilProfessor);
        tvEmailPerfilProfessor = findViewById(R.id.tvEmailPerfilProfessor);
        tvSenhaPerfilProfessor =  findViewById(R.id.tvSenhaPerfilProfessor);

        Button btnAlterarFotoPerfilProfessor = findViewById(R.id.btnAlterarImagemProfessor);
        Button btnAlterarNomePerfilProfessor = findViewById(R.id.btnAlterarNomePerfilProfessor);
        Button btnAlterarDataNascimentoPerfilProfessor = findViewById(R.id.btnAlterarDataNascimentoPerfilProfessor);
        Button btnAlterarEnderecoPerfilProfessor = findViewById(R.id.btnAlterarEnderecoPerfilProfessor);
        Button btnAlterarInstituicaoPerfilProfessor = findViewById(R.id.btnAlterarInstituicaoPerfilProfessor);
        Button btnAlterarEmailPerfilProfessor = findViewById(R.id.btnAlterarEmailPerfilProfessor);
        Button btnAlterarSenhaPerfilProfessor = findViewById(R.id.btnAlterarSenhaPerfilProfessor);

        //fim instancias

        //Referência storage
        referenciaStorage = FirebaseStorage.getInstance().getReference();

        //setando os txt's do perfil professor
        downloadFoto();
        informacoesPerfilProfessor();

        //Drawer Menu Lateral
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.sp_navigation_drawer_open, R.string.sp_navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setando email e nome do header
        menuLateral.setNomeEmailView(navigationView, user);

        //Alterar foto
        btnAlterarFotoPerfilProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Galeria!
                escolherFoto();
            }
        });
        //Alterar Nome
        btnAlterarNomePerfilProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAlteraNomeActivity();
            }
        });

        //Alterar datanascimento
        btnAlterarDataNascimentoPerfilProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAlteraDataNascimentoActivity();
            }
        });

        //Alterar Endereco
        btnAlterarEnderecoPerfilProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAlterarEnderecoActivity();
            }
        });

        //Alterar Instituição
        btnAlterarInstituicaoPerfilProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaAlteraInstituicaoActivity();
            }
        });

        //Alterar Email
        btnAlterarEmailPerfilProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAlteraEmailActivity();
            }
        });

        //Alterar Senha
        btnAlterarSenhaPerfilProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaAlterarSenhaActivity();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_meu_perfil:
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_turmas:
                        abrirTelaMainProfessorActivity();
                        return true;
                    case R.id.nav_disciplinas:
                        Toast.makeText(MeuPerfilProfessorActivity.this, R.string.sp_em_construcao, Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav_sair:
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
        getMenuInflater().inflate(R.menu.meu_perfil_professor, menu);
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

        if (id == R.id.nav_meu_perfil) {
            // Handle the camera action
        } else if (id == R.id.nav_turmas) {

        } else if (id == R.id.nav_disciplinas) {

        } else if (id == R.id.nav_sair) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //método abre a galeria para o usuário selecionar um foto
    private void escolherFoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Selecione uma imagem"),  PICK_IMAGE_REQUEST);
    }
    //este método captura o resultado do método anterior
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imgPerfilProfessor.setImageBitmap(bitmap);
                uploadFoto();

            }catch(IOException e ){
                e.printStackTrace();
            }
        }
    }
    //método que realiza o upload da foto
    public void uploadFoto(){
        if (filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = referenciaStorage.child("images/" + user.getUid()+".bmp");
            ref.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    progressDialog.dismiss();
                    Auxiliar.criarToast(MeuPerfilProfessorActivity.this, "Sucesso!");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Auxiliar.criarToast(MeuPerfilProfessorActivity.this, "Falhou!"+e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Upload " + (int)progress+"%");
                }
            });

        }
    }

    public void downloadFoto(){
        StorageReference ref = referenciaStorage.child("images/" + user.getUid() + ".bmp");

        try {
            final File localFile = File.createTempFile("images", "bmp");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener< FileDownloadTask.TaskSnapshot >() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    minhaFoto = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imgPerfilProfessor.setImageBitmap(minhaFoto);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void informacoesPerfilProfessor() {

        AcessoFirebase.getFirebase().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Pessoa pessoa = dataSnapshot.child("pessoa").child(user.getUid()).getValue(Pessoa.class);
                Usuario usuario = dataSnapshot.child("usuario").child(user.getUid()).getValue(Usuario.class);
                Endereco endereco = dataSnapshot.child("endereco").child(user.getUid()).getValue(Endereco.class);

                if (usuario != null && pessoa != null) {
                    tvNomePerfilProfessor.setText(pessoa.getNome());
                    tvDataNascimentoPerfilProfessor.setText(pessoa.getDataNascimento());

                    //montando string endereço
                    String enderecoPerfilAluno = endereco.getEstado() + " - " + endereco.getCidade() + " - " + endereco.getEndereco() + " - " + endereco.getComplemento();

                    tvEnderecoPerfilProfessor.setText(enderecoPerfilAluno);
                    tvEmailPerfilProfessor.setText(user.getEmail());
                    tvInstituicaoPerfilProfessor.setText(usuario.getInstituicao());
                    tvSenhaPerfilProfessor.setText("******");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sair() {
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
                alertaSair.dismiss();
            }
        });
        //cria o AlertDialog
        alertaSair = builder.create();
        //Exibe
        alertaSair.show();
    }

    //Intent Alterar Nome
    public void abrirTelaAlteraNomeActivity(){
        Intent intentAbrirTelaAlteraNomeActivity = new Intent(MeuPerfilProfessorActivity.this, AlterarNomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tipoConta", "professor");
        intentAbrirTelaAlteraNomeActivity.putExtras(bundle);
        startActivity(intentAbrirTelaAlteraNomeActivity);
    }

    //Intent Alterar Data
    public void abrirTelaAlteraDataNascimentoActivity(){
        Intent intentAbrirTelaAlteraDataNascimentoActivity = new Intent(MeuPerfilProfessorActivity.this, AlterarDataNascimentoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tipoConta", "professor");
        intentAbrirTelaAlteraDataNascimentoActivity.putExtras(bundle);
        startActivity(intentAbrirTelaAlteraDataNascimentoActivity);
    }

    //Intent Alterar Endereço
    public void abrirTelaAlterarEnderecoActivity(){
        Intent intentAbrirTelaAlteraEnderecoActivity= new Intent(MeuPerfilProfessorActivity.this, AlterarEnderecoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tipoConta", "professor");
        intentAbrirTelaAlteraEnderecoActivity.putExtras(bundle);
        startActivity(intentAbrirTelaAlteraEnderecoActivity);
    }

    //Intent Alterar Instituicao
    public void abrirTelaAlteraInstituicaoActivity(){
        Intent intentAbrirTelaAlteraInstituicaoActivity = new Intent(MeuPerfilProfessorActivity.this, AlterarInstituicaoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tipoConta", "professor");
        intentAbrirTelaAlteraInstituicaoActivity.putExtras(bundle);
        startActivity(intentAbrirTelaAlteraInstituicaoActivity);
    }
    //Intent Alterar Email
    public void abrirTelaAlteraEmailActivity(){
        Intent intent = new Intent(MeuPerfilProfessorActivity.this, AlterarEmailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tipoConta", "professor");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    //Intent Alterar Senha
    public void abrirTelaAlterarSenhaActivity(){
        Intent intentAbrirTelaAlteraSenhaActivity = new Intent(MeuPerfilProfessorActivity.this, AlterarSenhaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tipoConta", "professor");
        intentAbrirTelaAlteraSenhaActivity.putExtras(bundle);
        startActivity(intentAbrirTelaAlteraSenhaActivity);
    }

    //Intent Alterar MainProfessor
    public void abrirTelaMainProfessorActivity(){
        Intent intent= new Intent(MeuPerfilProfessorActivity.this, MainProfessorActivity.class);
        startActivity(intent);
    }
    //Intent SingOut
    public void abrirTelaLoginActivity(){
        Intent intentAbrirTelaLoginAcitivty = new Intent(MeuPerfilProfessorActivity.this, LoginActivity.class);
        startActivity(intentAbrirTelaLoginAcitivty);
        finish();
    }
}
