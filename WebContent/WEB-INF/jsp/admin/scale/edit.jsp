<%@ include file="/common/taglibs.jsp"%>

<s:if test="item!=null">
	<s:set name="title" value="%{item.host}" />
</s:if>
<s:else>
	<s:set name="title" value="%{'Register new item'}" />
</s:else>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><s:property value="#title" /></title>
</head>
<body>

<s:form action="update" namespace="/admin/scale" method="POST">
	<table class="inputform">
		<colgroup>
			<col width="20%" />
			<col />
		</colgroup>
		<tbody>
			<tr>
				<td>Host:</td>
				<td><s:textfield name="scale.host" value="%{scale.host}" label="Scale host" /></td>
			</tr>
			<tr>
				<td>Port:</td>
				<td><s:textfield name="scale.port" value="%{scale.port}" label="Host port" /></td>
			</tr>
			<tr>
				<td />
				<td><s:hidden name="id" value="%{scale.id}" /> <s:hidden name="scale.version" value="%{scale.version}" /> <s:submit value="Update" /> <s:submit name="redirect-action:index"
					value="Back" /></td>
			</tr>
		</tbody>
	</table>
</s:form>

</body>
</html>