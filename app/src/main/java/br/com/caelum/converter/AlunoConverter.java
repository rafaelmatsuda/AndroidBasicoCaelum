package br.com.caelum.converter;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

import br.com.caelum.model.Aluno;

public class AlunoConverter
{
    public String toJSON(List<Aluno> alunos) throws JSONException {
        JSONStringer jsonStringer = new JSONStringer();

        try
        {
            jsonStringer.object().key("list").array()
                    .object().key("aluno").array();

            for (Aluno aluno : alunos)
            {
            jsonStringer.object().key("id").value(aluno.getId())
                    .key("nome").value(aluno.getNome())
                    .key("telefone").value(aluno.getTelefone())
                    .key("endereco").value(aluno.getEndereco())
                    .key("site").value(aluno.getSite())
                    .key("caminhofoto").value(aluno.getCaminhoFoto())
                    .key("nota").value(aluno.getNota())
                    .endObject();
            }

            String json = jsonStringer.endArray()
                    .endObject().endArray().endObject().toString();

            return json;
        }
        catch (JSONException e)
        {
            throw e;
        }

    }
}
