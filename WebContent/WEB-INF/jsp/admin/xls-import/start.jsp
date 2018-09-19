<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>XLS data import</title>
</head>
<body>
<s:include value="menu.jsp" />

<s:form method="post" enctype="multipart/form-data" action="xls-import!upload">
	<table class="inputform">
		<colgroup>
			<col width="200px" />
			<col />
		</colgroup>
		<tr>
			<td>Target entity:</td>
			<td><s:select list="entities" emptyOption="true" name="target" value="%{target}" listKey="name" listValue="name" /></td>
		</tr>
		<tr>
			<td>XLS file:</td>
			<td><s:file name="uploads" /></td>
		</tr>
		<tr>
			<td />
			<td><s:submit value="Upload data" /></td>
		</tr>
	</table>
</s:form>
</body>
</html>