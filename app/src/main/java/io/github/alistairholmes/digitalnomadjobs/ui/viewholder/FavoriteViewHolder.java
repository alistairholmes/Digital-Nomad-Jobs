package io.github.alistairholmes.digitalnomadjobs.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.alistairholmes.digitalnomadjobs.R;
import io.github.alistairholmes.digitalnomadjobs.ui.views.WishListIconView;

public class FavoriteViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_job_title) public TextView jobTitle;
    @BindView(R.id.text_company_name) public TextView companyName;
    @BindView(R.id.text_date) public TextView datePosted;
    @BindView(R.id.image_logo) public ImageView companyLogo;
    @BindView(R.id.favoriteIcon) public WishListIconView favoriteButton;

    public FavoriteViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
