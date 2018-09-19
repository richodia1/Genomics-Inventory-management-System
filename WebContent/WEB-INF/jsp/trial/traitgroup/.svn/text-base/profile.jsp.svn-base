<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>Trait group: <s:property value="traitGroup.title" /></title>
</head>
<body iita:help="inventory/trials">
<table class="inputform" id="traitgroup_editor">
	<colgroup>
		<col width="200" />
		<col />
		<col width="200" />
		<col />
	</colgroup>
	<tr>
		<td>Title:</td>
		<td colspan="3"><big><s:property value="traitGroup.title" /></big></td>
	</tr>
	<tr>
		<td>Short name:</td>
		<td colspan="3"><s:property value="traitGroup.shortName" /></td>
	</tr>
	<tr>
		<td>Description:</td>
		<td colspan="3"><s:property value="traitGroup.description" /></td>
	</tr>
	<tr>
		<td class="label">Created:</td>
		<td><iita:date name="traitGroup.createdDate" format="dd/MM/yyyy" /> by <s:property value="traitGroup.createdBy" /></td>
		<td class="label">Updated:</td>
		<td><iita:date name="traitGroup.lastUpdated" format="dd/MM/yyyy" /> by <s:property value="traitGroup.lastUpdatedBy" /></td>
	</tr>
	<tr>
		<td />
		<td>
		</td>
	</tr>
</table>


<ol>
<s:iterator value="traitGroup.traits">
	<li><s:include value="../trait/short.jsp" /></li>
</s:iterator>
</ol>

</body>
</html>