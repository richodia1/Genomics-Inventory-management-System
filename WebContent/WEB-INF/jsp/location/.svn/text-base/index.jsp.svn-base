<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Location manager</title>
</head>
<body>
<div style="height: 30px;">
	<s:if test="location.id!=null">
	<div style="float: right;"><security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_MANAGER,ROLE_USER"><a href="<c:url value="/browse/location/"  /><s:property value="location.id" />">Browse location</a></security:authorize>
	<a href="<s:url action="summary" namespace="/browse" />?export.location=<s:property value="location.id" />">Summary</a>
	<a href="<s:url action="trial/create" namespace="/" />?trial.name=Location trial&amp;source=location&amp;locId=<s:property value="location.id" />">Create trial</a>
	</div>
	</s:if>
	<s:form method="GET" action="index">
	<b style="margin-right: 20px;">Location path:</b>

	<a style="margin-right: 20px;" href="<c:url value="/location/" />">ROOT</a>
	<s:iterator value="locationPath">
		<a style="margin-right: 20px;" href="<c:url value="/location/${id}" />"><s:property value="name" /></a>
	</s:iterator>

	<s:if test="subLocations!=null && subLocations.size>0">
		<s:select name="locid" list="subLocations" listKey="id" listValue="name" theme="simple" emptyOption="true"
			onchange="javascript: window.location.pathname='%{virtualDirectory}/location/' + this.value;" />
	</s:if>

	<s:elseif test="location!=null && location.parent!=null">
		<s:select cssStyle="visibility: hidden;" name="locid" list="location.parent.children" listKey="id" listValue="name" theme="simple" headerKey="%{locid}"
			headerValue="%{location.name}" onchange="javascript: this.form.submit();" />
	</s:elseif>
</s:form></div>

<%-- Regenerate --%>
<s:if test="editedLocation==null">
	<s:form action="index!locationflat">
		<s:submit value="Regenerate Location tree" />
	</s:form>
</s:if>

<s:if test="editedLocation!=null">
	<s:if test="editedLocation.id==null">
		<h2>Adding sub-location to: <s:property value="location.name" /></h2>
	</s:if>
	<s:else>
		<h2>Selected location: <s:property value="editedLocation.name" /></h2>
	</s:else>
	<s:form method="POST" action="index!update">
		<table class="inputform">
			<colgroup>
				<col width="20%" />
				<col width="30%" />
				<col width="20%" />
				<col width="30%" />
			</colgroup>
			<tr class="identifying">
				<td class="tdLabel"><label>Name:</label></td>
				<td><s:textfield name="editedLocation.name" value="%{editedLocation.name}" /></td>
			</tr>
			<tr>
				<td class="tdLabel"><label>Type:</label></td>
				<td><s:textfield name="editedLocation.locationType" value="%{editedLocation.locationType}" /></td>
			</tr>
			<tr>
				<td />
				<td><s:if test="editedLocation.id==null">
					<s:submit value="Update"
						onclick="javascript: return window.confirm('Are you sure you want to add a sub-location to:\n\t%{editedLocation.parent.name}\nwith name:\n\t' + $(index_editedLocation_name).value );" />
				</s:if> <s:else>
					<s:submit value="Update"
						onclick="javascript: return window.confirm('Are you sure you want to rename location\n\t%{editedLocation.name}\nto:\n\t' + $(index_editedLocation_name).value );" />
					<authz:authorize ifAnyGranted="ROLE_ADMIN">
						<s:submit action="index!remove" value="Remove" />
					</authz:authorize>
				</s:else></td>
			</tr>
			<s:if test="editedLocation.id!=null">
				<tr>
					<td class="tdLabel"><label>Browse location:</label></td>
					<td><a href="<c:url value="/browse/location/${editedLocation.id}" />" title="Browse <s:property value="editedLocation.name" />">View lots at <s:property
						value="editedLocation.name" /></a></td>
				</tr>
			</s:if>
		</table>
		<s:hidden name="editedLocation.id" value="%{editedLocation.id}" />
		<s:hidden name="editedLocation.version" value="%{editedLocation.version}" />
		<!--<s:if test="editedLocation.parent!=null">
			<s:hidden name="editedLocation.parent.id" value="%{editedLocation.parent.id}" />
		</s:if>
		-->
		<s:hidden name="locid" value="%{location.id}" />
		<s:hidden name="editid" value="%{editedLocation.id}" />
	</s:form>
</s:if>

<h3>Sub-locations</h3>
<s:form method="POST" action="index">
	<s:hidden name="locid" value="%{location.id}" />
	<s:submit value="Add sublocation" action="index!add" />
</s:form>
<s:if test="subLocations!=null && subLocations.size>0">
	<table class="data-listing">
		<colgroup>
			<col width="80" />
			<col width="120" />
			<col />
		</colgroup>
		<thead>
			<tr>
				<td class="ar">#</td>
				<td>Type</td>
				<td>Name</td>
			</tr>
		</thead>
		<tbody>
			<s:iterator status="status" value="subLocations">
				<tr>
					<td class="ar identifying"><s:property value="#status.index+1" /></td>
					<td><s:property value="locationType" /></td>
					<td><a href="<c:url value="/location/${id}" />"><s:if test="name==''">[Not named]</s:if><s:else><s:property value="name" /></s:else></a></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>

</s:if>
<s:else>
	<p>This location has no sub-locations.</p>
</s:else>

</body>
</html>