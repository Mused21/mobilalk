package hu.mobilalk.yagta;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {

    private static final String CHANNEL_ID = "gym_tracker_channel";
    private static final int NOTIFICATION_ID = 0;
    private Context mContext;
    private NotificationManager mManager;

    public NotificationHandler(Context context) {
        this.mContext = context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Gym Tracker Notification",
                NotificationManager.IMPORTANCE_HIGH
        );

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.RED);
        channel.setDescription("Gym Tracker azt akarja, hogy eddz!");
        this.mManager.createNotificationChannel(channel);
    }

    public void send(String message) {
        Intent intent = new Intent(mContext, GymMenuActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("Gym Tracker")
                .setContentText(message)
                .setSmallIcon(R.drawable.gym_noti)
                .setContentIntent(pendingIntent);

        this.mManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancel() {
        this.mManager.cancel(NOTIFICATION_ID);
    }
}
