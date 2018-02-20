package com.example.spudydev.spudy.infraestrutura.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Auxiliar.
 */
public final class Auxiliar {

    private Auxiliar(){
        //Construtor vazio
    }

    /**
     * Verifica expressao regular email boolean.
     *
     * @param email the email
     * @return the boolean
     */
//Aplicando pattern
    public static boolean verificaExpressaoRegularEmail(String email) {

        if (!email.isEmpty()) {
            String excecoes = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
            Pattern pattern = Pattern.compile(excecoes);
            Matcher matcher = pattern.matcher(email);

            return matcher.matches();//se igual a true tem alguma expressão irregular.
        }
        return false;
    }

    /**
     * Criar toast.
     *
     * @param context the context
     * @param texto   the texto
     */
//Criando Toast
    public static void criarToast(Context context, String texto){
        Toast.makeText(context, texto, Toast.LENGTH_SHORT).show();
    }

    /**
     * Criar mascara.
     *
     * @param editText the edit text
     */
//Criando máscara para os campos data
    public static void criarMascara(EditText editText){
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(editText, smf);

        editText.addTextChangedListener(mtw);
    }
}

