package co.techmagic.hr.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import co.techmagic.hr.R
import co.techmagic.hr.presentation.ui.ProfileTypes

class EmployeeDetailsActivity : AppCompatActivity() {

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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)
    }
}
