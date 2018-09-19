<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>XLS data import - Mapping</title>
</head>
<body>

<s:include value="menu.jsp" />

<s:form method="post" action="xls-import!map">
	<table class="inputform">
		<colgroup>
			<col width="100px" />
			<col width="200px" />
			<col />
		</colgroup>
		<s:iterator value="availableProperties">
		<tr>
			<td><b><s:property value="type.simpleName" /></b></td>
			<td><s:property value="ognl" /></td>
			<td><s:select list="xlsColumns" name="mapping['%{ognl}']" value="%{mapping[ognl]}" emptyOption="true"  /></td>
		</tr>
		</s:iterator>
		<tr>
			<td />
			<td><s:submit value="Update mapping" /></td>
		</tr>
	</table>
</s:form>
</body>
</html>