<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>카테고리</title>
    <script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
    <link rel="stylesheet" href="<c:url value='/resources/css/styleKIM.css'/>"/>
</head>
<body>   
    <div class="container">
        <%@ include file="/WEB-INF/include/header.jsp" %> <!-- 헤더 포함 -->
        <div class="main-category">
            <c:forEach var="category" items="${vo}">
                <div class="category-item">
                    <button class="category-button">
                        <img src="${category.imagePath}" alt="${category.categoryName}" style="width: 72px; height: 72px;"> <!-- 이미지 표시 -->
                    </button>
                    <span class="category-text">${category.categoryName}</span> 
                    <button class="category-next" onclick="navigateToCategory('${category.categoryName}')">&gt;</button>
                </div>
            </c:forEach>
        </div>
        
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="<c:url value='/category?page=1' />">첫페이지</a>
                <a href="<c:url value='/category?page=${currentPage - 1}' />">이전페이지</a>
            </c:if>
            
            <c:forEach begin="${startPage}" end="${endPage > totalPage ? totalPage : endPage}" var="pageNum">
                <c:choose>
                    <c:when test="${currentPage == pageNum}">
                        <span>${pageNum}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/category?page=${pageNum}' />">${pageNum}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            
            <c:if test="${currentPage < totalPage}">
                <a href="<c:url value='/category?page=${currentPage + 1}' />">다음페이지</a>
                <a href="<c:url value='/category?page=${totalPage}' />">끝페이지</a>
            </c:if>
        </div>
    </div>
    
    <script>
        /* function navigateToCategory(category) {
            window.location.href = '/' + category + 'game';  // 카테고리 페이지로 이동
        } */
        function navigateToCategory(category) {
            const baseUrl = window.location.origin + window.location.pathname.substring(0, window.location.pathname.lastIndexOf('/'));
            window.location.href = baseUrl + "/game/" + category;
        }
    </script>
</body>
</html>
