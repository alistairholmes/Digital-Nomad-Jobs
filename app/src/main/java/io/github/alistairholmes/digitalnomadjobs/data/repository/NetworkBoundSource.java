package io.github.alistairholmes.digitalnomadjobs.data.repository;

import android.annotation.SuppressLint;

import io.github.alistairholmes.digitalnomadjobs.utils.Resource;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@SuppressWarnings("ResultOfMethodCallIgnored")
public abstract class NetworkBoundSource<LocalType, RemoteType> {

    @SuppressLint("CheckResult")
    NetworkBoundSource(FlowableEmitter<Resource<LocalType>> emitter) {

        Disposable firstDataDisposable = getLocal()
                .map(Resource::loading)
                .subscribe(emitter::onNext);

        getRemote().map(mapper())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<LocalType>() {
                    @Override
                    public void accept(LocalType localTypeData) throws Exception {
                        firstDataDisposable.dispose();
                        NetworkBoundSource.this.saveCallResult(localTypeData);
                        NetworkBoundSource.this.getLocal()
                                .map(Resource::success)
                                .onErrorReturn(msg -> Resource.error(msg.getMessage()))
                                .subscribe(emitter::onNext);

                    }
                }, Timber::e);
    }

    public abstract Observable<RemoteType> getRemote();

    public abstract Flowable<LocalType> getLocal();

    public abstract void saveCallResult(LocalType data);

    public abstract Function<RemoteType, LocalType> mapper();

}