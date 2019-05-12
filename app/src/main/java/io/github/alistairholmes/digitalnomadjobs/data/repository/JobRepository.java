package io.github.alistairholmes.digitalnomadjobs.data.repository;

import android.content.ContentResolver;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.alistairholmes.digitalnomadjobs.data.local.dao.FavoriteDao;
import io.github.alistairholmes.digitalnomadjobs.data.local.dao.JobDao;
import io.github.alistairholmes.digitalnomadjobs.data.model.Job;
import io.github.alistairholmes.digitalnomadjobs.data.provider.JobsProvider;
import io.github.alistairholmes.digitalnomadjobs.data.remote.RequestInterface;
import io.github.alistairholmes.digitalnomadjobs.utils.Optional;
import io.github.alistairholmes.digitalnomadjobs.utils.Resource;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

import static io.github.alistairholmes.digitalnomadjobs.utils.DbUtil.ID_PROJECTION;
import static io.github.alistairholmes.digitalnomadjobs.utils.DbUtil.ID_PROJECTION_MAP;

@Singleton
public class JobRepository {

    private final RequestInterface requestInterface;
    private final JobDao jobDao;
    private final FavoriteDao favoriteDao;
    private final ContentResolver contentResolver;

    @Inject
    public JobRepository(JobDao jobDao, FavoriteDao favoriteDao, RequestInterface requestInterface,
                         ContentResolver contentResolver) {
        this.requestInterface = requestInterface;
        this.jobDao = jobDao;
        this.favoriteDao = favoriteDao;
        this.contentResolver = contentResolver;
    }

    public Flowable<Resource<List<Job>>> retrieveJobs() {
        return Flowable.create(emitter -> new NetworkBoundSource<List<Job>, List<Job>>(emitter) {
            @Override
            public Observable<List<Job>> getRemote() {
                return Observable.combineLatest(requestInterface.getAllJobs(), savedJobIds(),
                        (jobList, favoriteIds) -> {
                            for (Job job : jobList) {
                                job.setFavorite(favoriteIds.contains(job.getId()));
                            }
                            Timber.e(String.valueOf(favoriteIds.size()));
                            Timber.e(String.valueOf(jobList.size()));
                            return jobList;
                        })
                        .subscribeOn(Schedulers.io())
                        .doOnError(Timber::e);
            }

            @Override
            public Flowable<List<Job>> getLocal() {
                Timber.e("Getting data from database.....");
                return jobDao.getJobs();
            }

            @Override
            public void saveCallResult(@NonNull List<Job> jobList) {
                jobDao.saveJobs(jobList);
                Timber.e("Save to database.....");
            }

            @Override
            public Function<List<Job>, List<Job>> mapper() {
                return jobList -> jobList;
            }
        }, BackpressureStrategy.BUFFER);
    }

    public void saveJob(Job job) {
        /*AsyncQueryHandler handler = new AsyncQueryHandler(mContentResolver) {};
        handler.startInsert(-1, null, Movies.CONTENT_URI, new Movie.Builder().movie(movie).build());*/
    }

    private Observable<Set<Integer>> savedJobIds() {
        return Observable
                .just(new Optional<>(contentResolver.query(JobsProvider.URI_JOB,
                        ID_PROJECTION, null, null, null)))
                .map(ID_PROJECTION_MAP)
                .subscribeOn(Schedulers.io());
    }

}
