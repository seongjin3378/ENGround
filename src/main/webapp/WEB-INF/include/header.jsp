<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="header">
		<c:choose>
			<c:when test="${user.userType eq null}">
				<a href="<c:url value='/login'/>" class="lang-button"></a>
			</c:when>
			<c:otherwise>
				<a href="<c:url value='/mainPage'/>" class="lang-button"></a>
			</c:otherwise>
		</c:choose>
		<div class="dropdown">
			<c:choose>
				<c:when test="${user.userType eq '0'}">
					<a href="javascript:void(0)" class="dropbtn"
						onclick="toggleDropdown()">My Page</a>
				</c:when>
				<c:when test="${user.userType eq '1'}">
					<a href="javascript:void(0)" class="dropbtn"
						onclick="toggleDropdown()">My Page</a>
				</c:when>
				<c:when test="${user.userType eq '2'}">
					<a href="javascript:void(0)" class="dropbtn"
						onclick="toggleDropdown()">My Page</a>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
			<div id="dropdown-content" class="dropdown-content">

				<c:choose>
					<c:when test="${user.userType eq '2'}">
						<a href="<c:url value='/myPage'/>">내 정보</a>
						<a href="<c:url value='/logout'/>">로그아웃</a>
					</c:when>
					<c:when test="${user.userType eq '1'}">
						<a href="<c:url value='/myPage'/>">내 정보</a>
						<a href="<c:url value='/logout'/>">로그아웃</a>
						<a href="<c:url value='/manager'/>">관리자페이지</a>
					</c:when>
					<c:otherwise>
						<%-- <c:when test="${empty sessionScope.user}"> --%>
						<a href="<c:url value='/login'/>">로그인</a>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>

	<script>
		function toggleDropdown() {
			var dropdownContent = document.getElementById("dropdown-content");
			if (dropdownContent.style.display === "block") {
				dropdownContent.style.display = "none";
			} else {
				dropdownContent.style.display = "block";
			}
		}

		// Close the dropdown if the user clicks outside of it
		window.onclick = function(event) {
			if (!event.target.matches('.dropbtn')) {
				var dropdowns = document
						.getElementsByClassName("dropdown-content");
				for (var i = 0; i < dropdowns.length; i++) {
					var openDropdown = dropdowns[i];
					if (openDropdown.style.display === "block") {
						openDropdown.style.display = "none";
					}
				}
			}
		}
	</script>
	<style>
</style>
</body>
</html>
