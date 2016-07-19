package de.dreamy.view.adapters;

import android.app.Notification;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.service.notification.StatusBarNotification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import de.dreamy.Constants;
import de.dreamy.DreamyApplication;
import de.dreamy.R;
import de.dreamy.settings.SettingsDao;

/**
 * Array Adapter to display status bar notifications
 */
public class NotificationListAdapter extends ArrayAdapter<StatusBarNotification> {

    private final Context context;
    private List<StatusBarNotification> notifications = new ArrayList<>();

    @Inject
    SettingsDao settingsDao;

    public NotificationListAdapter(final Context context, final ArrayList<StatusBarNotification> items) {
        super(context, 0, items);
        this.context = context;
        this.notifications.addAll(items);
        DreamyApplication.getDreamyComponent().inject(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Notification notification = getItem(position).getNotification();
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolderItem viewHolder;

        // Create view if no recyclable view was given
        if (convertView == null) {
//            Settings settings = settingsDao.getSettings(context);
            convertView = inflater.inflate(R.layout.notification_layout, parent, false);
            viewHolder = new ViewHolderItem();
            // store the holder with the view.
            convertView.setTag(viewHolder);
            viewHolder.headline = (TextView) convertView.findViewById(R.id.notificationHeadline);
            viewHolder.timeView = (TextView) convertView.findViewById(R.id.notificationTime);
            viewHolder.description = (TextView) convertView.findViewById(R.id.notificationDescription);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.notificationIcon);

        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();

        }

        if (notification.when > 0) {
            final Date date = new Date(notification.when);
            final String dateString = new SimpleDateFormat(Constants.TIME_PATTERN, Locale.GERMANY).format(date);
            viewHolder.timeView.setText(dateString);
        }

        if (notification.extras.get(Constants.NOTIFICATION_TITLE) != null) {
            viewHolder.headline.setText(notification.extras.get(Constants.NOTIFICATION_TITLE).toString());
        } else {
            viewHolder.headline.setText(null);
        }
        if (notification.extras.get(Constants.NOTIFICATION_CONTENT) != null) {
            viewHolder.description.setText(notification.extras.get(Constants.NOTIFICATION_CONTENT).toString());
        } else {
            viewHolder.description.setText(null);
        }
        // Set the icon
        Icon icon = notification.getLargeIcon();
        if (icon == null) {
            icon = notification.getSmallIcon();
            icon.setTint(context.getColor(R.color.white));
        }

        viewHolder.imageView.setImageIcon(icon);
        return convertView;
    }


    public void setNotifications(final List<StatusBarNotification> notifications) {
        // items that are new to the list
        final ArrayList<StatusBarNotification> newNotifications = new ArrayList<>();
        // Find new items in list (filter out items that are already present)
        for (final StatusBarNotification singleNewNotification : notifications) {
            if (!this.notifications.contains(singleNewNotification)) {
                newNotifications.add(singleNewNotification);
            }
        }
        // items that are currently displayed but should be deleted
        final ArrayList<StatusBarNotification> deleteNotifications = new ArrayList<>();
        for (final StatusBarNotification oldNotifications : this.notifications) {
            if (!notifications.contains(oldNotifications)) {
                deleteNotifications.add(oldNotifications);
                remove(oldNotifications);
            }
        }

        this.notifications.removeAll(deleteNotifications);
        this.notifications.addAll(newNotifications);
        addAll(newNotifications);
        notifyDataSetChanged();
    }


    static class ViewHolderItem {
        TextView headline;
        TextView timeView;
        TextView description;
        ImageView imageView;
    }
}
