package io.github.alistairholmes.digitalnomadjobs.ui.search;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import io.github.alistairholmes.digitalnomadjobs.data.model.SearchResponse;
import io.github.alistairholmes.digitalnomadjobs.data.repository.JobRepository;
import io.reactivex.Observable;

public class SearchViewModel extends ViewModel {

    private JobRepository jobRepository;

    @Inject
    SearchViewModel(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Observable<SearchResponse> search(final String query) {
        return jobRepository
                .getSearch(query)
                /*.delay(2, TimeUnit.SECONDS)*/;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
