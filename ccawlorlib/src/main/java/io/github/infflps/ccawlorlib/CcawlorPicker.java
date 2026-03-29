package io.github.infflps.ccawlorlib;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class CcawlorPicker {

    public interface Listener {
        void onColorSelected(int color);
    }

    /**
     * Show the color picker dialog.
     * @param context Context
     * @param defaultColor Starting color
     * @param listener Callback for color selection
     */
    public static void show(Context context, int defaultColor, Listener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.ccawlor_dialog, null);
        
        View preview = view.findViewById(R.id.prev);
        SeekBar red = view.findViewById(R.id.red_sb);
        SeekBar green = view.findViewById(R.id.green_sb);
        SeekBar blue = view.findViewById(R.id.blue_sb);
        TextView hexTv = view.findViewById(R.id.hex_tv);
        SeekBar alpha = view.findViewById(R.id.alpha_sb);
        boolean useAlpha = alpha != null;
        
        LinearLayout swatchesLayout = view.findViewById(R.id.swatches_layout);
        
        red.setProgress(Color.red(defaultColor));
        green.setProgress(Color.green(defaultColor));
        blue.setProgress(Color.blue(defaultColor));
        if(useAlpha) alpha.setProgress(Color.alpha(defaultColor));
        
        List<Integer> presetColors = Arrays.asList(
                Color.RED, Color.GREEN, Color.BLUE,
                Color.YELLOW, Color.MAGENTA, Color.CYAN,
                Color.BLACK, Color.WHITE, Color.GRAY
        );

        if(swatchesLayout != null){
            for (int color : presetColors) {
                View swatch = new View(context);

                int size = 100;
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
                lp.setMargins(8, 8, 8, 8);
                swatch.setLayoutParams(lp);
                
                GradientDrawable gd = new GradientDrawable();
                gd.setShape(GradientDrawable.OVAL);
                gd.setColor(color);
                gd.setStroke(2, Color.DKGRAY);
                swatch.setBackground(gd);
                
                swatch.setTag(color);
                
                swatch.setOnClickListener(v -> {
                    int c = (int) v.getTag();
                    red.setProgress(Color.red(c));
                    green.setProgress(Color.green(c));
                    blue.setProgress(Color.blue(c));
                    if (alpha != null) alpha.setProgress(Color.alpha(c));
                });

                swatchesLayout.addView(swatch);
            }
        }
        
        Runnable updateColor = () -> {
            int r = red.getProgress();
            int g = green.getProgress();
            int b = blue.getProgress();
            int a = useAlpha ? alpha.getProgress() : 255;
            int color = Color.argb(a, r, g, b);

            preview.setBackgroundColor(color);
            if(hexTv != null){
                String hex = useAlpha ?
                        String.format("#%02X%02X%02X%02X", a, r, g, b) :
                        String.format("#%02X%02X%02X", r, g, b);
                hexTv.setText(hex);
            }
        };
        
        SeekBar.OnSeekBarChangeListener sbListener = new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateColor.run();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        };
        
        red.setOnSeekBarChangeListener(sbListener);
        green.setOnSeekBarChangeListener(sbListener);
        blue.setOnSeekBarChangeListener(sbListener);
        if(useAlpha) alpha.setOnSeekBarChangeListener(sbListener);
        
        updateColor.run();
        
        new AlertDialog.Builder(context)
                .setTitle("Pick a color")
                .setView(view)
                .setPositiveButton("OK", (d, w) -> {
                    int color = Color.argb(useAlpha ? alpha.getProgress() : 255,
                            red.getProgress(), green.getProgress(), blue.getProgress());
                    listener.onColorSelected(color);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}