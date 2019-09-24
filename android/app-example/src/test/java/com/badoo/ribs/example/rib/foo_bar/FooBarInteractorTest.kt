package com.badoo.ribs.example.rib.foo_bar

import com.badoo.common.ribs.RibTestHelper
import com.badoo.ribs.android.PermissionRequester
import com.badoo.ribs.example.rib.foo_bar.FooBarView.Event.CheckPermissionsButtonClicked
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable.empty
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Before
import org.junit.Test

class FooBarInteractorTest {

    private val router: FooBarRouter = mock()
    private val permissionRequester: PermissionRequester = mock<PermissionRequester>().apply {
        whenever(this.events(any())).doReturn(empty())
    }

    private val foobarViewEventEmitter = PublishRelay.create<FooBarView.Event>()
    private val dummyViewInput = PublishRelay.create<FooBarView.ViewModel>()

    private lateinit var interactor: FooBarInteractor
    private lateinit var nodeHelper: RibTestHelper<FooBarView>

    @Before
    fun setup() {
        interactor = FooBarInteractor(
            savedInstanceState = null,
            router = router,
            permissionRequester = permissionRequester,
            dummyViewInput = dummyViewInput
        )

        nodeHelper = RibTestHelper.create(interactor, foobarViewEventEmitter, router)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `check permission when check permission button clicked`() {
        val inputSubscriber = dummyViewInput.test()
        val result = PermissionRequester.CheckPermissionsResult(emptyList(), emptyList(), emptyList())
        whenever(permissionRequester.checkPermissions(any(), any())).doReturn(result)

        nodeHelper.onStarted {
            foobarViewEventEmitter.accept(CheckPermissionsButtonClicked)

            inputSubscriber.assertLast(FooBarView.ViewModel(result.toString()))
        }
    }

    @Test
    fun `does not check permission when check permission button clicked event came after onStop`() {
        val inputSubscriber = dummyViewInput.test()
        val result = PermissionRequester.CheckPermissionsResult(emptyList(), emptyList(), emptyList())
        whenever(permissionRequester.checkPermissions(any(), any())).doReturn(result)

        nodeHelper.onStarted {}

        foobarViewEventEmitter.accept(CheckPermissionsButtonClicked)
        inputSubscriber.assertLastPredicate { it.text != result.toString() }
    }

    /**
     * TODO: Add real tests.
     */
    @Test
    fun `an example test with some conditions should pass`() {
    }
}

private fun <T> TestObserver<T>.assertLast(value: T) {
    assertValueAt(valueCount() - 1, value)
}

private fun <T> TestObserver<T>.assertLastPredicate(predicate: (T) -> Boolean) {
    assertValueAt(valueCount() - 1, predicate)
}

