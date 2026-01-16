// collecter.js
// version : 0.1
// create date : 2018.03.29
// authors : Youngjoo Lee
// description : 신한DAMOA 서비스 중 기관관리자에서 동작하는 javascript 들

$(document).ready(function() {

    // 개별 고객 등록
    // 조건에 따른 활성화 여부 체크
    $("select.parents").on("change",function(){
        var _target = $(this).val();

        if (_target.length == 0){
            $(".first-depth-child").attr("disabled", true);
        } else if(_target.length =! 0){
            $(".first-depth-child").attr("disabled", false);
        }
    });

    // 선택된 행을 제외 시킬 때의 alert 메세지
    $(".btn-except-selected-row").click(function(){
        var thisId = $(this).closest(".list-id").attr("id");

        executeExceptSelectedRow (thisId);
    });


    // 고객 정보 수정
    $('.btn-open-modify-payer-information').click(function(){
        var thisId = $(this).closest(".list-id").attr("id");

        callModPayerInfo(thisId);
    });


    // 선택된 청구를 수정할 때
    $('.btn-modify-charge-popup').click(function(){
        var thisId = $(this).closest(".list-id").attr("id");

        modifyChargeSelectedRow(thisId, modMessage);
    });

    // 선택된 행을 삭제 시킬 때의 alert 메세지
    $(".btn-delete-selected-row").click(function(){
        var thisId = $(this).closest(".list-id").attr("id");

        delSelectedRow(thisId, delMessage, noSelectMsg);
    });

    // 임시저장
    $(".btn-keep-it-temporary").click(function(){
        keepItTemporary();
    });

    // 임시저장
    $(".btn-update-settings").click(function(){
        updateSettings();
    });


    // SMS 고지 팝업 불러오기
    $('.btn-sms-notification-popup').click(function(){
        var thisId = $(this).closest(".list-id").attr("id");

        callSmsNotification(thisId);
    });

    // email 고지 팝업 불러오기
    $('.btn-email-notification-popup').click(function(){
        var thisId = $(this).closest(".list-id").attr("id");

        callEmailNotification(thisId);
    });

    // console.log("collecter.js OK!");
});


// 선택된 행을 제외 시킬 때의 alert 메세지
function executeExceptSelectedRow (thisId){
    var countCheckboxTick = $("#" + thisId + " .table-check-child").is(":checked");

    if (countCheckboxTick == false){
        swal({
           type: 'question',
           text: '선택하신 납부제외 건이 없습니다.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
       });

    } else if (countCheckboxTick == true){
        swal({
            type: 'warning',
            text: '선택된 건의 납부를 제외하시겠습니까?',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        });
    }
}


//
function callModPayerInfo(thisId){
    var countCheckboxTick = $("#" + thisId + " .table-check-child").is(":checked");

    if (countCheckboxTick == false){
        swal({
           type: 'question',
           text:'선택하신 정보수정 건이 없습니다.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
       });

    } else if (countCheckboxTick == true){
        $('#modify-payer-information').modal("show");
    }
}


//
function callSmsNotification(thisId) {
    var countCheckboxTick = $("#" + thisId + " .table-check-child").is(":checked");

    if (countCheckboxTick == false){
        swal({
           type: 'question',
           text:'선택하신 고지 건이 없습니다.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
       });

    } else if (countCheckboxTick == true){
        $('#sms-notification').modal("show");
    }
}


//
function callEmailNotification(thisId) {
    var countCheckboxTick = $("#" + thisId + " .table-check-child").is(":checked");

    if (countCheckboxTick == false){
        swal({
           type: 'question',
           text:'선택하신 고지 건이 없습니다.',
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
       });

    } else if (countCheckboxTick == true){
        $('#email-notification').modal("show");
    }
}


// 선택된 행을 삭제 시킬 때의 alert 메세지
function delSelectedRow(thisId, delMessage, noSelectMsg) {

    var countCheckboxTick = $("#" + thisId + " .table-check-child").is(":checked");

    if (countCheckboxTick == false){
        swal({
           type: 'question',
           text: noSelectMsg,
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
       });

    } else if (countCheckboxTick == true){
        swal({
            type: 'warning',
            html: delMessage,
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: '확인',
            cancelButtonText: '취소'
        });
    }
}


// 선택된 청구를 수정할 때
function modifyChargeSelectedRow(thisId, modMessage) {
    var countCheckboxTick = $("#" + thisId + " .table-check-child").is(":checked");

    if (countCheckboxTick == false){
        swal({
           type: 'question',
           text: modMessage,
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
       });

    } else if (countCheckboxTick == true){
        $('#modify-charge-popup').modal("show");
    }
}


// 임시저장
function keepItTemporary(){
    swal({
       type: 'success',
       text:'임시저장되었습니다.',
       confirmButtonColor: '#3085d6',
       confirmButtonText: '확인'
   });
}

// 저장
function keepItPermenantly(){
    swal({
       type: 'success',
       text:'저장되었습니다.',
       confirmButtonColor: '#3085d6',
       confirmButtonText: '확인'
   });
}


// 저장
function updateSettings(){
    swal({
       type: 'success',
       text:'저장되었습니다.',
       confirmButtonColor: '#3085d6',
       confirmButtonText: '확인'
   });
}

$(window).on('load', function(){

});
