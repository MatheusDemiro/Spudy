package com.example.spudydev.spudy.infraestrutura.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Auxiliar {
    /*
    REFAZER MÉTODO PARA TROCA DE TELAS.
    public static void transicaoTela(Context telaAtual, Class classe){
        Intent intent = new Intent(telaAtual,classe.getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        telaAtual.startActivity(intent);
    }
    */
    public static boolean verificaExpressaoRegularEmail(String email) {

        if (!email.isEmpty()) {
            String excecoes = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
            Pattern pattern = Pattern.compile(excecoes);
            Matcher matcher = pattern.matcher(email);

            return matcher.matches();//se igual a true tem alguma expressão irregular.
        }
        return false;
    }

    public static void criarToast(Context context, String texto){
        Toast.makeText(context, texto, Toast.LENGTH_SHORT).show();
    }

    public static void criarMascara(EditText editText){
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(editText, smf);

        editText.addTextChangedListener(mtw);
    }
}

