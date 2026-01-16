<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/views/include/sys/header.jsp" flush="false" />

<link href="/assets/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
<link href="/assets/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<!-- FooTable -->
<link href="/assets/css/plugins/footable/footable.core.css" rel="stylesheet">

<script src="/assets/js/common.js?version=${project.version}"></script>

<script>
    var oneDepth = "adm-nav-2";
    var twoDepth = "adm-sub-02-1";
</script>

</div>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
        <h2>기관그룹관리</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/sys/index">대시보드</a>
            </li>
            <li>
                <a>기관관리</a>
            </li>
            <li class="active">
                <strong>기관그룹관리</strong>
            </li>
        </ol>
        <p class="page-description">기관 그룹의 관리자를 등록/수정하는 화면입니다.</p>
    </div>
    <div class="col-lg-2">

    </div>
</div>

<div class="wrapper-content">
    <div class="animated fadeInRight article">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>검색</h5>
                    </div>

                    <div class="ibox-content">
                        <form onsubmit="return false;">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group form-group-sm">
                                        <label class="form-label block">그룹코드</label>
                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid" id="group-id-filter" style="height: 34px;" />
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <div class="form-group form-group-sm">
                                        <label class="form-label block">그룹이름</label>
                                        <input type="text" class="form-control ng-untouched ng-pristine ng-valid" id="group-name-filter" style="height: 34px;" />
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label class="form-label block">그룹상태</label>
                                    <div class="form-group form-group-sm">
                                        <div class="input-group col-md-12">
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="groupAll" name="status-filter" value="">
                                                <label for="groupAll"> 전체 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="group-1" name="status-filter" value="ST01">
                                                <label for="group-1"> 정상 </label>
                                            </div>
                                            <div class="radio radio-primary radio-inline">
                                                <input type="radio" id="group-2" name="status-filter" value="ST02">
                                                <label for="group-2"> 해지 </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <hr>

                            <div class="text-center">
                                <button class="btn btn-primary" type="button" id="search-button">조회</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="animated fadeInRight">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox">
                    <div class="ibox-title">
                        <div class="col-lg-6">
                            전체 기관 : <strong class="text-success" id="total-item-count"></strong> 건
                        </div>

                        <div class="col-lg-6 form-inline form-searchOrderBy">
                            <select class="form-control" name="" id="order-by">
                                <option value="id_asc">그룹코드순 정렬</option>
                                <option value="name_asc">그룹이름순 정렬</option>
                                <option value="memberCount_desc">기관개수순 정렬</option>
                            </select>
                            <select class="form-control" name="pageScale" id="page-size">
                                <option value="10">10개씩 조회</option>
                                <option value="20">20개씩 조회</option>
                                <option value="50">50개씩 조회</option>
                                <option value="100">100개씩 조회</option>
                                <option value="200">200개씩 조회</option>
                            </select>
                            <button class="btn btn-md btn-primary" type="button" id="download-cha-group">파일저장</button>
                        </div>
                    </div>

                    <div class="ibox-content">
                        <div class="table-responsive">
                            <table class="table table-stripped table-align-center" id="item-table">
                                <colgroup>
                                    <col width="68">
                                    <col width="140">
                                    <col width="140">
                                    <col width="100">
                                    <col width="140">
                                    <col width="110">
                                    <col width="100">                                    
                                    <col width="150">
                                </colgroup>

                                <thead>
                                    <tr>
                                        <th>NO</th>
                                        <th>그룹ID</th>
                                        <th>그룹명</th>
                                        <th>기관 개수</th>
                                        <th>그룹상태</th>
                                        <th>기관 종류</th>
                                        <th>&nbsp;</th>
                                    </tr>
                                </thead>

                                <tbody>
                                </tbody>
                            </table>
                        </div>

                        <div class="col-lg-12 text-center">
                            <div class="btn-group" id="paginate"></div>
                        </div>

                        <div class="row">
                            <div class="col-lg-12 text-right">
                                <button type="button" class="btn btn-primary" id="new-group-button">그룹등록</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 어드민용 스피너 추가 -->
<div class="spinner-area" style="display:none;">
    <div class="sk-spinner sk-spinner-cube-grid">
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
        <div class="sk-cube"></div>
    </div>
    <div class="modal-backdrop-back-spinner"></div>
</div>
<!-- 어드민용 스피너 추가 -->
<!-- ##################################################################### -->


<jsp:include page="/WEB-INF/views/include/sys/footer.jsp" flush="false" />

<!-- Data picker -->
<script src="/assets/js/plugins/datapicker/bootstrap-datepicker.js"></script>

<!-- FooTable -->
<script src="/assets/js/plugins/footable/footable.all.min.js"></script>

<!-- 기관검색 팝업 -->
<jsp:include page="/WEB-INF/views/include/modal/lookup-collecter-sys.jsp" flush="false"/>

<!-- 그룸 수정 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/partner-info.jsp" flush="false"/>

<!-- 그룸 등록 -->
<jsp:include page="/WEB-INF/views/include/modal/admin/new-group.jsp" flush="false"/>

<script>
    $(function () {
        var paramMap = parseHashQuery();

        paramMap.status = paramMap.status || ''; 
        $('[name="status-filter"]').filter('[value="' + paramMap.status + '"]').attr('checked', true);
        $('#group-id-filter').val(paramMap.groupId || '');
        $('#group-name-filter').val(paramMap.groupName || '');
        $('#order-by').val(paramMap.orderBy || 'name_asc');
        $('#page-size').val(paramMap.pageSize || 10);

        $(".btn-lookup-collecter").click(function () {
            $("#lookup-collecter-popup").modal({
                backdrop: 'static',
                keyboard: false
            });
        });

        $(".btn-partner-modify").click(function() {
            $("#popup-partner-info").modal({
                backdrop: 'static',
                keyboard: false
            });
        });

        $('#order-by').change(newSearch);

        $('#search-button').click(newSearch);

        $('#page-size').change(newSearch);

        $('#download-cha-group').click(download);
        
        $('#new-group-button').click(popupNewGroup);

        $('#popup-partner-info .cha-search-box').autocomplete({
            source: function(request, response) {
                $.getJSON('/sys/chaMgmt/cha', {
                    query: request.term
                }, function(data) {
                    var itemList = data.map(function(v) {
                        return {label: v.name + '('+v.chaCd+')', value: v.chaCd};
                    });
                    
                    response(itemList);
                });
            }, select: function(event, ui) {
                var groupId = $('#popup-partner-info').data('group-id');
                var chaCd = ui.item.value;
                moveToGroup(groupId, chaCd);
            }
        });

        newSearch();

        function download() {
            window.open('/sys/chaMgmt/cha-group/download?' + location.hash.substring(1));
        }

        function newSearch() {
            goPage(1);
        }

        function goPage(pageNo) {
            paramMap.pageNo = pageNo;
            search();
        }

        function search() {
            paramMap.groupId = $('#group-id-filter').val() || '';
            paramMap.groupName = $('#group-name-filter').val() || '';
            paramMap.status = $('[name=status-filter]').filter(':checked').val() || '';
            paramMap.pageNo = paramMap.pageNo || 1;
            paramMap.pageSize = $('#page-size').val() || 10;
            paramMap.orderBy = $('#order-by').val() || 'name_asc';

            buildHashQuery(paramMap);

            $.getJSON('/sys/chaMgmt/cha-group', paramMap, function(data) {
                // 전체 개수
                var $totalItemCount = $('#total-item-count');
                $totalItemCount.text(data.totalItemCount);

                // 데이터 표시
                var $itemList = $('#item-table').find('tbody');
                $itemList.empty();

                data.itemList.forEach(function (v, i) {
                    var $showDetailsButton = $('<button type="button" class="btn btn-xs btn-primary btn-partner-modify">상세정보</button>').data('group-id', v.id);
                    $showDetailsButton.click(function() {
                        popupGroupDetails($(this).data('group-id'));
                    });
                    
                    var $item = $('<tr></tr>').appendTo($itemList);
                    $('<td></td>').text((paramMap.pageNo - 1) * paramMap.pageSize + i + 1).appendTo($item);
                    $('<td></td>').text(v.id).appendTo($item);
                    $('<td></td>').text(v.name).appendTo($item);
                    $('<td></td>').text(v.memberCount).appendTo($item);
                    $('<td></td>').text(v.status == 'ST01' ? '정상' : '해지').appendTo($item);
                    $('<td></td>').text(v.transactionType == '01' ? '웹' : 'API').appendTo($item);
                    $('<td></td>')
                        .append($showDetailsButton)
                        .append(' ')
                        .append($('<button type="button" class="btn btn-xs btn-info btn-partner-modify">화면이동</button>').data('group-id', v.id).click(function() {
                            runAs($(this).data('group-id'));
                        }))
                        .appendTo($item);
                });

                // 페이지
                var paginate = {};
                var pageGroupSize = 10;
                paginate['first'] = 1;
                paginate['last'] = Math.max(Math.ceil(data.totalItemCount / paramMap.pageSize), 1);
                var pageGroupNo = Math.max(Math.ceil(paramMap.pageNo / pageGroupSize), 1);
                pageGroupNo = Math.min(Math.ceil(paginate['last'] / pageGroupSize), pageGroupNo)
                paginate['start'] = Math.max((pageGroupNo - 1) * pageGroupSize + 1, 1);
                paginate['end'] = Math.min(pageGroupNo * pageGroupSize, paginate['last']);
                c = Math.max(paginate['start'] - 1, paginate['first']);
                paginate['next'] = Math.min(paginate['end'] + 1, paginate['last']);

                var $paginate = $('#paginate');
                $paginate.empty();
                $('<button class="btn btn-white"></button>').data('pageNo', paginate['previous']).append($('<i class="fa fa-chevron-left"></i>')).click(function () {
                    goPage($(this).data('pageNo'));
                }).appendTo($paginate);
                for (var i = paginate['start']; i <= paginate['end']; i++) {
                    var $button = $('<button class="btn btn-white"></button>').data('pageNo', i).text(i).appendTo($paginate);
                    if (i == paramMap.pageNo) {
                        $button.addClass('active');
                    } else {
                        $button.click(function () {
                            goPage($(this).data('pageNo'));
                        });
                    }
                }
                $('<button class="btn btn-white"></button>').data('pageNo', paginate['next']).append($('<i class="fa fa-chevron-right"></i>')).click(function () {
                    goPage($(this).data('pageNo'));
                }).appendTo($paginate);
            });
        }

        function buildHashQuery(paramMap) {
            var paramList = [];
            for (var key in paramMap) {
                paramList.push(key + '=' + encodeURIComponent(paramMap[key]));
            }

            location.hash = '#' + paramList.join('&');
        }

        function parseHashQuery() {
            var hashString = location.hash;
            if (!hashString) {
                return {};
            }

            hashString = hashString.substring(1);
            var paramList = hashString.split('&');
            var paramMap = paramList.reduce(function (previousValue, currentValue) {
                var split = currentValue.split('=');
                var key = split[0];
                var value = decodeURIComponent(split[1]);

                if (typeof previousValue[key] == 'string') {
                    previousValue[key] = [previousValue[key], value];
                } else if (typeof previousValue[key] == 'array') {
                    previousValue[key].splice(previousValue[key].length, 0, value);
                } else {
                    previousValue[key] = value;
                }

                return previousValue;
            }, {});

            return paramMap;
        }
        
        function initGroupDetailsPopup() {
            $('#popup-partner-info .group-id').val('').attr('disabled', true);
            $('#popup-partner-info .group-name').val('');
            $('#popup-partner-info [name=group-status][value="ST01"]').attr('checked', true);
            $('#popup-partner-info [name=group-transaction-type][value="01"]').attr('checked', true);
            $('#popup-partner-info .group-remark').val('');
            $('#popup-partner-info .group-password').val('');

            $('#popup-partner-info .save-cha-info').unbind('click');
            $('#popup-partner-info .save-cha-info').click(function() {
                if(!confirm('저장하시겠습니까?')) {
                    return;
                }
                
                saveChaGroup();
            });
        }
        
        function initNewGroupPopup() {
            $('#popup-new-group .group-id').val('').attr('disabled', true);
            $('#popup-new-group .group-name').val('');
            $('#popup-new-group .group-type').val('WEB');
            $('#popup-new-group [name=group-status][value="ST01"]').attr('checked', true);
            $('#popup-new-group [name=group-transaction-type][value="01"]').attr('checked', true);
            $('#popup-new-group .group-remark').val('');
            $('#popup-new-group .group-password').val(generatePassword());
        }
        
        function generatePassword() {
            var random = [0,0,0,0].map(function() {
                return Math.floor(Math.random()*10);
            }).join('');
            
            return 'finger' + random;
        }
        
        function popupGroupDetails(groupId) {
            var url = '/sys/chaMgmt/cha-group/' +  groupId;
            
            $.ajax(url, {
                success: function(data) {
                    initGroupDetailsPopup();

                    var $popup = $('#popup-partner-info');
                    $popup.data('group', data);
                    $popup.data('group-id', data.id);
                    $('#popup-partner-info .group-id').val(data.id).attr('disabled', true);
                    $('#popup-partner-info .group-name').val(data.name);
                    $('#popup-partner-info [name=group-status]').filter('[value="'+data.status+'"]').attr('checked', true);
                    $('#popup-partner-info [name=group-transaction-type]').filter('[value="'+data.transactionType+'"]').attr('checked', true);
                    $('#popup-partner-info .group-remark').val(data.remark);
                    // $('#popup-partner-info .group-password').val('');

                    var $memberList = $popup.find('.member-list');
                    $memberList.empty();

                    data.chaList.forEach(function(v, i) {
                        var $member = $('<tr></tr>').data('cha-cd', v.chaCd).data('cha', v);
                        $('<td></td>').text(i+1).appendTo($member);
                        $('<td></td>').text(v.chaCd).appendTo($member);
                        $('<td></td>').text(v.name).appendTo($member);
                        $('<td></td>').text(v.owner).appendTo($member);
                        $('<td></td>').text(v.chaOffNo.substring(0, 3) + '-' + v.chaOffNo.substring(3, 5) + '-' + v.chaOffNo.substring(5, 10)).appendTo($member);
                        $('<td></td>').text(v.ownerTel).appendTo($member);
                        $('<td></td>').text(v.regDt).appendTo($member);
                        $('<td></td>').append($('<button type="button" class="btn btn-xs btn-danger">삭제</button>').click(function() {
                            removeFromGroup(groupId, $member.data('cha-cd'));
                        })).appendTo($member);

                        $member.appendTo($memberList);
                    });

                    $("#popup-partner-info").modal({
                        backdrop: 'static',
                        keyboard: false
                    });
                }
            });
        }
        
        function removeFromGroup(groupId, chaCd) {
            if(!confirm('해당 기관을 그룹에서 해제하시겠습니까?')) {
                return;
            }
            
            $.ajax('/sys/chaMgmt/cha-group/' + groupId + '/' + chaCd, {
                type: 'DELETE',
                success : function() {
                    popupGroupDetails(groupId);
                }
            });
        }

        function moveToGroup(groupId, chaCd) {
            if(!confirm('해당 기관을 그룹으로 설정하시겠습니까?')) {
                return;
            }

            $.ajax('/sys/chaMgmt/cha-group/' + groupId + '/' + chaCd, {
                type: 'PUT',
                success : function() {
                    popupGroupDetails(groupId);
                }
            });
        }
        
        function popupNewGroup() {
            initNewGroupPopup();
            
            $("#popup-new-group").modal({
                backdrop: 'static',
                keyboard: false
            });
        }

        function runAs(groupId) {
            var $form = $('<form></form>');
            $form.attr('action', '/sys/groupMoveAuth');
            $form.attr('target', '_blank');
            $form.append($('<input />').attr('type', 'hidden').attr('name', 'groupId').attr('value', groupId));
            $form.appendTo($('html'));
            $form.submit();
        }
        
        function saveChaGroup() {
            var data = {};
            data.id = $('#popup-partner-info').data('group-id');
            data.name = $('#popup-partner-info .group-name').val();
            data.password = $('#popup-partner-info .group-password').val();
            data.remark = $('#popup-partner-info .group-remark').val();
            data.status = $('#popup-partner-info [name=group-status]:checked').val();
            data.transactionType = $('#popup-partner-info [name=group-transaction-type]:checked').val();

            $.ajax('/sys/chaMgmt/cha-group/' + data.id, {
                type: 'PUT',
                data : JSON.stringify(data),
                contentType : 'application/json',
                success: successToModifyGroup,
                failure: failToModifyGroup
            });
        }
        
        function successToModifyGroup() {
            alert('저장했습니다');
            
            $('#popup-partner-info').modal('hide');
        }
        
        function failToModifyGroup() {
            alert('오류가 발생했습니다. 다시 확인해주세요.');
        }
    });
</script>
