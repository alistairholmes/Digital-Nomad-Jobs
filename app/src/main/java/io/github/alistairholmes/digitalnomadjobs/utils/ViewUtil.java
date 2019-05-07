package io.github.alistairholmes.digitalnomadjobs.utils;

import android.content.Context;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.github.alistairholmes.digitalnomadjobs.R;

public class ViewUtil {

    @Nullable
    public static String formatDayMonth(@NonNull Context context, @Nullable Date date) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(
                context.getString(R.string.format_date),
                Locale.US);
        return sdf.format(date);
    }

    public static final ViewOutlineProvider CIRCULAR_OUTLINE = new ViewOutlineProvider() {
        @Override
        public void getOutline(View view, Outline outline) {
            outline.setOval(view.getPaddingLeft(),
                    view.getPaddingTop(),
                    view.getWidth() - view.getPaddingRight(),
                    view.getHeight() - view.getPaddingBottom());
        }
    };
}
