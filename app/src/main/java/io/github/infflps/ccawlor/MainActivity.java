package io.github.infflps.ccawlor;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import io.github.infflps.ccawlorlib.CcawlorPicker;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout bg = findViewById(R.id.background);
        CcawlorPicker.show(this, Color.parseColor("#FF8800"), color -> {
            bg.setBackgroundColor(color);
        });
        
        bg.setOnClickListener(v -> {
            int currentColor = ((ColorDrawabl) bg.getBackground()).getColor();
            CcawlorPicker.show(this, currentColor, color -> bg.setBackgroundColor(color));
        });
    }
}