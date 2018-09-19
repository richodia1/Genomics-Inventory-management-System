<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title><s:property value="item.name" /></title>
</head>
<body>

<h1><a href="<s:url action="item" />?id=<s:property value="item.id" />"><s:property value="item.name" /></a></h1>
<table class="data-listing">
	<tr>
		<td>Name:</td>
		<td><b><s:property value="item.name" /></b></td>
	</tr>
	<s:if test="item.alternativeIdentifier!=null">
		<tr>
			<td>Alternative identifier:</td>
			<td><b><s:property value="item.alternativeIdentifier" /></b></td>
		</tr>
	</s:if>
	<tr>
		<td>Notes:</td>
		<td><s:property value="item.notes" /></td>
	</tr>
</table>

<s:if test="item.lots.size>0">
<h3>Lots</h3>
<table class="data-listing">
	<s:iterator value="item.lots">
		<tr>
			<td><a href="<s:url action="lot" />?id=<s:property value="id" />"><s:property value="quantity" /> <s:property value="scale" /></a></td>
			<td><s:if test="location!=null">
				<s:property value="location.pathNames" />
			</s:if><s:else>NOT ASSIGNED</s:else></td>
		</tr>
	</s:iterator>
</table>
</s:if>

</body>
</html>