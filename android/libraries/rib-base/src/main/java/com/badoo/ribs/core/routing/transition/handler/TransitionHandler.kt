package com.badoo.ribs.core.routing.transition.handler

import com.badoo.ribs.core.routing.transition.TransitionElement
import com.badoo.ribs.core.routing.transition.Transition
import com.badoo.ribs.core.routing.transition.TransitionPair


interface TransitionHandler<C> {

    fun onTransition(elements: List<TransitionElement<out C>>): TransitionPair

    companion object {
        fun <C> multiple(vararg transitionhandlers: TransitionHandler<C>) =
            Multiple(transitionhandlers.toList())
    }

    class Multiple<C>(
        private val transitionhandlers: List<TransitionHandler<C>>
    ) : TransitionHandler<C> {
        private val handlers: List<TransitionHandler<C>>

        init {
            handlers = moveSharedElementToBeginningOfList()
        }

        private fun moveSharedElementToBeginningOfList(): List<TransitionHandler<C>> {
            val list = transitionhandlers.toMutableList()
            val idx = list.indexOfFirst { it is SharedElements<*> }
            if (idx > 0) {
                val removed = list.removeAt(idx)
                list.add(0, removed)
            }
            return list
        }

        override fun onTransition(elements: List<TransitionElement<out C>>): TransitionPair {
            val pairs = handlers.map { it.onTransition(elements) }

            return TransitionPair(
                exiting = Transition.multiple ( pairs.map { it.exiting } ),
                entering = Transition.multiple ( pairs.map { it.entering } )
            )
        }


    }
}

