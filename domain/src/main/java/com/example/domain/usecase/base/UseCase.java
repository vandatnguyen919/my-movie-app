package com.example.domain.usecase.base;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class UseCase {

    protected Disposable lastDisposable;
    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void disposeLast() {
        if (lastDisposable != null && !lastDisposable.isDisposed()) {
            lastDisposable.dispose();
        }
    }

    public void dispose() {
        compositeDisposable.clear();
    }

    public interface OnSuccess<T> {
        void onSuccess(T t);
    }

    public interface OnError {
        void onError(Throwable t);
    }

    public interface OnFinished {
        void onFinished();
    }
}
