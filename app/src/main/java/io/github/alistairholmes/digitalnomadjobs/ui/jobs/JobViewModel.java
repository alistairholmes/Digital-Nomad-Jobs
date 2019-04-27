package io.github.alistairholmes.digitalnomadjobs.ui.jobs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.github.alistairholmes.digitalnomadjobs.data.model.Job;
import io.github.alistairholmes.digitalnomadjobs.data.repository.JobRepository;
import io.github.alistairholmes.digitalnomadjobs.utils.Resource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class JobViewModel extends ViewModel {

    private final JobRepository jobRepository;
    private final LiveData<Resource<List<Job>>> jobsLiveData;

    @Inject
    JobViewModel(JobRepository jobRepository) {
        this.jobRepository = jobRepository;

        this.jobsLiveData = LiveDataReactiveStreams
                .fromPublisher(jobRepository.retrieveJobs()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                );
    }

    LiveData<Resource<List<Job>>> getJobsLiveData() {
        return jobsLiveData;
    }

}
