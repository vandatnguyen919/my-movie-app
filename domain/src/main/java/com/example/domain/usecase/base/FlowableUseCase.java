package com.example.domain.usecase.base;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class FlowableUseCase<T> extends UseCase{

    protected abstract Flowable<T> buildUseCaseFlowable();

    public void execute(
            OnSuccess<T> onSuccess,
            OnError onError,
            OnFinished onFinished
    ) {
        disposeLast();
        lastDisposable = buildUseCaseFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(onFinished::onFinished)
                .subscribe(onSuccess::onSuccess, onError::onError);

        compositeDisposable.add(lastDisposable);
    }
}
