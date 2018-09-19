<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Summary Overview</title>
</head>
<body>
<div style="height: 30px;">
<div style="float: right;"><security:authorize ifAnyGranted="ROLE_ADMIN,ROLE_MANAGER">
	<a href="<c:url value="/location/"  />?<s:property value="location.id" />">Edit location</a>
	<a
		href="<s:url action="download-xls" />?export.location=<s:property value="export.location.id" />&amp;export.scale=<s:property value="export.scale" />&amp;export.itemType=<s:property value="export.itemType" />">Download
	XLS</a>
	<a
		href="<s:url action="download-xls-summary" />?export.location=<s:property value="export.location.id" />&amp;export.scale=<s:property value="export.scale" />&amp;export.itemType=<s:property value="export.itemType" />">Summary Download
	XLS</a>
</security:authorize> <a href="<c:url value="/browse/location/" /><s:property value="export.location.id" />">Browse</a></div>
<s:form method="GET" action="index">
	<b style="margin-right: 20px;">Location path:</b>

	<a style="margin-right: 20px;"
		href="<s:url action="summary" />?export.type=<s:property value="export.type" />&amp;export.scale=<s:property value="export.scale" />&amp;export.itemType=<s:property value="export.itemType" />">ROOT</a>
	<s:iterator value="export.location.path">
		<a style="margin-right: 20px;"
			href="<s:url action="summary" />?export.location=${id}&amp;export.type=<s:property value="export.type" />&amp;export.scale=<s:property value="export.scale" />&amp;export.itemType=<s:property value="export.itemType" />"><s:property
			value="name" /></a>
	</s:iterator>

	<s:if test="subLocations!=null && subLocations.size>0">
		<s:select name="export.location" list="subLocations" listKey="id" listValue="name" theme="simple" emptyOption="true"
			onchange="javascript: window.location='%{virtualDirectory}/browse/summary.jspx?export.location=' + this.value + '%{'&export.type=' + export.type + '&export.scale=' + (export.scale==null ? '' : export.scale) + '&export.itemType=' + (export.itemType==null ? '' : export.itemType.id)}';" />
	</s:if>

	<s:elseif test="export.location!=null && export.location.parent!=null">
		<s:select cssStyle="visibility: hidden;" name="locid" list="export.location.parent.children" listKey="id" listValue="name" theme="simple" headerKey="%{locid}"
			headerValue="%{location.name}" onchange="javascript: this.form.submit();" />
	</s:elseif>
</s:form></div>

<s:form method="GET" action="summary">
<s:hidden name="export.location" value="%{export.location}" />
<s:select list="#{'0':'Crop summary','1':'Item summary'}" name="export.type" value="%{export.type}" />
<s:select list="itemTypes" name="export.itemType" value="%{export.itemType.id}" headerKey="" headerValue="ANY" listKey="id" listValue="name" />
<s:select list="#{'g':'gram','seed':'seed','tube':'tubes'}" name="export.scale" value="%{export.scale}" headerKey="" headerValue="ANY" />
<s:submit value="Display" />
</s:form>

<h2><s:if test="export.type==0">Crop</s:if><s:else>Item</s:else> summary of <em><s:if test="export.location==null">All locations</s:if><s:else>
	<s:property value="export.location.name" />
</s:else></em></h2>

<s:if test="export.type!=0">
<s:include value="/WEB-INF/jsp/paging.jsp">
	<s:param name="page" value="paged.page" />
	<s:param name="pages" value="paged.pages" />
	<s:param name="pageSize" value="paged.pageSize" />
	<s:param name="href"
		value="%{'export.location=' + export.location.id + '&amp;export.type=' + export.type + '&amp;export.scale=' + (export.scale==null ? '' : export.scale) + '&amp;export.itemType=' + (export.itemType==null ? '' : export.itemType.id)}" />
</s:include>
</s:if>

<s:set name="totalLots" value="0" />
<s:set name="totalQty" value="0" />
<table class="data-listing">
	<colgroup>
		<col width="5%" />
		<col />
		<col width="15%" />
		<col width="20%" />
		<col width="15%" />
	</colgroup>
	<thead>
		<tr>
			<td class="ar">#</td>
			<td />
			<td class="ar">Lots</td>
			<td class="ar">Quantity</td>
			<td />
		</tr>
	</thead>
	<tbody>
		<s:iterator value="paged.results" status="status">
			<tr>
				<td class="ar"><s:property value="#status.count + paged.startAt" /></td>
				<td class="identifying">
				<s:property value="top[0]" /> -- 
				<s:property value="top[1]" /> -- 
				<s:property value="top[2]" /> -- 
				<s:property value="top[3]" /> -- 
				<s:if test="export.type==0">
					<s:property value="top[0]" />
				</s:if>
				<s:else>
					<a href="<c:url value="/item" />/<s:property value="top[0].itemType.shortName" />/<s:property value="top[0].name" />"><s:property value="top[0].name" /></a>
				</s:else></td>
				<td class="ar">
					<s:set name="totalLots" value="%{#totalLots + top[2]}" />
					<s:text name="format.number">
						<s:param value="top[2]" />
					</s:text>
				</td>
				<td class="ar">
					<s:set name="totalQty" value="%{#totalQty + top[3]}" /><s:text name="format.number">
						<s:param value="top[3]" />
					</s:text>
				</td>
				<td><a
					href="<s:url action="summary" />?export.location=<s:property value="export.location" />&amp;export.type=<s:property value="export.type" />&amp;export.scale=<s:property value="top[1]" />&amp;export.itemType=<s:property value="export.itemType" />"><s:property
					value="top[1]" /></a></td>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr><td /><td /><td class="ar"><s:text name="format.number"><s:param value="#totalLots" /></s:text></td>
		<td class="ar"><s:text name="format.number"><s:param value="#totalQty" /></s:text></td><td /></tr>
	</tfoot>
</table>
</body>
</html>