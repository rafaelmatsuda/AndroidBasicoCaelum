package br.com.caelum.security;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;

public class Permissao {

    private static final int CODE = 123;
    private static ArrayList<String> listaPermissoes = new ArrayList<>();

    public static void fazPermissao(Activity activity) {
        String[] permissoes = {Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {

            for (String permissao : permissoes) {

                if (activity.checkSelfPermission(permissao) != PackageManager.PERMISSION_GRANTED) {
                    listaPermissoes.add(permissao);
                }
            }
            request(activity);
        }

    }

    private static void request(Activity activity)
    {
        String[] array = listaPermissoes.toArray(new String[]{});

        if(listaPermissoes.size() > 0)
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                activity.requestPermissions(array, CODE);
            }
        }
    }
}
