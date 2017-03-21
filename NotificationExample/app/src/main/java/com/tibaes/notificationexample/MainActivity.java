package com.tibaes.notificationexample;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final int NOTIFICATION_ID = 1;
    public static final String TAG = "deu certo";
    NotificationCompat.Builder mBuilder ;
    NotificationManager mNotifyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendNotification(View view) {

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://developer.android.com/" +
                        "guide/topics/ui/notifiers/notifications.html?hl=pt-br"));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.drawable.ic_stat_notification);

        builder.setContentIntent(pendingIntent);

        builder.setAutoCancel(true);

        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

        builder.setContentTitle("Exemplo de notificação simples");
        builder.setContentText("O medo é o caminho para o lado negro.");
        builder.setSubText("Clique para aprender mais sobre notificações");

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());


    }

    public void updateNotification(View view){
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_notification)
                        .setContentTitle("Esteja consciente de seus pensamentos.")
                        .setContentText("Você não pode impedir as mudanças, assim como não pode impedir o pôr do sol.");

        // Cria uma intent explícita para uma activity em seu aplicativo
        Intent resultIntent = new Intent(this, ResultActivity.class);

        // O objeto do construtor de pilha conterá uma pilha de volta artificial para a atividade iniciada.
        // Isso garante que a navegação para trás a partir da atividade leva fora do seu aplicativo para a tela inicial.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adiciona a pilha traseira para o Intent (mas não para o Intent propriamente dito)
        stackBuilder.addParentStack(ResultActivity.class);

        // Adiciona a intent que inicia a atividade no topo da pilha
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // MId permite que você atualize a notificação mais tarde.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


    }

    public void downloadNotification(View view){
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        String[] events = new String[6];
        mNotifyManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Download da imagem")
                .setContentText("Progresso")
                .setSmallIcon(R.drawable.ic_stat_notification);
        // Iniciar uma operação demorada em um segmento de plano de fundo
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        // Faça a operação "longa" 20 vezes
                        for (incr = 0; incr <= 100; incr+=5) {
                            // Define o indicador de progresso como um valor máximo,
                            // a porcentagem de conclusão atual e o estado "determinado"
                            mBuilder.setProgress(100, incr, false);
                            // Exibe a barra de progresso pela primeira vez.
                            mNotifyManager.notify(0, mBuilder.build());
                            // Dorme o fio,
                            // simulando uma operação que leva tempo
                            try {
                                // Durma por 5 segundos
                                Thread.sleep(5*1000);
                            } catch (InterruptedException e) {
                                Log.d(TAG, "sleep failure");
                            }
                        }
                        // Quando o loop for concluído, atualiza a notificação
                        mBuilder.setContentText("Download completo")
                                // Remove a barra de progresso
                                .setProgress(0,0,false);
                        mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
                    }
                }
        // Inicia o thread chamando o método run () em seu Runnable
        ).start();
    }

    public void sendSimpleNotification(View view) {

        EditText editText = (EditText) findViewById(R.id.edit_text);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_notification)
                        .setContentTitle("Mensagem")
                        .setContentText(editText.getText().toString());

        Intent resultIntent = new Intent(this, ResultActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
