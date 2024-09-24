<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>광고 페이지</title>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<link rel="stylesheet" href="/ENGround0909/resources/css/styleKIM.css">
</head>
<body>
    <%@ include file="/WEB-INF/include/AD.jsp" %>
    <div class="container">
    <%@ include file="/WEB-INF/include/header.jsp" %>
        <div class="ad-container">
            <div class="ad-head">광고 페이지</div>
            <div class="ad-body">
            <form id="adForm" action="<c:url value='/ad/insert'/>" method="post" enctype="multipart/form-data">
                    <div class="AdfileBox">
                        <div class="upload-name">
                            <input class="upload-name" placeholder="첨부파일" readonly>
                            <label for="file">이미지 등록</label>
                            <input type="file" id="file" name="imageFile" required>
                        </div>
                        <div class="URL-name">
                            <input class="URL-name" type="text" name="url" placeholder="URL을 입력하세요">
                            <!-- URL 주소를 저장하거나 전송하기 위한 버튼입니다. -->
                            <button type="button" onclick="submitUrl()">URL 등록</button>
                        </div>
                    </div>
                </div>
            <div class="ad-insert-btn">
                <button type="submit">추가</button>
            </div>
            </form>    
        </div>
    </div>
</body>
<script>
$("#file").on('change',function(){
      var fileName = $("#file").val();
      $(".upload-name").val(fileName);
    });
    
function submitUrl() {
    var url = document.querySelector(".URL-name input[name='url']").value;
    if (url) {
        alert("URL이 등록되었습니다: " + url);
        // URL을 서버로 전송하는 로직을 추가할 수 있습니다.
    } else {
        alert("URL을 입력하세요.");
    }
}
</script>
</html>
