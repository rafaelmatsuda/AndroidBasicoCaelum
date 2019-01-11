package br.com.caelum.cadastro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

public class ProvaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provas);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(getResources().getBoolean(R.bool.isTablet))
        {
         transaction.replace(R.id.provas_lista, new ListaProvasFragment());
         transaction.replace(R.id.provas_detalhes, new DetalhesProvaFragment());
        }
        else{
            transaction.replace(R.id.provas_view, new ListaProvasFragment());
        }
        transaction.commit();
    }

    public void mostraDetalhes(){
        DetalhesProvaFragment detalhesProvaFragment = new DetalhesProvaFragment();
        Bundle args = new Bundle();
        //Troca o fragment pelo detalhe da prova
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.provas_view, new DetalhesProvaFragment());
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public boolean isTablet(){
        return getResources().getBoolean(R.bool.isTablet);
    }

    public void selecionaProva(Prova prova){
        FragmentManager manager = getSupportFragmentManager();

        if(isTablet()){
            DetalhesProvaFragment detalhesProva = (DetalhesProvaFragment) manager.findFragmentById(R.id.provas_detalhes);

            detalhesProva.populaCampos(prova);

        }
        else{
            Bundle argumentos = new Bundle();
            argumentos.putSerializable("prova", prova);

            DetalhesProvaFragment detalhesProva = new DetalhesProvaFragment();
            detalhesProva.setArguments(argumentos);

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.provas_view, detalhesProva);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
