<%@ include file="/common/taglibs.jsp"%>

<s:if test="item!=null">
	<s:set name="title" value="%{item.name}" />
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

<s:form action="update" namespace="/admin/print" method="POST">
	<table class="inputform">
		<colgroup>
			<col width="20%" />
			<col />
		</colgroup>
		<tbody>
			<tr>
				<td>Label type:</td>
				<td><s:if test="printer==null || printer.id==null">
					<s:select name="label" list="labelInfos" listKey="id" listValue="name" />
				</s:if> <s:else>
					<s:property value="%{printer.labelInfo.name}" />
				</s:else></td>
			</tr>
			<tr>
				<td>Name:</td>
				<td><s:textfield name="printer.name" value="%{printer.name}" label="Print name" /></td>
			</tr>
			<tr>
				<td>Location:</td>
				<td><s:textfield name="printer.location" value="%{printer.location}" label="Print location" /></td>
			</tr>
			<tr>
				<td>Allowed IP list:</td>
				<td><s:textarea name="printer.allowedIPaddresses" value="%{printer.allowedIPaddresses}" label="Print allowed IP addresses" /></td>
			</tr>
			<s:if test="printer instanceof org.iita.inventory.printing.SystemPrinterInfo">
				<tr>
					<td>Printer service name:</td>
					<td><%@ include file="editSystemPrinter.jsp"%></td>
				</tr>
			</s:if>
			<tr>
				<td />
				<td><s:hidden name="id" value="%{printer.id}" /> <s:hidden name="printer.version" value="%{printer.version}" /> <s:submit value="Update" /> <s:submit name="redirect-action:index"
					value="Back" /></td>
			</tr>
		</tbody>
	</table>
</s:form>

</body>
</html>