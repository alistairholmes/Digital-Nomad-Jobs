package io.github.alistairholmes.digitalnomadjobs.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import io.github.alistairholmes.digitalnomadjobs.utils.ViewUtil;


/**
 * https://github.com/nickbutcher/plaid
 * 
 * An extension to image view that has a circular outline.
 */
public class CircularImageView extends ForegroundImageView {

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOutlineProvider(ViewUtil.CIRCULAR_OUTLINE);
        setClipToOutline(true);
    }
}
