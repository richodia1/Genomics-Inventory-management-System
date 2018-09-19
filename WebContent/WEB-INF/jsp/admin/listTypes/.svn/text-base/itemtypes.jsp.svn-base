<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>Inventory items</title>
</head>
<body><s:form>
	<!--<s:submit name="redirect-action:update!input" value="New printer" theme="simple" />-->
<s:submit name="redirect-action:conf/itemtypes/itemtype"   value="New Item Type" theme="simple" />
</s:form>
<s:if test="typeresults.size > 0">
	<table class="data-listing">
		<colgroup>
			<col width="40" />
			<col />
			<col width="200" />
		</colgroup>
		<thead>
			<tr>
				<td />
				<td>Name</td>
				<td class="ar">Manage</td>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="typeresults" status="status">
				<s:url action="conf/itemtypes/itemtype" id="itemEditUrl" includeParams="none">
					<s:param name="id" value="id" />
				</s:url>
				<tr>
					<td class="ar"><s:property value="#status.index+1" /></td>
					<td><s:a href="%{itemEditUrl}"><s:property value="name" /></s:a></td>
					<td class="ar"><s:a href="%{itemEditUrl}">
					Edit</s:a> | <s:url action="conf/deleteitemtype" id="itemDelUrl" includeParams="none">
						<s:param name="id" value="id" />
					</s:url><s:a href="%{itemDelUrl}">Delete</s:a></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</s:if>
</body>
</html>