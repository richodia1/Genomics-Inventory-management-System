<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><s:text name="menu.Browse" /></title>
</head>
<body>
<div style="height: 30px;">
	<div style="float: right;">
	<security:authorize ifNotGranted="ROLE_BREEDER">
		<security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_MANAGER,ROLE_USER"><a href="<c:url value="/location/"  /><s:property value="location.id" />">Edit location</a></security:authorize>
		<a href="<c:url value="/browse/summary.jspx" />?export.location=<s:property value="location.id" />">Summary</a>
		<a href="<s:url action="trial/create" namespace="/" />?trial.name=Location trial&amp;trial.description=Trial at ${location.pathNames}&amp;source=location&amp;locId=<s:property value="location.id" />">Create trial</a>
	</security:authorize>
	</div>
 <s:form method="GET" action="index">
	<b style="margin-right: 20px;">Location path:</b>

	<a style="margin-right: 20px;" href="<c:url value="/browse/location/" />">ROOT</a>
	<s:iterator value="locationPath">
		<a style="margin-right: 20px;" href="<c:url value="/browse/location/${id}" />"><s:property value="name" /></a>
	</s:iterator>

	<s:if test="subLocations!=null && subLocations.size>0">
		<s:select name="locid" list="subLocations" listKey="id" listValue="name" theme="simple" emptyOption="true"
			onchange="javascript: window.location.pathname='%{virtualDirectory}/browse/location/' + this.value;" />
	</s:if>

	<s:elseif test="location!=null && location.parent!=null">
		<s:select cssStyle="visibility: hidden;" name="locid" list="location.parent.children" listKey="id" listValue="name" theme="simple" headerKey="%{locid}"
			headerValue="%{location.name}" onchange="javascript: this.form.submit();" />
	</s:elseif>
</s:form></div>

<s:if test="results.size > 0">
	<div style="margin: 5px 0px 20px 5px;"><s:form method="POST" namespace="/browse">
		<s:hidden id="locid" name="locid" value="%{locid}" />
		<s:hidden name="startAt" value="%{startAt}" />
		<s:submit action="index!printLabels" value="Print labels" theme="simple" />
		<s:submit action="index!selectAll" value="Add to selection" theme="simple" />
		<s:submit action="index!clearAll" value="Clear selection" theme="simple" />
		<span style="margin-left: 100px;">Lots in selection: <b><s:property value="selectionService.selectedList.selectedLots.size" /></b></span>
	</s:form></div>
	
	<s:include value="/WEB-INF/jsp/paging.jsp">
		<s:param name="page" value="(startAt / pageSize) + 1" />
		<s:param name="pages" value="(totalRecords / pageSize) + 1" />
		<s:param name="pageSize" value="pageSize" />
		<s:param name="queryString" value="q" />
		<s:param name="href" value="%{''}" />
	</s:include>
	
	<%@ include file="/WEB-INF/jsp/browse/list.jsp" %>

	<s:if test="results[0] instanceof org.iita.inventory.model.InVitroLot && !getUser().hasRole('ROLE_BREEDER')">
		<div style="margin-top: 20px"><a href="<s:url action="inventorize"><s:param name="locid" value="locid" /></s:url>">Inventorize</a></div>
	</s:if>



	<s:include value="/WEB-INF/jsp/paging.jsp">
		<s:param name="page" value="(startAt / pageSize) + 1" />
		<s:param name="pages" value="(totalRecords / pageSize) + 1" />
		<s:param name="pageSize" value="pageSize" />
		<s:param name="queryString" value="q" />
		<s:param name="href" value="%{''}" />
	</s:include>

	<s:if test="totalRecords>startAt+pageSize">
		<div style="margin-top: 20px; text-align: center"><b style="color: Red;">Not all records are displayed on this page.</b> <a style="margin-right: 20px;"
			href="<c:url value="/browse/location/${locid}" />?startAt=${startAt+pageSize}">View next page</a></div>
	</s:if>

</s:if>
<s:else>
	<p>Select a location using the dropdown above.</p>
</s:else>

</body>
</html>