package br.com.caelum.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.io.Console;

import br.com.caelum.cadastro.R;
import br.com.caelum.dao.AlunoDAO;

public class EsperaSms extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Chegou SMS", Toast.LENGTH_LONG).show();

        Object[] pdus = (Object[]) intent.getSerializableExtra("pdus");

        byte[] inicioMsg = (byte[]) pdus[0];

        String formato = intent.getStringExtra("format");

        SmsMessage sms = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            sms = SmsMessage.createFromPdu(inicioMsg, formato);
        }
        String telefone = sms.getDisplayOriginatingAddress();
        AlunoDAO dao = new AlunoDAO(context);
        if(dao.existeAluno(telefone)){
            //existe telefone na agenda, entao toca musica

            MediaPlayer mp = MediaPlayer.create(context, R.raw.msg);
            mp.start();
        }

    }
}
