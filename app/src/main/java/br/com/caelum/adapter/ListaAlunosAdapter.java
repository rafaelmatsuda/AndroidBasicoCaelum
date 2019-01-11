package br.com.caelum.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.caelum.cadastro.R;
import br.com.caelum.model.Aluno;

public class ListaAlunosAdapter extends BaseAdapter {

    private List<Aluno>  listaAlunos;
    private Activity _activity;

    public ListaAlunosAdapter(Activity activity, List<Aluno> alunos)
    {
        listaAlunos = alunos;
        _activity = activity;
    }
    @Override
    public int getCount() {
        return listaAlunos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaAlunos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaAlunos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //cria um inflater para inflar o Layout
        LayoutInflater inflater = _activity.getLayoutInflater();
        //infla o xml para o componente pai e devolve em uma View
        View layout = inflater.inflate(R.layout.simple_lista_aluno_item, parent,false);
        //busca o TextView do componente activity_list_item (clicar no ctrl + clique na linha de cima)
        TextView textView = layout.findViewById(R.id.nome_aluno);
        //Seta o texto com o nome do aluno
        textView.setText(listaAlunos.get(position).getNome());
        //busca o image View no componente activity_list_item
        ImageView imageView = layout.findViewById(R.id.imagem_aluno);

        if(listaAlunos.get(position).getCaminhoFoto() != null)
        {
            //Se aluno tem foto
            //Vamos transformar o caminho da foto em imagem
            Bitmap imagem = BitmapFactory.decodeFile(listaAlunos.get(position).getCaminhoFoto());
            Bitmap imagemMenor = Bitmap.createScaledBitmap(imagem, 100, 100, true);
            //Vamos definir a imagem menor no ImageView
            imageView.setImageBitmap(imagemMenor);
        }
        else
            {
                //se aluno nao tem foto
                Bitmap imagem = BitmapFactory.decodeResource(_activity.getResources(),R.drawable.ic_no_image);
                Bitmap imagemMenor = Bitmap.createScaledBitmap(imagem,100,100, true);
                //Vamos definir a imagem menor no ImageView
                imageView.setImageBitmap(imagemMenor);
            }
        return layout;
    }
}
