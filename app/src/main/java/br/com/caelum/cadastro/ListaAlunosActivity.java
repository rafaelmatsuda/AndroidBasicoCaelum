package br.com.caelum.cadastro;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URI;
import java.util.List;

import br.com.caelum.dao.AlunoDAO;
import br.com.caelum.model.Aluno;
import br.com.caelum.security.Permissao;

public class ListaAlunosActivity extends AppCompatActivity {

    private ListView listaAlunos;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_alunos);

        this.listaAlunos = findViewById(R.id.lista_alunos);

        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int posicao, long id) {

                Aluno aluno = (Aluno) adapter.getItemAtPosition(posicao);

                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intent.putExtra("aluno", aluno);
                startActivity(intent);
            }
        });

        listaAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Aluno aluno = (Aluno)  parent.getItemAtPosition(position);
//                ContextActionBar actionBar = new ContextActionBar(ListaAlunosActivity.this, aluno);
//                ListaAlunosActivity.this.startSupportActionMode(actionBar);

                return false ;
            }
        });

        //Cria referenncia ao botao

        FloatingActionButton botao_lista = findViewById(R.id.botao_lista);

        botao_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);

                startActivity(intent);
            }
        });

        registerForContextMenu(listaAlunos);

        Permissao.fazPermissao(this);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        this.carregaLista();
    }


    public void carregaLista()
    {
        //Carrega a Lista de Alunos
        AlunoDAO dao = new AlunoDAO(this);

        List<Aluno> alunos = dao.getLista();
        dao.close();

        ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos);

        this.listaAlunos.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {


        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        final Aluno alunoSelecionado = (Aluno) listaAlunos.getAdapter().getItem(info.position);

        //region Menu Site
        final Intent intentSite = new Intent(Intent.ACTION_VIEW);
        intentSite.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String site = alunoSelecionado.getSite();
        if(!site.startsWith("http://")){
            site = "http://"+site;
        }
        intentSite.setData(Uri.parse(site));

        menu.add("Navegar no Site").setIntent(intentSite);

        //endregion

        //region Menu Mapa
        final Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0,0?z=14&q="+ Uri.encode(alunoSelecionado.getEndereco())));
        intentMapa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        menu.add("Achar no Mapa").setIntent(intentMapa);
        //endregion


        //region Menu SMS
        final Intent intentSms = new Intent(Intent.ACTION_VIEW);
        intentSms.setData(Uri.parse("sms:"+alunoSelecionado.getTelefone()));
        intentSms.putExtra("sms_body","Olá "+alunoSelecionado.getNome());
        intentSms.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        menu.add("Enviar SMS")/*.setIntent(intentSms)*/.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SmsManager smsManager = SmsManager.getDefault();
                PendingIntent sentIntent = PendingIntent.getActivity(ListaAlunosActivity.this, 0, new Intent(), 0);

                if(PhoneNumberUtils.isWellFormedSmsAddress(alunoSelecionado.getTelefone())){
                    smsManager.sendTextMessage(alunoSelecionado.getTelefone(), null, "Olá "+alunoSelecionado.getNome()+"! Sua nota é "+alunoSelecionado.getNota(), sentIntent, null);
                    Toast.makeText(ListaAlunosActivity.this,"SMS enviado com sucesso!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ListaAlunosActivity.this,"Falha no SMS - número inválido!", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        //endregion

        //region Menu Ligar
        MenuItem ligar = menu.add("Ligar");
        final Intent intentLigar = new Intent(Intent.ACTION_CALL);
        intentLigar.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentLigar.setData(Uri.parse("tel:"+alunoSelecionado.getTelefone()));
        ligar.setIntent(intentLigar);
        //endregion

        //region Menu DELETAR
        MenuItem deletar = menu.add("Deletar");

        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(ListaAlunosActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deletar")
                        .setMessage("Deseja Mesmo Deletar?")
                        .setPositiveButton("Quero",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                                        dao.deleta(alunoSelecionado);
                                        dao.close();
                                        carregaLista();
                                        Toast.makeText(ListaAlunosActivity.this, "Aluno "+ alunoSelecionado.getNome()+" deletado", Toast.LENGTH_LONG).show();
                                    }
                                }).setNegativeButton("Não", null).show();
                return false;
            }
        });

        //endregion


    }

}
