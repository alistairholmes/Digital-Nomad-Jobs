package io.github.alistairholmes.digitalnomadjobs.ui.favorite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.github.alistairholmes.digitalnomadjobs.data.local.entity.FavoriteJob;
import io.github.alistairholmes.digitalnomadjobs.data.repository.JobRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FavoriteViewModel extends ViewModel {

    private JobRepository jobRepository;
    private LiveData<List<FavoriteJob>> favoriteJobLiveData;

    @Inject
    FavoriteViewModel(JobRepository jobRepository) {
        this.jobRepository = jobRepository;

        favoriteJobLiveData = LiveDataReactiveStreams.fromPublisher(jobRepository.getFavorites()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()));
    }

    LiveData<List<FavoriteJob>> getFavorites() {
        return favoriteJobLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
