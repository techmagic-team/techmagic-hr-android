package co.techmagic.hr.presentation.ui.activity

import android.os.Bundle
import android.support.v7.app.ActionBar
import co.techmagic.hr.R
import co.techmagic.hr.presentation.mvp.presenter.RequestTimeOffPresenter
import co.techmagic.hr.presentation.mvp.view.impl.RequestTimeOffViewImpl
import org.jetbrains.anko.toast

/**
 * Created by Roman Ursu on 6/6/17
 */
class RequestTimeOffActivity : BaseActivity<RequestTimeOffViewImpl, RequestTimeOffPresenter>() {

    private lateinit var requestTimeOffPresenter: RequestTimeOffPresenter
    private var actionBar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar = supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.title = "Request Time off"
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
            override fun showData() {
                toast("Data is ready")
            }
        }
    }

    override fun initPresenter(): RequestTimeOffPresenter {
        requestTimeOffPresenter = RequestTimeOffPresenter()
        return requestTimeOffPresenter
    }
}