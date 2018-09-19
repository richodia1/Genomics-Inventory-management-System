<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>Create new trial</title>
</head>
<body iita:help="inventory/trials">
<s:form method="post" action="trial/create!create">
	<s:hidden name="source" value="%{source}" />
	<s:hidden name="bulkId" value="%{bulkId}" />
	<s:hidden name="locId" value="%{locId}" />
	<table class="inputform" id="lotprofile_editor">
		<colgroup>
			<col width="200" />
			<col />
			<col width="200" />
			<col />
		</colgroup>
		<tr>
			<td>Trial:</td>
			<td colspan="3"><s:textfield name="trial.name" value="%{trial.name}" /></td>
		</tr>
		<tr>
			<td>Date of trial:</td>
			<td colspan="3"><iita:datepicker name="trial.date" value="%{trial.date}" /></td>
		</tr>
		<tr>
			<td>Trait group:</td>
			<td colspan="3"><s:select name="traitGroup" value="%{traitGroup.id}" list="traitGroups" listKey="id" listValue="title" /></td>
		</tr>
		<tr>
			<td>Description:</td>
			<td colspan="3"><s:textarea name="trial.description" value="%{trial.description}" cssClass="sizable-textarea" /></td>
		</tr>
		<tr>
			<td />
			<td><s:submit value="Create trial" /></td>
		</tr>
	</table>
</s:form>
<s:if test="importedLots!=null">
<h2>Lots included in trial</h2>
<s:push value="importedLots">
	<s:include value="/WEB-INF/jsp/lot/tabular-list.jsp" />
</s:push>
</s:if>
</body>
</html>