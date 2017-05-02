package com.develop.zuzik.redux.model.permissions

/**
 * User: zuzik
 * Date: 4/30/17
 */
sealed class Permission {
	sealed class Calendar : Permission() {
		class ReadCalendar : Calendar()
		class WriteCalendar : Calendar()
	}

	sealed class Camera : Permission() {
		class Camera : Permission.Camera()
	}

	sealed class Contacts : Permission() {
		class ReadContacts : Contacts()
		class WriteContacts : Contacts()
		class GetAccountsContacts : Contacts()
	}

	sealed class Location : Permission() {
		class AccessFineLocation : Location()
		class AccessCoarseLocation : Location()
	}

	sealed class Microphone : Permission() {
		class RecordAudio : Microphone()
	}

	sealed class Phone : Permission() {
		class ReadPhoneState : Phone()
		class CallPhone : Phone()
		class ReadCallLog : Phone()
		class WriteCallLog : Phone()
		class AddVoiceMail : Phone()
		class UseSIP : Phone()
		class ProcessOutgoingCalls : Phone()
	}

	sealed class Sensors : Permission() {
		class BodySensors : Sensors()
	}

	sealed class SMS : Permission() {
		class SendSMS : SMS()
		class ReceiveSMS : SMS()
		class ReadSMS : SMS()
		class ReceiveWAPPush : SMS()
		class ReceiveMMS : SMS()
	}

	sealed class Storage : Permission() {
		class ReadExternalStorage : Storage()
		class WriteExternalStorage : Storage()
	}

	class Custom(val permission: String) : Permission()
}