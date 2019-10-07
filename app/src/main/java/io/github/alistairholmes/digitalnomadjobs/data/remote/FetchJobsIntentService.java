package io.github.alistairholmes.digitalnomadjobs.data.remote;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.github.alistairholmes.digitalnomadjobs.data.repository.JobRepository;

public class FetchJobsIntentService extends IntentService {

    private static final String ACTION_GET_REMOTE_JOBS
            = "io.github.alistairholmes.digitalnomadjobs.data.remote.action.FOO";

    @Inject
    JobRepository jobRepository;

    public FetchJobsIntentService() {
        super("FetchJobsIntentService");
    }

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    public static void startActionGetRemoteJobs(Context context) {
        Intent intent = new Intent(context, FetchJobsIntentService.class);
        intent.setAction(ACTION_GET_REMOTE_JOBS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_REMOTE_JOBS.equals(action)) {
                handleAction();
            }
        }
    }

    private void handleAction() {
        jobRepository.retrieveJobs();
    }

}
