package io.github.alistairholmes.digitalnomadjobs;

import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class DetailActivity extends AppCompatActivity {

    public TextView tv_JobTitle;
    public TextView tv_CompanyName;
    public ImageView iv_CompanyLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tv_JobTitle = findViewById(R.id.textView_jobtitle);
        tv_CompanyName = findViewById(R.id.textView_companyname);
        iv_CompanyLogo = findViewById(R.id.imageView_Logo);


        //get the intent in the target activity
        Intent intent = getIntent();
        //get the attached bundle from the intent
        Bundle extras = intent.getExtras();
        //Extracting the stored data from the bundle
        String job_title = extras.getString("JOB_TITLE");
        String company_name = extras.getString("COMPANY_NAME");
        String company_logo = extras.getString("COMPANY_LOGO");
        //String job_description = extras.getString("JOB_DESCRIPTION");

        tv_JobTitle.setText(job_title);
        tv_CompanyName.setText(company_name);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_foreground)
                .circleCrop();
        Glide.with(this)
                .load(company_logo)
                .apply(requestOptions)
                .into(iv_CompanyLogo);
       /* if (job.getUrl() != null) {
                                    // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
                                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                    // set toolbar color and set custom actions before invoking build()
                                    builder.setToolbarColor(ContextCompat.getColor(JobActivity.this, R.color.colorAccent));
                                    // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
                                    CustomTabsIntent customTabsIntent = builder.build();
                                    // and launch the desired Url with CustomTabsIntent.launchUrl()
                                    customTabsIntent.launchUrl(JobActivity.this, Uri.parse(job.getUrl()));

                                } else {
                                    Toast.makeText(JobActivity.this, "Sorry no URL is available for this job at the moment. Please try again later",
                                            Toast.LENGTH_SHORT).show();
                                }*/

    }
}
