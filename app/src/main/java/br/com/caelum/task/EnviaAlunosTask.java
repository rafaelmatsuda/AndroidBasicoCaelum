package br.com.caelum.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import br.com.caelum.cadastro.R;
import br.com.caelum.model.Aluno;
import br.com.caelum.support.Webclient;

public class EnviaAlunosTask extends AsyncTask <Object, Object, String> {

    Activity _activity;
    List<Aluno> _alunos;
    ProgressBar progressBar;

    public EnviaAlunosTask(Activity activity, List<Aluno> listaAlunos){
    _activity = activity;
    _alunos = listaAlunos;

    }
    @Override
    protected String doInBackground(Object... objects) {

        String resposta = new Webclient().post(_alunos);
        return resposta;
    }

    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(result);
        Toast.makeText(_activity, result, Toast.LENGTH_LONG).show();
        //Dispose na ProgressBar
        progressBar.setVisibility(View.GONE);

    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    //É executado antes de iniciar a Thread secundária
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressBar = new ProgressBar(_activity);

        //define que o progressbar é indeterminado
        progressBar.setIndeterminate(true);

        //carrega o layout a partir de uma Activity
        RelativeLayout layout = _activity.findViewById(R.id.layout_lista_alunos);
        //Define onde a progress bar será exibida
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(300,300);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        //Informa que o progressbar faz parte do layout
        layout.addView(progressBar,params);
        //Faz o progressbar ficar visivel
        progressBar.setVisibility(View.VISIBLE);

    }
}
