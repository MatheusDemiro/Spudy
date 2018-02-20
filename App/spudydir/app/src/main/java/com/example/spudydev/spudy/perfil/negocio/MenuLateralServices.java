package com.example.spudydev.spudy.perfil.negocio;

import android.support.design.widget.NavigationView;

import com.example.spudydev.spudy.perfil.persistencia.MenuLateralDAO;
import com.google.firebase.auth.FirebaseUser;


/**
 * The type Menu lateral services.
 */
public class MenuLateralServices {
    /**
     * Set nome email view.
     *
     * @param navigationView the navigation view
     * @param user           the user
     */
    public static void setNomeEmailView(final NavigationView navigationView, final FirebaseUser user){
        MenuLateralDAO.setNomeEmailView(navigationView,user);
    }
}
