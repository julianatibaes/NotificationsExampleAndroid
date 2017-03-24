package com.tibaes.customnotificationexample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void createNotification() {

        // BEGIN_INCLUDE(notificationCompat)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        // END_INCLUDE(notificationCompat)

        // BEGIN_INCLUDE(intent)
        //Criar a Intent para iniciar esta Activit novamente se a notificação for clicada.
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);
        // END_INCLUDE(intent)

        // BEGIN_INCLUDE(ticker)
        // Define o texto do ticker
        builder.setTicker(getResources().getString(R.string.custom_notification));

        // Define o ícone pequeno para o ticker
        builder.setSmallIcon(R.drawable.ic_stat_custom);
        // END_INCLUDE(ticker)

        // BEGIN_INCLUDE(buildNotification)
        // Cancelar a notificação quando clicada
        builder.setAutoCancel(true);

        // Build the notification
        Notification notification = builder.build();
        // END_INCLUDE(buildNotification)

        // BEGIN_INCLUDE(customLayout)
        // Inflar o layout de notificação como RemoteViews
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);

        // Definir o texto em um TextView no RemoteViews programaticamente.
        final String time = DateFormat.getTimeInstance().format(new Date()).toString();
        final String text = getResources().getString(R.string.collapsed, time);
        contentView.setTextViewText(R.id.textView, text);

        /* Workaround: É necessário definir a visualização de conteúdo aqui diretamente na notificação.
         * NotificationCompatBuilder contém um bug que impede que isso funcione em versões de plataforma
         * HoneyComb.
         * Veja mais em https://code.google.com/p/android/issues/detail?id=30495
         */
        notification.contentView = contentView;

        /* Adicione uma grande exibição de conteúdo à notificação, se suportada.
        * O suporte para notificações expandidas foi adicionado na API level 16.
        * (O contentView normal é mostrado quando a notificação é recolhida,
        * quando expandida a exibição de conteúdo grande aqui é exibida.)
        */
        if (Build.VERSION.SDK_INT >= 16) {
            // Inflar e definir o layout para a exibição de notificação expandida
            RemoteViews expandedView =
                    new RemoteViews(getPackageName(), R.layout.notification_expanded);
            notification.bigContentView = expandedView;
        }
        // END_INCLUDE(customLayout)

        // START_INCLUDE(notify)
        // Use o NotificationManager para mostrar a notificação
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, notification);
        // END_INCLUDE(notify)
    }

    public void showNotificationClicked(View v) {
        createNotification();
    }
}
