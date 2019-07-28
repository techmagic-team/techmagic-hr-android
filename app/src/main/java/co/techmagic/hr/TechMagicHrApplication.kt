package co.techmagic.hr

import android.app.Application
import android.content.Context

import com.crashlytics.android.Crashlytics
import com.google.firebase.FirebaseApp

import co.techmagic.hr.data.manager.impl.NetworkManagerImpl
import co.techmagic.hr.data.repository.TimeReportNetworkRepository
import co.techmagic.hr.data.store.TimeTrackerApi
import co.techmagic.hr.data.store.client.ApiClient
import co.techmagic.hr.device.time_tracker.tracker_service.TimeTrackerDataSource
import co.techmagic.hr.domain.interactor.TimeTrackerInteractor
import co.techmagic.hr.domain.repository.TimeReportRepository
import co.techmagic.hr.presentation.ui.manager.AccountManager
import co.techmagic.hr.presentation.util.LocaleUtil
import co.techmagic.hr.presentation.util.SharedPreferencesUtil
import io.fabric.sdk.android.Fabric
import io.github.eterverda.sntp.SNTP
import io.github.eterverda.sntp.android.AndroidSNTPCacheFactory
import io.github.eterverda.sntp.android.AndroidSNTPClientFactory


class TechMagicHrApplication : Application(), RepositoriesProvider {

    override fun attachBaseContext(base: Context) {
        // Always English language for the app
        super.attachBaseContext(LocaleUtil.onAttach(base, "en"))
    }


    override fun onCreate() {
        super.onCreate()
        NetworkManagerImpl.initNetworkManager(applicationContext)
        SharedPreferencesUtil.init(this)

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }

        FirebaseApp.initializeApp(this)

        SNTP.setClient(AndroidSNTPClientFactory.create())
        SNTP.setCache(AndroidSNTPCacheFactory.create(this))
    }

    override fun provideTimeReportRepository(): TimeReportRepository {
        val okHttpClientClient = ApiClient.buildOkHttpClientClient()
        val retrofit = ApiClient.getRetrofit(okHttpClientClient)
        val timeTrackerApi = retrofit.create(TimeTrackerApi::class.java)
        return TimeReportNetworkRepository(timeTrackerApi, NetworkManagerImpl.getNetworkManager(), AccountManager(applicationContext))
    }

    override fun provideTimeTrackerInteractor(): TimeTrackerInteractor =
            TimeTrackerInteractor(TimeTrackerDataSource(applicationContext))
}

//TODO: setup DI framework
interface RepositoriesProvider {
    fun provideTimeReportRepository(): TimeReportRepository
    fun provideTimeTrackerInteractor(): TimeTrackerInteractor
}