<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Inventory Item Update Form</title>
</head>
<body>
<s:form method="POST" action="conf/itemtypes/itemtype!saveType">
	<table class="inputform">
		<colgroup>
			<col width="20%" />
			<col />
		</colgroup>
		<tr>
			<td><label>Item Type Name:</label></td>
			<td><s:textfield name="itype.name" label="Item Type Name" id="name"></s:textfield></td>
		</tr>
		<tr>
			<td><label>Short name:</label></td>
			<td><s:textfield name="itype.shortName" label="Short Name" id="shortName"></s:textfield></td>
		</tr>
		<tr>
			<td />
			<td><s:submit value="Submit" /></td>
		</tr>
	</table>
	<s:hidden name="itype.id" value="%{itype.id}" />
	<s:hidden name="itype.version" value="%{itype.version}" />
</s:form>
</body>
</html>