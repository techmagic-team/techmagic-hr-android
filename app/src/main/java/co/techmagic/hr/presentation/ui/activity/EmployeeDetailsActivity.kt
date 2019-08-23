package co.techmagic.hr.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import co.techmagic.hr.R
import co.techmagic.hr.presentation.ui.ProfileTypes
import co.techmagic.hr.presentation.ui.fragment.DetailsFragment
import co.techmagic.hr.presentation.ui.view.ActionBarChangeListener
import co.techmagic.hr.presentation.ui.view.ChangeBottomTabListener

class EmployeeDetailsActivity : AppCompatActivity(), ActionBarChangeListener, ChangeBottomTabListener {

    companion object {
        private const val EXTRA_USER_ID = "user_id_param"
        private const val EXTRA_PROFILE_TYPE = "profile_type_param"

        fun start(context: Context, userId: String, profileType: ProfileTypes) {
            context.startActivity(Intent(context, EmployeeDetailsActivity::class.java)
                    .also {
                        it.putExtra(EXTRA_USER_ID, userId)
                        it.putExtra(EXTRA_PROFILE_TYPE, profileType)
                    }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val userId = intent.getStringExtra(EXTRA_USER_ID)
        val profileType = intent.getSerializableExtra(EXTRA_PROFILE_TYPE) as ProfileTypes

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        if (savedInstanceState == null) {
            supportFragmentManager!!.beginTransaction()
                    .add(R.id.content, DetailsFragment.newInstance(userId, profileType))
                    .commitNowAllowingStateLoss()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    @Deprecated("Implements ChangeBottomTabListener only to prevent crash due to fragment implementation requires it")
    override fun allowBottomTabClick() {
        /* no-op */
    }

    @Deprecated("Implements ChangeBottomTabListener only to prevent crash due to fragment implementation requires it")
    override fun disableBottomTabClick() {
        /* no-op */
    }
}
