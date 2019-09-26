package co.techmagic.hr.presentation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import co.techmagic.hr.data.entity.User;
import co.techmagic.hr.data.entity.time_report.ProjectResponse;
import co.techmagic.hr.data.entity.time_report.TaskResponse;

import static co.techmagic.hr.presentation.util.SharedPreferencesUtil.SharedPreferencesKeys.SHARED_PREFS_KEY_VERSION;

// Use AccountManager or move needed logic to it
@Deprecated
public class SharedPreferencesUtil {

    private static final int SHARED_PREFERENCES_VERSION = 1;

    private static SharedPreferences prefs;
    private static Gson gson = new Gson();

    private SharedPreferencesUtil() {
    }


    public static void init(@NonNull Context appContext) {
        if (prefs == null) {
            prefs = appContext.getSharedPreferences(SharedPreferencesKeys.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            migrate();
        }
    }


    public static void saveAccessToken(String token) {
        saveAccessTokenLength(token.length());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferencesKeys.ACCESS_TOKEN_KEY, token);
        editor.apply();
    }


    public static String getAccessToken() {
        return prefs.getString(SharedPreferencesKeys.ACCESS_TOKEN_KEY, null);
    }


    public static void saveUser(final User user) {
        String u = gson.toJson(user);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferencesKeys.LOGGED_ID_USER_KEY, u);
        editor.apply();
    }


    public static User readUser() {
        final String u = prefs.getString(SharedPreferencesKeys.LOGGED_ID_USER_KEY, null);
        return gson.fromJson(u, User.class);
    }


    public static void saveSelectedDepartmentId(String depId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferencesKeys.SELECTED_DEPARTMENT_ID_KEY, depId);
        editor.apply();
    }


    public static String getSelectedDepartmentId() {
        return prefs.getString(SharedPreferencesKeys.SELECTED_DEPARTMENT_ID_KEY, null);
    }


    public static void saveSelectedLeadId(String leadId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferencesKeys.SELECTED_LEAD_ID_KEY, leadId);
        editor.apply();
    }


    public static String getSelectedLeadId() {
        return prefs.getString(SharedPreferencesKeys.SELECTED_LEAD_ID_KEY, null);
    }


    public static void saveSelectedProjectId(String projectId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferencesKeys.SELECTED_PROJECT_ID_KEY, projectId);
        editor.apply();
    }


    public static String getSelectedProjectId() {
        return prefs.getString(SharedPreferencesKeys.SELECTED_PROJECT_ID_KEY, null);
    }


    public static void clearPreferences() {
        prefs.edit().clear().apply();
    }


    public static void saveMyTeamSelection(boolean isMyTeamSelected) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(SharedPreferencesKeys.CALENDAR_FILTERS_SELECTED_MY_TEAM_KEY, isMyTeamSelected);
        editor.apply();
    }


    public static boolean getMyTeamSelection() {
        return prefs.getBoolean(SharedPreferencesKeys.CALENDAR_FILTERS_SELECTED_MY_TEAM_KEY, true); // selected by default
    }


    public static void saveSelectedFromTime(long timeInMillis) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(SharedPreferencesKeys.CALENDAR_FILTERS_SELECTED_FROM_KEY, timeInMillis);
        editor.apply();
    }


    public static long getSelectedFromTime() {
        return prefs.getLong(SharedPreferencesKeys.CALENDAR_FILTERS_SELECTED_FROM_KEY, 0);
    }


    public static void saveSelectedToTime(long timeInMillis) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(SharedPreferencesKeys.CALENDAR_FILTERS_SELECTED_TO_KEY, timeInMillis);
        editor.apply();
    }


    public static long getSelectedToTime() {
        return prefs.getLong(SharedPreferencesKeys.CALENDAR_FILTERS_SELECTED_TO_KEY, 0);
    }


    public static void saveSelectedCalendarDepartmentId(String calDepId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferencesKeys.CALENDAR_FILTERS_SELECTED_DEPARTMENT_ID_KEY, calDepId);
        editor.apply();
    }


    public static String getSelectedCalendarDepartmentId() {
        return prefs.getString(SharedPreferencesKeys.CALENDAR_FILTERS_SELECTED_DEPARTMENT_ID_KEY, null);
    }


    public static void saveSelectedCalendarProjectId(String projectId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferencesKeys.CALENDAR_FILTERS_SELECTED_PROJECT_ID_KEY, projectId);
        editor.apply();
    }


    public static String getSelectedCalendarProjectId() {
        return prefs.getString(SharedPreferencesKeys.CALENDAR_FILTERS_SELECTED_PROJECT_ID_KEY, null);
    }


    private static void saveAccessTokenLength(int tokenLength) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SharedPreferencesKeys.ACCESS_TOKEN_KEY_LENGTH, tokenLength);
        editor.apply();
    }


    private static int getAccessTokenLength() {
        return prefs.getInt(SharedPreferencesKeys.ACCESS_TOKEN_KEY_LENGTH, 0);
    }

    public static void saveLastSelectedProject(ProjectResponse projectResponse) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferencesKeys.LAST_SELECTED_PROJECT, gson.toJson(projectResponse));
        editor.apply();
    }

    @Nullable
    public static ProjectResponse getLastSelectedProject() {
        return gson.fromJson(prefs.getString(SharedPreferencesKeys.LAST_SELECTED_PROJECT, ""), ProjectResponse.class);
    }

    public static void saveLastSelectedTask(TaskResponse taskResponse) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPreferencesKeys.LAST_SELECTED_PROJECT_TASK, gson.toJson(taskResponse));
        editor.apply();
    }

    @Nullable
    public static TaskResponse getLastSelectedTask() {
        return gson.fromJson(prefs.getString(SharedPreferencesKeys.LAST_SELECTED_PROJECT_TASK, ""), TaskResponse.class);
    }

    private static int getSharedPreferencesVersion() {
        return prefs.getInt(SHARED_PREFS_KEY_VERSION, 0);
    }

    private static void setSharedPrefsVersion(int version) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SHARED_PREFS_KEY_VERSION, version);
        editor.apply();
    }

    private static void migrate() {
        int previousVersion = getSharedPreferencesVersion();

        if (previousVersion == SHARED_PREFERENCES_VERSION) {
            return;
        }

        if (previousVersion == 0) {
            clearPreferences();
            previousVersion++;
        }

        //put your migration changes here

        setSharedPrefsVersion(SHARED_PREFERENCES_VERSION);
    }


    interface SharedPreferencesKeys {
        String SHARED_PREFS_NAME = "appPrefs";
        String SHARED_PREFS_KEY_VERSION = "share_prefs_key_version";
        String ACCESS_TOKEN_KEY = "access_token_key";
        String ACCESS_TOKEN_KEY_LENGTH = "access_token_key_length";
        String LOGGED_ID_USER_KEY = "logged_in_user_key";
        String SELECTED_DEPARTMENT_ID_KEY = "selected_department_id_key";
        String SELECTED_LEAD_ID_KEY = "selected_lead_id_key";
        String SELECTED_PROJECT_ID_KEY = "selected_project_id_key";
        String CALENDAR_FILTERS_SELECTED_MY_TEAM_KEY = "calendar_filters_selected_my_team_key";
        String CALENDAR_FILTERS_SELECTED_FROM_KEY = "calendar_filters_selected_from_key";
        String CALENDAR_FILTERS_SELECTED_TO_KEY = "calendar_filters_selected_to_key";
        String CALENDAR_FILTERS_SELECTED_DEPARTMENT_ID_KEY = "calendar_filters_selected_department_id_key";
        String CALENDAR_FILTERS_SELECTED_PROJECT_ID_KEY = "calendar_filters_selected_project_id_key";
        String LAST_SELECTED_PROJECT = "time_reports_last_selected_project";
        String LAST_SELECTED_PROJECT_TASK = "time_reports_last_selected_project_task";
    }
}