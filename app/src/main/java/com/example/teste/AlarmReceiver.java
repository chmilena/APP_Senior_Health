package com.example.teste;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "medication_channel_id"; // ID do canal de notificação

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "Recebido o alarme!"); // Adicione esta linha
        createNotification(context, "Lembrete de Medicamento", "É hora de tomar o medicamento!");
    }

    private void createNotification(Context context, String title, String message) {
        // Crie uma intenção para abrir a atividade desejada quando a notificação for clicada
        Intent intent = new Intent(context, ListaMedicamentosActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT| PendingIntent.FLAG_MUTABLE
        );

        // Configure o canal de notificação (necessário para Android 8.0 e superior)
        CharSequence channelName = "Medication Channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
        }

        // Crie o NotificationManager e defina o canal de notificação
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }

        // Crie a notificação usando NotificationCompat.Builder
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.logo) // Defina o ícone aqui
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.video))
                .build();

        // Notifique a notificação usando o NotificationManager
        notificationManager.notify(0, notification);
    }
}
