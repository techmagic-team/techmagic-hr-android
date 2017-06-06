package co.techmagic.hr.presentation.ui.activity

import android.os.Bundle
import co.techmagic.hr.R
import co.techmagic.hr.presentation.mvp.presenter.RequestTimeOffPresenter
import co.techmagic.hr.presentation.mvp.view.impl.RequestTimeOffViewImpl

/**
 * Created by Roman Ursu on 6/6/17
 */
class RequestTimeOffActivity : BaseActivity<RequestTimeOffViewImpl, RequestTimeOffPresenter>() {

    private lateinit var requestTimeOffPresenter: RequestTimeOffPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar!!.setHomeButtonEnabled(true)
    }

    override fun onStart() {
        super.onStart()

        presenter.loadData()
    }

    override fun initLayout() {
        setContentView(R.layout.activity_request_timeoff)
    }

    override fun initView(): RequestTimeOffViewImpl {
        return object : RequestTimeOffViewImpl(this, findViewById(android.R.id.content)) {

        }
    }

    override fun initPresenter(): RequestTimeOffPresenter {
        requestTimeOffPresenter = RequestTimeOffPresenter()
        return requestTimeOffPresenter
    }
}