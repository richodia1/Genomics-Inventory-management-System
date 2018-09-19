<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Inventory Container Update Form</title>
</head>
<body>
<s:form method="POST" action="conf/containertypes/container!saveContainerType">
	<table class="inputform">
		<colgroup>
			<col width="20%" />
			<col />
		</colgroup>
		<tr>
			<td><label>Container Type Name:</label></td>
			<td><s:textfield name="ctype.name" label="Container Type Name" id="name"></s:textfield></td>
		</tr>
		<tr>
			<td />
			<td><s:submit value="Submit" /></td>
		</tr>
	</table>
	<s:hidden name="ctype.id" value="%{ctype.id}" />
	<s:hidden name="ctype.version" value="%{ctype.version}" />
</s:form>
</body>
</html>