package de.dreamy.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.dreamy.R;

public class ColorPickerDialog extends DialogFragment implements View.OnClickListener {

    private int colorId;
    private List<ColorPickedListener> colorPickedListeners = new ArrayList();


    public void addColorPickedListener(final ColorPickedListener colorPickedListener) {
        this.colorPickedListeners.add(colorPickedListener);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.color_picker_dialog_layout, null));

        builder.setNegativeButton(R.string.dismiss,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dismiss();
                    }
                }
        );
        return builder.create();
    }

    @Override
    public void onResume() {
        final Dialog dialog = this.getDialog();
        dialog.findViewById(R.id.white).setOnClickListener(this);
        dialog.findViewById(R.id.gray).setOnClickListener(this);
        dialog.findViewById(R.id.red).setOnClickListener(this);
        dialog.findViewById(R.id.maroon).setOnClickListener(this);
        dialog.findViewById(R.id.purple).setOnClickListener(this);
        dialog.findViewById(R.id.yellow).setOnClickListener(this);
        dialog.findViewById(R.id.green).setOnClickListener(this);
        dialog.findViewById(R.id.black).setOnClickListener(this);
        dialog.findViewById(R.id.teal).setOnClickListener(this);
        dialog.findViewById(R.id.aqua).setOnClickListener(this);
        dialog.findViewById(R.id.navy).setOnClickListener(this);
        dialog.findViewById(R.id.blue).setOnClickListener(this);
        super.onStart();
    }


    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.gray:
                colorId = R.color.gray;
                break;
            case R.id.red:
                colorId = R.color.red;
                break;
            case R.id.maroon:
                colorId = R.color.maroon;
                break;
            case R.id.purple:
                colorId = R.color.purple;
                break;
            case R.id.yellow:
                colorId = R.color.yellow;
                break;
            case R.id.green:
                colorId = R.color.green;
                break;
            case R.id.black:
                colorId = R.color.black;
                break;
            case R.id.teal:
                colorId = R.color.teal;
                break;
            case R.id.aqua:
                colorId = R.color.aqua;
                break;
            case R.id.navy:
                colorId = R.color.navy;
                break;
            case R.id.blue:
                colorId = R.color.blue;
                break;
            default:
                colorId = R.color.white;
        }

        for (ColorPickerDialog.ColorPickedListener colorPickedListener : colorPickedListeners) {
            colorPickedListener.onColorPicked(colorId);
        }
        this.dismiss();
    }


    public interface ColorPickedListener {
        void onColorPicked(int colorId);
    }


}