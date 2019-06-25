package co.techmagic.hr.presentation.ui.manager

import android.content.Context
import co.techmagic.hr.data.entity.User
import co.techmagic.hr.data.entity.time_tracker.ProjectResponse
import co.techmagic.hr.data.entity.time_tracker.TaskResponse
import co.techmagic.hr.presentation.util.SharedPreferencesUtil

class AccountManager(private val context: Context) {

    fun saveUser(user: User) {
        SharedPreferencesUtil.saveAccessToken(user.accessToken)
        SharedPreferencesUtil.saveUser(user)
    }

    fun saveLastSelectedProject(projectResponse: ProjectResponse?) = SharedPreferencesUtil.saveLastSelectedProject(projectResponse)


    fun getLastSelectedProject(): ProjectResponse? = SharedPreferencesUtil.getLastSelectedProject()

    fun saveLastSelectedTask(taskResponse: TaskResponse?) = SharedPreferencesUtil.saveLastSelectedTask(taskResponse)

    fun getLastSelectedTask(): TaskResponse? = SharedPreferencesUtil.getLastSelectedTask()
}