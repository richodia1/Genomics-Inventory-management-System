<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Lot variables</title>
</head>
<body>
<h2>Variable</h2>
<s:form action="updateVariable" namespace="/browse" method="post">
<s:hidden name="id" value="%{id}"></s:hidden>
<table class="inputform" id="lotvariable_editor">
	<colgroup>
		<col width="150px" />
		<col />
	</colgroup>
	<tr>
		<td>Variable Name</td>
		<td><s:textfield  name="variable.name" value="%{variable.name}" /></td>
	</tr>
	<tr>
		<td colspan="2"><s:submit value="Update" /></td>
	</tr>
</table>
</s:form>
<h2>Variables list</h2>
<s:if test="varResults!=null && varResults.size()>0">
	<s:push value="varResults">
		<s:include value="/WEB-INF/jsp/browse/variables-tabular-list.jsp" />
	</s:push>
</s:if>
<s:else><em>Not items found!</em></s:else>
</body>
</html>