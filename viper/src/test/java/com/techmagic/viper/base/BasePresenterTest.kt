package com.techmagic.viper.base

import com.techmagic.viper.Router
import com.techmagic.viper.View
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@RunWith(Enclosed::class)
class BasePresenterTest {

    class ViperBindTests {

        @Mock
        lateinit var view1: View

        @Mock
        lateinit var view2: View

        @Mock
        lateinit var router: Router

        @Before
        fun setup() {
            MockitoAnnotations.initMocks(this)
        }

        @Test
        fun `when a new view is attached the previous one is destroyed`() {
            // given
            val presenter = object : BasePresenter<View, Router>() {}
            BasePresenter.bind(view1, presenter, router)

            // when
            BasePresenter.bind(view2, presenter, router)

            // then
            assertSame(view2, presenter.view)
            verify(view1).destroyView()
            verify(view1).detachViewOutput()
            verify(view2, never()).destroyView()
            verify(view2, never()).detachViewOutput()
        }
    }
}