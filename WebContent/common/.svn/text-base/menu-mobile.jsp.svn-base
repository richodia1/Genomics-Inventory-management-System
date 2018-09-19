<%@ include file="/common/taglibs.jsp"%>

<a style="margin-right: 10px;" href="<c:url value='/mobile/' />"><s:text name="menu.Dashboard" /></a>
<a style="margin-right: 10px;" href="javascript: window.history.go(-1);">Back</a>

<security:authorize ifAnyGranted="ROLE_ADMIN">
<%--	<a style="margin-right: 10px;" href="<c:url value="/admin/" />">Adm</a> --%>
</security:authorize>
<security:authorize ifAnyGranted="ROLE_USER">
	<a style="margin-right: 10px;" href="<c:url value='/j_spring_security_logout' />" title="Log out"><s:text name="menu.LogOut" /></a>
</security:authorize>