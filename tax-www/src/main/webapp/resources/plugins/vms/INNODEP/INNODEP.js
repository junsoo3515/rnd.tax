var PTZ_UP = 1;
var PTZ_DOWN = 7;
var PTZ_LEFT = 3;
var PTZ_RIGHT = 5;
var PTZ_ZOOM_IN = 11;
var PTZ_ZOOM_OUT = 12;
var PTZ_FOCUS_IN = 9;
var PTZ_FOCUS_OUT = 10;

var PTZ_CONTROL_START = 29;
var PTZ_CONTROL_STOP = 30;
var PTZ_CONTROL_END = 31;

var PTZ_PRESET_MOVE = 32;
var PTZ_PRESET_EDIT = 33;
var PTZ_PRESET_DELETE = 34;

var PLAYBACK_PLAY_BACK = -1; 
var PLAYBACK_PLAY_HALF = 5;
var	PLAYBACK_STOP =  0; 
var	PLAYBACK_PLAY = 1;  
var	PLAYBACK_PLAY2 = 2; 
var PLAYBACK_PLAY4 = 3; 
var PLAYBACK_PLAY8 = 4; 
var PLAYBACK_PAUSE = 7; 

/*
var PTZ_LEFT_UP = 0;
var PTZ_RIGHT_UP = 2;
var PTZ_CENTER = 4;
var PTZ_LEFT_DOWN = 6;
var PTZ_RIGHT_DOWN = 8;
var PTZ_AUTOPAN_ON = 13;
var PTZ_AUTOPAN_OFF = 14;
var PTZ_TOURING_START = 15;
var PTZ_TOURING_STOP = 16;
var PTZ_POWER_ON = 17;
var PTZ_POWER_OFF = 18;
var PTZ_WIPER_ON = 19;
var PTZ_WIPER_OFF = 20;
var PTZ_LAMP_ON = 21;
var PTZ_LAMP_OFF = 22;
var PTZ_AUX_1_ON = 23;
var PTZ_AUX_1_OFF = 24;
var PTZ_AUX_2_ON = 25;
var PTZ_AUX_2_OFF = 26;
var PTZ_IRIS_ON = 27;
var PTZ_IRIS_OFF = 28;
var PTZ_SPEED_CHANGE = 35;
var PTZ_OSD_MENU_ON = 36;
var PTZ_OSD_MENU_OFF = 37;
var PTZ_OSD_MENU_ENTER = 38;
var PTZ_OSD_MENU_ESC = 39;
var PTZ_PRESET_HOME = 40;
*/

var isInnodepInit = false;
var isInnodepLogon = false;
var isInnodepCastNetInit = false;
var isInnodepCastNetLogon = false;
var isInnodepPopInit = false;
var isInnodepPopLogon = false;
var innodep = null;
var innodepCastNet = null;
var innodepPop = null;
var enchainPlay = null;

$(function() {
	$(window).unload(function() {
		if (isInnodepInit) {

			destroyVms();
		}
		if (isInnodepCastNetInit) {

			destroyCastNetVms();
		}
		if (isInnodepPopInit) {

			destroyPopVms();
		}
	});
});

function initVms(iWidth, iHeight) {
	var object = '<object id="innodep" classid="clsid:CD3799FB-DFE1-4FA7-AE98-4734FE267BE9" codebase="wnetwebsdk.CAB#version=1,0,0,0" style="width: 100%; height: 100%" width="100%" height="100%"></object>';

	if(!$('#innodep').length) {
		$('#view-vms').html(object);
	}
	innodep = document.getElementById('innodep');
	var result = innodep.CreateInstance(); // success : 1, failure : 0
	if(result == 1) {
		console.log('[VMS] innodep : init');
		isInnodepInit = true;
	}
	else {
		console.log('[VMS] innodep : init failure');
		isInnodepInit = false;
	}

	if(iWidth) {
		$('#view-vms').css('width', iWidth);
	}
	if(iHeight) {
		$('#view-vms').css('height', iHeight);
	}
}

function initCastNetVms(iWidth, iHeight) {
	console.log('[VMS] initInnodepCastnet');
	var result;
	var rtn = 1;
	var object = $('<object/>', {
		id : 'innodepCastNet',
		classid : 'clsid:CD3799FB-DFE1-4FA7-AE98-4734FE267BE9',
		codebase : 'wnetwebsdk.CAB#version=1,0,0,0',
		style : 'width: 100%; height: 100%',
		width : '100%',
		height : '100%'
	});
	
	if(!$('#innodepCastNet').length) {
		$('#view-castNetVms').html(object);
	}
	innodepCastNet = document.getElementById('innodepCastNet');
	
	try {
		result = innodepCastNet.CreateInstance(); // success : 1, failure : 0
	} catch(e) {
		console.log(e.name + ':' + e.message);
		rtn = 0;
	}
	
	if(result == 1) { 
		console.log('[VMS] innodepCastNet : init');
		isInnodepInit = true;
	} else {
		console.log('[VMS] innodepCastNet : init failure');
		isInnodepInit = false;
	}
	
	if(iWidth) {
		$('#view-castNetVms').css('width', iWidth);
	}
	if(iHeight) {
		$('#view-castNetVms').css('height', iHeight);
	}
	return rtn;
}

function destroyVms() {
	if (isInnodepInit) {
		if(innodep != null && typeof innodep != 'undefined') {
			console.log('[VMS] innodep : destroy');
			if(typeof canAudio != 'undefined' && canAudio) { innodep.SoundOnOff(0); }
			if(typeof canMic != 'undefined' && canMic) { innodep.SendAudio(0); }
			
			innodep.SendPTZ(PTZ_CONTROL_END);
			innodep.StopVideoLive();
			innodep.LogOff();
			innodep.Destroy();
			innodep = null;
			mainVms = '';
			isInnodepInit = false;
			isInnodepLogon = false;
		}
		else {
			console.log('[VMS] innodep : is null');
		}
	}
	else {
		console.log('[VMS] innodep : not initialized');
	}
}

function destroyCastNetVms() {
	if (isInnodepCastNetInit) {
		if(innodepCastNet != null && typeof innodepCastNet != 'undefined') {
			console.log('[VMS] innodepCastNet : destroy');
			if(typeof canAudio != 'undefined' && canAudio) { innodepCastNet.SoundOnOff(0); }
			if(typeof canMic != 'undefined' && canMic) { innodepCastNet.SendAudio(0); }

			innodepCastNet.SendPTZ(PTZ_CONTROL_END);
			innodepCastNet.StopVideoLive();
			innodepCastNet.LogOff();
			innodepCastNet.Destroy();
			innodepCastNet = null;
			mainVms = '';
			isInnodepCastNetInit = false;
			isInnodepCastNetLogon = false;
		}
		else {
			console.log('[VMS] innodepCastNet : is null');
		}
	}
	else {
		console.log('[VMS] innodepCastNet : not initialized');
	}
}

function sendPtzPresetInnodepCastNet(nPresetNo, nPtzPresetMode) {
	var result;
	if (nPtzPresetMode == PTZ_PRESET_MOVE) {
		result = innodepCastNet.SendPTZPreset(PTZ_PRESET_MOVE, nPresetNo, 0, 0);
	}
	else if (nPtzPresetMode == PTZ_PRESET_EDIT) {
		result = innodepCastNet.SendPTZPreset(PTZ_PRESET_EDIT, nPresetNo, 0, 0);
	}
	else if (nPtzPresetMode == PTZ_PRESET_DELETE) {
		result = innodepCastNet.SendPTZPreset(PTZ_PRESET_DELETE, nPresetNo, 0, 0);
	}
}

function playVms(cctv_uid) {
	destroyVms();
	if (!isInnodepInit) {
		var chkInit = initVms();
		if (chkInit == 0){
			alert("VMS 초기화 실패! VMS OCX 정상설치여부 확인바랍니다.")
			return false;
		}
		$('#innodep').show();
	}


	setTimeout(function() {
		innodep.DeviceID = cctv_uid;
		innodep.SetLiveScaling(1);
		innodep.SetLabelFont(20);
		innodep.SetViewInfo(3);
		var result = innodep.LogOn(vmsIp, eval(vmsPort), vmsId, vmsPassword, 1);
		if (result == 1) {
			innodep.StartVideoLive();
			//insertViewLog(fcltId, evtOcrNo);
			isInnodepLogon = true;
			console.log('StartVideoLive : ok');
			console.log('==================================================');
		}
		else {
			console.log('StartVideoLive : not logon');
			isInnodepLogon = false;
			console.log('==================================================');
		}
	}, 1000);

	if(vmsPlayTime) {
		enchainPlay = setTimeout(function() {
			innodep.StopVideoLive();
			innodep.LogOff();
			isInnodepLogon = false;
			var flag = confirm('기본 재생시간을 초과했습니다. 계속 재생하시겠습니까?');
			if(!flag) {
				self.opener = self;
				window.close();
			}
			else {
				clearTimeout(enchainPlay);
				enchainPlay = null;
				console.log('clearTimeout');
				playVms();
			}

		}, Number(updatedPlayTime) * 60 * 1000);
		console.log('setTimeout : ' + updatedPlayTime + ' min');
	}
}

function playCastNetVms(cctv) {

	var width = $('view-castNetVms').css('width');
	var result;

	$('#innodepCastNet').css('width', width + 'px');

	if (cctv.length == 1) {
		innodepCastNet.DeviceID = cctv[0].cctv_uid;
	}
	else {
		innodepCastNet.SetVideoSplit(convertViewModeInnodep(cctv.length));
		innodepCastNet.SetLiveScaling(1);
		$.each(cctv, function(i, v) {
			innodepCastNet.SetLiveDeviceInfo(i + 1, v.cctv_uid, 0);
					console.log(" ==== startCastNet >>>> i=" + i + ':'  + v.cctv_uid);
//			insertViewLog(v.fcltId, evt);
		});

		result = innodepCastNet.LogOn(vmsIp, eval(vmsPort), vmsId, vmsPwd, 1);
		if (result == 1) {
			innodepCastNet.SetLabelFont(25);
			innodepCastNet.SetViewInfo(3);
			isInnodepCastNetLogon = true;

			// 프리셋 이동
			$.each(cctv, function(i, v) {
				if (v.preset_num != '0') {
					innodepCastNet.SetOcxSelect(eval(i));
					sendPtzPresetInnodepCastNet(eval(v.preset_num), PTZ_PRESET_MOVE);
					console.log(" ==== startCastNet >>>> i=" + i + ':'  + v.preset_num);
				}
			});

			innodepCastNet.SetOcxSelect(0);
					console.log('innodepCastNet StartVideoLive : ok');
					console.log('==================================================');
		} else {
					console.log('innodepCastNet StartVideoLive : not logon');
			isInnodepCastNetLogon = false;
					console.log('==================================================');
		}
	}
}

function convertViewModeInnodep(n) {
	if 		(1 == n) { return 1; }
	else if (2 == n) { return 2; }
	else if (3 == n) { return 3; }
	else if (4 == n) { return 4; }
	else if (5 == n) { return 5; }
}

///////////////////// 영상팝업 부분
function initPopVms(iWidth, iHeight) {
	var rtn 	= 1;
	var result 	= 1;
	var object = '<object id="innodepPop" classid="clsid:CD3799FB-DFE1-4FA7-AE98-4734FE267BE9" codebase="wnetwebsdk.CAB#version=1,0,0,0" style="width: 100%; height: 100%" width="100%" height="100%"></object>';

	if(!$('#innodepPop').exists()) {
		$('#view-popVms').html(object);
	}
	innodepPop = document.getElementById('innodepPop');
	try {
		result = innodepPop.CreateInstance(); // success : 1, failure : 0
	} catch(e) {
		console.log('**** initInnodep exception ==== ' + e.name + ':' + e.message);
		rtn 	= 0;
		result 	= 0;
	}

	if(result == 1) {
//		console.log('[VMS] innodep : init');
		isInnodepPopInit = true;
	} else {
//		console.log('[VMS] innodep : init failure');
		isInnodepPopInit = false;
	}

	if(iWidth) {
		$('#view-popVms').css('width', iWidth);
	}
	if(iHeight) {
		$('#view-popVms').css('height', iHeight);
	}
	return rtn;
}

function playPopVms(cctv_uid, vmsPlayTime) {
	destroyPopVms();
	if (!isInnodepPopInit) {
		var chkInit = initPopVms();
		if (chkInit == 0){
			alert("VMS 초기화 실패! VMS OCX 정상설치여부 확인바랍니다.")
			return false;
		}
		$('#innodep').show();
	}


	setTimeout(function() {
		innodepPop.DeviceID = cctv_uid;
		innodepPop.SetLiveScaling(1);
		innodepPop.SetLabelFont(20);
		innodepPop.SetViewInfo(3);
		var result = innodepPop.LogOn(vmsIp, eval(vmsPort), vmsId, vmsPassword, 1);
		if (result == 1) {
			innodepPop.StartVideoLive();
			//insertViewLog(fcltId, evtOcrNo);
			isInnodepPopLogon = true;
			console.log('StartVideoLive : ok');
			console.log('==================================================');
		}
		else {
			console.log('StartVideoLive : not logon');
			isInnodepPopLogon = false;
			console.log('==================================================');
		}
	}, 1000);

	if(vmsPlayTime) {
		enchainPlay = setTimeout(function() {
			innodepPop.StopVideoLive();
			innodepPop.LogOff();
			isInnodepPopLogon = false;
			var flag = confirm('기본 재생시간을 초과했습니다. 계속 재생하시겠습니까?');
			if(!flag) {
				self.opener = self;
				window.close();
			}
			else {
				clearTimeout(enchainPlay);
				enchainPlay = null;
				console.log('clearTimeout');
				playPopVms();
			}

		}, Number(vmsPlayTime) * 60 * 1000);
		console.log('setTimeout : ' + vmsPlayTime + ' min');
	}
}

function searchPopVms(cctv_uid) {
	var momentFrom = $('#search-input-from').val();
	var momentTo = $('#search-input-to').val();

	momentFrom = moment(momentFrom, 'YYYY-MM-DD HH:mm:ss');
	momentTo = moment(momentTo, 'YYYY-MM-DD HH:mm:ss');

	if( !momentFrom.isValid() && !momentTo.isValid() ) {
		alert('올바른 날짜 형식이 아닙니다.');
		return false;
	}

	if( momentFrom.valueOf() > momentTo.valueOf() ) {
		alert('시작시간과 끝시간을 확인해 주세요.');
		return false;
	}

	destroyPopVms();
	if (!initPopVms()) {
		initPopVms();
		$('#innodepPop').show();
	}

	setTimeout(function() {
		innodepPop.DeviceID = cctv_uid;
		innodepPop.SetLabelFont(30);
		innodepPop.SetViewInfo(2);
		var result = innodep.LogOn(vmsIp, eval(vmsPort), vmsId, vmsPassword, 0);
		if (result == 1) {
			var mode = $('#search-speed option:selected').val();
			if(mode == 'PLAYBACK_PLAY') innodepPop.StartVideoSearchEx(0, 1, momentFrom.format('YYYYMMDDHHmmss'), momentTo.format('YYYYMMDDHHmmss'));
			else if(mode == 'PLAYBACK_PLAY_BACK') innodepPop.StartVideoSearchEx(0, -1, momentFrom.format('YYYYMMDDHHmmss'), momentTo.format('YYYYMMDDHHmmss'));
			else if(mode == 'PLAYBACK_PLAY_HALF') innodepPop.StartVideoSearchEx(0, 5, momentFrom.format('YYYYMMDDHHmmss'), momentTo.format('YYYYMMDDHHmmss'));
			else if(mode == 'PLAYBACK_PLAY2') innodepPop.StartVideoSearchEx(0, 2, momentFrom.format('YYYYMMDDHHmmss'), momentTo.format('YYYYMMDDHHmmss'));
			else if(mode == 'PLAYBACK_PLAY4') innodepPop.StartVideoSearchEx(0, 4, momentFrom.format('YYYYMMDDHHmmss'), momentTo.format('YYYYMMDDHHmmss'));
			else if(mode == 'PLAYBACK_PLAY8') innodepPop.StartVideoSearchEx(0, 8, momentFrom.format('YYYYMMDDHHmmss'), momentTo.format('YYYYMMDDHHmmss'));
			//insertViewLog(cctv_id, evtOcrNo);
			isInnodepPopLogon = true;
			console.log('momentFrom : ' + momentFrom.format('YYYY-MM-DD HH:mm:ss'));
			console.log('momentTo : ' + momentTo.format('YYYY-MM-DD HH:mm:ss'));
			console.log('StartVideoSearchEx : ok ');
			console.log('==================================================');
		}
		else {
			console.log('StartVideoSearchEx : not logon');
			isInnodepPopLogon = false;
			console.log('==================================================');
		}
	}, 1000);
}

function stopPopVms() {
	innodepPop.StopVideoSearch();
}

function snapShot(sFcltLblNm, sFcltId) {
	if (isInnodepPopInit) {
		var date = new Date();
		var sPath = localImgDir + evtOcrNo + '_' + sFcltId + '_' + moment().format('YYYYMMDDHHmmssSS') + '.jpg';
		var nCompressLevel = 95;
		innodepPop.setOcxSelect(0);
		innodepPop.SaveToJPG(sPath, nCompressLevel);
		alert('캡쳐되었습니다. : ' + sPath);
	}
	else {
		alert('영상 재생 상태를 확인하세요.');
	}
}

function sendAudio() {
	if (canMic) {
		$('#toggle-mic').css('color', 'grey');
		$('#toggle-mic').text(' off ');
		innodepPop.SendAudio(0);
		canMic = false;
	}
	else {
		$('#toggle-mic').css('color', 'green');
		$('#toggle-mic').text(' on ');
		innodepPop.SendAudio(1);
		canMic = true;
	}
}

function playAudio() {
	if (canAudio) {
		$('#toggle-audio').css('color', 'grey');
		$('#toggle-audio').text(' off ');
		innodepPop.SoundOnOff(0);
		canAudio = false;
	}
	else {
		$('#toggle-audio').css('color', 'green');
		$('#toggle-audio').text(' on ');
		innodepPop.SoundOnOff(1);
		canAudio = true;
	}
}

function setPlayMode(number) {
	innodepPop.SetSearchPlayMode(number);
}

function destroyPopVms() {
	if (isInnodepPopInit) {
		if(innodepPop != null && typeof innodepPop != 'undefined') {
//			console.log('[VMS] innodep : destroy');
			if(typeof canAudio != 'undefined' && canAudio) { innodepPop.SoundOnOff(0); }
			if(typeof canMic != 'undefined' && canMic) { innodepPop.SendAudio(0); }

			innodepPop.SendPTZ(PTZ_CONTROL_END);
			innodepPop.StopVideoLive();
			innodepPop.LogOff();
			innodepPop.Destroy();
			innodepPop = null;
			mainVms = '';
			isInnodepPopInit = false;
			isInnodepPopLogon = false;
		}
		else {
//			console.log('[VMS] innodep : is null');
		}
	}
	else {
//		console.log('[VMS] innodep : not initialized');
	}
}