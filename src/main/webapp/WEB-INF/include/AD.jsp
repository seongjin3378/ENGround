<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>광고 목록</title>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<link rel="stylesheet" href="<c:url value='/resources/css/styleJANG.css'/>"/>
</head> 
<body>
	<div class="ADside">
		<a href="${ads[0].targetUrl}" target="_blank">
			<img src="${ads[0].imageUrl}" alt="광고 이미지" style="width:100%;height:40%;">
		</a>
	</div>
</body>
<style>
    @media (max-width: 1200px) {
        .side {
            display: none; /* 화면 너비가 1200px 이하일 때 사이드바를 숨김 */
        }
    }
</style>


<script>
/* ad데이터베이스 마지막 데이터를 불러오는 ajax */
	    $.ajax({
        type: "get",
        url: "<c:url value='/AdAjax'/>", // c:url로 변경
        success: function(data) {
            console.log(data);
            if (data.result == "fail") {
                // 실패 처리
            } else {
                $(".ADside > a").attr("href", data.targetUrl);
                $(".ADside > a > img").attr("src", "<c:url value='/'/>" + data.imageUrl); // c:url로 변경
            }
        }
    });
</script>
</html>
