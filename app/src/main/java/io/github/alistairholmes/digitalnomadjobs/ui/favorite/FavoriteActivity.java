package io.github.alistairholmes.digitalnomadjobs.ui.favorite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import io.github.alistairholmes.digitalnomadjobs.R;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, FavoriteFragment.newInstance())
                    .commitNow();
        }
    }
}
