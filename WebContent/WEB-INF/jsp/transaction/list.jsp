<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Browse transaction list</title>
</head>
<body>

<div class="actionMessage"><s:form method="post" action="transaction/list!download">
	<span>Start date:</span>
	<iita:datepicker name="startDate" format="dd/MM/yyyy" />
	<span style="margin-left: 10px">End date:</span>
	<iita:datepicker name="endDate" format="dd/MM/yyyy" />
	<span><s:submit value="Download XLS" /></span>
</s:form></div>

<s:push value="paged">
	<s:include value="/WEB-INF/jsp/paging.jsp" />
</s:push>

<table class="data-listing">
	<colgroup>
		<col width="140" />
		<col width="140" />
		<col width="140" />
		<col width="10%" />
		<col width="15%" />
		<col />
	</colgroup>
	<thead>
		<tr>
			<td class="ar">Date &amp; Time</td>
			<td>User</td>
			<td>Type</td>
			<td>Lot</td>
			<td class="ar">Quantity</td>
			<td>Source</td>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="paged.results" status="status">
			<tr style="<s:if test="quantity<0">color: Red;</s:if>">
				<td class="ar"><s:date name="date" format="dd/MM/yyyy HH:mm" /></td>
				<td class="identifying"><s:property value="lastUpdatedBy" /></td>
				<td><s:property value="type" /> <s:property value="subtype" /></td>
				<td><a href="<c:url value="/lot/" /><s:property value="lot.id" />"><s:property value="lot.item.name" /></a></td>
				<td class="ar"><s:text name="format.number">
					<s:param value="quantity" />
				</s:text> <s:property value="scale" /></td>
				<td><s:if test="rel!=null">
					<s:if test="source+''=='BULK'">
						<a href="<s:url namespace="/update" action="update!edit" />?id=<s:property value="rel" />">Bulk inventory update</a>
					</s:if>
					<s:else>
						<s:property value="source" />
						<s:property value="rel" />
					</s:else>
				</s:if><s:else>
					<s:property value="source" />
				</s:else></td>
			</tr>
		</s:iterator>
	</tbody>
</table>

<s:push value="paged">
	<s:include value="/WEB-INF/jsp/paging.jsp" />
</s:push>


</body>
</html>