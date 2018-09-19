<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>Inventory items</title>
</head>
<body>
<div style="height: 30px;"><s:form method="GET" action="item/index" theme="simple">
	<b>Item filter:</b>
	<s:textfield name="q" value="%{q}" />
	<s:submit value="Search for items" />
</s:form> <s:if test="results!=null">
	<span style="margin-left: 100px;">Found <b><s:property value="allResults" /></b> matching records.</span>
</s:if></div>

<s:if test="results.size > 0">
	<table class="data-listing">
		<colgroup>
			<col width="40" />
			<col width="100" />
			<col width="140" />
			<col />
			<col width="140" />
			<col width="100" />
			<col width="200" />
		</colgroup>
		<thead>
			<tr>
				<td />
				<td>Type</td>
				<td>Name</td>
				<td>Latin name</td>
				<td>Altenative</td>
				<td>Prefix</td>
				<td>Accession Identifier</td>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="results" status="status">
				<tr>
					<td class="ar"><s:property value="#status.index+1" /></td>
					<td><s:property value="itemType.name" /></td>
					<td class="identifying"><a href="<s:url action="item/edit" />?id=<s:property value="id" />"><s:property value="name" /></a></td>
					<td class="identifying"><i><s:property value="latinName" /></i></td>
					<td><s:property value="alternativeIdentifier" /></td>
					<td><s:property value="prefix" /></td>
					<td><s:property value="accessionIdentifier" /></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</s:if>
</body>
</html>