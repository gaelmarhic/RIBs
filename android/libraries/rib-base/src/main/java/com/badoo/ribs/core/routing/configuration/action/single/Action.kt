package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.routing.transition.TransitionElement

internal interface Action<C : Parcelable> {
    fun onBeforeTransition()
    fun onTransition()
    fun onFinish()
    val transitionElements: List<TransitionElement<C>>
}
