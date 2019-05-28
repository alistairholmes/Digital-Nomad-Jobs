package io.github.alistairholmes.digitalnomadjobs.ui.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;

/**
 * https://github.com/airbnb/lottie-android
 *
 * @author Osaigbovo Odiase.
 */
public class WishListIconView extends LottieAnimationView {

    private boolean activate;

    public WishListIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WishListIconView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public final void isFavorite(boolean favorite) {
        activate = favorite;
        this.setProgress(favorite ? 1.0F : 0.0F);
    }

    public final void toggleWishlisted() {
        this.setActivated(/*!this.isActivated()*/ !activate);
    }

    @Override
    public void setActivated(boolean activated) {
        super.setActivated(activated);
        activate = activated;

        this.setSpeed(activated ? 1.0F : -2.0F);
        this.setProgress(0.0F);
        this.playAnimation();
    }

}
