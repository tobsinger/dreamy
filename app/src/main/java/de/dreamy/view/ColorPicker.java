package de.dreamy.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import de.dreamy.R;

/**
 */
public class ColorPicker extends ImageButton implements View.OnClickListener, ColorPickerDialog.ColorPickedListener {
    private static final String TAG = ColorPicker.class.getSimpleName();

    public List<ColorPickedListener> colorPickedListeners = new ArrayList<>();

    public ColorPicker(final Context context) {
        super(context);
//        this.setOnClickListener(this);
    }

    public ColorPicker(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.setOnClickListener(this);
    }


    public void addColorPickedListener(final ColorPickedListener colorPickedListener) {
        this.colorPickedListeners.add(colorPickedListener);
    }

    @Override
    public void onClick(final View view) {
        ColorPickerDialog dialog = new ColorPickerDialog();
        try {
            final Activity activity = (Activity) getContext();
            final FragmentManager fm = activity.getFragmentManager();
            dialog.addColorPickedListener(this);
            dialog.show(fm, "NoticeDialogFragment");
        } catch (ClassCastException e) {
            Log.d(TAG, "Can't get the fragment manager with this");
        }
    }

    @Override
    public void onColorPicked(final int color) {
        final int colorPickerBackground;

        int colorId;

        if (color == R.color.gray || color == getColor(R.color.gray)) {
            colorPickerBackground = R.drawable.color_picker_background_gray;
            colorId = R.color.gray;
        } else if (color == R.color.red || color == getColor(R.color.red)) {
            colorPickerBackground = R.drawable.color_picker_background_red;
            colorId = R.color.red;
        } else if (color == R.color.maroon || color == getColor(R.color.maroon)) {
            colorPickerBackground = R.drawable.color_picker_background_maroon;
            colorId = R.color.maroon;
        } else if (color == R.color.purple || color == getColor(R.color.purple)) {
            colorPickerBackground = R.drawable.color_picker_background_purple;
            colorId = R.color.purple;
        } else if (color == R.color.yellow || color == getColor(R.color.yellow)) {
            colorPickerBackground = R.drawable.color_picker_background_yellow;
            colorId = R.color.yellow;
        } else if (color == R.color.green || color == getColor(R.color.green)) {
            colorPickerBackground = R.drawable.color_picker_background_green;
            colorId = R.color.green;
        } else if (color == R.color.lime || color == getColor(R.color.lime)) {
            colorPickerBackground = R.drawable.color_picker_background_lime;
            colorId = R.color.lime;
        } else if (color == R.color.teal || color == getColor(R.color.teal)) {
            colorPickerBackground = R.drawable.color_picker_background_teal;
            colorId = R.color.teal;
        } else if (color == R.color.aqua || color == getColor(R.color.aqua)) {
            colorPickerBackground = R.drawable.color_picker_background_aqua;
            colorId = R.color.aqua;
        } else if (color == R.color.navy || color == getColor(R.color.navy)) {
            colorPickerBackground = R.drawable.color_picker_background_navy;
            colorId = R.color.navy;
        } else if (color == R.color.blue || color == getColor(R.color.blue)) {
            colorPickerBackground = R.drawable.color_picker_background_blue;
            colorId = R.color.blue;
        } else if (color == R.color.black || color == getColor(R.color.black)) {
            colorPickerBackground = R.drawable.color_picker_background_black;
            colorId = R.color.black;
        } else if (color == R.color.darkgray || color == getColor(R.color.darkgray)) {
            colorPickerBackground = R.drawable.color_picker_background_dark_gray;
            colorId = R.color.darkgray;
        } else if (color == R.color.olive || color == getColor(R.color.olive)) {
            colorPickerBackground = R.drawable.color_picker_background_olive;
            colorId = R.color.olive;
        } else if (color == R.color.orange || color == getColor(R.color.orange)) {
            colorPickerBackground = R.drawable.color_picker_background_olive;
            colorId = R.color.orange;
        } else if (color == R.color.fuchsia || color == getColor(R.color.fuchsia)) {
            colorPickerBackground = R.drawable.color_picker_background_fuchsia;
            colorId = R.color.fuchsia;
        } else {
            colorPickerBackground = R.drawable.color_picker_background_white;
            colorId = R.color.white;
        }

        setBackgroundResource(colorPickerBackground);
        for (ColorPickedListener colorPickedListener : this.colorPickedListeners) {
            colorPickedListener.onColorPicked(colorId);
        }
    }

    private int getColor(int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }

    public interface ColorPickedListener {
        void onColorPicked(int colorId);
    }


}
