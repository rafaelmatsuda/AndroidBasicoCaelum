package br.com.caelum.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import br.com.caelum.cadastro.ListaAlunosActivity;
import br.com.caelum.dao.AlunoDAO;

public class ContextActionBar implements ActionMode.Callback
{
    private ListaAlunosActivity activity;
    private Aluno alunoSelecionado;
    public ContextActionBar (ListaAlunosActivity activity, Aluno alunoSelecionado)
    {
        this.activity = activity;
        this.alunoSelecionado = alunoSelecionado;
    }

    @Override
    public boolean onCreateActionMode(final ActionMode mode, Menu menu) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menu;

//        final Aluno alunoSelecionado = (Aluno) listaAlunos.getAdapter().getItem(info.position);

        menu.add("Ligar");
        menu.add("Enviar SMS");
        menu.add("Achar no Mapa");
        menu.add("Navegar no Site");

        MenuItem deletar = menu.add("Deletar");

        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deletar")
                        .setMessage("Deseja Mesmo Deletar?")
                        .setPositiveButton("Quero",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AlunoDAO dao = new AlunoDAO(activity);
                                        dao.deleta(alunoSelecionado);
                                        dao.close();
                                        activity.carregaLista();
                                        Toast.makeText(activity, "Aluno "+ alunoSelecionado.getNome()+" deletado", Toast.LENGTH_LONG).show();
                                    }
                                }).setNegativeButton("Não", null).show();
                return false;
            }
        });


//        MenuItem ligar = menu.add("Ligar");
//        MenuItem sms = menu.add("Enviar SMS");
//        MenuItem mapa = menu.add("Achar no Mapa");
//        MenuItem site = menu.add("Navegar no Site");
//
//        MenuItem deletar = menu.add("Deletar");
//        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                AlunoDAO dao = new AlunoDAO(activity);
//                dao.deleta(alunoSelecionado);
//                activity.carregaLista();
//                mode.finish();
//                return false;
//            }
//        });
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
