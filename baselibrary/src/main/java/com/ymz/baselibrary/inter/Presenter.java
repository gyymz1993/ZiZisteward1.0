package com.ymz.baselibrary.inter;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}
