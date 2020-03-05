<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<script src="${p}/res/plugins/jquery-2.1.3/jquery-2.1.3.min.js"></script>
<script>
var dataArr = [597117,782366,600643,779313,597294,786964,594499,817466,597117,782366,598277,783072];
var request = $.ajax({
    url:'http://127.0.0.1:10005/tax/movingRoute',
    type:'GET',
//    dataType:'json',
    contentType: 'text/plain; charset=utf-8',
//    contentType: 'application/x-www-form-urlencoded',
    data: {point : JSON.stringify(dataArr)},
    async:false,
    cache: false,
    error: function(xhr, status, msg) {

        console.log(xhr + ' :: ' + status + ' :: ' + msg);
    },
    success : function(msg) {

        console.log(msg);
    }
});
</script>