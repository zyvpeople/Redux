package com.develop.zuzik.redux.model.permissions

import android.Manifest
import android.os.Build

/**
 * User: zuzik
 * Date: 4/30/17
 */
class PermissionMapper {

	private val permissions = mutableMapOf(
			Manifest.permission.READ_CALENDAR to Permission.Calendar.ReadCalendar(),
			Manifest.permission.WRITE_CALENDAR to Permission.Calendar.WriteCalendar(),
			Manifest.permission.CAMERA to Permission.Camera.Camera(),
			Manifest.permission.READ_CONTACTS to Permission.Contacts.ReadContacts(),
			Manifest.permission.WRITE_CONTACTS to Permission.Contacts.WriteContacts(),
			Manifest.permission.GET_ACCOUNTS to Permission.Contacts.GetAccountsContacts(),
			Manifest.permission.ACCESS_FINE_LOCATION to Permission.Location.AccessFineLocation(),
			Manifest.permission.ACCESS_COARSE_LOCATION to Permission.Location.AccessCoarseLocation(),
			Manifest.permission.RECORD_AUDIO to Permission.Microphone.RecordAudio(),
			Manifest.permission.READ_PHONE_STATE to Permission.Phone.ReadPhoneState(),
			Manifest.permission.CALL_PHONE to Permission.Phone.CallPhone(),
			Manifest.permission.ADD_VOICEMAIL to Permission.Phone.AddVoiceMail(),
			Manifest.permission.USE_SIP to Permission.Phone.UseSIP(),
			Manifest.permission.PROCESS_OUTGOING_CALLS to Permission.Phone.ProcessOutgoingCalls(),
			Manifest.permission.SEND_SMS to Permission.SMS.SendSMS(),
			Manifest.permission.RECEIVE_SMS to Permission.SMS.ReceiveSMS(),
			Manifest.permission.READ_SMS to Permission.SMS.ReadSMS(),
			Manifest.permission.RECEIVE_WAP_PUSH to Permission.SMS.ReceiveWAPPush(),
			Manifest.permission.RECEIVE_MMS to Permission.SMS.ReceiveMMS(),
			Manifest.permission.WRITE_EXTERNAL_STORAGE to Permission.Storage.WriteExternalStorage())
			.apply {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					put(Manifest.permission.READ_CALL_LOG, Permission.Phone.ReadCallLog())
					put(Manifest.permission.WRITE_CALL_LOG, Permission.Phone.WriteCallLog())
					put(Manifest.permission.READ_EXTERNAL_STORAGE, Permission.Storage.ReadExternalStorage())
				}
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
					put(Manifest.permission.BODY_SENSORS, Permission.Sensors.BodySensors())
				}
			}
			.toMap()

	fun mapToString(permission: Permission): String? =
			when (permission) {
				is Permission.Custom -> permission.permission
				else -> permissions.entries.firstOrNull { it.value.javaClass == permission.javaClass }?.key
			}

	fun mapFromString(permission: String): Permission =
			permissions[permission] ?: Permission.Custom(permission)
}