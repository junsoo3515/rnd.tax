var isVurixInit = false;
var isVurixCastnetInit = [false, false, false, false, false];
var isVurixPopInit = false;
var vurix = null;
var isVurixPlaying = false;
var vurixCastnet = [null, null, null, null, null];
var isVurixCastnetPlaying = [false, false, false, false, false];
var vurixPop = null;
var isVurixPopPlaying = false;
var isVurixPopPausing = false;
var enchainPlay = null;
var interval = 0;

$(function () {
    $(window).unload(function () {
        if (isVurixInit) {

            destroyVms();
        }
        if (isVurixCastnetInit.indexOf(true) != -1) {

            destroyCastNetVms();
        }
        if (isVurixPopInit) {

            destroyPopVms();
        }
    });
});

// 영상 표출용 Object 태그 생성(실시간 감시)
function initVms(iWidth, iHeight) {
    var rtn = 1;
    var object = '<object classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921" codebase="http://download.videolan.org/pub/videolan/vlc/2.2.8/win32/vlc-2.2.8-win32.exe" id="vurix" name="vurix">' +
        '<param name="autostart" value="false" />' +
        '<param name="allowfullscreen" value="false" />' +
        '<param name="controls" value="false" />' +
        '<embed name="vurix" pluginspage="http://www.videolan.org" type="application/x-vlc-plugin" controls="false" allowfullscreen="false" autostart="false"/>' +
        '</object>';

    if (!$('#vurix').length > 0) {
        $('#view-vms').html(object);
    }

    $('#vurix').css({
        width: '100%',
        height: '100%'
    });
    try {
        vurix = $('#vurix')[0];
        vurix.VersionInfo != undefined ? rtn = 1 : rtn = 0;
    } catch (e) {
        console.log('**** initVurix exception ==== ' + e.name + ':' + e.message);
        rtn = 0;
    }

    if (rtn == 1) {
        console.log('[VMS] VURIX : init');
        isVurixInit = true;
    } else {
        console.log('[VMS] VURIX : init failure');
        isVurixInit = false;
    }

    if (iWidth) {
        $('#view-vms').css('width', iWidth);
    }
    if (iHeight) {
        $('#view-vms').css('height', iHeight);
    }
    return rtn;
}

// 영상 표출용 Object 태그 삭제(실시간 감시)
function destroyVms() {
    try {
        if (isVurixInit) {

            if (vurix != null && typeof vurix != 'undefined') {

                console.log('[VMS] VURIX : destroy');

                if (typeof canAudio != 'undefined' && canAudio) {

                    vurix.audio.mute = true;
                }

                vurix.playlist.stop();
                vurix.playlist.items.clear();

                vurix = null;
                isVurixInit = false;
                isVurixPlaying = false;
            }
        }

        $('#vurix').remove();
    } catch (e) {

        console.log(e);
        common.setOSXModal('VLC를 지원하지 않는 브라우저거나, VLC가 정상 설치되어있지 않습니다.');
    }
}

// 영상 표출용 Object 태그 생성(주변 영상 5개)
function initCastNetVms(cctvCnt, iWidth, iHeight) {
    var rtn = [1, 1, 1, 1, 1];
    var object;
    var chkInit = 1;

    var viewCastNetVms = $('#view-castNetVms');

    for (var i = 0; i < cctvCnt; i++) {

        viewCastNetVms.append('<div id="view-vms' + i + '" style="width : 20%; height : 100%; display: inline-block;"/>');

        object = '<object classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921" codebase="http://download.videolan.org/pub/videolan/vlc/2.2.8/win32/vlc-2.2.8-win32.exe" id="vurix' + i + '" name="vurix' + i + '">' +
        '<param name="autostart" value="false" />' +
        '<param name="allowfullscreen" value="false" />' +
        '<param name="controls" value="false" />' +
        '<embed name="vurix' + i + '" pluginspage="http://www.videolan.org" type="application/x-vlc-plugin" controls="false" allowfullscreen="false" autostart="false"/>' +
        '</object>';

        if (!$('#vurix' + i).length > 0) {

            $('#view-vms' + i).html(object);
        }

        $('#vurix' + i).css({
            width: '100%',
            height: '100%'
        });

        try {

            vurixCastnet[i] = $('#vurix' + i)[0];
            vurixCastnet[i].VersionInfo != undefined ? rtn[i] = 1 : rtn[i] = 0;
        } catch (e) {

            console.log(e.name + ':' + e.message);
            rtn[i] = 0;
        }

        if (rtn[i] == 1) {

            console.log('[VMS] VURIX : init');
            isVurixCastnetInit[i] = true;
        } else {

            console.log('[VMS] VURIX : init failure');
            isVurixCastnetInit[i] = false;
        }
    }

    rtn.indexOf(1) != -1 ? chkInit = 1 : chkInit = 0;

    return chkInit;
}

// 영상 표출용 Object 태그 삭제(주변 영상 5개)
function destroyCastNetVms() {
    try {

        for (var i = 0; i < vurixCastnet.length; i++) {

            if (isVurixCastnetInit[i] != false) {

                if (vurixCastnet[i] != null && isVurixCastnetPlaying[i] != false) {

                    console.log('[VMS] VURIX : destroy');

                    if (vurixCastnet[i] != null) {

                        if (vurixCastnet[i].VersionInfo != undefined && vurixCastnet[i].playlist.isPlaying) {

                            vurixCastnet[i].playlist.stop();
                            vurixCastnet[i].playlist.items.clear();
                        }

                        vurixCastnet[i] = null;
                        isVurixCastnetPlaying[i] = false;
                        isVurixCastnetInit[i] = false;
                    }
                }
            }

            $('#vurix' + i).remove();
        }
    } catch (e) {

        console.log(e);
        common.setOSXModal('VLC를 지원하지 않는 브라우저거나, VLC가 정상 설치되어있지 않습니다.');
    }
}

// 실시간 감시 이벤트 발생 시 발생 CCTV 영상 재생
function playVms(cctv_uid, cctv_id) {

    var rtspUrl;

    destroyPopVms();

    if (!isVurixInit) {

        var chkInit = initVms();

        if (chkInit == 0) {

            alert('VLC를 지원하지 않는 브라우저거나, VLC가 정상 설치되어있지 않습니다.');

            return false;
        }
    }

    var evt = 'NO_EVT_OCR_NO';

    if (typeof triSeq != 'undefined' && triSeq != '') {

        evt = triSeq;
    }

    if(evt != 'NO_EVT_OCR_NO') {

        rtspUrl = getRtspUrl('realtime', evt, cctv_id, cctv_nm, 'S');

        if(rtspUrl != null && rtspUrl.url != null) {

            setTimeout(function () {

                vurix.playlist.add(rtspUrl.url);
                vurix.playlist.play();
                isVurixPlaying = true;
            }, 1000);
        }
    }
}

// 투망감시 설정 된 CCTV 영상 재생
function playCastNetVms(cctv, ocrCctvId) {

    var castNetRtspUrl = {};

    try {

        var evt = 'NO_EVT_OCR_NO';

        if (typeof triSeq != 'undefined' && triSeq != '') {

            evt = triSeq;
        }

        if(evt != 'NO_EVT_OCR_NO') {

            // VURIX rtsp url 가져오기
            castNetRtspUrl = getCastNetRtspUrl(ocrCctvId);

            if(castNetRtspUrl != null && castNetRtspUrl.length > 0) {

                if (cctv.length == 1) {

                    //vurixCastnet[0].playlist.add(castNetRtspUrl[0].stream);
                    vurixCastnet[0].playlist.add(castNetRtspUrl[0].stream);
                    vurixCastnet[0].playlist.play();
                    isVurixCastnetPlaying[0] = true;
                }
                else {

                    $.each(cctv, function (i, v) {
                        //vurixCastnet[i].playlist.add(castNetRtspUrl[i].stream);
                        vurixCastnet[i].playlist.add(castNetRtspUrl[i].stream);
                        vurixCastnet[i].playlist.play();
                        isVurixCastnetPlaying[i] = true;
                    });
                }
            }
        }
    } catch (e) {
        console.log('**** 영상재생실패 ==== ' + e.name + ':' + e.message);
    }
}

///////////////////// 영상팝업 부분

// 영상 표출용 Object 태그 생성(영상 팝업)
function initPopVms(iWidth, iHeight) {

    var rtn = 1;
    var object = '<object classid="clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921" codebase="http://download.videolan.org/pub/videolan/vlc/2.2.8/win32/vlc-2.2.8-win32.exe" id="vurixPop" name="vurixPop">' +
        '<param name="autostart" value="false" />' +
        '<param name="allowfullscreen" value="false" />' +
        '<param name="controls" value="false" />' +
        '<embed name="vurixPop" pluginspage="http://www.videolan.org" type="application/x-vlc-plugin" controls="false" allowfullscreen="false" autostart="false"/>' +
        '</object>';

    if (!$('#vurixPop').length > 0) {

        $('#view-popVms').html(object);
    }

    $('#vurixPop').css({
        width: '100%',
        height: '100%'
    });

    try {

        vurixPop = $('#vurixPop')[0];
        vurixPop.VersionInfo != undefined ? rtn = 1 : rtn = 0;
    } catch (e) {

        console.log('**** initVurixPop exception ==== ' + e.name + ':' + e.message);
        rtn = 0;
    }

    if (rtn == 1) {

        console.log('[VMS] VURIX : init');
        isVurixPopInit = true;
    } else {

        console.log('[VMS] VURIX : init failure');
        isVurixPopInit = false;
    }

    if (iWidth) {
        $('#view-popVms').css('width', iWidth);
    }
    if (iHeight) {
        $('#view-popVms').css('height', iHeight);
    }
    return rtn;
}

// 현재 CCTV 영상 재생
function playPopVms(cctv_uid, cctv_id, vmsPlayTime) {

    var rtspUrl;

    isVurixPopPausing = false;

    destroyPopVms();

    if (!isVurixPopInit) {

        var chkInit = initPopVms();

        if (chkInit == 0) {

            alert('VLC를 지원하지 않는 브라우저거나, VLC가 정상 설치되어있지 않습니다.');

            return false;
        }
    }

    var evt = 'NO_EVT_OCR_NO';

    if (typeof tri_seq != 'undefined' && tri_seq != '') {

        evt = tri_seq;
    }

    if(evt != 'NO_EVT_OCR_NO') {

        rtspUrl = getRtspUrl('current', tri_seq, cctv_id, cctv_nm, 'L');

        if(rtspUrl != null && rtspUrl.url != null) {

            setTimeout(function () {

                vurix.playlist.add(rtspUrl.url);
                vurix.playlist.play();

                isVurixPlaying = true;
            }, 1000);

            if (vmsPlayTime) {

                enchainPlay = setTimeout(function () {

                    vurix.playlist.stop();
                    isVurixPlaying = false;

                    var flag = confirm('기본 재생시간을 초과했습니다. 계속 재생하시겠습니까?');
                    if (!flag) {

                        self.opener = self;
                        window.close();
                    }
                    else {

                        clearTimeout(enchainPlay);
                        enchainPlay = null;
                        console.log('clearTimeout');
                        playPopVms(cctv_uid, cctv_id, vmsPlayTime);
                    }

                }, Number(updatedPlayTime) * 60 * 1000);

                console.log('setTimeout : ' + updatedPlayTime + ' min');
            }
        }
    }
}

// 과거 CCTV 영상 재생
function searchPopVms(cctv_uid, cctv_id) {

    if (isVurixPopPlaying != true) {

        var momentFrom = $('#search-input-from').val();
        var momentTo = $('#search-input-to').val();
        var mode = $('#search-speed option:selected').val();
        var rtspUrl = '';

        momentFrom = moment(momentFrom, 'YYYY-MM-DD HH:mm:ss');
        momentTo = moment(momentTo, 'YYYY-MM-DD HH:mm:ss');

        if (!momentFrom.isValid() && !momentTo.isValid()) {

            alert('올바른 날짜 형식이 아닙니다.');
            return false;
        }

        if (momentFrom.valueOf() > momentTo.valueOf()) {

            alert('시작시간과 끝시간을 확인해 주세요.');
            return false;
        }

        destroyPopVms();

        if (!isVurixPopInit) {

            var chkInit = initPopVms();

            if (chkInit == 0) {

                alert('VLC를 지원하지 않는 브라우저거나, VLC가 정상 설치되어있지 않습니다.');
                return false;
            }
        }

        var evt = 'NO_EVT_OCR_NO';

        if (typeof tri_seq != 'undefined' && tri_seq != '') {

            evt = tri_seq;
        }



        if(evt != 'NO_EVT_OCR_NO') {

            rtspUrl = getRtspUrl('past', tri_seq, cctv_id, cctv_nm, 'L', mode, momentFrom.format('YYYYMMDDHHmmss'));

            if(rtspUrl != null && rtspUrl.url != null) {
                setTimeout(function () {

                    vurix.playlist.add(rtspUrl.url);
                    vurix.playlist.play();

                    vmsStartYmdHms = new Date();
                    isVurixPlaying = true;

                    console.log('momentFrom : ' + momentFrom.format('YYYY-MM-DD HH:mm:ss'));
                    console.log('momentTo : ' + momentTo.format('YYYY-MM-DD HH:mm:ss'));
                    console.log('StartVideoSearchEx : ok ');
                    console.log('==================================================');
                }, 1000);

                setTimeout(function () {

                    $('#searchVmsTimeBefore').attr('disabled', true);
                    $('#searchVmsTimeAfter').attr('disabled', true);
                    $('#search-input-from').attr('disabled', true);
                    $('#search-play').attr('disabled', true);
                    $('#search-pause').attr('disabled', false);
                    $('#btn-test-1').attr('disabled', false);
                    $('#btn-snapShot').attr('disabled', false);
                }, 2500);

                interval = new Date((new Date(momentTo.format('YYYY/MM/DD HH:mm:ss')) - new Date(momentFrom.format('YYYY/MM/DD HH:mm:ss'))));
                interval = interval.getTime();

                setTimeout(function () {

                    isVurixPlaying = false;
                    isVurixInit = false;

                    vurix.playlist.stop();

                    $('#vurix').remove();

                    $('#searchVmsTimeBefore').attr('disabled', false);
                    $('#searchVmsTimeAfter').attr('disabled', false);
                    $('#search-input-from').attr('disabled', false);
                    $('#search-play').attr('disabled', false);
                    $('#search-pause').attr('disabled', true);
                    $('#btn-test-1').attr('disabled', true);
                    $('#btn-snapShot').attr('disabled', true);

                }, interval);
            }
        }
    }
    else {

        vurixPop.playlist.play();
        isVurixPopPlaying = true;
        isVurixPopPausing = false;

        $('#search-input-from').attr('disabled', true);
        $('#search-play').attr('disabled', true);
        $('#search-pause').attr('disabled', false);
        $('#btn-test-1').attr('disabled', false);
        $('#btn-snapShot').attr('disabled', false);
    }
}

// 정지 버튼 클릭 시
function stopPopVms() {

    try {

        isVurixPopPlaying = false;
        isVurixPopInit = false;
        isVurixPopPausing = false;

        vurixPop.playlist.stop();

        $('#vurixPop').remove();

        $('#searchVmsTimeBefore').attr('disabled', false);
        $('#searchVmsTimeAfter').attr('disabled', false);
        $('#search-input-from').attr('disabled', false);
        $('#search-play').attr('disabled', false);
        $('#search-pause').attr('disabled', true);
        $('#btn-test-1').attr('disabled', true);
        $('#btn-snapShot').attr('disabled', true);
    } catch (e) {

        console.log(e);
        alert('VLC를 지원하지 않는 브라우저거나, VLC가 정상 설치되어있지 않습니다.');
    }
}

// 오디오
// spk on : 1 | spk off : 0
function playAudio() {

    try {
        if (canAudio) {

            $('#toggle-audio').css('color', 'grey');
            $('#toggle-audio').text(' off ');

            vurixPop.audio.mute = false;
            canAudio = false;
        }
        else {

            $('#toggle-audio').css('color', 'green');
            $('#toggle-audio').text(' on ');

            vurixPop.audio.mute = true;
            canAudio = true;
        }
    } catch (e) {

        console.log(e);
        alert('VLC를 지원하지 않는 브라우저거나, VLC가 정상 설치되어있지 않습니다.');
    }
}

// 일시정지 버튼 클릭 시
function setPlayMode() {

    try {

        vurixPop.playlist.pause();
        isVurixPopPausing = true;

        $('#search-input-from').attr('disabled', true);
        $('#search-play').attr('disabled', false);
        $('#search-pause').attr('disabled', false);
        $('#btn-test-1').attr('disabled', true);
        $('#btn-snapShot').attr('disabled', false);
    } catch (e) {

        console.log(e);
        alert('VLC를 지원하지 않는 브라우저거나, VLC가 정상 설치되어있지 않습니다.');
    }
}

// 영상 표출용 Object 태그 삭제(영상 팝업)
function destroyPopVms() {

    try {
        if (isVurixPopInit) {

            if (vurixPop != null && typeof vurixPop != 'undefined') {

                console.log('[VMS] VURIX : destroy');
                if (typeof canAudio != 'undefined' && canAudio) {

                    vurixPop.audio.mute = true;
                }

                vurixPop.playlist.stop();
                vurixPop.playlist.items.clear();

                vurixPop = null;
                isVurixPopInit = false;
                isVurixPlaying = false;
            }
        }

        $('#vurixPop').remove();
    } catch (e) {

        console.log(e);
        alert('VLC를 지원하지 않는 브라우저거나, VLC가 정상 설치되어있지 않습니다.');
    }
}
/////////////////////

// 뷰릭스 RTSP URL 가져오기 (영상 5개)
function getCastNetRtspUrl(ocrCctvId) {

    var rtn = null;

    $.ajax({
    	type : 'POST',
    	url : './castNetRtspUrl',
    	contentType : 'application/json; charset=utf-8',
    	async : false,
    	data : JSON.stringify({
            triSeq : triSeq,
            ocrCctvId : ocrCctvId
        }),
    	success : function(data) {

            rtn = data;
    	},
    	error : function(msg) {

            console.log(msg);
    	}
    });

    return rtn;
}

// 뷰릭스 RTSP URL 가져오기 (영상 1개)
function getRtspUrl(viewType, triSeq, cctvId, cctvNm, size, speed, startDt) {

    var rtn = null;

    $.ajax({
    	type : 'POST',
    	url : './rtspUrl',
    	async : false,
    	contentType : 'application/json; charset=utf-8',
    	data : JSON.stringify({
    		viewType : viewType,
            triSeq : triSeq,
    		cctvId : cctvId,
    		cctvNm : cctvNm,
    		size : size,
    		speed : speed,
    		startDt : startDt
    	}),
    	success : function(data) {

    			rtn = data;
    	},
    	error : function(msg) {
    		console.log(msg);
    	}
    });

    return rtn;
}