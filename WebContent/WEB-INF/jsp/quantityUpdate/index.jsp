<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>List of inventory updates</title>
</head>
<body>
<s:form method="GET" action="index" namespace="/update">
	<b>Subtypes:</b>
	<s:checkboxlist name="type" list="transactionSubtypes" value="%{selectedSubtypes}" /><br />
	<b style="margin-right: 20px;">Location path:</b>

	<a style="margin-right: 20px;" href="<c:url value="/update/index.jspx" />">ROOT</a>
	<s:iterator value="locationPath">
		<a style="margin-right: 20px;" href="<c:url value="/update/index.jspx?locid=${id}" />"><s:property value="name" /></a>
	</s:iterator>

	<s:if test="subLocations!=null && subLocations.size>0">
		<s:select name="locid" list="subLocations" listKey="id" listValue="name" theme="simple" emptyOption="true"
			onchange="javascript: window.location.pathname='%{virtualDirectory}/update/index.jspx?locid=' + this.value;" />
	</s:if>

	<s:elseif test="location!=null && location.parent!=null">
		<s:select cssStyle="visibility: hidden;" name="locid" list="location.parent.children" listKey="id" listValue="name" theme="simple" headerKey="%{locid}"
			headerValue="%{location.name}" onchange="javascript: this.form.submit();" />
	</s:elseif>
	 <s:hidden name="locationid" value="%{location.id}" />
	<strong>Title:</strong> <s:textfield name="title" /> <s:submit value="Display" />
</s:form>

<s:if test="paged.results.size>0">

	<p>Found <b><s:property value="paged.totalHits" /></b> matching records.</p>

	<s:push value="paged">
		<s:include value="../paging.jsp">
			<s:param name="href" value="selectedTypesQueryString" />
		</s:include>
	</s:push>

	<table class="data-listing">
		<colgroup>
			<col width="50" />
			<col width="110" />
			<col width="110" />
			<col width="80" />
			<col width="160" />
			<col />
			<%--<col width="60" />--%>
		</colgroup>
		<thead>
			<tr>
				<td class="ar">#</td>
				<td>Created by</td>
				<td>Status</td>
				<td>Date</td>
				<td>Type</td>
				<td>Batch title</td>
				<%--<td class="ar">Lots</td> --%>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="paged.results" status="status">
				<tr>
					<s:if test="transactionSubtype=='CONTAMINATION' || transactionSubtype=='NECROSIS'">
						<s:url id="editUrl" namespace="/update" action="invitro/monitor" includeParams="none">
							<s:param name="id" value="id" />
						</s:url>
					</s:if>
					<s:elseif test="transactionSubtype=='SUBCULTURING'">
						<s:url id="editUrl" namespace="/update" action="invitro/subculture" includeParams="none">
							<s:param name="id" value="id" />
						</s:url>
					</s:elseif>
					<s:else>
						<s:url id="editUrl" namespace="/update" action="update!edit" includeParams="none">
							<s:param name="id" value="id" />
						</s:url>
					</s:else>
					<td class="ar"><s:property value="#status.count + paged.startAt" /></td>
					<td><s:property value="createdBy" /></td>
					<td><s:text name="quantityUpdate.status.%{status}" /></td>
					<td><s:date name="date" format="%{getText('date.format')}" /></td>
					<td style="<s:if test="transactionType.ordinal()==0">color: Red;</s:if>"><s:property value="transactionSubtype" /></td>
					<td class="identifying"><s:a href="%{editUrl}">
						<s:property value="title" />
					</s:a></td>
					<%--<td class="ar"><s:property value="lots.size" /></td>--%>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</s:if>
<s:else>
	<p>No inventory updates are registered.</p>
</s:else>
</body>
</html>