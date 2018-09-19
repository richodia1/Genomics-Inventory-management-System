<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Germplasm requests</title>
</head>
<body iita:help="inventory/requests">
<iita:authorize ifAnyGranted="ROLE_ORDER, ROLE_BREEDER">
	<div class="button-bar">Register <a href="<s:url action="request/edit!create" />">new order</a>
	<span style="margin-left: 20px;">List by Status:</span> <b><a href="<s:url action="request/index" />">ALL</a></b> <s:iterator value="@org.iita.inventory.model.Order$OrderState@values()"><a href="<s:url action="request/index" />?state=<s:property />"><s:property /></a> </s:iterator>
	</div>
</iita:authorize>

<s:include value="../paging.jsp">
	<s:param name="page" value="paged.page" />
	<s:param name="pages" value="paged.pages" />
	<s:param name="pageSize" value="paged.pageSize" />
	<s:param name="href" value="%{'state=' + state}" />
</s:include>
<table class="data-listing">
	<colgroup>
		<col width="50" />
		<col width="80" />
		<col width="80" />
		<col width />
		<col width="150" />
		<col width />
		<col width />
		<col width />
		<col width />
		<col width />
	</colgroup>
	<thead>
		<tr>
			<td class="ar">#</td>
			<td>Date</td>
			<td>State</td>
			<td>Title</td>
			<td>Client</td>
			<td>SMTA</td>
			<td>Import Permit</td>
			<td>Specific</td>
			<td>Shipping</td>
			<td>Production</td>
			<td>Stock</td>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="paged.results" status="status">
			<tr>
				<td class="ar"><s:property value="#status.count" /></td>
				<td><s:date name="lastUpdated" format="dd/MM/yyyy" /></td>
				<td><s:property value="state" /></td>
				<td class="identifying"><a href="<s:url action="request/order" />?id=<s:property value="id" />"><iita:text maxLength="60" value="%{title}" /></a></td>
				<td><s:property value="lastName" />, <s:property value="firstName" /></td>
				<td><s:iterator value="findTagByCategory(top, 'SMTA')"><s:property value="tagNoCat" /></s:iterator></td>
				<td><s:iterator value="findTagByCategory(top, 'Import Permit')"><s:property value="tagNoCat" /></s:iterator></td>
				<td><s:iterator value="findTagByCategory(top, 'Specific')"><s:property value="tagNoCat" /></s:iterator></td>
				<td><s:iterator value="findTagByCategory(top, 'Shipping')"><s:property value="tagNoCat" /></s:iterator></td>
				<td><s:iterator value="findTagByCategory(top, 'Production')"><s:property value="tagNoCat" /></s:iterator></td>
				<td><s:iterator value="findTagByCategory(top, 'Stock')"><s:property value="tagNoCat" /></s:iterator></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
</body>
</html>