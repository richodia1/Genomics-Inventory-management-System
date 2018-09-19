<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>Trait: <s:property value="trait.title" /></title>
</head>
<body iita:help="inventory/trials">
<table class="inputform">
	<colgroup>
		<col width="200" />
		<col />
	</colgroup>
	<tr>
		<td>Title:</td>
		<td><s:property value="trait.title" /></td>
	</tr>
	<tr>
		<td>Var name:</td>
		<td><s:property value="trait.var" /></td>
	</tr>
	<tr>
		<td>Unit of measure:</td>
		<td><s:property value="trait.uom" /></td>
	</tr>
	<tr>
		<td>Description:</td>
		<td><s:property value="trait.description" /></td>
	</tr>
	<tr>
		<td>Coded:</td>
		<td><s:property value="trait.coded" /></td>
	</tr>
</table>

</body>
</html>