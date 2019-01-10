package br.com.caelum.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import br.com.caelum.cadastro.R;
import br.com.caelum.model.Aluno;

public class FormularioHelper {

    private Activity _activity;

    private EditText campoNome;
    private EditText campoTel;
    private EditText campoEnd;
    private EditText campoSite;
    private RatingBar campoNota;
    private FloatingActionButton botaoFoto;
    private ImageView foto;

    private Aluno aluno ;

    public FormularioHelper(Activity activity){
        _activity = activity;

        aluno = new Aluno();

        campoNome = _activity.findViewById(R.id.nome);
        campoTel = _activity.findViewById(R.id.telefone);
        campoEnd = _activity.findViewById(R.id.endereco);
        campoSite = _activity.findViewById(R.id.site);
        campoNota = _activity.findViewById(R.id.nota);
        botaoFoto = _activity.findViewById(R.id.botao_foto);
        foto = _activity.findViewById(R.id.foto);
    }

    public Aluno getAluno(){

        aluno.setNome(campoNome.getText().toString());
        aluno.setTelefone(campoTel.getText().toString());
        aluno.setEndereco(campoEnd.getText().toString());
        aluno.setSite(campoSite.getText().toString());
        aluno.setNota(campoNota.getRating());
        aluno.setCaminhoFoto((String)foto.getTag());


        return aluno;
    }

    public boolean temNome()
    {
     return !campoNome.getText().toString().isEmpty();
    }

    public void mostraErro()
    {
        campoNome.setError("Campo nome é obrigatório!");
    }

    public void setAluno(Aluno alunoSelecionado)
    {
        campoNome.setText(alunoSelecionado.getNome());
        campoTel.setText(alunoSelecionado.getTelefone());
        campoEnd.setText(alunoSelecionado.getEndereco());
        campoSite.setText(alunoSelecionado.getSite());
        campoNota.setRating(alunoSelecionado.getNota().floatValue());
        aluno = alunoSelecionado;

        if(alunoSelecionado.getCaminhoFoto() != null)
        {
            carregaImagem(alunoSelecionado.getCaminhoFoto());
        }
    }

    //region Devolve o Botao de Adicionar Foto
    public FloatingActionButton getBotao()
    {
        return botaoFoto;
    }
    //endregion

    //region Carrega a imagem baseada no caminho
    public void carregaImagem(String caminho)
    {
        Bitmap bm = BitmapFactory.decodeFile(caminho);

        //Compacta a foto
        Bitmap imagemFoto = Bitmap.createScaledBitmap(bm,bm.getWidth(), 300, true);

        foto.setImageBitmap(imagemFoto);
        foto.setScaleType(ImageView.ScaleType.FIT_XY);
        foto.setTag(caminho);
    }
    //endregion
}
