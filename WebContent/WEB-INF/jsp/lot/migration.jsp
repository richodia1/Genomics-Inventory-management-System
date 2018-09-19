<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Lot migration</title>
</head>
<body>

<div style="height: 30px;"><s:form method="GET" action="index">
	<b style="margin-right: 20px;">Destination location:</b>

	<a style="margin-right: 20px;" href="<c:url value="/update/migrate/" />">ROOT</a>
	<s:iterator value="locationPath">
		<a style="margin-right: 20px;" href="<c:url value="/update/migrate/${id}" />"><s:property value="name" /></a>
	</s:iterator>

	<s:if test="subLocations!=null && subLocations.size>0">
		<s:select name="locid" list="subLocations" listKey="id" listValue="name" theme="simple" emptyOption="true"
			onchange="javascript: window.location.pathname='%{virtualDirectory}/update/migrate/' + this.value;" />
	</s:if>

	<s:elseif test="location!=null && location.parent!=null">
		<s:select cssStyle="visibility: hidden;" name="locid" list="location.parent.children" listKey="id" listValue="name" theme="simple" headerKey="%{locid}"
			headerValue="%{location.name}" onchange="javascript: this.form.submit();" />
	</s:elseif>
</s:form></div>

<s:if test="location==null">
	<h2>Recent migrations</h2>

	<table class="data-listing">
		<colgroup>
			<col width="30%" />
			<col width="40%" />
			<col width="40%" />
		</colgroup>
		<thead>
			<tr>
				<td>Recent migrations</td>
				<td>From</td>
				<td>To</td>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="migrationsIn.results">
				<tr>
					<td><s:date name="migrationDate" format="dd/MM/yyyy HH:mm" /> <b><a href="${virtualDirectory}/lot/${lot.id}"><s:property value="lot.item.name" /></a></b></td>
					<td><a href="${virtualDirectory}/update/migrate/${oldLocationId}"><s:property value="oldLocationName" /></a></td>
					<td><a href="${virtualDirectory}/update/migrate/${newLocationId}"><s:property value="newLocationName" /></a></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</s:if>
<s:else>
	<h2>Migrate lot to: <s:property value="location.name" /></h2>

	<form method="POST" action="${virtualDirectory}/update/migrate!scanLot/${location.id}">
	<table class="inputform">
		<colgroup>
			<col width="20%" />
			<col />
		</colgroup>
		<tr>
			<td class="tdLabel"><label>Reason:</label></td>
			<td><s:textfield name="reason" value="%{reason}" label="Reason" /></td>
		</tr>
		<tr>
			<td class="tdLabel"><label>Scan barcode:</label></td>
			<td><s:textfield name="barCode" value="%{barCode}" label="Scan barcode" cssClass="autofocus" cssStyle="width: 100px;" /> 
			<s:if test="!getUser().hasRole('ROLE_BREEDER')">
				<s:submit value="Migrate" />
			</s:if>
			</td>
		</tr>
	</table>
	</form>

	<h2>Migration log</h2>

	<s:include value="/WEB-INF/jsp/paging.jsp">
		<s:param name="page" value="migrationsIn.page" />
		<s:param name="pages" value="migrationsIn.pages >= migrationsOut.pages ? migrationsIn.pages : migrationsOut.pages" />
		<s:param name="pageSize" value="migrationsIn.pageSize" />
		<s:param name="href" value="%{''}" />
	</s:include>

	<table class="data-listing">
		<colgroup>
			<col width="50%" />
			<col width="50%" />
		</colgroup>
		<thead>
			<tr>
				<td>To this location</td>
				<td>From this location</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><s:iterator value="migrationsIn.results">
					<div><s:date format="dd/MM/yyyy HH:mm" name="migrationDate" /> <b><a href="${virtualDirectory}/lot/${lot.id}"><s:property value="lot.item.name" /></a></b> from <a
						href="${virtualDirectory}/update/migrate/${oldLocationId}"><s:property value="oldLocationName" /></a></div>
				</s:iterator></td>
				<td><s:iterator value="migrationsOut.results">
					<div><s:date format="dd/MM/yyyy HH:mm" name="migrationDate" /> <b><a href="${virtualDirectory}/lot/${lot.id}"><s:property value="lot.item.name" /></a></b> to <a
						href="${virtualDirectory}/update/migrate/${newLocationId}"><s:property value="newLocationName" /></a></div>
				</s:iterator></td>
			</tr>
		</tbody>
	</table>
</s:else>
</body>
</html>