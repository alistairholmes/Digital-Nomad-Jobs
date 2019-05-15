package io.github.alistairholmes.digitalnomadjobs.ui.favorite;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import io.github.alistairholmes.digitalnomadjobs.data.repository.JobRepository;

public class FavoriteViewModel extends ViewModel {

    private JobRepository jobRepository;

    @Inject
    public FavoriteViewModel(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
