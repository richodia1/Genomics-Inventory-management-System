<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Selected lots</title>
</head>
<body>

<s:if test="results.size > 0">
	<div style="margin: 5px 0px 20px 5px;"><s:form method="POST" namespace="/selection">
		<s:submit action="index!printLabels" value="Print labels" />
		<s:submit action="index!clearAll" value="Clear selection" />
		<s:submit action="index!download" value="Download XLS" />
		<s:submit action="index!downloadWithFieldVariables" value="Download XLS with FieldVariables" />
	</s:form>
	<s:form method="POST" action="trial/create" namespace="/">
		<s:hidden name="source" value="selection" />
		<s:submit value="Create trial" />
	</s:form>
		<span style="margin-left: 100px;">Lots in selection: <b><s:property value="selectionService.selectedList.selectedLots.size" /></b></span>
	</div>

	<%@ include file="/WEB-INF/jsp/browse/list.jsp"%>
</s:if>
<s:else>
	<p>There are no lots in your selection list.</p>
</s:else>

<h3>Load and save your list</h3>
<s:form method="post" action="index!save">
	List name: <s:textfield name="list.name" value="%{list.name}" />
	<s:if test="list.id==null">
		<s:submit value="Store list" />
	</s:if>
	<s:else>
		<s:submit value="Update stored list" />
	</s:else>
</s:form>
<s:if test="list.id!=null">
	<s:form method="post" action="index!delete">
		<s:hidden name="list.id" value="%{list.id}" />
		<s:submit value="Delete" onclick="javascript: return confirm('Delete list: %{list.name}?');" />
	</s:form>
</s:if>

<s:form method="post" action="index!load">
	<s:select name="list.id" list="existingLists" listKey="id" listValue="name" />
	<s:submit value="Load list" />
</s:form>

<iita:collapse id="addstuff" closedHeading="Bulk add lots to selection" heading="Close importer">
	<s:form method="post" action="index!addBulk">
		<table class="inputform">
			<colgroup>
				<col width="200" />
				<col />
			</colgroup>
			<tr>
				<td>Paste Lot IDs:<br />or Scan barcodes:</td>
				<td><s:textarea name="lotIds" /></td>
			</tr>
			<tr>
				<td />
				<td><s:submit value="This is Lot IDs" /> <s:submit action="index!addBar" value="This is barcodes" /></td>
			</tr>
		</table>
	</s:form>
</iita:collapse>
</body>
</html>