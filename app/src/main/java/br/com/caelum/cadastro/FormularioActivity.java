package br.com.caelum.cadastro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.File;

import br.com.caelum.dao.AlunoDAO;
import br.com.caelum.helper.FormularioHelper;
import br.com.caelum.model.Aluno;

public class FormularioActivity extends AppCompatActivity {

    FormularioHelper helper;
    private String caminho;
    private final int reqFoto = 123;

    private Intent intentCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        this.helper = new FormularioHelper(this);

        Intent intent = getIntent();

        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");

        if (aluno != null){
            helper.setAluno(aluno);
        }

        FloatingActionButton botaoFoto =  helper.getBotao();
        botaoFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Começa aqui a jornada para criar a URI - A partir do Android 7
                caminho = getExternalFilesDir(null)+"/"+System.currentTimeMillis()+".jpg";
                //Gera o arquivo na pasta
                File file = new File(caminho);
                //Pega o uri do arquivo
                Uri uriImagem = FileProvider.getUriForFile(FormularioActivity.this, BuildConfig.APPLICATION_ID+".provider", file);

                intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //Informa para a camera qual o caminho que deve ser gerado o arquivo de foto
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uriImagem);
                startActivityForResult(intentCamera, reqFoto);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Verifica se a requisicao é a que criamos para a camera
        if (requestCode == reqFoto)
        {
            if(resultCode == Activity.RESULT_OK){
                helper.carregaImagem(caminho);
            }
            else
                {
                    new AlertDialog.Builder(FormularioActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Tirar outra foto?")
                            .setMessage("Deseja Tirar outra foto?")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivityForResult(intentCamera, reqFoto);
                                }
                            })
                            .setNegativeButton("Não", null).show();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.salvar:
                //captura os dados do fomulario e armazena no objeto aluno
                Aluno aluno = helper.getAluno();
                // Verifica se o nome do aluno esta preenchido
                if(helper.temNome())
                {
                    //salva no banco SQLite (banco local) o objeto Aluno
                    AlunoDAO dao = new AlunoDAO(this);
                    if(aluno.getId() == null)
                    {
                        dao.insere(aluno);
                        Toast.makeText(FormularioActivity.this, "Objeto aluno CRIADO: " + aluno.getNome(), Toast.LENGTH_LONG).show();
                    }
                    else{
                        //altera aluno
                        dao.altera(aluno);
                        Toast.makeText(FormularioActivity.this, "Objeto aluno ALTERADO: " + aluno.getNome(), Toast.LENGTH_LONG).show();
                    }
                    //Fecha a conexao com o banco
                    dao.close();
                    //Exibe mensagem que o aluno foi inserido com sucesso

                    finish();
                }
                else
                {
                    //mostra o erro
                    helper.mostraErro();
                }

                return false;

            case R.id.fechar:
                Toast.makeText(FormularioActivity.this, "Cadastro Cancelado", Toast.LENGTH_LONG).show();
                finish();
            default:
                return super.onOptionsItemSelected(item);


        }
    }
}
