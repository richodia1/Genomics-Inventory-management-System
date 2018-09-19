<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>List of inventory updates</title>
</head>
<body>
<s:form method="GET" action="updates">
	<s:select name="type" list="transactionSubtypes" value="%{selectedSubtypes}" headerKey="" headerValue="All" />
	<s:submit value="Display" />
</s:form>

<s:if test="paged.results.size>0">
	<s:push value="paged">
		<s:include value="/WEB-INF/jsp/paging-mobile.jsp">
			<s:param name="href" value="selectedTypesQueryString" />
		</s:include>
	</s:push>

	<table class="data-listing">
		<thead>
			<tr>
				<td>Owner</td>
				<td>Batch title</td>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="paged.results" status="status">
				<tr>
					<s:if test="transactionSubtype=='CONTAMINATION' || transactionSubtype=='NECROSIS'">
						<s:url id="editUrl" action="invitro/monitor" includeParams="none">
							<s:param name="id" value="id" />
						</s:url>
					</s:if>
<%--					<s:elseif test="transactionSubtype=='SUBCULTURING'">
						<s:url id="editUrl" action="invitro/subculture" includeParams="none">
							<s:param name="id" value="id" />
						</s:url>
					</s:elseif>
					<s:else>
						<s:url id="editUrl" action="update!edit" includeParams="none">
							<s:param name="id" value="id" />
						</s:url>
				</s:else>
--%>
<s:else></s:else>
					<td><s:property value="createdBy" /></td>
					<td class="identifying"  style="<s:if test="transactionType.ordinal()==0">color: Red;</s:if>"><s:text name="quantityUpdate.status.%{status}" />: <s:a href="%{editUrl}">
						<s:property value="title" />
					</s:a></td>
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