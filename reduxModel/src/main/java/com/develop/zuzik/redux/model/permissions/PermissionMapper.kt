package com.develop.zuzik.redux.model.permissions

import android.Manifest

/**
 * User: zuzik
 * Date: 4/30/17
 */
class PermissionMapper {

	//TODO: api level is different
	private val permissions = mapOf(
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
			Manifest.permission.READ_CALL_LOG to Permission.Phone.ReadCallLog(),
			Manifest.permission.WRITE_CALL_LOG to Permission.Phone.WriteCallLog(),
			Manifest.permission.ADD_VOICEMAIL to Permission.Phone.AddVoiceMail(),
			Manifest.permission.USE_SIP to Permission.Phone.UseSIP(),
			Manifest.permission.PROCESS_OUTGOING_CALLS to Permission.Phone.ProcessOutgoingCalls(),
			Manifest.permission.BODY_SENSORS to Permission.Sensors.BodySensors(),
			Manifest.permission.SEND_SMS to Permission.SMS.SendSMS(),
			Manifest.permission.RECEIVE_SMS to Permission.SMS.ReceiveSMS(),
			Manifest.permission.READ_SMS to Permission.SMS.ReadSMS(),
			Manifest.permission.RECEIVE_WAP_PUSH to Permission.SMS.ReceiveWAPPush(),
			Manifest.permission.RECEIVE_MMS to Permission.SMS.ReceiveMMS(),
			Manifest.permission.READ_EXTERNAL_STORAGE to Permission.Storage.ReadExternalStorage(),
			Manifest.permission.WRITE_EXTERNAL_STORAGE to Permission.Storage.WriteExternalStorage())

	fun mapToString(permission: Permission): String? =
			when (permission) {
				is Permission.Custom -> permission.permission
				else -> permissions.entries.firstOrNull { it.value.javaClass == permission.javaClass }?.key
			}

	fun mapFromString(permission: String): Permission =
			permissions[permission] ?: Permission.Custom(permission)
}