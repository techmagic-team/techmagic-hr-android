package co.techmagic.hr.data.store;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import co.techmagic.hr.data.db.UserEntry;
import io.realm.Realm;

/**
 * Responsible for saving of users into db
 */

public class UserStorage {


    public static void saveUsers(@NonNull final List<UserEntry> usersList) {
        executeTransaction(realm -> realm.copyToRealmOrUpdate(usersList));
    }


    private static void executeTransaction(@NonNull Realm.Transaction transaction) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(transaction);
        } catch (Throwable e) {
            Log.e("executeTransaction", e.getMessage());
        } finally {
            close(realm);
        }
    }


    private static void close(@Nullable Realm realm) {
        if (realm != null) {
            realm.close();
        }
    }
}