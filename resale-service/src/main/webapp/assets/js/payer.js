// collecter.js
// version : 0.1
// create date : 2018.03.29
// authors : Youngjoo Lee
// description : 신한DAMOA 서비스 중 기관관리자에서 동작하는 javascript 들

$(document).ready(function() {

    // 선택된 행을 제외 시킬 때의 alert 메세지
    $(".btn-except-selected-row").click(function(){
        var thisId = $(this).closest(".list-id").attr("id");

        executeExceptSelectedRow (thisId);
    });


    // 고객 정보 수정
    $('.btn-payment-receipt').click(function(){
        var thisId = $(this).closest(".list-id").attr("id");

        paymentReceipt(thisId, message);
    });

    // console.log("payer.js OK!");
});



// 선택된 청구를 수정할 때
function paymentReceipt(thisId, message) {
    var countCheckboxTick = $("#" + thisId + " .table-check-child").is(":checked");

    if (countCheckboxTick == false){
        swal({
           type: 'question',
           text: message,
           confirmButtonColor: '#3085d6',
           confirmButtonText: '확인'
       });

    } else if (countCheckboxTick == true){
        $('#payment-receipt').modal("show");
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
