package mx.com.maiktmp.skilltestphndr.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<View> : LifecycleObserver {

    private var view: View? = null
    private val disposable = CompositeDisposable()

    fun attachView(view: View, viewLifecycle: Lifecycle) {
        this.view = view
        viewLifecycle.addObserver(this)
    }

    protected fun view(): View? {
        return view
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onViewDestroyed() {
        view = null
        disposable.dispose()
    }

    protected fun Disposable.autoClear() {
        disposable.add(this)
    }

}