<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=10">
<title>${cctvInfo.cctvNm}(${cctvInfo.cctvId})</title>
<link rel="stylesheet" type="text/css" href="/tax/res/plugins/resetCss/resetCss.css">
<link rel="stylesheet" type="text/css" href="/tax/res/assets/plugins/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/tax/res/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
<script>
	/* user configure */
	var vmsUseYn = '${configure.vmsUseYn}';
	var vmsIp = '${configure.vmsIp}';
	var vmsPort = '${configure.vmsPort}';
	var vmsId = '${configure.vmsId}';
	var vmsPwd = '${configure.vmsPwd}';
	var vmsImgDir = '${configure.vmsImgDir}';

	var cctv_id = '${cctvInfo.cctvId}';
	var cctv_uid = '${cctvInfo.cctvUid}';
	var cctv_nm = '${cctvInfo.cctvNm}';
	var tri_seq = '${cctvInfo.triSeq}';

	var vmsPlayTime = '${configure.vmsPlayTime}';
	var vmsPlaybackTimeBase = '${configure.vmsPlayBackTimeBase}';
	var vmsPlaybackTimeMax = '${configure.vmsPlayBackTimeMax}';
	var vmsPlaybackTimeMaxAf = '${configure.vmsPlayBackTimeMaxAf}';

	// mic on : 1 | mic off : 0
	var canMic = false;

	// spk on : 1 | spk off : 0
	var canAudio = false;

</script>
<script src="/tax/res/plugins/jquery-2.1.3/jquery-2.1.3.min.js"></script>
<script src="/tax/res/plugins/cmm/cmm.js"></script>
<script src="/tax/res/plugins/vms/${configure.vmsSoftware}/${configure.vmsSoftware}.js"></script>
<style>
body {
	position: absolute;
	top: 0px;
	right: 0px;
	bottom: 0px;
	left: 0px;
}

div#view-popVms {
	position: inherit;
	top: 0px;
	left: 0px;
	right: 0px;
	bottom: 45px;
}

div#handle-vms {
	position: inherit;
	left: 0px;
	right: 0px;
	bottom: 0px;
	padding: 10px;
	height: 50px;
}

span#play-deck {
	display: none;
}

span#search-deck {
	display: none;
}

span#search-span2 {
	display: none;
}

#toggle-audio {
	color: gray;
}

#toggle-mic {
	color: gray;
}
</style>
</head>
<body>
<div id="view-popVms"></div>
<div id="handle-vms">
	<div class="row" style="margin: 0px;">
		<div class="col-xs-12" style="padding-left: 0px; padding-right: 0px;">
			<div class="form-inline">
				<label class="radio-inline">
					<input type="radio" name="vms-mode" id="chk-play-deck" value="play"
						   onchange="javascript:activateVmsMode('play');">
					현재
				</label>
				<label class="radio-inline">
					<input type="radio" name="vms-mode" id="chk-search-deck" value="search"
						   onchange="javascript:activateVmsMode('search');" checked>
					과거
				</label>
				<!-- search-deck -->
					<span id="search-deck">
						<select class="form-control" id="search-speed">
							<option value="10" selected="selected">재생(1x)</option>
							<option value="15">재생(1.5x)</option>
							<option value="20">재생(2x)</option>
						</select>
						<div class="form-group">
							<div class='input-group date' id='search-picker-from'>
								<input type='text' class="form-control" id="search-input-from" style="width: 160px;"/>
								<span class="input-group-addon">
									<span class="glyphicon glyphicon-calendar" title="시작일시"></span>
								</span>
							</div>
							<span id="search-span2">
							<span> ~ </span>
							<div class='input-group date' id='search-picker-to'>
								<input type='text' class="form-control" id="search-input-to" style="width: 160px;"/>
								<span class="input-group-addon">
									<span class="glyphicon glyphicon-calendar" title="종료일시"></span>
								</span>
							</div>
							</span>
						</div>
						<button class="btn btn-default btn-sm btn-ucp" id="searchVmsTimeBefore" type="button"
								title="재생시작시간의1분전부터 재생" onclick="javascript:searchVmsTime(-1);">이전
						</button>
						<button class="btn btn-default btn-sm btn-ucp" id="searchVmsTimeAfter" type="button"
								title="재생시작시간의1분후부터 재생" onclick="javascript:searchVmsTime(1);">다음
						</button>
						<button class="btn btn-default btn-sm btn-ucp" id="search-play" type="button" title="재생"
								onclick="javascript:searchPopVms();">
							<span class="glyphicon glyphicon-play" aria-hidden="true"></span>
						</button>
						<button class="btn btn-default btn-sm btn-ucp" id="search-pause" type="button" title="정지"
								onclick="javascript:stopPopVms();">
							<span class="glyphicon glyphicon-stop" aria-hidden="true"></span>
						</button>
						<button class="btn btn-default btn-sm btn-ucp" id="btn-test-1" type="button" title="일시정지"
								onclick="javascript:setPlayMode(7);">
							<span class="glyphicon glyphicon-pause" aria-hidden="true"></span>
						</button>
					</span>
				<button class="btn btn-default btn-sm btn-ucp" id="btn-screen" type="button" title="전체화면"
						onclick="javascript:fullScreenWindow();">
					<span class="glyphicon glyphicon-resize-full" id="btn-screen-icon" aria-hidden="true"></span>
				</button>
					<span id="search-div2">
						<button class="btn btn-default btn-sm btn-ucp" id="btn-ocrTime" type="button" title="발생시간"
								disabled="disabled"></button>
						<button class="btn btn-default btn-sm btn-ucp" id="btn-snapShot" type="button" title="정지영상저장"
								onclick="javascript:snapShot(cctv_nm, cctv_id);" disabled>
							<span class="glyphicon glyphicon-camera" aria-hidden="true"></span>
						</button>
 					</span>
			</div>
		</div>
	</div>
</div>
	<script src="/tax/res/assets/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="/tax/res/plugins/bootstrap-datetimepicker/js/moment.js"></script>
	<script src="/tax/res/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
	<script>
		var evtOcrNo = '${cctvInfo.cctvId}';
		var enchainPlay = null;
		var vmsStartYmdHms = null;
		
		$(function() {

			var momentOcr = moment();
			var momentFrom = momentOcr.clone().add(Number(vmsPlaybackTimeBase) * -1, 'minutes');
			var momentTo = momentFrom.clone().add(Number(vmsPlayTime), 'minutes');

			if(vmsPlayTime == '0') momentTo = momentOcr;
			else if(momentTo.valueOf() >= momentOcr.valueOf()) momentTo = momentOcr;

			$('#search-picker-from').datetimepicker({
				format: 'YYYY-MM-DD HH:mm:ss',
				minDate: momentOcr.clone().add(Number(vmsPlaybackTimeMax) * -1, 'minutes'),
				maxDate: momentOcr
			});

			$('#search-picker-to').datetimepicker({
				format: 'YYYY-MM-DD HH:mm:ss',
				minDate: momentOcr,
				maxDate: momentOcr.clone().add(Number(vmsPlaybackTimeMaxAf) * 1, 'minutes')
			});

			$('#btn-ocrTime').text('발생(' + momentOcr.format('YYYY-MM-DD HH:mm:ss') + ')');
			$('#search-input-from').val(momentFrom.format('YYYY-MM-DD HH:mm:ss'));
			$('#search-input-to').val(momentTo.format('YYYY-MM-DD HH:mm:ss'));

			$('#search-deck').show();
			$('#chk-search-deck').prop('checked', true);

			if(vmsUseYn == 'Y') {

				searchPopVms();
			}

			$('#search-picker-from').on('dp.show', function (e) {
				$('#view-popVms').hide();
				$('#search-picker-from').find('.picker-switch a[data-action="togglePicker"]').click();
			});
			$('#search-picker-from').on('dp.hide', function (e) {
				$('#view-popVms').show();
			});
			$('#search-picker-to').on('dp.show', function (e) {
				$('#view-popVms').hide();
				$('#search-picker-to').find('.picker-switch a[data-action="togglePicker"]').click();
			});
			$('#search-picker-to').on('dp.hide', function (e) {
				$('#view-popVms').show();
			});
		});
		
		function searchVmsTime(tm) {
			var momentFrom = $('#search-input-from').val();
//			var momentTo = $('#search-input-to').val();
			
			momentFrom = moment(momentFrom, 'YYYY-MM-DD HH:mm:ss');
//			momentTo = moment(momentTo, 'YYYY-MM-DD HH:mm:ss');
			
			momentFrom = momentFrom.clone().add( Number(tm) , 'minutes');
//			momentTo = momentTo.clone().add( Number(tm) , 'minutes');
			
			$('#search-input-from').val(momentFrom.format('YYYY-MM-DD HH:mm:ss'));
//			$('#search-input-to').val(momentTo.format('YYYY-MM-DD HH:mm:ss'));
			
//			searchPopVms();
		}

		function activateVmsMode(mode) {

			if(enchainPlay != null) {

				clearTimeout(enchainPlay);

				enchainPlay = null;

				console.log('clearTimeout');
			}
			
			if(mode == 'play') {

				$('#search-deck').hide();
				$('#search-div2').hide();
				$('#play-deck').show();

				if(vmsUseYn == 'Y') {

					playPopVms(cctv_uid, cctv_id, vmsPlayTime);
				}
			}
			if(mode == 'search') {

				$('#play-deck').hide();
				$('#search-deck').show();
				$('#search-div2').show();

				if(vmsUseYn == 'Y') {

					searchPopVms(cctv_uid, cctv_id);
				}
			}
		}

		function defaultScreenWindow() {
			$('#btn-screen').prop('onclick', '').off('click');
			$('#btn-screen').prop('onclick', '').click(fullScreenWindow);
			$('#btn-screen-icon').prop('class', 'glyphicon glyphicon-resize-full');

			window.resizeTo('1024', '768');
		}

		function fullScreenWindow() {
			$('#btn-screen').prop('onclick', '').off('click');
			$('#btn-screen').prop('onclick', '').click(defaultScreenWindow);
			$('#btn-screen-icon').prop('class', 'glyphicon glyphicon-resize-small');

			window.moveTo(0, 0);
			top.window.resizeTo(screen.availWidth, screen.availHeight);
		}

		// 사진기 버튼 클릭 시
		function snapShot(sFcltLblNm, sFcltId) {
			console.log(vurixPop);
			if (isVurixPopPlaying && vurixPop.playlist.isPlaying) {

				var momentFrom = $('#search-input-from').val();
				momentFrom = moment(momentFrom, 'YYYY-MM-DD HH:mm:ss');
				var ymdHmsm = momentFrom.format('YYYYMMDDHHmmssSS');
				var snapShotYmdHms = new Date();

				var currMillis = new Date(ymdHmsm.substring(0,4), ymdHmsm.substring(4,6) - 1, ymdHmsm.substring(6,8), ymdHmsm.substring(8,10), ymdHmsm.substring(10,12), ymdHmsm.substring(12,14), ymdHmsm.substring(14,16));
				var vmsPlayMills = currMillis.getTime() + (snapShotYmdHms.getTime() - vmsStartYmdHms.getTime());
				var vmsPlayDate = new Date(vmsPlayMills);
				var vmsPlayYmdHmsm = vmsPlayDate.getFullYear() + '' +
						(vmsPlayDate.getMonth()+1 < 10 ? '0' + (vmsPlayDate.getMonth() + 1) : vmsPlayDate.getMonth() +1) + '' +
						(vmsPlayDate.getDate() < 10 ? '0' + vmsPlayDate.getDate() : vmsPlayDate.getDate())+ '' +
						(vmsPlayDate.getHours() < 10 ? '0' + vmsPlayDate.getHours() : vmsPlayDate.getHours())+ '' +
						(vmsPlayDate.getMinutes() < 10 ? '0' + vmsPlayDate.getMinutes() : vmsPlayDate.getMinutes())+ '' +
						(vmsPlayDate.getSeconds() < 10 ? '0' + vmsPlayDate.getSeconds() : vmsPlayDate.getSeconds())+ '' +
						(vmsPlayDate.getMilliseconds() / 10 < 0 ? '0' + (vmsPlayDate.getMilliseconds().toString()).substring(0,1) : (vmsPlayDate.getMilliseconds().toString()).substring(0,2));
				var vmsPlayYmdHms = vmsPlayDate.getFullYear() + '' +
						(vmsPlayDate.getMonth()+1 < 10 ? '0' + (vmsPlayDate.getMonth() + 1) : vmsPlayDate.getMonth() +1) + '' +
						(vmsPlayDate.getDate() < 10 ? '0' + vmsPlayDate.getDate() : vmsPlayDate.getDate())+ '' +
						(vmsPlayDate.getHours() < 10 ? '0' + vmsPlayDate.getHours() : vmsPlayDate.getHours())+ '' +
						(vmsPlayDate.getMinutes() < 10 ? '0' + vmsPlayDate.getMinutes() : vmsPlayDate.getMinutes())+ '' +
						(vmsPlayDate.getSeconds() < 10 ? '0' + vmsPlayDate.getSeconds() : vmsPlayDate.getSeconds());


				var fileName = evtOcrNo + '_' + sFcltId + '_' + vmsPlayYmdHmsm + '.jpg';

//				window.location.assign(contextRoot + '/mntr/vms/snapShot.json?fileName=' + fileName + '&creno=' + evtOcrNo + '&cctvId=' + fcltId + '&cctvNm=' + encodeURI(encodeURIComponent(fcltLblNm)) + '&timestamp=' + vmsPlayYmdHms);
				window.location.assign('/main/setSnapShot?fileName=' + fileName + '&creno=' + evtOcrNo + '&cctvId=' + sFcltId + '&cctvNm=' + encodeURI(encodeURIComponent('024-3.(서구불법-024)테크노월드 (기업은행)')) + '&timestamp=' + vmsPlayYmdHms);
			}
			else {

				alert('영상 재생 상태를 확인하세요.');
			}
		}
	</script>
</body>
</html>