<%@ include file="/common/taglibs.jsp"%>

<a style="margin-right: 10px;" href="<c:url value='/' />"><s:text name="menu.Dashboard" /></a>
<!-- Other menus -->
<security:authorize ifAnyGranted="ROLE_USER">
	<a style="margin-right: 10px;" href="<s:url namespace='/browse' action='index' includeParams="none" />"><s:text name="menu.Browse" /></a>
	<a style="margin-right: 10px;" href="<s:url namespace='/lot' action='index' includeParams="none" />"><s:text name="menu.Scan" /></a>
	<a style="margin-right: 10px;" href="<s:url namespace='/selection' action='index' includeParams="none" />"><s:text name="menu.Selection" /></a>
	<security:authorize ifNotGranted="ROLE_BREEDER">
		<a style="margin-right: 10px;" href="<s:url namespace='/' action='printer' includeParams="none" />"><s:text name="menu.Printer" /></a>
	</security:authorize>
	<security:authorize ifNotGranted="ROLE_BREEDER">
		<a style="margin-right: 10px;" href="<s:url namespace='/' action='scale' includeParams="none" />"><s:text name="menu.Scale" /></a>
	</security:authorize>
	<a style="margin-right: 10px;" href="<s:url namespace='/' action='query/index' includeParams="none" />"><s:text name="menu.Query" /></a>
	<a style="margin-right: 10px;" href="<s:url namespace='/' action='request/index' includeParams="none" />"><s:text name="menu.Requests" /></a>
</security:authorize>

<security:authorize ifNotGranted="ROLE_USER">
	<a style="margin-right: 10px;" href="<c:url value='/login.jspx' />" title="Log in"><s:text name="menu.LogIn" /></a>
</security:authorize>

<!-- End -->
<security:authorize ifAnyGranted="ROLE_USER">
	<a style="margin-right: 10px;" href="<s:url namespace='/' action='user/notification' />" title="Manage application notifications">Notifications</a>
	<a style="margin-right: 10px;" href="<s:url namespace='/' action='user/delegate' />">Delegate</a>
	<a style="margin-right: 10px;" href="<s:url namespace='/' action='user/changepasswordsetting' />" title="Change password preferences">Password</a>
</security:authorize>
<security:authorize ifAnyGranted="ROLE_ADMIN">
	<a style="margin-right: 10px;" href="<c:url value="/admin/" />"><s:text name="menu.Administration" /></a>
</security:authorize>
<a style="margin-right: 10px; background-image: url('<c:url value="/img/help.png" />'); background-repeat: no-repeat; background-position: 0px 0px; padding-left: 20px;" onclick="javascript: return IITAHELP.helpFrame();" title="Help!">Help</a>
<a style="margin-right: 10px;" href="mailto:software.support@iita.org?subject=Application bug report" title="Send a bug report">Bug</a>

<security:authorize ifAnyGranted="ROLE_USER">
	<a style="margin-right: 10px;" href="<c:url value='/j_spring_security_logout' />" title="Log out"><s:text name="menu.LogOut" /></a>
</security:authorize>