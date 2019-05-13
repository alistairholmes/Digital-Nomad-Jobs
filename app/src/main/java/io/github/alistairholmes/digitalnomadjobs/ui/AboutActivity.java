package io.github.alistairholmes.digitalnomadjobs.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.Libs;

import io.github.alistairholmes.digitalnomadjobs.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        new LibsBuilder()
                //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                .withActivityStyle(Libs.ActivityStyle.LIGHT)
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withAboutDescription("This is a small sample which can be set in the about my app description file.<br /><b>You can style this with html markup :D</b>")
                //start the activity
                .start(this);
    }
}
