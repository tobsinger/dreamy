package de.blue_robot.dreamy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.service.dreams.DreamService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import java.util.List;

import de.blue_robot.dreamy.notifications.NotificationListener;


/**
 * Created by Tobs on 24/10/15.
 */
public class RobotDaydream extends DreamService implements OnClickListener {

	private ImageView iconView;

	private LocalBroadcastManager localBroadcastManager;

	/**
	 * Set icon of latest notification
	 *
	 * @param drawable Icon to set
	 */
	public void setIcon(Drawable drawable) {
		ColorMatrix matrix = new ColorMatrix();
		matrix.setSaturation(0);

		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

		drawable.setColorFilter(filter);
		iconView.setImageDrawable(drawable);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("test", "creating daydream service");
		initBroadcastManager();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		localBroadcastManager.unregisterReceiver(bcr);
	}

	@Override
	public void onAttachedToWindow() {
		//setup daydream
		super.onAttachedToWindow();

		setInteractive(false);
		setFullscreen(true);

		Point screenSize = new Point();
		getWindowManager().getDefaultDisplay().getSize(screenSize);
		setContentView(R.layout.daydream_layout);
		iconView = (ImageView) findViewById(R.id.iconView);
	}


	@Override
	public void onDreamingStarted() {
		//daydream started
		super.onDreamingStarted();
		viewNotifications();
	}


	@Override
	public void onClick(View v) {
		this.finish();
	}

	private void initBroadcastManager() {
		localBroadcastManager = LocalBroadcastManager.getInstance(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction("notification_update");
		localBroadcastManager.registerReceiver(bcr, filter);
	}

	private BroadcastReceiver bcr = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			viewNotifications();
		}
	};

	private void viewNotifications() {
		List<StatusBarNotification> notifications = NotificationListener.getNotifications();
		Log.d("test", "number of notifications: " + notifications.size());
		if (notifications.size() > 0) {
			StatusBarNotification nf = notifications.get(0);
			try {
				setIcon(createPackageContext(nf.getPackageName(), Context.CONTEXT_IGNORE_SECURITY)
								.getResources().getDrawable(nf.getNotification().icon));
			} catch (PackageManager.NameNotFoundException e) {
			}
		}
	}
}
