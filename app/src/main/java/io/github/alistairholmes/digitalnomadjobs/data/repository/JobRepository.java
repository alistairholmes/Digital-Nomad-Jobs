package io.github.alistairholmes.digitalnomadjobs.data.repository;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.alistairholmes.digitalnomadjobs.data.local.dao.JobDao;
import io.github.alistairholmes.digitalnomadjobs.data.model.Job;
import io.github.alistairholmes.digitalnomadjobs.data.remote.RequestInterface;
import io.github.alistairholmes.digitalnomadjobs.utils.Resource;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

@Singleton
public class JobRepository {

    private CompositeDisposable disposables = new CompositeDisposable();
    private BehaviorSubject<List<Job>> todoSubject = BehaviorSubject.create();
    private Observable todoObservable = todoSubject.hide();
    private final RequestInterface requestInterface;
    private final JobDao jobDao;

    @Inject
    public JobRepository(JobDao jobDao, RequestInterface requestInterface) {
        this.requestInterface = requestInterface;
        this.jobDao = jobDao;
    }

    public Flowable<Resource<List<Job>>> retrieveJobs() {
        return Flowable.create(emitter -> {
            new NetworkBoundSource<List<Job>, List<Job>>(emitter) {
                @Override
                public Observable<List<Job>> getRemote() {
                    return Observable.interval(0, 300, TimeUnit.SECONDS).
                            flatMap(i -> {
                                Timber.e("Calling API for data.....");
                                return requestInterface.getAllJobs();
                            });
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
            };
        }, BackpressureStrategy.BUFFER);
    }


    public void fetchTodos() {
        disposables.add(
                requestInterface.getAllJobs()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(todoSubject::onNext));

    }

}
