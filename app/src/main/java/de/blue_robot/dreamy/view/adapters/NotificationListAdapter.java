package de.blue_robot.dreamy.view.adapters;

import android.app.Notification;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.service.notification.StatusBarNotification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.blue_robot.dreamy.R;

/**
 */
public class NotificationListAdapter extends BaseAdapter {

    private final Context context;
    private List<StatusBarNotification> notifications;

    public NotificationListAdapter(Context context) {
        this(context, new ArrayList<StatusBarNotification>());
    }

    public NotificationListAdapter(final Context context, final List<StatusBarNotification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notifications.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Notification notification = notifications.get(position).getNotification();
        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.notification_layout, parent, false);
        final TextView headline = (TextView) rowView.findViewById(R.id.notificationHeadline);
        final TextView timeView = (TextView) rowView.findViewById(R.id.notificationTime);
        final TextView description = (TextView) rowView.findViewById(R.id.notificationDescription);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.notificationIcon);


        if (notification.when > 0) {
            final Date date = new Date(notification.when);
            final String dateString = new SimpleDateFormat("HH:mm").format(date);
            timeView.setText(dateString);
        }

        description.setText(notification.extras.get("android.text").toString());
        Icon icon;
        headline.setText((String) notification.extras.get("android.title"));
        icon = notification.getLargeIcon();
        if (icon == null) {
            icon = notification.getSmallIcon();
            icon.setTint(context.getColor(R.color.white));
        }
        imageView.setImageIcon(icon);

        return rowView;

    }

    public List<StatusBarNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<StatusBarNotification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }
}
