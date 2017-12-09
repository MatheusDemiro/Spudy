package com.example.lucasmarsol.spudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity {
    private String[] tipoConta = new String[]{"Selecione o tipo de conta", "Aluno", "Professor"};
    private String[] perguntaSegurança = new String[]{"Selecione a pergunta secreta", "Nome do seu primeiro animal de estimação", "Nome do melhor amigo de infância", "Filme predileto"};
    private EditText nomeReg;
    private EditText emailReg;
    private EditText dataNascimentoReg;
    private EditText instituicaoReg;
    private Spinner tipodecontaReg;
    private Spinner perguntaSegur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nomeReg = (EditText) findViewById(R.id.edt_nomeReg);
        emailReg = (EditText) findViewById(R.id.edt_emailReg);
        dataNascimentoReg = (EditText) findViewById(R.id.edt_dataReg);
        instituicaoReg = (EditText) findViewById(R.id.edt_instituicaoReg);
        tipodecontaReg = (Spinner) findViewById(R.id.spn_tipodeconta);
        perguntaSegur = (Spinner) findViewById(R.id.spn_perguntaSeguranca);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tipoConta);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sp = (Spinner) findViewById(R.id.spn_tipodeconta);
        sp.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, perguntaSegurança);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sp2 = (Spinner) findViewById(R.id.spn_perguntaSeguranca);
        sp2.setAdapter(adapter2);

        //Variáveis para formar os objetos
        //String nome = nomeReg.getText().toString();
        //String email = emailReg.getText().toString();
        //String data = dataNascimentoReg.getText().toString();
        //String instituicao = instituicaoReg.getText().toString();
    }
    //Caso o campo Selecione o tipo de conta esteja vazio (Apenas testando)
    public void confirmRegistro(View view) {
        //resgantando os valores dos spinner's (Tipo de Conta e Pergunta de Segurança)
        String tipo = tipodecontaReg.getSelectedItem().toString();
        String pergunta = perguntaSegur.getSelectedItem().toString();
        //realizando comparações para validação
        //usando Toast apenas para teste
        if (tipo ==  "Selecione o tipo de conta" || pergunta == "Selecione a pergunta secreta"){
           Toast.makeText(RegistroActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
        }else {
            if (tipo == "Aluno"){
                //criar objeto aluno
                Toast.makeText(RegistroActivity.this, tipo, Toast.LENGTH_SHORT).show();
            }
            else{
                //criar objeto professor
                Toast.makeText(RegistroActivity.this, tipo, Toast.LENGTH_SHORT).show();
            }
        }
      }
    }





