/**
 * User: 이준수
 * Date: 2018.01.09
 * Time: 오후 12:48
 */
define('local', ['common', 'formcheck', 'jqGrid.setting', 'openlayers', 'proj4', 'jquery', 'socket.io', 'bootstrap-switchery', vmsSoftware, 'bootstrap-select', 'jqGrid'], function (common, formcheck, jqFn, openLayers, proj4, jQuery, io, Switchery) {

    proj4.defs('EPSG:5181', '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +units=m +no_defs');

    jQuery.jgrid.defaults.width = 780;
    jQuery.jgrid.defaults.responsive = true;
    jQuery.jgrid.defaults.styleUI = 'Bootstrap';

    // 리턴 스크립트 체크
    function inputCheckScript(tarID) {

        switch (tarID) {

            case 'infoPanel':

                var userInputPattern = /[가-힣]{2,4}\/[0-9]{3}\-[0-9]{4}\-[0-9]{4}\/[가-힣|0-9]{2,10}/;
                var seizeDtPattern = /^(19|20)\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[0-1])$/;

                var prcUserInput = jQuery('#prc_user_input');

                if (prcUserInput.length > 0) {

                    if (prcUserInput.val() != '') {

                        if (!prcUserInput.val().match(userInputPattern)) {


                            common.setOSXModal('이름(2~4자리, 한글 가능)/010-0000-0000/부서명(한글, 숫자 가능) 형식으로 입력해주세요.');

                            return false;
                        }
                    }
                }

                var seizeUserInput = jQuery('#seize_user_input');

                if (seizeUserInput.length > 0) {

                    if (seizeUserInput.val() != '') {

                        if (!seizeUserInput.val().match(userInputPattern)) {

                            common.setOSXModal('이름(2~4자리, 한글 가능)/010-0000-0000/부서명(한글, 숫자 가능) 형식으로 입력해주세요.');

                            return false;
                        }

                    }
                }

                formcheck.checkForm(tarID);

                return processChk();

                break;

            default :

                return formcheck.checkForm(tarID);

                break;
        }
    }

    // 패널 초기화
    function panelClear(isAll, objID, isListReset) {

        if (isAll === undefined) isAll = false; // 전체 reset 여부
        if (objID === undefined) objID = ''; // panelID
        if (isListReset === undefined) isListReset = true; // Master List reset 여부

        if (isAll) {
            // 모든 패널 초기화
            panelClear(false, 'infoPanel', false);

            return false;
        }

        switch (objID) {
            case 'infoPanel':
                // 체납차량 발생정보, 상황처리 입력폼
                common.clearElement('#' + objID); // form element

                changeSwitchery('#prc_fl', false);
                changeSwitchery('#seize_fl', false);

                var sitPcsList = jQuery('#sitPcsList');
                var rowid = sitPcsList.jqGrid('getGridParam', 'selrow');

                var html = '<button id="btnPhoto" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-camera"></i> 차량번호 사진</button>';
                jQuery('#spanBtn .btn').remove();
                jQuery('#spanBtn').append(html);
                jQuery('#btnPhoto').attr('disabled', true);
                jQuery('#btnSave').attr('disabled', true);

                if (rowid !== null && isListReset) {

                    sitPcsList.jqGrid('resetSelection'); // Grid Select Reset 처리

                }

                break;
        }
    }

    //jqGrid data 리프레쉬
    function dataReload(tarID) {

        if (authCrud.READ_FL === 'N') {
            // 조회 권한이 없으면 데이터 조회 실패
            return false;
        }
        if (authCrud.MOD_FL === 'N') {
            // 수정 권한이 없으면 승인여부, 영치여부 적용 실패
            return false;
        }

        var listID = tarID + "List";
        var dataGrid = jQuery("#" + listID);
        var filterData = {};

        var jqOpt = {};

        switch (listID) {

            case 'sitPcsList' :
                // 상황처리 목록
                jqOpt = {
                    url: './getSitPcsList'
                };

                break;
        }

        dataGrid
            .jqGrid("setGridParam", jQuery.extend(true, {
                search: true,
                postData: {
                    filters: JSON.stringify(filterData)
                }
            }, jqOpt))
            .trigger("reloadGrid", [{page: 1}]);
    }

    // 상세정보 적용 이벤트
    function dataSend() {
        // 로딩 시작
        jQuery.fn.loadingStart();

        // 적용 모드
        if (authCrud.MOD_FL === 'N') return false;

        var sitPcsList = jQuery('#sitPcsList');
        var rowid = sitPcsList.jqGrid('getGridParam', 'selrow');
        var formData = jQuery('#infoPanel :input');
        var reqData = formData.serializeObject();

        // 기본 입력 폼의 값(key 변경 : vo 변수명에 맞춰서)
        reqData = common.changeKeys(reqData, [
            {k: 'seize_dt', v: 'seize_dt'},
            {k: 'prc_cont', v: 'prc_cont'}
        ]);

        var prcUserInput = jQuery('#prc_user_input');
        var seizeUserInput = jQuery('#seize_user_input');

        (prcUserInput.length > 0) ? reqData.prc_user_seq = managerInsert(prcUserInput.val()) : reqData.prc_user_seq = jQuery('#prc_user_nm option:selected').val();
        (seizeUserInput.length > 0) ? reqData.seize_user_seq = managerInsert(seizeUserInput.val()) : reqData.seize_user_seq = jQuery('#seize_user_nm option:selected').val();


        reqData.prc_fl = jQuery("input:checkbox[id='prc_fl']").is(":checked") === true ? 'Y' : 'N';
        reqData.seize_fl = jQuery("input:checkbox[id='seize_fl']").is(":checked") === true ? 'Y' : 'N';
        rowid === null ? reqData.tri_seq = '' : reqData.tri_seq = sitPcsList.jqGrid('getRowData', rowid).tri_seq; // 상황처리 고유 번호
        rowid === null ? reqData.car_no = '' : reqData.car_no = sitPcsList.jqGrid('getRowData', rowid).car_no; // 상황처리 선택 행 차량 번호
        rowid === null ? reqData.state = '' : reqData.state = sitPcsList.jqGrid('getRowData', rowid).state; // 상황처리 선택 행 상태


        // 데이터 전송
        jQuery.when(
            jQuery.ajax({
                url: './setDetailInfoAct',
                type: "POST",
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(reqData)
            })
        )
            .then(function (data) {
                // 결과에 따라 다음 이벤트 처리
                if (data !== null) {

                    common.setOSXModal('변경사항을 성공적으로 저장하였습니다.');
                    panelClear(true);
                    dataReload('sitPcs');
                    sitPcsSel = undefined;
                    getInterestCarList();
                    jQuery('#interestSelect').selectpicker('refresh');

                } else {

                    common.setOSXModal('저장이 실패하였습니다.');
                }
            })
            .fail(common.ajaxError)
            .always(function () {

                jQuery.fn.loadingComplete();
                return false;
            });

        return false;
    }

    //레이아웃 변경 시 사이즈 조절 리턴 함수
    function resizePanel(id) {

        if (id === undefined) {

            id = null;
        }

        if (id !== null) {

            jQuery.each([{list: id + "List", panel: id + "Panel"}], function (sIdx, data) {

                jQuery("#" + data["list"]).jqGrid('setGridWidth', jQuery("#" + data["panel"]).width() - 2);
            });
        }
    }

    // 상세정보 입력 폼 Setting
    function infoSetting(rowID, obj) {

        if (rowID) {

            jQuery.when(
                jQuery.ajax({
                    url: './getDetailInfoData',
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify({
                        tri_seq: jQuery('#sitPcsList').jqGrid('getRowData', rowID).tri_seq
                    })
                })
            ).then(function (data) {

                    // 리턴 결과 값을 가지고 입력 값 Setting
                    // 상세 정보
                    common.setValues({
                        generate_type: data.generate_type,
                        car_no: data.car_no,
                        tot_money_cnt: data.tot_money_cnt,
                        generate_info: data.generate_info,
                        cctv_info: data.cctv_info,
                        //car_type: data.car_type,
                        //car_color: data.car_color,
                        //car_md_year: data.car_md_year,
                        prc_user_nm: data.prc_user_seq,
                        seize_user_nm: data.seize_user_seq,
                        seize_dt: data.seize_dt,
                        prc_cont: data.prc_cont
                    });

                    var files = data.files;
                    var filesLength = files.length;

                    if (filesLength == 0) {

                        var html = '<button id="btnPhoto" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-camera"></i> 차량번호 사진</button>';
                        jQuery('#spanBtn').empty();
                        jQuery('#spanBtn').append(html);
                        jQuery('#btnPhoto').attr('disabled', true);
                    }

                    if (filesLength > 0) {

                        var html = '';

                        jQuery('#spanBtn .btn').remove();

                        for (var i = 0; i < filesLength; i++) {

                            html += '<a id="' + files[i].real_tb + files[i].real_seq + i + '" href="' + jQuery.fn.sysUrl + '/res/assets/img/carno/' + files[i].files_seq + '.jpg" data-lightbox=' + files[i].real_tb + files[i].real_seq + '>';

                            (i == 0) ? html += '<button id="btnPhoto' + files[i].files_seq + '" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-camera"></i> 차량번호 사진</button></a>' : html += '<button id="btnPhoto' + files[i].files_seq + '" class="btn btn-primary btn-sm" style="display: none;"><i class="glyphicon glyphicon-camera"></i> 차량번호 사진</button></a>';
                        }

                        jQuery('#spanBtn').empty();
                        jQuery('#spanBtn').append(html);
                    }

                    return data;
                }).then(function (data) {

                    changeSwitchery('#prc_fl', data.prc_fl === 'Y' ? true : false);
                    changeSwitchery('#seize_fl', data.seize_fl === 'Y' ? true : false);
                }).done(function () {
                })
                .fail(common.ajaxError)
                .always(function () {

                    return false;
                });
        }
    }

    // 사용하는 jqGrid Setting
    function gridSetting(tarID) {

        var listID = tarID + "List";
        var pageID = tarID + 'Pager';

        var dataGrid = jQuery("#" + listID);

        var jqDefault = {
            url: '',
            datatype: 'json',
            mtype: 'POST',
            sortname: '', // 소팅 인자
            sortorder: '', // 처음 소팅
            pager: pageID,
            rowNum: 30,                 // 처음에 로드될 때 표출되는 row 수
            rowList: [], // row 갯수 표출 세팅
            viewsortcols: [true, 'vertical', true], // 소팅 인자 세팅
            rownumbers: true,           // Grid의 RowNumber 표출
            viewrecords: true,          // 우측 View 1-4 Text 표출 부분
            gridview: true,             // Grid Alert
            autowidth: true,            // width 자동 맞춤
            shrinkToFit: true,          // width에 맞춰 Cell Width 자동 설정
            height: 302,                // 세로 크기
            caption: '',                // 캡션 명(없으면 표출 안됨)
            beforeRequest: function () {
                // POST 보내기 전 이벤트
                if (jQuery(this).jqGrid('getGridParam', 'url') === '') return false;
            },
            gridComplete: function () {

                jQuery('#' + pageID + ' .ui-pg-input').attr('readonly', true);
            }
        };
        var jqOpt = {};

        switch (listID) {
            case 'sitPcsList' :
                // 상황처리 목록
                jqOpt = {
                    url: '',
                    height: 100,
                    scroll: 1,
                    scrollrows: true,
                    rowList: [10, 30, 50, 100],
                    colNames: ['', '', '', '', '', '차량번호', '금액(건수)', '발생일시', '상태'],
                    colModel: [
                        {name: 'ocr_dts', index: 'ocr_dts', hidden: true},
                        {name: 'cctv_id', index: 'cctv_id', hidden: true},
                        {name: 'tri_seq', index: 'tri_seq', hidden: true},
                        {name: 'point_x', index: 'point_x', hidden: true},
                        {name: 'point_y', index: 'point_y', hidden: true},
                        {name: 'car_no', index: 'car_no', width: 1, align: "center", sortable: false},
                        {name: 'tax_money_cnt', index: 'tax_money_cnt', width: 1, align: "center", sortable: false},
                        {name: 'reg_dts', index: 'reg_dts', width: 1, align: "center"},
                        {name: 'state', index: 'state', width: 2, align: "center", sortable: false}
                    ],
                    onInitGrid: function () {
                        dataReload(tarID);
                    },
                    onSelectRow: function (id, status, event) {

                        if (!(jQuery('#disStandChk').is(':checked') || jQuery('#interestSelect option:selected').val())) {

                            var ret = dataGrid.jqGrid('getRowData', id);
                            // 행 선택 시
                            if (id && id !== sitPcsSel) {

                                if (sitPcsSel !== undefined) {

                                    jQuery(this).jqGrid('restoreRow', sitPcsSel);
                                    jqFn.jqGridListIcon(this.id, sitPcsSel);

                                    // jqGrid 버그 수정 addRow 후에 다른 row 선택을 여러번 해보면 highlight 버그 해결
                                    var tmpObj = jQuery(this).find('#' + sitPcsSel);

                                    if (tmpObj.hasClass('success')) {

                                        tmpObj.removeClass('success').removeAttr('aria-selected');
                                    }
                                }

                                sitPcsSel = id;

                                var prcUserBtn = jQuery('#prc_user_btn');
                                var seizeUserBtn = jQuery('#seize_user_btn');

                                // 상세정보 폼, 상황전파 목록, 체납정보 목록 갱신
                                infoSetting(id, ret);

                                if (prcUserBtn.text() == '선택') {

                                    prcUserBtn.text('직접입력');
                                    jQuery('#prc_user_input').replaceWith('<select id="prc_user_nm" name="prc_user_nm" class="form-control" style="width: 60%; display: inline; margin-left: 5%;"/>');
                                    common.setSelectOpt(jQuery('#prc_user_nm'), '-선택-', managerList);
                                    prcWsChk = 'prc_user_nm';
                                }

                                if (seizeUserBtn.text() == '선택') {

                                    seizeUserBtn.text('직접입력');
                                    jQuery('#seize_user_input').replaceWith('<select id="seize_user_nm" name="seize_user_nm" class="form-control" style="width: 60%; display: inline; margin-left: 5%;"/>');
                                    common.setSelectOpt(jQuery('#seize_user_nm'), '-선택-', managerList);
                                    seizeWsChk = 'seize_user_nm';
                                }

                                if (reCctvMarkerLayer != undefined) {

                                    mapInfo.map.removeLayer(reCctvMarkerLayer);
                                    reCctvMarkerLayer = undefined;
                                }

                                if (cctvCnt != 0) {

                                    triSeq = ret.tri_seq;
                                    zoomGeoData(ret.point_x, ret.point_y);
                                    getNetSetCctvGeoData(ret.cctv_id, jQuery('#cctvTypeSelect').val());
                                }
                                else {

                                    common.setOSXModal('레이어에 해당하는 CCTV가 없습니다.');
                                }


                                jQuery('#btnSave').attr('disabled', false);
                            }
                        }
                    },
                    loadComplete: function (data) {
                        // 그리드에 모든 데이터 로딩 완료 후
                        jQuery.when(
                            resizePanel(tarID) // 브라우저 창 크기 변경 시 grid 크기 자동 적용
                        ).then(function () {
                                // 데이터가 존재 할 경우 처음 행 선택
                                var dataCnt = data.rows.length;

                                if (dataCnt > 0) {

                                    dataGrid.jqGrid('setSelection', dataGrid.find('tr[id]:eq(0)').attr('id'));
                                }
                                if (dataCnt == 0) {

                                    jQuery('#btnPhoto, #btnSave').attr('disabled', true);
                                }

                                getInterestCarList(); // 관심차량 이동경로 Select 가져오기
                                getCctvGeoData(jQuery('#cctvTypeSelect').val(), dataCnt, GridYn); // 상황처리 목록 CCTV GIS 표출

                                GridYn = 'N';
                            }).always(function () {

                                jQuery.fn.loadingComplete();
                                return false;
                            });
                    }
                };

                break;
        }

        dataGrid
            .jqGrid(jQuery.extend(true, jqDefault, jqOpt))
            .jqGrid('navGrid', '#' + pageID,
            {edit: false, add: false, del: false, search: false, refresh: false}, // options
            {}, // edit options
            {}, // add options
            {}, // del options
            {width: '100%'}, // search options
            {} // view options
        );

        return false;
    }

    // 체크박스 switchery 변경
    function changeSwitchery(element, checked) {
        var switchery;
        var defaults = {
            color: '#64bd63'
            , secondaryColor: '#dfdfdf'
            , jackColor: '#fff'
            , jackSecondaryColor: null
            , className: 'switchery'
            , disabled: false
            , disabledOpacity: 0.5
            , speed: '0.1s'
            , size: 'default'
        };

        elem = document.querySelector(element);


        elem.checked = checked;

        if (jQuery('#sitPcsList').jqGrid('getGridParam', 'selrow') == null) {

            jQuery(element).siblings().remove();

            switchery = new Switchery(elem, defaults);

            jQuery('.switchery, .switchery small').css({
                height: '23px'
            });

            return switchery;
        }

        jQuery(element).siblings().remove();

        switchery = new Switchery(elem, defaults);

        jQuery('.switchery, .switchery small').css({
            height: '23px'
        });

        return switchery;
    }

    // 직접입력시 담당자 중복 확인
    function chkDupManager(id) {

        var managerId = jQuery('#' + id);
        var nmArrLength = nmArr.length;

        managerId.focusout(function () {

            if (nmArr.indexOf(managerId.val()) != -1) {

                var html = '';

                for (var i = 0; i < nmArrLength; i++) {

                    if (nmArr[i] == managerId.val()) {

                        html += '\n( ' + nmArr[i] + ' / ' + hpArr[i] + ' / ' + partNmArr[i] + ' )';
                    }
                }

                var input = confirm('담당자 이름중에' + html + '\n이 이미 존재 합니다. 계속하시겠습니까?');

                if (input) {
                } else {

                    managerId.val('');
                }
            }
        });
    }

    // 기존에 등록 되어있는 담당자 가져오기
    function chkDbDupManager() {
        jQuery.when(
            jQuery.ajax({
                url: '../event/getDupChkManagerList',
                type: 'POST',
                contentType: 'application/json; charset=utf-8'
            })
        ).then(function (data) {
                seqArr = [];
                nmArr = [];
                hpArr = [];
                partNmArr = [];

                var length = data.length;

                if (data != null) {

                    for (var i = 0; i < length; i++) {

                        seqArr.push(data[i].cm_seq);
                        nmArr.push(data[i].nm);
                        hpArr.push(data[i].hp);
                        partNmArr.push(data[i].part_nm);
                    }
                }

            }).fail(common.ajaxError)
            .always(function () {
                return false;
            });
    }

    // 직접입력 시 담당자 추가
    function managerInsert(elementVal) {

        if (elementVal != '') {
            var valArr = elementVal.split('/');
            var hp = '';
            var part_nm = '';
            var obj = {};


            if (valArr[1] != undefined || valArr[2] != undefined) {

                hp = valArr[1];
                part_nm = valArr[2];
            }

            jQuery.when(
                jQuery.ajax({
                    url: '../event/setManagerInsert',
                    type: 'POST',
                    async: false,
                    contentType: 'application/json; charset=utf-8',
                    dataType: 'json',
                    data: JSON.stringify({
                        nm: valArr[0],
                        hp: hp,
                        part_nm: part_nm
                    })
                })
            ).then(function (data) {

                    if (data.tmpOk == 0) {
                        return false;
                    }

                    obj.id = data.seq;
                    obj.val = valArr[0] + ' / ' + hp + ' / ' + part_nm;
                    obj.text = null;
                    obj.text2 = null;

                    managerList.push(obj);

                }).fail(common.ajaxError)
                .always(function () {
                    return false;
                });

            return obj.id;
        }

        if (elementVal == '') {
            return '';
        }
    }

    // 상황처리 유효성 체크
    function processChk() {

        var prc = 'prc_user_nm';
        var seize = 'seize_user_nm';

        if (jQuery('#prc_user_input').length > 0) prc = 'prc_user_input'
        if (jQuery('#seize_user_input').length > 0) seize = 'seize_user_input'

        var idPrc = jQuery('#' + prc);
        var idSeize = jQuery('#' + seize);
        var prcFl = jQuery('#prc_fl');
        var prcCont = jQuery('#prc_cont');
        var seizeFl = jQuery('#seize_fl');
        var seizeDt = jQuery('#seize_dt');

        if (prcFl.is(':checked') != true && idPrc.val() != '') {

            common.setOSXModal('승인여부를 선택하세요.');

            return false;
        }

        if (!(prcFl.is(':checked') == true && idPrc.val() != '') && (seizeFl.is(':checked') == true || idSeize.val() != '' || seizeDt.val() != '')) {

            common.setOSXModal('먼저 승인처리를 확인하세요.');

            return false;
        }

        if ((prcFl.is(':checked') == true && idPrc.val() != '') && !(seizeFl.is(':checked') == true && idSeize.val() != '' && seizeDt.val() != '' && seizeDt.val().length == 10) && !(seizeFl.is(':checked') != true && idSeize.val() == '' && seizeDt.val() == '')) {

            common.setOSXModal('영치결과 여부 선택 및 영치일, 처리내용, 영치 담당자를 확인하세요.');

            return false;
        }
    }

    // 관심차량 가져오기(Select)
    function getInterestCarList() {

        var carno = '';
        var interestSelect = jQuery('#interestSelect');

        if (sitPcsSel != undefined) {

            var sitPcsList = jQuery('#sitPcsList');
            var sitPcsIds = sitPcsList.jqGrid('getDataIDs');
            var sitPcsIdsLength = sitPcsIds.length;

            for (var i = 0; i < sitPcsIdsLength; i++) {
                carno += (carno == '') ? sitPcsList.jqGrid('getRowData', sitPcsIds[i]).car_no : ',' + sitPcsList.jqGrid('getRowData', sitPcsIds[i]).car_no;
            }
        }

        if (sitPcsSel == undefined) {
            carno = 'null';
        }

        jQuery.when(
            jQuery.ajax({
                url: './getInterestCarList',
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                async: false,
                data: carno
            })
        ).then(function (data) {

                if (jQuery('#interestSelect option').length > 0) {
                    jQuery('#interestSelect option').remove();
                }

                var html = '';

                var length = data.length;

                if (length > 0) {

                    for (var i = 0; i < length; i++) {

                        html += '<option value="' + data[i].id + '">' + data[i].val + '</option>';
                    }
                }

                if (length == 0) {

                    html += '<option value="none">관심차량이 없습니다.</option>'
                }

                interestSelect.append(html);

            }).fail(common.ajaxError)
            .always(function () {

                jQuery('#interestSelect').selectpicker('refresh');

                return false;
            });
    }

    // 레이어 가져오기(Select)
    function getCctvTypeList() {
        jQuery.when(
            jQuery.ajax({
                url: './getCctvTypeList',
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                async: false
            })
        ).then(function (data) {

                if (jQuery('#cctvTypeSelect option').length > 0) {
                    jQuery('#cctvTypeSelect option').remove();
                }

                var html = '';

                var length = data.length;

                if (length > 0) {

                    for (var i = 0; i < length; i++) {
                        html += '<option value="' + data[i].id + '"data-thumbnail="' + jQuery.fn.sysUrl + '/res/assets/img/cctvicon/' + data[i].id + '_FT.png" data-content="' + data[i].val + '" selected>' + data[i].val + '</option>';
                    }

                    return jQuery('#cctvTypeSelect').append(html);
                }
            }).fail(common.ajaxError)
            .always(function () {
                return false;
            });
    }

    // 관심차량 이동경로 좌표 가져오기
    function getRouteGeoData(car_no) {

        var sitOcrDt = "";

        var sitPcsList = jQuery('#sitPcsList');
        var sitPcsIds = sitPcsList.jqGrid('getDataIDs');
        var sitPcsIdsLength = sitPcsIds.length;

        for (var i = 0; i < sitPcsIdsLength; i++) {

            if (car_no == sitPcsList.jqGrid('getRowData', sitPcsIds[i]).car_no) {

                sitOcrDt = sitPcsList.jqGrid('getRowData', sitPcsIds[i]).ocr_dts;
            }
        }

        jQuery.when(
            jQuery.ajax({
                url: '../event/getRouteGeoData',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify({
                    car_no: car_no,
                    reg_dts: sitOcrDt,
                    gubun: 'monitor'
                }),
                async: false
            })
        ).then(function (data) {

                var tmp;
                var point = [];

                tmp = data;

                var tmpLength = tmp.length;

                if (tmpLength > 0) {

                    jQuery('#routeLineDiv').css('display', 'block');

                    for (var i = 0; i < tmpLength; i++) {

                        var tmpWgsPoint = [];

                        tmpWgsPoint.push(tmp[i].point_x);
                        tmpWgsPoint.push(tmp[i].point_y);

                        var tmpTmPoint = [];
                        tmpTmPoint = openLayers.proj.transform(tmpWgsPoint, 'EPSG:4326', 'EPSG:5181');

                        point.push(Math.round(tmpTmPoint[0] * 2.5));
                        point.push(Math.round(tmpTmPoint[1] * 2.5));
                    }
                    getRouteLine(point);
                }

                if (tmpLength == 0) {

                    common.setOSXModal('이동경로가 존재하지 않습니다.');

                    mapInfo.map.removeLayer(routeLayer);
                    routeLayer = undefined;
                }

            }).fail(common.ajaxError)
            .always(function () {
                return false;
            });
    }

    // 관심차량 이동경로 표출
    function getRouteLine(point) {

        var idx = 0;
        var path = [];
        var nodes = [];

        if (routeLayer != undefined) {
            mapInfo.map.removeLayer(routeLayer);
        }

        jQuery.when(
            jQuery.ajax({
                url: 'http://' + gisWebserviceIp + ':' + gisWebservicePort + '/tax/movingRoute',
                type: 'GET',
                contentType: 'text/plain; charset=utf-8',
                data: {
                    point: JSON.stringify(point)
                },
                dataType: 'jsonp',
                async: true
            })
        ).then(function (resData) {

                path = resData.path;
                nodes = resData.nodes;

            }).fail(common.ajaxError)
            .always(function () {

                var pathLength = path.length;
                var nodesLength = nodes.length;

                var features = [];

                for (var i = 0; i < pathLength; i++) {

                    path[i] = openLayers.proj.fromLonLat(openLayers.proj.transform(path[i], 'EPSG:5181', 'EPSG:4326'));
                }

                for (var i = 0; i < nodesLength; i++) {

                    nodes[i] = openLayers.proj.fromLonLat(openLayers.proj.transform(nodes[i], 'EPSG:5181', 'EPSG:4326'));
                }

                var lineString = new openLayers.geom.LineString([]);

                lineString.setCoordinates(path);

                var routeCoords = lineString.getCoordinates();
                var routeLength = routeCoords.length;

                var routeFeature = new openLayers.Feature({
                    type: 'route',
                    geometry: lineString
                });

                routeFeature.setStyle(
                    new openLayers.style.Style({
                        stroke: new openLayers.style.Stroke({
                            width: 6, color: [237, 212, 0, 0.8]
                        }),
                        zIndex: 2
                    })
                );

                var startMarker = new openLayers.Feature({
                    geometry: new openLayers.geom.Point(routeCoords[0]),
                    name: 'start'
                });

                startMarker.setStyle(
                    [
                        new openLayers.style.Style({
                            image: new openLayers.style.Icon({
                                scale: .7,
                                opacity: 1,
                                rotateWithView: false,
                                anchor: [0.5, 1],
                                src: jQuery.fn.sysUrl + '/res/assets/img/example/number_1.png'
                            }),
                            zIndex: 5
                        }), new openLayers.style.Style({
                        image: new openLayers.style.Circle({
                            radius: 6,
                            fill: new openLayers.style.Fill({color: '#ff0000'}),
                            stroke: new openLayers.style.Stroke({color: '#ff0000'})
                        }),
                        zIndex: 4
                    })
                    ]
                );

                features.push(startMarker);

                if (point.length > 2) {
                    // 경유지
                    if (nodesLength > 0) {
                        for (var i = 1; i < pathLength - 1; i++) {

                            for (var j = 0; j < nodesLength; j++) {

                                if ((path[i][0] == nodes[j][0]) && (path[i][1] == nodes[j][1])) {

                                    var viaMarker = new openLayers.Feature({
                                        geometry: new openLayers.geom.Point(routeCoords[i])
                                    });

                                    viaMarker.setStyle(
                                        [new openLayers.style.Style({
                                            image: new openLayers.style.Icon({
                                                scale: .7,
                                                opacity: 1,
                                                rotateWithView: false,
                                                anchor: [0.5, 1],
                                                src: jQuery.fn.sysUrl + '/res/assets/img/example/number_' + parseInt(j + 2) + '.png'
                                            }),
                                            zIndex: 5
                                        }),
                                            new openLayers.style.Style({
                                                image: new openLayers.style.Circle({
                                                    radius: 6,
                                                    fill: new openLayers.style.Fill({color: '#FFFFFF'}),
                                                    stroke: new openLayers.style.Stroke({color: '#000000'})
                                                }),
                                                zIndex: 4
                                            })]
                                    );
                                    features.push(viaMarker);
                                }
                            }
                        }
                    }

                    features.push(routeFeature);

                    var endMarker = new openLayers.Feature({
                        geometry: new openLayers.geom.Point(routeCoords[routeLength - 1]),
                        name: 'end'
                    });

                    endMarker.setStyle(
                        [new openLayers.style.Style({
                            image: new openLayers.style.Icon({
                                scale: .7,
                                opacity: 1,
                                rotateWithView: false,
                                anchor: [0.5, 1],
                                src: jQuery.fn.sysUrl + '/res/assets/img/example/number_' + parseInt(nodesLength + 2) + '.png'
                            }),
                            zIndex: 5
                        }), new openLayers.style.Style({
                            image: new openLayers.style.Circle({
                                radius: 6,
                                fill: new openLayers.style.Fill({color: '#ff0000'}),
                                stroke: new openLayers.style.Stroke({color: '#ff0000'})
                            }),
                            zIndex: 4
                        })
                        ]
                    );
                    features.push(endMarker);
                }

                routeLayer = new openLayers.layer.Vector({
                    source: new openLayers.source.Vector({
                        features: features
                    })
                });

                mapInfo.map.addLayer(routeLayer);
                mapInfo.map.getView().setResolution(19.109257067871095);
                mapInfo.map.getView().setCenter(routeCoords[0]);
            });
    }

    // 관심차량 이동경로 목록
    function getRouteList(car_no) {

        jQuery('#routeLineList').empty();

        var sitPcsList = jQuery('#sitPcsList');
        var sitOcrDt = "";
        var sitPcsIds = sitPcsList.jqGrid('getDataIDs');
        var sitPcsIdsLength = sitPcsIds.length;


        for (var i = 0; i < sitPcsIdsLength; i++) {

            if (car_no == sitPcsList.jqGrid('getRowData', sitPcsIds[i]).car_no) {

                sitOcrDt = sitPcsList.jqGrid('getRowData', sitPcsIds[i]).ocr_dts;
            }
        }

        var tmp;

        jQuery.when(
            jQuery.ajax({
                url: './getRouteList',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                async: false,
                data: JSON.stringify({
                    car_no: car_no,
                    reg_dts: sitOcrDt
                })
            })
        ).then(function (data) {

                tmp = data;

                var tmpLength = tmp.length;

                if (tmpLength > 0) {
                    var html = '';
                    html = '<thead>' +
                    '<tr>' +
                    '<th>CCTV 명</th>' +
                    '<th>위치(주소)</th>' +
                    '<th>발생일시</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>';
                    for (var i = 0; i < tmpLength; i++) {
                        html += '<tr>' +
                        '<td>' + tmp[i].cctv_nm + '</td>' +
                        '<td>' + (tmp[i].cctv_adres == null ? '' : tmp[i].cctv_adres) + '</td>' +
                        '<td>' + tmp[i].reg_dts + '</td>' +
                        '</tr>';
                    }
                    html += '</tbody>';

                    return jQuery('#routeLineList').append(html);
                }
            }).fail(common.ajaxError)
            .always(function () {
                return false;
            });
    }

    // GIS CCTV 마커 표출
    function getCctvGeoData(cctv_type, gridSel, GridSetYn) {

        if (cctvMarkerLayer != undefined) {

            mapInfo.map.removeLayer(cctvMarkerLayer);
            cctvMarkerLayer = undefined
        }

        if (GridSetYn == undefined) {

            GridYn = 'N';
        }

        var tmp;
        var tmpLength;
        var tmpPoint = [];
        var wgsPoint = [];
        var tmPoint = [];
        var features = [];
        var strCctvType = '';
        var cctvIds = '';

        if (cctv_type != null) {
            for (var i = 0; i < cctv_type.length; i++) {
                strCctvType += (strCctvType == '' ) ? strCctvType += cctv_type[i] : ',' + cctv_type[i];
            }
        }

        if (cctv_type == null) {
            strCctvType = 'null';
        }

        if (gridSel == 0) {

            gridSel = 'noGridData';
        }

        if (gridSel != 0) {

            var sitPcsList = jQuery('#sitPcsList');
            var sitPcsIds = sitPcsList.jqGrid('getDataIDs');
            var sitPcsIdsLength = sitPcsIds.length;

            for (var i = 0; i < sitPcsIdsLength; i++) {

                cctvIds += (cctvIds == '') ? cctvIds += sitPcsList.jqGrid('getRowData', sitPcsIds[i]).cctv_id : ',' + sitPcsList.jqGrid('getRowData', sitPcsIds[i]).cctv_id;
            }
        }

        jQuery.when(
            jQuery.ajax({
                url: './getCctvGeoData',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                async: false,
                data: JSON.stringify({
                    cctv_type: strCctvType,
                    cctv_id: gridSel,
                    cctvIds: cctvIds,
                    tri_seq: triSeq
                })
            })
        ).then(function (data) {
                tmp = data;
                tmpLength = tmp.length;
                cctvCnt = tmpLength;
            }).fail(common.ajaxError)
            .always(function () {
                return false;
            });

        if (tmpLength > 0) {
            for (var i = 0; i < tmpLength; i++) {
                tmpPoint.push(+tmp[i].point_x);
                tmpPoint.push(+tmp[i].point_y);
                wgsPoint.push(tmpPoint);
                tmPoint[i] = openLayers.proj.fromLonLat(wgsPoint[i]);
                tmpPoint = [];
            }

            var lineString = new openLayers.geom.LineString([]);

            lineString.setCoordinates(tmPoint);

            var routeCoords = lineString.getCoordinates();
            var routeLength = routeCoords.length;

            for (var i = 0; i < routeLength; i++) {
                var cctvMarker = new openLayers.Feature({
                    geometry: new openLayers.geom.Point(routeCoords[i]),
                    id: tmp[i].cctv_id,
                    cctvTy: tmp[i].cctv_type,
                    name: tmp[i].cctv_type + '_' + tmp[i].cctv_rotate,
                    type: 'cctvMarker'
                });

                features.push(cctvMarker);
            }

            var source = new openLayers.source.Vector({
                features: features
            });

            var clusterSource = new openLayers.source.Cluster({
                distance: 70,
                source: source
            });

            var styleCache = {};
            cctvMarkerLayer = new openLayers.layer.Vector({
                source: clusterSource,
                style: function (feature) {
                    var size = feature.get('features').length;
                    var style = styleCache[size];
                    style = new openLayers.style.Style({
                        image: new openLayers.style.Icon({
                            scale: .7,
                            opacity: 1,
                            rotateWithView: false,
                            anchor: [0.5, 1],
                            src: jQuery.fn.sysUrl + '/res/assets/img/cctvicon/' + feature.get('features')[size - 1].O.name + '.png'
                        }),
                        zIndex: 5
                    });
                    styleCache[size] = style;
                    return style;
                }
            });

            cctvMarkerLayer.setZIndex(1);

            mapInfo.map.addLayer(cctvMarkerLayer);

            if (gridSel == 'noGridData') {

                noGridDataPtX = tmp[0].point_x;
                noGridDataPtY = tmp[0].point_y;
                triSeq = tmp[0].tri_seq;

                zoomGeoData(tmp[0].point_x, tmp[0].point_y);
                getNetSetCctvGeoData(tmp[0].cctv_id, jQuery('#cctvTypeSelect').val());
            }

            if (gridSel != 'noGridData') {

                zoomGeoData(jQuery('#sitPcsList').jqGrid('getRowData', sitPcsSel).point_x, jQuery('#sitPcsList').jqGrid('getRowData', sitPcsSel).point_y);

                if (GridYn == 'N') {

                    getNetSetCctvGeoData(jQuery('#sitPcsList').jqGrid('getRowData', sitPcsSel).cctv_id, jQuery('#cctvTypeSelect').val());
                }
            }

        } else {

            if (reCctvMarkerLayer != undefined) {

                mapInfo.map.removeLayer(reCctvMarkerLayer);
                reCctvMarkerLayer = undefined;
            }

            if (jQuery('#selectMarker').length) {

                jQuery('#selectMarker').hide();
            }

            if (jQuery('.selectNetSetMarker').length) {

                jQuery('.selectNetSetMarker').hide();
            }

            if (netSetLayer != undefined) {

                mapInfo.map.removeLayer(netSetLayer);
            }

            if (crossHairLayer != undefined) {

                mapInfo.map.removeLayer(crossHairLayer);
            }

            if (vmsUseYn == 'Y') {

                destroyCastNetVms();
            }

            common.setOSXModal('레이어에 해당하는 CCTV가 없습니다.');
        }
    }

    // 반경 500m 표시
    function drawCrossHair(point, radius) {


        if (crossHairLayer != undefined) {

            mapInfo.map.removeLayer(crossHairLayer);
            crossHairLayer = undefined;
        }

        var features = [];
        var circle = new openLayers.geom.Circle(point, radius);
        var crossHairFeature = new openLayers.Feature(circle);
        crossHairFeature.setStyle(
            new openLayers.style.Style({
                fill: new openLayers.style.Fill({
                    color: 'rgba(241, 196, 15, 0.25)'
                }),
                stroke: new openLayers.style.Stroke({
                    color: 'rgba(255, 0, 0, 1)',
                    lineCap: 'round',
                    width: 3,
                    lineDash: [5, 5]
                }),
                text: new openLayers.style.Text({
                    font: '13px sans-serif',
                    text: '반경 500m',
                    stroke: new openLayers.style.Stroke({color: '#FF0000'}),
                    fill: new openLayers.style.Fill({color: '#190707'}),
                    offsetY: -60,
                    offsetX: 0
                }),
                zIndex: 5
            })
        );

        features.push(crossHairFeature);

        crossHairLayer = new openLayers.layer.Vector({
            source: new openLayers.source.Vector({
                features: features
            })
        });

        crossHairLayer.setZIndex(0);

        mapInfo.map.addLayer(crossHairLayer);
    }

    // 이벤트 발생 CCTV 포커스 및 GIF 이미지 씌우기
    function zoomGeoData(point_x, point_y) {

        var selectMarker = jQuery('#selectMarker');

        if (selectMarker.length > 0) {
            selectMarker.remove();
        }

        var point = [];

        point.push(+point_x);
        point.push(+point_y);

        jQuery('#overlayTmp').append('<div id="selectMarker"></div>');

        selectMarker = jQuery('#selectMarker');

        selectMarker.css({
            width: '64px',
            height: '64px',
            background: 'url("' + jQuery.fn.sysUrl + '/res/assets/img/example/point_select.gif") no-repeat 0% 0% transparent',
            zIndex: '0'
        });

        var selectCctvMarker = new openLayers.Overlay({
            position: openLayers.proj.fromLonLat(point),
            element: selectMarker[0],
            offset: [0, -19],
            positioning: 'center-center',
            stopEvent: false,
            insertFirst: false
        });

        mapInfo.map.addOverlay(selectCctvMarker);

        mapInfo.map.getView().setCenter(openLayers.proj.fromLonLat(point));
        mapInfo.map.getView().setResolution(2.388657133483887);

        drawCrossHair(openLayers.proj.fromLonLat(point), 630);
    }

    // 이벤트 발생 CCTV 주변 투망감시 설정된 CCTV 표출 및 GIF 이미지 씌우기
    function getNetSetCctvGeoData(cctv_id, cctv_type, mapClkFeature) {
        var selectNetSetMarkerCl = jQuery('.selectNetSetMarker');

        if (netSetLayer != undefined) {
            mapInfo.map.removeLayer(netSetLayer);
            netSetLayer = undefined;
        }

        if (selectNetSetMarkerCl.length > 0) {
            selectNetSetMarkerCl.remove();
        }

        if (mapClkFeature != undefined) {

            var features = [];
            var tmpPoint = [];
            var point = [];

            tmpPoint.push(+mapClkFeature.O.name.split(',')[0]);
            tmpPoint.push(+mapClkFeature.O.name.split(',')[1]);
            point.push(openLayers.proj.fromLonLat(tmpPoint));

            var reCctvMarker = new openLayers.Feature({
                geometry: new openLayers.geom.Point(point[0]),
                id: mapClkFeature.O.id,
                cctvTy: mapClkFeature.O.name.split(',')[2],
                name: mapClkFeature.O.name.split(',')[2] + '_' + mapClkFeature.O.name.split(',')[3],
                type: 'cctvMarker'
            });

            features.push(reCctvMarker);

            if (reCctvMarkerLayer != undefined) {
                var length = reCctvMarkerLayer.getSource().getFeatures().length;

                mapInfo.map.removeLayer(reCctvMarkerLayer);
                for (var i = 0; i < length; i++) {
                    features.push(reCctvMarkerLayer.getSource().getFeatures()[i].O.features[0]);
                }
            }

            var source = new openLayers.source.Vector({
                features: features
            });

            var clusterSource = new openLayers.source.Cluster({
                distance: 70,
                source: source
            });

            var styleCache = {};
            reCctvMarkerLayer = new openLayers.layer.Vector({
                source: clusterSource,
                style: function (feature) {
                    var size = feature.get('features').length;
                    var style = styleCache[size];
                    style = new openLayers.style.Style({
                        image: new openLayers.style.Icon({
                            scale: .7,
                            opacity: 1,
                            rotateWithView: false,
                            anchor: [0.5, 1],
                            src: jQuery.fn.sysUrl + '/res/assets/img/cctvicon/' + feature.get('features')[size - 1].O.name + '.png'
                        }),
                        zIndex: 5
                    });
                    styleCache[size] = style;
                    return style;
                }
            });

            reCctvMarkerLayer.setZIndex(1);

            mapInfo.map.addLayer(reCctvMarkerLayer);
        }


        var cctv;
        var cctvLength;
        var tmpPoint = [];
        var wgsPoint = [];
        var tmPoint = [];
        var features = [];
        var strCctvType = '';

        if (cctv_type != null) {
            for (var i = 0; i < cctv_type.length; i++) {
                strCctvType += (strCctvType == '' ) ? strCctvType += cctv_type[i] : ',' + cctv_type[i];
            }
        }
        if (cctv_type == null) {
            strCctvType = 'null';
        }

        jQuery.when(
            jQuery.ajax({
                url: './getNetSetCctvGeoData',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                async: false,
                data: JSON.stringify({
                    cctv_id: cctv_id,
                    cctv_type: strCctvType
                })
            })
        ).then(function (data) {
                cctv = data;
                cctvLength = cctv.length;
            }).fail(common.ajaxError)
            .always(function () {
                return false;
            });

        if (cctvLength > 0) {

            for (var i = 0; i < cctvLength; i++) {
                tmpPoint.push(+cctv[i].point_x);
                tmpPoint.push(+cctv[i].point_y);
                wgsPoint.push(tmpPoint);
                tmPoint[i] = openLayers.proj.fromLonLat(wgsPoint[i]);
                tmpPoint = [];
            }

            var lineString = new openLayers.geom.LineString([]);

            lineString.setCoordinates(tmPoint);

            var routeCoords = lineString.getCoordinates();
            var routeLength = routeCoords.length;

            var overlayTmp = jQuery('#overlayTmp');

            for (var i = 0; i < routeLength; i++) {
                var netSetMarker = new openLayers.Feature({
                    geometry: new openLayers.geom.Point(routeCoords[i]),
                    id: cctv[i].cctv_id,
                    cctvTy: cctv[i].cctv_type,
                    name: cctv[i].point_x + ',' + cctv[i].point_y + ',' + cctv[i].cctv_type + ',' + cctv[i].cctv_rotate,
                    type: 'netSetMarker'
                });

                features.push(netSetMarker);

                overlayTmp.append('<div class="selectNetSetMarker"/>');

                selectNetSetMarkerCl = jQuery('.selectNetSetMarker');

                selectNetSetMarkerCl.css({
                    width: '64px',
                    height: '64px',
                    background: 'url("' + jQuery.fn.sysUrl + '/res/assets/img/example/point_carrot.gif") no-repeat 0% 0% transparent',
                    zIndex: '0'
                });

                var selectNetSetMarker = new openLayers.Overlay({
                    position: openLayers.proj.fromLonLat([+cctv[i].point_x, +cctv[i].point_y]),
                    element: selectNetSetMarkerCl.eq(i)[0],
                    offset: [0, -19],
                    positioning: 'center-center',
                    stopEvent: false,
                    insertFirst: false
                });

                mapInfo.map.addOverlay(selectNetSetMarker);

                netSetMarker.setStyle(
                    new openLayers.style.Style({
                        image: new openLayers.style.Icon({
                            scale: .7,
                            opacity: 1,
                            rotateWithView: false,
                            anchor: [0.5, 1],
                            src: jQuery.fn.sysUrl + '/res/assets/img/cctvicon/' + cctv[i].cctv_type + '_' + cctv[i].cctv_rotate + '.png'
                        })
                    })
                );
            }

            netSetLayer = new openLayers.layer.Vector({
                source: new openLayers.source.Vector({
                    features: features
                })
            });

            netSetLayer.setZIndex(1);

            mapInfo.map.addLayer(netSetLayer);

            if (vmsUseYn == 'Y') {

                startVmsCastNet(cctv, cctv_id);
            }
        }

        if (cctvLength == 0) {
            selectNetSetMarkerCl.remove();
            common.setOSXModal('투망감시 설정한 CCTV가 없습니다.');

            if (vmsUseYn == 'Y') {

                destroyCastNetVms();
            }
        }
    }

    // 분포도분석
    function getDistributionGeoData() {

        if (distributionLayer != undefined) {
            mapInfo.map.removeLayer(distributionLayer);
            distributionLayer = undefined;
        }

        var tmp;
        var tmpLength;
        var tmpPoint = [];
        var wgsPoint = [];
        var tmPoint = [];
        var features = [];

        jQuery.when(
            jQuery.ajax({
                url: './getDistributionGeoData',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                async: false
            })
        ).then(function (data) {
                tmp = data;
                tmpLength = tmp.length;
            }).fail(common.ajaxError)
            .always(function () {
                return false;
            });

        if (tmpLength > 0) {

            for (var i = 0; i < tmpLength; i++) {
                tmpPoint.push(+tmp[i].point_x);
                tmpPoint.push(+tmp[i].point_y);
                wgsPoint.push(tmpPoint);
                tmPoint[i] = openLayers.proj.fromLonLat(wgsPoint[i]);
                tmpPoint = [];
            }

            var lineString = new openLayers.geom.LineString([]);

            lineString.setCoordinates(tmPoint);

            var routeCoords = lineString.getCoordinates();
            var routeLength = routeCoords.length;

            for (var i = 0; i < routeLength; i++) {
                var distributionMarker = new openLayers.Feature({
                    geometry: new openLayers.geom.Point(routeCoords[i]),
                    type: 'distributionMarker'
                });

                features.push(distributionMarker);
            }

            distributionLayer = new openLayers.layer.Heatmap({
                source: new openLayers.source.Vector({
                    features: features
                }),
                blur: 60,
                radius: 20
            });

            mapInfo.map.addLayer(distributionLayer);
            mapInfo.map.getView().setResolution(76.43702827148438);
            mapInfo.map.getView().setCenter(routeCoords[0]);
        }
        else {

            common.setOSXModal('체납차량 분포도분석이 존재하지 않습니다.');
        }
    }

    // 실시간 감시(socket.io)
    function realtimeObserve(conState) {

        if(conState == 'connect') {

            var lcStoreReatltime = '';
            var chkRealtime = '';
            var viewVms = jQuery('#view-vms');

            if (wsRealtimeObserveIp != '' && wsRealtimeObservePort != '') {

                socket = io.connect('http://' + wsRealtimeObserveIp + ':' + wsRealtimeObservePort);

                socket.on('realtimeInfoData', function (realtimeInfoData) {

                    lcStoreReatltime = localStorage['realtimeObserve'];
                    chkRealtime = jQuery('#realtimeObserve').is(':checked');

                    if (lcStoreReatltime == 'true' || chkRealtime == true) {

                        jQuery.when(
                            jQuery.ajax({
                                url: './getRealtimeSitPcsList',
                                type: 'POST',
                                contentType: 'text/plain; charset=utf-8',
                                async: false,
                                data: realtimeInfoData.data
                            })
                        ).then(function (data) {

                                if (data != null && data != '') {

                                    var gridData = {};

                                    gridData['cctv_id'] = data.cctv_id;
                                    gridData['ocr_dts'] = data.ocr_dts;
                                    gridData['tri_seq'] = data.tri_seq;
                                    gridData['point_x'] = data.point_x;
                                    gridData['point_y'] = data.point_y;
                                    gridData['car_no'] = data.car_no;
                                    gridData['tax_money_cnt'] = data.tax_money_cnt;
                                    gridData['reg_dts'] = data.reg_dts;
                                    gridData['state'] = data.state;

                                    var sitPcsList = jQuery('#sitPcsList');
                                    var sitPcsIds = sitPcsList.jqGrid('getDataIDs');
                                    var sitPcsIdsLength = sitPcsIds.length;

                                    for (var i = 0; i < sitPcsIdsLength; i++) {

                                        if (data.car_no == sitPcsList.jqGrid('getRowData', sitPcsIds[i]).car_no) {

                                            var rowId = sitPcsIds[i];
                                            sitPcsList.jqGrid('delRowData', rowId);
                                        }
                                    }

                                    var rowId = jQuery.jgrid.randId('jqgadd');

                                    sitPcsList.jqGrid('addRowData', rowId, gridData, 'first');
                                    sitPcsList.jqGrid('setSelection', sitPcsList.find('tr[id]:eq(0)').attr('id'));

                                    sitPcsSel = sitPcsList.jqGrid('getGridParam', 'selrow');

                                    jQuery('#interestSelect').selectpicker('refresh');
                                    jQuery('#ocrCctvPanel').css('display', 'block');

                                    getInterestCarList();

                                    if (vmsUseYn == 'Y') {

                                        startVms(viewVms, data.cctv_uid, data.cctv_id);
                                    }

                                    GridYn = 'Y';
                                    getCctvGeoData(jQuery('#cctvTypeSelect').val(), sitPcsSel, GridYn);
                                }
                            }).fail(common.ajaxError)
                            .always(function () {
                                return false;
                            });
                    }
                });
            }
            else {

                common.setOSXModal('잘못된 소켓 접속 정보입니다. 이벤트 수신이 불가합니다.');
            }
        }

        if(conState == 'disConnect') {

            socket.disconnect();
        }
    }

    // Base 지도 타일 변경
    function baseMapChange(id, layers, map) {

        var layer = layers[id];

        jQuery.when(
        ).then(function () {

                jQuery.fn.loadingStart();
            }).done(function () {

                if (layer) {

                    layer.setOpacity(1);
                    map.getLayers().setAt(0, layer);
                }
            })
            .fail(common.ajaxError)
            .always(function () {

                jQuery.fn.loadingComplete();
                return false;
            });
    }

    // 실시간 이벤트 발생 시 지도 좌측에 발생지점 CCTV 영상 표출
    function startVms(viewVms, cctv_uid, cctv_id) {
        try {

            destroyVms();

            var chkInit = initVms(viewVms.width(), viewVms.height());

            if (chkInit == 0) {

                common.setOSXModal('VLC를 지원하지 않는 브라우저거나, VLC가 정상 설치되어있지 않습니다.');
                return false;

            } else {

                playVms(cctv_uid, cctv_id);
            }
        } catch (e) {

            common.setOSXModal('**** 영상재생실패 ==== ' + e.name + ':' + e.message);
        }
    }

    // 투망감시 설정된 CCTV 영상 표출
    function startVmsCastNet(cctv, ocrCctvId) {
        var viewVms = jQuery('#view-castNetVms');
        try {

            destroyCastNetVms();

            viewVms.empty();
            var chkInit = initCastNetVms(cctv.length, viewVms.width(), viewVms.height());
            if (chkInit == 0) {

                common.setOSXModal('VLC를 지원하지 않는 브라우저거나, VLC가 정상 설치되어있지 않습니다.');
                return false;
            } else {

                playCastNetVms(cctv, ocrCctvId);
            }
        } catch (e) {

            common.setOSXModal('**** 영상재생실패 ==== ' + e.name + ':' + e.message);
        }
    }

    // CCTV 영상 팝업
    function startVmsPopup(cctvId) {

        var url = './popupVms?cctvId=' + cctvId + '&triSeq=' + triSeq;
        var vmsPlayer = window.open(url, 'vmsPlayer', 'status=no, width=1024, height=768');
    }

    // 지도 거리/면적
    function createHelpTooltip() {

        if (helpTooltipElement != undefined) {

            helpTooltipElement.remove();
        }

        helpTooltipElement = jQuery('<div id="tooltipHidden" class="tooltip hidden"></div>');

        helpTooltip = new openLayers.Overlay({
            element: helpTooltipElement[0],
            offset: [15, 0],
            positioning: 'center-left'
        });

        mapInfo.map.addOverlay(helpTooltip);
    }


    // 지도 거리/면적
    function createMeasureTooltip() {

        if (measureTooltipElement != undefined) {

            measureTooltipElement.remove();
        }

        measureTooltipElement = jQuery('<div id="tooltipMeasure" class="tooltip tooltip-measure"></div>');
        measureTooltipElement.css({
            position: 'relative',
            background: 'rgba(0, 0, 0, 0.5)',
            borderRadius: '4px',
            color: 'white',
            padding: '4px 8px',
            whiteSpace: 'nowrap',
            opacity: '1',
            fontWeight: 'bold'
        });

        measureTooltip = new openLayers.Overlay({
            element: measureTooltipElement[0],
            offset: [0, -15],
            positioning: 'bottom-center'
        });
        mapInfo.map.addOverlay(measureTooltip);
    }

    // 지도 거리/면적
    function formatArea(polygon) {
        var area = openLayers.Sphere.getArea(polygon);
        var output;
        if (area > 10000) {
            output = (Math.round(area / 1000000 * 100) / 100) +
            ' ' + 'km<sup>2</sup>';
        } else {
            output = (Math.round(area * 100) / 100) +
            ' ' + 'm<sup>2</sup>';
        }
        return output;
    };

    // 지도 거리/면적
    function formatLength(line) {
        var length = openLayers.Sphere.getLength(line);
        var output;
        if (length > 100) {
            output = (Math.round(length / 1000 * 100) / 100) +
            ' ' + 'km';
        } else {
            output = (Math.round(length * 100) / 100) +
            ' ' + 'm';
        }
        return output;
    };

    // 지도 거리/면적
    function addInteraction(btnType) {

        if (draw != undefined)
            mapInfo.map.removeInteraction(draw);

        var tooltipMeasure = jQuery('#tooltipMeasure');

        var type = (btnType == 'btnArea' ? 'Polygon' : 'LineString');

        if (measureLayer == undefined) {

            var source = new openLayers.source.Vector();
            measureLayer = new openLayers.layer.Vector({
                source: source,
                style: new openLayers.style.Style({
                    fill: new openLayers.style.Fill({
                        color: 'rgba(255, 255, 255, 0.2)'
                    }),
                    stroke: new openLayers.style.Stroke({
                        color: '#ffcc33',
                        width: 2
                    }),
                    image: new openLayers.style.Circle({
                        radius: 7,
                        fill: new openLayers.style.Fill({
                            color: '#ffcc33'
                        })
                    })
                })
            });

            mapInfo.map.addLayer(measureLayer);
        }


        draw = new openLayers.interaction.Draw({
            source: measureLayer.getSource(),
            type: type,
            style: new openLayers.style.Style({
                fill: new openLayers.style.Fill({
                    color: 'rgba(255, 255, 255, 0.2)'
                }),
                stroke: new openLayers.style.Stroke({
                    color: 'rgba(0, 0, 0, 0.5)',
                    lineDash: [10, 10],
                    width: 2
                }),
                image: new openLayers.style.Circle({
                    radius: 5,
                    stroke: new openLayers.style.Stroke({
                        color: 'rgba(0, 0, 0, 0.7)'
                    }),
                    fill: new openLayers.style.Fill({
                        color: 'rgba(255, 255, 255, 0.2)'
                    })
                })
            })
        });

        mapInfo.map.addInteraction(draw);

        createMeasureTooltip();
        createHelpTooltip();

        var listener;
        draw.on('drawstart',
            function (evt) {
                // set sketch
                sketch = evt.feature;

                var tooltipCoord = evt.coordinate;

                listener = sketch.getGeometry().on('change', function (evt) {
                    var geom = evt.target;
                    var output;

                    if (geom instanceof openLayers.geom.Polygon) {

                        output = formatArea(geom);
                        tooltipCoord = geom.getInteriorPoint().getCoordinates();
                    } else if (geom instanceof openLayers.geom.LineString) {

                        output = formatLength(geom);
                        tooltipCoord = geom.getLastCoordinate();
                    }
                    measureTooltipElement.html(output);
                    measureTooltip.setPosition(tooltipCoord);
                });
            }, this);

        draw.on('drawend',
            function () {
                measureTooltipElement.attr('class', 'tooltip tooltip-static');

                measureTooltipElement.css({
                    position: 'relative',
                    background: 'rgba(0, 0, 0, 0.5)',
                    borderRadius: '4px',
                    padding: '4px 8px',
                    opacity: '0.7',
                    whiteSpace: 'nowrap',
                    backgroundColor: '#ffcc33',
                    color: 'black',
                    border: '1px solid white'
                });

                measureTooltip.setOffset([0, -7]);
                // unset sketch
                sketch = null;
                // unset tooltip so that a new one can be created
                measureTooltipElement = null;
                createMeasureTooltip();
                openLayers.Observable.unByKey(listener);
            }, this);
    }

    return {
        inputCheckScript: inputCheckScript,
        setEvents: formcheck.setEvents,
        panelClear: panelClear,
        dataReload: dataReload,
        dataSend: dataSend,
        resizePanel: resizePanel,
        baseMapChange: baseMapChange,
        infoSetting: infoSetting,
        gridSetting: gridSetting,
        changeSwitchery: changeSwitchery,
        chkDupManager: chkDupManager,
        chkDbDupManager: chkDbDupManager,
        getInterestCarList: getInterestCarList,
        getCctvTypeList: getCctvTypeList,
        getRouteGeoData: getRouteGeoData,
        getRouteList: getRouteList,
        getCctvGeoData: getCctvGeoData,
        zoomGeoData: zoomGeoData,
        getNetSetCctvGeoData: getNetSetCctvGeoData,
        getDistributionGeoData: getDistributionGeoData,
        realtimeObserve: realtimeObserve,
        startVmsPopup: startVmsPopup,
        addInteraction: addInteraction
    }
});


require(['common', 'darkhand', 'local', 'openlayers', 'proj4', 'jquery', 'lightbox', 'socket.io', 'bootstrap-switchery', 'bootstrap-select'], function (common, darkhand, lc, openLayers, proj4, jQuery, lightbox, io, Switchery) {

    // 엔터 적용
    function enterCheck(idx) {

        if (idx === undefined) idx = 0;

        var tw = [];

        switch (idx) {
            case 0:

                tw.push({
                    chk: jQuery("#srcPanel :input"),
                    script: function () {

                        var lc = require('local');
                        return lc.inputCheckScript('srcPanel');
                    },
                    ret: "btnSrch",
                    state: function () {

                        var lc = require('local');

                        jQuery.fn.loadingStart();

                        lc.panelClear(true); // 전체 폼 초기화
                        lc.dataReload('user'); // 사용자 목록
                    }
                });

                if (authCrud.MOD_FL === 'Y') {

                    tw.push({
                        chk: jQuery("#infoPanel :input"),
                        script: function () {

                            var lc = require('local');
                            return lc.inputCheckScript('infoPanel');
                        },
                        ret: "btnSave",
                        state: function () {
                            // 적용 Event 처리
                            var lc = require('local');
                            lc.dataSend();
                            lc.chkDbDupManager();
                        }
                    });
                }
                break;
        }

        common.enterSend(tw);
    }

    // 페이지 로딩 완료 후 이벤트
    jQuery(function () {
        // 권한에 따른 버튼 비활성화
        if (authCrud.READ_FL === 'N') {

            jQuery('#btnSrch').attr('disabled', true);
        }
        if (authCrud.REG_FL === 'N' || authCrud.MOD_FL === 'N') {

            jQuery('#btnReg, #btnCancel').attr('disabled', true);
        }

        // 엔터키 이벤트 체크
        lc.setEvents();
        enterCheck(); // 엔터 적용
        openLayers.proj.setProj4(proj4); // OPENLAYER 3.36 이상부터는 proj setting 연동

        //openLayers
        mapInfo = mapInit('map', openLayers, {
            gisApiKey: gisApiKey,
            gisProjection: gisProjection,
            gisBoundsLeft: gisBoundsLeft,
            gisBoundsTop: gisBoundsTop,
            gisBoundsRight: gisBoundsRight,
            gisBoundsBottom: gisBoundsBottom
        });

        // 차량번호 사진(lightbox)
        lightbox.option({
            'alwaysShowNavOnTouchDevices': false,
            'disableScrolling': true,
            'maxWidth': 1024,
            'maxHeight': 768
        });

        // 관심차량 Select 설정
        jQuery('#interestSelect').selectpicker({
            style: 'btn-warning btn-sm',
            width: '130px'
        });

        lc.chkDbDupManager(); // 중복확인을 위한 전체 담당자 가져오기
        lc.getCctvTypeList(); // 레어이 Select 가져오기

        jQuery('#prc_user_nm, #seize_user_nm').css({
            width: '60%',
            display: 'inline',
            marginLeft: '5%'
        });

        // 상세정보 폼
        common.setSelectOpt(jQuery('#prc_user_nm'), '-선택-', managerList); // 승인처리 담당자
        common.setSelectOpt(jQuery('#seize_user_nm'), '-선택-', managerList); // 영치결과 담당자

        // 영치일자 포커스 하이픈 삭제
        jQuery('#seize_dt').click(function () {

            var seizeDtVal = jQuery('#seize_dt').val();

            jQuery('#seize_dt').val(seizeDtVal.replace(/-/gi, ''));
        });

        // 영치일자 포커스 아웃 하이픈 추가
        jQuery('#seize_dt').blur(function () {

            var seizeDtVal = jQuery('#seize_dt').val();
            var resSeizeDtVal = '';
            var seizeDtValLength = seizeDtVal.length;

            if (seizeDtValLength <= 4) {

                return jQuery('#seize_dt').val(seizeDtVal);
            }

            if (seizeDtValLength > 4 && seizeDtValLength < 7) {

                resSeizeDtVal += seizeDtVal.substr(0, 4);
                resSeizeDtVal += '-'
                resSeizeDtVal += seizeDtVal.substr(4);

                return jQuery('#seize_dt').val(resSeizeDtVal);
            }
            if (seizeDtValLength >= 7) {

                resSeizeDtVal += seizeDtVal.substr(0, 4);
                resSeizeDtVal += '-'
                resSeizeDtVal += seizeDtVal.substr(4, 2);
                resSeizeDtVal += '-'
                resSeizeDtVal += seizeDtVal.substr(6);

                return jQuery('#seize_dt').val(resSeizeDtVal);
            }
        });

        // CCTV 레이어 select 활성화
        jQuery('#cctvTypeSelect').selectpicker({
            style: 'btn-warning btn-sm',
            width: '190px',
            selectAllText: '모두 선택',
            deselectAllText: '선택 해제'

        });

        lc.gridSetting('sitPcs'); // 상황처리 목록 생성

        // 관심차량 이동경로 선택 시
        jQuery('#interestSelect').change(function () {

            if (jQuery('#interestSelect option:selected').val() != undefined) {

                jQuery('#disStandChk').attr('disabled', true);
                jQuery('#cctvTypeSelect').attr('disabled', true);
                jQuery('#cctvTypeSelect').selectpicker('refresh');
                jQuery('#realtimeObserve').attr('disabled', true);
                jQuery('#sitPcsBlock').css('pointer-events', 'none');
                jQuery('#btnSave').attr('disabled', true);

                if (jQuery('#interestSelect option:selected').val() != 'none') {

                    if (cctvMarkerLayer != undefined) {

                        mapInfo.map.removeLayer(cctvMarkerLayer);
                    }

                    if (netSetLayer != undefined) {

                        mapInfo.map.removeLayer(netSetLayer);
                    }

                    if (crossHairLayer != undefined) {

                        mapInfo.map.removeLayer(crossHairLayer);
                    }

                    if (jQuery('#ocrCctvPanel').css('display') == 'block') {

                        jQuery('#ocrCctvPanel').css('display', 'none');
                    }

                    if (jQuery('#selectMarker').length) {

                        jQuery('#selectMarker').hide();
                    }

                    if (jQuery('.selectNetSetMarker').length) {

                        jQuery('.selectNetSetMarker').hide();
                    }

                    for (var i = 0; i < vurixCastnet.length; i++) {

                        if (isVurixCastnetInit[i] != null && isVurixCastnetPlaying[i] == true) {

                            vurixCastnet[i].playlist.stop();
                        }

                        jQuery('#vurix' + i).hide();
                    }

                    lc.getRouteGeoData(jQuery('#interestSelect option:selected').text());
                    lc.getRouteList(jQuery('#interestSelect option:selected').text());
                }
            }
            if (jQuery('#interestSelect option:selected').val() == undefined) {

                jQuery('#disStandChk').attr('disabled', false);
                jQuery('#cctvTypeSelect').attr('disabled', false);
                jQuery('#cctvTypeSelect').selectpicker('refresh');
                jQuery('#realtimeObserve').attr('disabled', false);
                jQuery('#sitPcsBlock').css('pointer-events', 'auto');
                jQuery('#btnSave').attr('disabled', false);

                if (routeLayer != undefined) {

                    mapInfo.map.removeLayer(routeLayer);
                    routeLayer = undefined;

                    if (cctvMarkerLayer != undefined) {

                        mapInfo.map.addLayer(cctvMarkerLayer);

                        if (sitPcsSel == undefined) {

                            lc.zoomGeoData(noGridDataPtX, noGridDataPtY);
                        }
                        else {

                            lc.zoomGeoData(jQuery('#sitPcsList').jqGrid('getRowData', sitPcsSel).point_x, jQuery('#sitPcsList').jqGrid('getRowData', sitPcsSel).point_y);
                        }

                        for (var i = 0; i < vurixCastnet.length; i++) {

                            if (isVurixCastnetInit[i] != null && isVurixCastnetPlaying[i] == true) {

                                vurixCastnet[i].playlist.play();
                            }

                            jQuery('#vurix' + i).show();
                        }
                    }
                    if (netSetLayer != undefined) {

                        mapInfo.map.addLayer(netSetLayer);
                    }
                }

                if (jQuery('#routeLineDiv').css('display') == 'block') {

                    jQuery('#routeLineDiv').css('display', 'none');
                }

                if (jQuery('#selectMarker').length) {

                    jQuery('#selectMarker').show();
                }

                if (jQuery('.selectNetSetMarker').length) {

                    jQuery('.selectNetSetMarker').show();
                }
            }
        });

        // 분포도 분석 체크 시
        jQuery('#disStandChk').change(function () {

            if (jQuery('#disStandChk').is(':checked')) {

                jQuery('#cctvTypeSelect').attr('disabled', true);
                jQuery('#cctvTypeSelect').selectpicker('refresh');
                jQuery('#interestSelect').attr('disabled', true);
                jQuery('#interestSelect').selectpicker('refresh');
                jQuery('#realtimeObserve').attr('disabled', true);
                jQuery('#sitPcsBlock').css('pointer-events', 'none');
                jQuery('#btnSave').attr('disabled', true);

                if (cctvMarkerLayer != undefined) {

                    mapInfo.map.removeLayer(cctvMarkerLayer);
                }

                if (netSetLayer != undefined) {

                    mapInfo.map.removeLayer(netSetLayer);
                }

                if (crossHairLayer != undefined) {

                    mapInfo.map.removeLayer(crossHairLayer);
                }

                if (jQuery('#ocrCctvPanel').length) {

                    jQuery('#ocrCctvPanel').css('display', 'none');
                }

                if (jQuery('#selectMarker').length) {

                    jQuery('#selectMarker').hide();
                }

                if (jQuery('.selectNetSetMarker').length) {

                    jQuery('.selectNetSetMarker').hide();
                }

                for (var i = 0; i < vurixCastnet.length; i++) {

                    if (isVurixCastnetInit[i] != null && isVurixCastnetPlaying[i] == true) {

                        vurixCastnet[i].playlist.stop();
                    }

                    jQuery('#vurix' + i).hide();
                }

                lc.getDistributionGeoData();
            }
            else {

                jQuery('#cctvTypeSelect').attr('disabled', false);
                jQuery('#cctvTypeSelect').selectpicker('refresh');
                jQuery('#interestSelect').attr('disabled', false);
                jQuery('#interestSelect').selectpicker('refresh');
                jQuery('#realtimeObserve').attr('disabled', false);
                jQuery('#sitPcsBlock').css('pointer-events', 'auto');
                jQuery('#btnSave').attr('disabled', false);

                if (distributionLayer != undefined) {

                    mapInfo.map.removeLayer(distributionLayer);
                    distributionLayer = undefined;

                    if (cctvMarkerLayer != undefined) {
                        mapInfo.map.addLayer(cctvMarkerLayer);

                        if (sitPcsSel == undefined) {


                            lc.zoomGeoData(noGridDataPtX, noGridDataPtY);
                        }
                        else {

                            lc.zoomGeoData(jQuery('#sitPcsList').jqGrid('getRowData', sitPcsSel).point_x, jQuery('#sitPcsList').jqGrid('getRowData', sitPcsSel).point_y);
                        }

                        for (var i = 0; i < vurixCastnet.length; i++) {

                            if (isVurixCastnetInit[i] != null && isVurixCastnetPlaying[i] == true) {

                                vurixCastnet[i].playlist.play();
                            }

                            jQuery('#vurix' + i).show();
                        }
                    }

                    if (netSetLayer != undefined) {
                        mapInfo.map.addLayer(netSetLayer);
                    }
                }

                if (jQuery('#selectMarker').length) {

                    jQuery('#selectMarker').show();
                }

                if (jQuery('.selectNetSetMarker').length) {

                    jQuery('.selectNetSetMarker').show();
                }
            }
        });

        // 상황발생 CCTV 영상 닫기 클릭 시
        jQuery('#ocrCctvClose').on('click', function () {

            if (vmsUseYn == 'Y') {

                destroyVms();
            }

            jQuery('#ocrCctvPanel').css('display', 'none');
        });


        if (('localStorage' in window) && window['localStorage'] !== null) {

            jQuery('#realtimeObserve').attr('checked', localStorage['realtimeObserve'] == 'true' ? true : false);

            if (localStorage['realtimeObserve'] == 'true') {

                if(socket == undefined) {

                    lc.realtimeObserve('connect');
                }

                jQuery('#interestSelect').attr('disabled', true);
                jQuery('#disStandChk').attr('disabled', true);
            }
        }
        else {
            common.setOSXModal('웹 스토리지를 지원하지 않습니다.');
        }

        //  실시간 감시 체크 시
        jQuery('#realtimeObserve').change(function () {

            if (('localStorage' in window) && window['localStorage'] !== null) {

                localStorage['realtimeObserve'] = jQuery('#realtimeObserve').is(':checked');
            }
            else {
                common.setOSXModal('웹 스토리지를 지원하지 않습니다.');
            }

            if (jQuery('#realtimeObserve').is(':checked')) {

                if(socket == undefined) {

                    lc.realtimeObserve('connect');
                }

                jQuery('#interestSelect').attr('disabled', true);
                jQuery('#interestSelect').selectpicker('refresh');
                jQuery('#disStandChk').attr('disabled', true);
            }
            else {

                if(socket != undefined) {

                    lc.realtimeObserve('disconnect');
                }

                jQuery('#interestSelect').attr('disabled', false);
                jQuery('#interestSelect').selectpicker('refresh');
                jQuery('#disStandChk').attr('disabled', false);
            }
        });

        // CCTV 레이어 선택 시
        jQuery('#cctvTypeSelect').change(function () {

            if (netSetLayer != undefined) {

                mapInfo.map.removeLayer(netSetLayer);
            }

            if (sitPcsSel == undefined) {

                sitPcsSel = 0;
            }

            lc.getCctvGeoData(jQuery('#cctvTypeSelect').val(), sitPcsSel);
        });

        // 승인처리 직접입력 버튼 클릭 이벤트
        jQuery('#prc_user_btn').on('click', function () {

            if (jQuery('#prc_user_btn').text() == '직접입력') {

                prev_prc_user = jQuery('#prc_user_nm').val();
                jQuery('#prc_user_btn').text('선택');
                jQuery('#prc_user_nm').replaceWith('<input type="text" id="prc_user_input" placeholder="이름 / 전화번호 / 부서명 형식으로 입력해주세요." class="form-control" style="width: 60%; display: inline; margin-left: 5%;">');
                lc.chkDupManager('prc_user_input');
                prcWsChk = 'prc_user_input';

                return;
            }

            if (jQuery('#prc_user_btn').text() == '선택') {

                jQuery('#prc_user_btn').text('직접입력');
                jQuery('#prc_user_input').replaceWith('<select id="prc_user_nm" name="prc_user_nm" class="form-control" style="width: 60%; display: inline; margin-left: 5%;"/>');
                common.setSelectOpt(jQuery('#prc_user_nm'), '-선택-', managerList);
                jQuery('#prc_user_nm').val(prev_prc_user);
                prcWsChk = 'prc_user_nm';

                return;
            }
        });

        // 영치결과 직접입력 버튼 클릭 이벤트
        jQuery('#seize_user_btn').on('click', function () {

            if (jQuery('#seize_user_btn').text() == '직접입력') {

                prev_seize_user = jQuery('#seize_user_nm').val();
                jQuery('#seize_user_btn').text('선택');
                jQuery('#seize_user_nm').replaceWith('<input type="text" id="seize_user_input" placeholder="이름 / 전화번호 / 부서명 형식으로 입력해주세요." class="form-control" style="width: 60%; display: inline; margin-left: 5%;">');
                lc.chkDupManager('seize_user_input');
                seizeWsChk = 'seize_user_input';

                return;
            }

            if (jQuery('#seize_user_btn').text() == '선택') {

                jQuery('#seize_user_btn').text('직접입력');
                jQuery('#seize_user_input').replaceWith('<select id="seize_user_nm" name="seize_user_nm" class="form-control" style="width: 60%; display: inline; margin-left: 5%;"/>');
                common.setSelectOpt(jQuery('#seize_user_nm'), '-선택-', managerList);
                jQuery('#seize_user_nm').val(prev_seize_user);
                seizeWsChk = 'seize_user_nm';

                return;
            }
        });

        // 스위치 버튼
        lc.changeSwitchery('#prc_fl', false);
        lc.changeSwitchery('#seize_fl', false);

        // ToolBar Event
        // 일반/항공 지도 변경 이벤트
        jQuery('#mapPanel').find("input:radio[name='mapSwicher']").on('change', function () {

            lc.baseMapChange(jQuery(this).val(), mapInfo.layers, mapInfo.map);
        });

        // 이동 이벤트
        jQuery('#btnMove').on('click', function () {
            mapInfo.map.removeInteraction(draw);

            return false;
        });

        // 확대/축소 이벤트
        jQuery('#btnZoomIn, #btnZoomOut').on('click', function () {

            var step = this.id === 'btnZoomIn' ? 1 : -1;
            var view = mapInfo.map.getView();

            view.animate({
                zoom: view.getZoom() + step,
                duration: 500
            });
            return false;
        });

        // 초기화 이벤트
        jQuery('#btnInit').on('click', function () {

            if (draw != undefined && measureLayer != undefined) {
                mapInfo.map.removeInteraction(draw);
                mapInfo.map.removeLayer(measureLayer);
                jQuery('.tooltip-static').remove();
                jQuery('.tooltip-measure').remove();
                draw = undefined;
                measureLayer = undefined;
            }
        });

        // 거리 이벤트
        jQuery('#btnDistance').on('click', function () {

            lc.addInteraction('btnDistance');

        });

        // 면적 이벤트
        jQuery('#btnArea').on('click', function () {

            lc.addInteraction('btnArea');
        });

        mapInfo.map.on('pointermove', function (evt) {

            var feature = mapInfo.map.forEachFeatureAtPixel(evt.pixel,
                function (feature) {
                    return feature;
                });

            if (feature) {

                if (feature.get('type') == 'netSetMarker' || (feature.get('features') != undefined && feature.get('features')[0].O.type == 'cctvMarker')) {

                    mapInfo.map.getTargetElement().style.cursor = 'pointer';
                }
                else {

                    mapInfo.map.getTargetElement().style.cursor = '';
                }
            }
        });

        mapInfo.map.on('click', function (evt) {

            var feature = mapInfo.map.forEachFeatureAtPixel(evt.pixel,
                function (feature) {
                    return feature;
                });

            if (feature) {

                if (feature.get('type') == 'netSetMarker') {

                    lc.zoomGeoData(feature.get('name').split(',')[0], feature.get('name').split(',')[1]);
                    lc.getNetSetCctvGeoData(feature.get('id'), jQuery('#cctvTypeSelect').val(), feature);
                }

                if (feature.get('features') != undefined && feature.get('features')[0].O.type == 'cctvMarker') {

                    lc.startVmsPopup(feature.get('features')[0].O.id);
                }
            }
        });

        jQuery.fn.loadingComplete();
    });

    // 윈도우 화면 리사이즈 시 이벤트
    jQuery(window).bind('resize', function () {

        lc.resizePanel('sitPcs');

    }).trigger('resize');
});