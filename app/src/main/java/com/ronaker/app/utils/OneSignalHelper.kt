package com.onesignal

import android.database.Cursor
import com.onesignal.OneSignalDbContract.NotificationTable
import com.ronaker.app.utils.AppDebug

object OneSignalHelper {


    /**
     * Get all OneSignal notifications from the Notification Shade.  OneSignal notifications will be restored when
     * your app is restarted.
     */
    fun getOneSignalNotifications(): List<Notifications> {

        val notifications = ArrayList<Notifications>()

        val dbHelper = OneSignalDbHelper.getInstance(OneSignal.appContext)
        var cursor: Cursor? = null
        try {
            val readableDb = dbHelper.sqLiteDatabaseWithRetries
            val retColumn = arrayOf(
                NotificationTable.COLUMN_NAME_NOTIFICATION_ID,
                NotificationTable.COLUMN_NAME_ANDROID_NOTIFICATION_ID,
                NotificationTable.COLUMN_NAME_GROUP_ID,
                NotificationTable.COLUMN_NAME_COLLAPSE_ID,
                NotificationTable.COLUMN_NAME_IS_SUMMARY,
                NotificationTable.COLUMN_NAME_OPENED,
                NotificationTable.COLUMN_NAME_DISMISSED,
                NotificationTable.COLUMN_NAME_TITLE,
                NotificationTable.COLUMN_NAME_MESSAGE,
                NotificationTable.COLUMN_NAME_FULL_DATA,
                NotificationTable.COLUMN_NAME_CREATED_TIME,
                NotificationTable.COLUMN_NAME_EXPIRE_TIME


            )
            cursor = readableDb.query(
                NotificationTable.TABLE_NAME,
                retColumn,
                null,
                null,
                null,  // group by
                null,  // filter by row groups
                null // sort order
            )
            if (cursor.moveToFirst()) {
                do {
                    cursor.toString()


                    AppDebug.log("OneSignalHelper", "cursor: $cursor")
                    cursor.toNotifications()?.let {


                        notifications.add(it)
                    }


                } while (cursor.moveToNext())
            }


        } catch (t: Throwable) {
            AppDebug.log("OneSignalHelper", "Error get all notifications! ", t)
        } finally {
            cursor?.close()
        }
        return notifications
    }


    data class Notifications(
        val NOTIFICATION_ID: String?,

        val ANDROID_NOTIFICATION_ID: Int?,
        val GROUP_ID: String?,
        val COLLAPSE_ID: String?,
        val IS_SUMMARY: Int?,
        val OPENED: Int?,
        val DISMISSED: Int?,
        val TITLE: String?,
        val MESSAGE: String?,
        val FULL_DATA: String?,
        val CREATED_TIME: Long?,
        val EXPIRE_TIME: Long?,
    ) {


    }

    private fun Cursor.toNotifications(): Notifications? {
        return try {
            Notifications(
                NOTIFICATION_ID = this.getString(this.getColumnIndex(NotificationTable.COLUMN_NAME_NOTIFICATION_ID)),
                ANDROID_NOTIFICATION_ID = this.getInt(this.getColumnIndex(NotificationTable.COLUMN_NAME_ANDROID_NOTIFICATION_ID)),
                GROUP_ID = this.getString(this.getColumnIndex(NotificationTable.COLUMN_NAME_GROUP_ID)),
                COLLAPSE_ID = this.getString(this.getColumnIndex(NotificationTable.COLUMN_NAME_COLLAPSE_ID)),
                IS_SUMMARY = this.getInt(this.getColumnIndex(NotificationTable.COLUMN_NAME_IS_SUMMARY)),
                OPENED = this.getInt(this.getColumnIndex(NotificationTable.COLUMN_NAME_OPENED)),
                DISMISSED = this.getInt(this.getColumnIndex(NotificationTable.COLUMN_NAME_DISMISSED)),
                TITLE = this.getString(this.getColumnIndex(NotificationTable.COLUMN_NAME_TITLE)),
                MESSAGE = this.getString(this.getColumnIndex(NotificationTable.COLUMN_NAME_MESSAGE)),
                FULL_DATA = this.getString(this.getColumnIndex(NotificationTable.COLUMN_NAME_FULL_DATA)),
                CREATED_TIME = this.getLong(this.getColumnIndex(NotificationTable.COLUMN_NAME_CREATED_TIME)),
                EXPIRE_TIME = this.getLong(this.getColumnIndex(NotificationTable.COLUMN_NAME_EXPIRE_TIME))


            )

        } catch (ex: Exception) {
            AppDebug.log("OneSignalHelper", "Cursor.toNotifications", ex)
            null
        }

    }


}