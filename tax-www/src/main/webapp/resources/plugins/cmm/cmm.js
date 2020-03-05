if (typeof String.prototype.startsWith != 'function') {
	String.prototype.startsWith = function(str) {
		return this.indexOf(str) == 0;
	};
}

if (typeof String.prototype.replaceAll != 'function') {
	String.prototype.replaceAll = function(org, dest) {
		return this.split(org).join(dest);
	};
}

$.fn.exists = function() {
	return this.length > 0;
};

$.jqGrid = function(gridObj, params) {
	// gridObj.jqGrid("GridUnload");
	gridObj.jqGrid(params);
};

$.ajaxEx = function(gridObj, params) {
	$.ajax({
		type : "POST",
		url : params.url,
		dataType : params.dataType,
		data : params.data,
		success : function(data) {
			if (data.session == null || data.session == -1) {
				alert("세션이 끊어졌으므로 로그아웃하고 첫페이지로 보냄");
				return;
			}

			params.success(data);
		},
		error : function(e) {
			params.error(e);
		}
	});
};

var console = console || {
	log : function() {
	},
	warn : function() {
	},
	error : function() {
	}
};

/**
 * selectBox 공통 함수
 * 
 * @param selector :
 *            select 객체
 * @param url :
 *            호출 메소드 Request Url
 * @param param :
 *            json 타입의 파라미터
 * @param selected :
 *            selected 속성을 갖을 값
 */
function getSelectboxList(selector, url, param, selected, lbl) {
	$(selector + ' option').remove();

	if (selected == null)
		selected = "";
	$.ajax({
		type : 'POST',
		url : contextRoot + url,
		data : param,
		beforeSend : function() {
		},
		success : function(data) {
			$(selector).val(selected);
			var codeInfoList = data.list;

			if (typeof lbl != 'undefined' && lbl != null) {

			}
			else {
				lbl = '::: 전체 :::';
			}

			$(selector).append("<option value=''> " + lbl + " </option>");
			for (var i = 0; i < codeInfoList.length; i++) {
				if (typeof codeInfoList[i].cd == 'undefined') {
					$(selector).append("<option value='" + codeInfoList[i].cdId + "'>" + codeInfoList[i].cdNmKo + "</option>");
				}
				else {
					$(selector).append("<option value='" + codeInfoList[i].cd + "'>" + codeInfoList[i].nm + "</option>");
				}
			}
			$(selector).val(selected);
		},
		error : function() {
			alert("정보를 가져오지 못했습니다.");
		}
	});
}

/* 코드목록을 가져온다. */
function codeInfoList(selector, code, selected, lbl) {
	$(selector + ' option').remove();

	if (selected == null)
		selected = "";
	$.ajax({
		type : 'POST',
		url : contextRoot + '/cmm/selectCodeList.json',
		data : {
			gCodeId : code
		},
		beforeSend : function() {
		},
		success : function(data) {
			if (typeof lbl != 'undefined' && lbl != null) {

			}
			else {
				lbl = '::: 전체 :::';
			}

			var codeInfoList = data.list;

			$(selector).append("<option value=''> " + lbl + " </option>");

			for (var i = 0; i < codeInfoList.length; i++) {
				$(selector).append("<option value='" + codeInfoList[i].cdId + "'>" + codeInfoList[i].cdNmKo + "</option>");
			}
		},
		error : function() {
			alert("정보를 가져오지 못했습니다.");
		}
	});
}

/* 시설물 종류 목록을 가져온다. */
function cctvKindCodeInfoList(selector, selected) {

	if (selected == null)
		selected = "";
	$.ajax({
		type : 'POST',
		url : contextRoot + '/cmm/selectCctvKindList.json',
		data : {},
		beforeSend : function() {
		},
		success : function(data) {
			var codeInfoList = data.list;

			$(selector).append("<option value=''>시설물 종류 (전체)</option>");

			for (var i = 0; i < codeInfoList.length; i++) {
				$(selector).append("<option value='" + codeInfoList[i].cdId + "'>" + codeInfoList[i].cdNmKo + "</option>");
			}
			$(selector).val(selected);
		},
		error : function() {
			alert("정보를 가져오지 못했습니다.");
		}
	});
}

/* 시설물 사용유형 종류 목록을 가져온다. */
function fcltUsedTyList(selector, fcltKnd, selected) {

	$(selector + ' option').remove();

	if (selected == null)
		selected = "";
	$.ajax({
		type : 'POST',
		url : contextRoot + '/mntr/cmm/selectFcltUsedTyList.json',
		data : {
			gCodeId : fcltKnd
		},
		beforeSend : function() {
		},
		success : function(data) {
			var codeInfoList = data.list;

			if (selector != '#fcltUsedTyCd')
				$(selector).append("<option value=''>시설물 용도 (전체)</option>");

			for (var i = 0; i < codeInfoList.length; i++) {
				$(selector).append("<option value='" + codeInfoList[i].cdId + "'>" + codeInfoList[i].cdNmKo + "</option>");
			}
			$(selector).val(selected);
		},
		error : function() {
			alert("정보를 가져오지 못했습니다.");
		}
	});
}

/** 차트범례 */
function legend(parent, data) {
	parent.className = 'legend';
	var datas = data.hasOwnProperty('datasets') ? data.datasets : data;

	while (parent.hasChildNodes()) {
		parent.removeChild(parent.lastChild);
	}

	datas.forEach(function(d) {
		var title = document.createElement('span');
		title.className = 'title';
		title.style.borderColor = d.hasOwnProperty('strokeColor') ? d.strokeColor : d.color;
		title.style.borderStyle = 'solid';
		title.style.float = 'left';
		parent.appendChild(title);

		var text = document.createTextNode(d.label);
		title.appendChild(text);
	});
}

/** 숫자, 특수문자('-','.',...) 허용 */
function onlyNum(obj) {
	var keycode = window.event.keyCode;

	if (keycode == 8 || (keycode >= 35 && keycode <= 40) || (keycode >= 46 && keycode <= 57) || (keycode >= 96 && keycode <= 105) || keycode == 110 || keycode == 190) {
		window.event.returnValue = true;
	}
	else {
		alert("숫자만 입력가능합니다.");
		window.event.returnValue = true;
		obj.value = '';
	}
}

/** 숫자만 허용 */
function onlyNumber2(loc) {
	if (/[^0123456789]/g.test(loc.value)) {
		loc.value = "";
		loc.focus();
	}
}

/** 한글입력 방지 */
function fn_press_han(obj) {

	if (event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 37 || event.keyCode == 39 || event.keyCode == 46)
		return;

	obj.value = obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, '');
}

function getColor(param) {
	var colors = [
			"255,216,216", "255,193,158", "242,203,97", "196,183,59", "107,153,0", "250,224,212", "255,224,140", "217,229,255", "165,102,255", "255,178,217"
	];

	var idx;
	if (param == null || param == 0)
		idx = 0;

	idx = param % 10;

	return colors[idx];
}

function getColNames(gubun, param) {
	var unit = (param == '') ? "월" : "일";
	var size = getGridSize(param);

	var names = new Array();
	if (gubun != '')
		names.push(gubun);
	for (var i = 1; i <= size; i++) {
		names.push(i + unit);
	}
	return names;
}

function getColModel(param) {
	var size = getGridSize(param);

	var models = new Array();
	var wid = "'white-space: normal;width=80px;'";
	var firstModel = '({name:"gubun", align:"center", cellattr: function(){ return "style=' + wid + '" }})';

	models.push(eval(firstModel));

	for (var i = 1; i <= size; i++) {
		var string = "{name:'term" + i + "', align:'center'}";
		var obj = eval('(' + string + ')');
		models.push(obj);
	}
	return models;
}

function getGridSize(param) {
	// var lastDay = ( new Date( 년도입력, 월입력, 0) ).getDate();
	var size = 12;
	if (param == 2)
		size = 28;
	else if (param == 1 || param == 3 || param == 5 || param == 7 || param == 8 || param == 10 || param == 12)
		size = 31;
	else if (param == 4 || param == 6 || param == 9 || param == 11)
		size = 30;
	return size;
}

// 프리셋 레이어 구성
function sendPresetList(searchCctvId) {

	$.ajax({
		type : 'POST',
		url : contextRoot + '/getCctvPresetList.json',
		data : {
			searchCctvId : searchCctvId
		},
		success : function(data) {
			var list = data.list;
			if (list.length != 0) {
				for (var i = 0; i < list.length; i++) {
					goCmmPreset(list[i]);
				}
			}
		},
		error : function() {
			alert("정보를 가져오지 못했습니다.");
		}
	});
}

function goCmmPreset(data) {
	var format = new OpenLayers.Format.WKT();
	var geometry = format.read(data.geo);
	var attribute = {
		"prsetLine" : "lineFeature"
	};
	var style = {
		graphicZIndex : 10,
		cussor : "pointer",
		strokeWidth : 3,
		strokeOpacity : 1,
		strokeLinecap : "square",
		strokeColor : "#ee9900"
	};
	if (this.internalProjection && this.externalProjection) {
		geometry.geometry.transform(this.externalProjection, this.internalProjection);
	}
	var feature = new OpenLayers.Feature.Vector(geometry.geometry, attribute, style);
	pointLayer.addFeatures([
		feature
	]);

	var _homePixel = new OpenLayers.Pixel(data.pointX, data.pointY);
	var attribute = {
		"prsetPnt" : "pntFeature"
	};
	var style = {
		externalGraphic : contextRoot + "/images/gis/" + data.presetNum + ".gif",
		pointRadius : 4,
		graphicXOffset : -5,
		graphicYOffset : -5,
		graphicWidth : 12,
		graphicHeight : 12
	};
	var homeFeature = new OpenLayers.Feature.Vector(new OpenLayers.Geometry.Point(_homePixel.x, _homePixel.y), attribute, style);
	pointLayer.addFeatures([
		homeFeature
	]);
}

/* 날짜 형식 검증 yyyy/mm/dd */
function isDate(txtDate) {
	var reg = /^\d{4}\/\d{2}\/\d{2}$/;
	return reg.test(txtDate);
}

/* 두 포인트 사이의 거리를 측정 Lon Lat */
function distance(lat1, lon1, lat2, lon2, unit) {
	var radlat1 = Math.PI * lat1 / 180;
	var radlat2 = Math.PI * lat2 / 180;
	var radlon1 = Math.PI * lon1 / 180;
	var radlon2 = Math.PI * lon2 / 180;
	var theta = lon1 - lon2;
	var radtheta = Math.PI * theta / 180;
	var dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
	dist = Math.acos(dist);
	dist = dist * 180 / Math.PI;
	dist = dist * 60 * 1.1515;
	if (unit == "K") {
		dist = dist * 1.609344
	}
	;
	if (unit == "N") {
		dist = dist * 0.8684
	}
	;
	return dist;
}

/* ajax spinner */
var opts = {
	lines : 13, // The number of lines to draw
	length : 20, // The length of each line
	width : 10, // The line thickness
	radius : 30, // The radius of the inner circle
	corners : 1, // Corner roundness (0..1)
	rotate : 0, // The rotation offset
	direction : 1, // 1: clockwise, -1: counterclockwise
	color : '#000', // #rgb or #rrggbb or array of colors
	speed : 1, // Rounds per second
	trail : 60, // Afterglow percentage
	shadow : false, // Whether to render a shadow
	hwaccel : false, // Whether to use hardware acceleration
	className : 'spinner', // The CSS class to assign to the spinner
	zIndex : 2e9, // The z-index (defaults to 2000000000)
	top : '50%', // Top position relative to parent
	left : '50%' // Left position relative to parent
};

/* 시설물 아이디로 Feature 검색 */
function cmmGetFeaturesByFcltId(layer, getId, getValue) {
	var feature = layer.getFeaturesByAttribute(getId, getValue)[0];
	if (feature) {
		return feature;
	}
	else {
		var features = new Array();
		for (var i = 0; i < layer.features.length; i++) {
			if (layer.features[i].cluster) {
				for (var j = 0; j < layer.features[i].cluster.length; j++) {
					features.push(layer.features[i].cluster[j]);
					var attr = layer.features[i].cluster[j].attributes;

					if (getId == 'fcltId') {

						if (attr.fcltId == getValue) {
							return layer.features[i].cluster[j];
						}
					}
					else if (getId == 'cctvId') {

						if (attr.cctvId == getValue) {
							return layer.features[i].cluster[j];
						}
					}

				}
			}
			else {
				features.push(layer.features[i]);
				var attr = layer.features[i].attributes;

				if (getId == 'fcltId') {

					if (attr.fcltId == getValue) {
						return layer.features[i].cluster[j];
					}
				}
				else if (getId == 'cctvId') {

					if (attr.cctvId == getValue) {
						return layer.features[i].cluster[j];
					}
				}
			}
		}
	}
}

/* 오늘을 기준으로 period기간(일) 동안 from to를 자동으로 입력해 주는 기능 */
function setInputDate(from, to, period) {
	var inputFrom = document.getElementById(from);
	var inputTo = document.getElementById(to);

	var to = new Date();
	var yearTo = new String(to.getFullYear());
	var monthTo = new String(to.getMonth() + 1);
	var dateTo = new String(to.getDate());

	var from = new Date();
	from.setDate(to.getDate() - period);
	var yearFrom = new String(from.getFullYear());
	var monthFrom = new String(from.getMonth() + 1);
	var dateFrom = new String(from.getDate());

	if (monthTo.length == 1) {
		monthTo = '0' + monthTo;
	}
	if (dateTo.length == 1) {
		dateTo = '0' + dateTo;
	}
	if (monthFrom.length == 1) {
		monthFrom = '0' + monthFrom;
	}
	if (dateFrom.length == 1) {
		dateFrom = '0' + dateFrom;
	}

	$(inputFrom).val(yearFrom + '/' + monthFrom + '/' + dateFrom);
	$(inputTo).val(yearTo + '/' + monthTo + '/' + dateTo);
}

/* 페이징 갱신 */
function pagenationReload(selector, data, param) {
	$('#paginate-' + selector).empty();
	$('#paginate-' + selector).html($('<ul/>', {
		id : 'pagination-' + selector,
		class : 'paging'
	}));
	$('#pagination-' + selector).twbsPagination({
		startPage : data.page,
		totalPages : data.totalPages,
		visiblePages : 4,
		onPageClick : function(event, page) {
			gridReloadNoparam(selector, page); 
		},
		first : '&nbsp;&nbsp;',
		prev : '&nbsp;&nbsp;',
		next : '&nbsp;&nbsp;',
		last : '&nbsp;&nbsp;'
	});
}

/* 그리드 갱신 */
function gridReload(selector, page, param) {
	$('#grid-' + selector).setGridParam({
		page : page,
		postData : param
	}).trigger("reloadGrid");
}

function gridReloadNoparam(selector, page) {
	$('#grid-' + selector).setGridParam({
		page : page 
	}).trigger("reloadGrid");
}

/* 그리드 데이터 없음 */
function setGridNodata(selector, text) {
	if (typeof text == 'undefined') {
		text = '데이터가 없습니다.';
	}
	var td = $('<td/>', {
		role : 'gridcell',
		style : 'text-align:center; padding: 5px 3px 5px 3px;',
		class : 'jqgrid_cursor_pointer',
		text : text
	})
	var tr = $('<tr/>', {
		role : 'row',
		id : 'jqgrow-nodata',
		tabindex : '-1',
		class : 'ui-widget-content jqgrow ui-row-ltr'
	});
	var tbody = $('<tbody/>');
	tr.append(td);
	tbody.append(tr);

	$('#grid-' + selector).html(tbody);
}

function checkGridNodata(selector, data) {
	if (!data.totalRows || !data.rows.length) {
		setGridNodata(selector);
		return false;
	}
	else {
		if ($('#grid-' + selector + ' #jqgrow-nodata').length > 0) {
			$('#grid-' + selector + ' #jqgrow-nodata').detach();
		}
		else {
			var size = $('#grid-' + selector + ' tr[id=1]').length;
			if ($('#grid-' + selector + ' tr[id=1]').length > 1) {
				var tr = $('#grid-' + selector + ' tr[id=1]');
				$(tr[0]).detach();
			}
		}
		return true;
	}
}

//명시적으로 value == 사용 
//[], {} 도 빈값으로 처리 
//값이 있으면 true
function chkValue(value) { 
	if( value == "" || value == null || value == undefined || ( value != null && typeof value == "object" && !Object.keys(value).length ) ) { 
		return false 
	} else { 
	    return true 
	}
}

//명시적으로 value == 사용 
//[], {} 도 빈값으로 처리 
//값이 없는경우 true
function nonChkValue(value) { 
	if( value == "" || value == null || value == undefined || ( value != null && typeof value == "object" && !Object.keys(value).length ) ) { 
		return true 
	} else { 
	    return false 
	}
}
//alert 창닫기
function alertClose(msg) { 
   var sW = window.screen.width;
   var bcW = document.body.clientWidth;
   var bcH = document.body.clientHeight;
   var cW = 260;
   var cH = 100;
   var sX = window.screenX || window.screenLeft || 0;
   var sY = window.screenY || window.screenTop || 0;
   var pY = ((bcH - cH) / 2) + sY; 
   var pX = ((bcW - cW) / 2) + sX; 
   var options = 'left=' + pX + ',top=' + pY + ',width='+cW+',height='+cH+',toolbar=no,menubar=no,status=no,scrollbars=no,resizable=no';

	var w = window.open('','',options);
	w.document.write('<div style="display:table;width:100%; height:100%; text-align:center ;background-color:black;">');
	w.document.write('<div style="color:white; width:100%; height:100%; text-align:center;display:table-cell;vertical-align:middle;">');
	w.document.write(msg);
	w.document.write(pX + ':' +pY );
	w.document.write(msg);
	w.document.write('</div>');
	w.document.write('</div>');
	w.focus();
	setTimeout(function() {	w.close();	}, 1000);	
}
/* add_ask팝업:사건신고 */
function openAddAsk(userId,fcltUid,ftpYn) {
 var url = "/svcutil/eventwrks/sit/info/event_ask.do?userId="+userId+"&fcltUid="+fcltUid+"&ftpYn="+ftpYn;
 window.open(contextRoot + url);
}
