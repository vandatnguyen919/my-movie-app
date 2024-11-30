package com.example.domain.usecase.base;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class SingleUseCase<T> extends UseCase {

    protected abstract Single<T> buildUseCaseSingle();

    public void execute(
            OnSuccess<T> onSuccess,
            OnError onError,
            OnFinished onFinished
    ) {
        disposeLast();
        lastDisposable = buildUseCaseSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(onFinished::onFinished)
                .subscribe(onSuccess::onSuccess, onError::onError);
        compositeDisposable.add(lastDisposable);
    }
}
