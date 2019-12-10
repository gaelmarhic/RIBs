package com.badoo.ribs.android.recyclerview

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.ribs.core.Builder

class RecyclerViewHostBuilder<T : Parcelable>(
    override val dependency: RecyclerViewHost.Dependency<T>
) : Builder<RecyclerViewHost.Dependency<T>>() {

    fun build(savedInstanceState: Bundle? = null): RecyclerViewHostNode<T> {
        val timeCapsule = AndroidTimeCapsule(savedInstanceState)

        val feature = RecyclerViewHostFeature(
            timeCapsule = timeCapsule,
            initialElements = dependency.initialElements()
        )

        val router = RecyclerViewHostRouter(
            savedInstanceState = savedInstanceState,
            feature = feature,
            ribResolver = dependency.resolver()
        )

        val adapter = Adapter(
            initialEntries = feature.state.items,
//            items = { feature.state.items },
            router = router
        )

        val interactor = RecyclerViewHostInteractor(
            savedInstanceState = savedInstanceState,
            router = router,
            input = dependency.recyclerViewHostInput(),
            feature = feature,
            adapter = adapter
        )

        return RecyclerViewHostNode(
            savedInstanceState = savedInstanceState,
            router = router,
            interactor = interactor,
            viewDeps =  object : RecyclerViewHostView.Dependency {
                override fun router(): RecyclerViewHostRouter<*> = router
                override fun adapter(): Adapter<*> = adapter
                override fun feature(): RecyclerViewHostFeature<*> = feature
            },
            timeCapsule = timeCapsule
        )
    }
}
