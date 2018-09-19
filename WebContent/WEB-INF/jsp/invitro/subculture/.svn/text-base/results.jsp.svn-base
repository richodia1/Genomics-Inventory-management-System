<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>In-Vitro germplasm subcultured</title>
</head>
<body>

<s:form namespace="/update" action="invitro/subcultured!save" method="POST">
	<table class="inputform">
		<colgroup>
			<col width="200px" />
			<col />
			<col width="200px" />
			<col width="30%" />
		</colgroup>
		<tr>
			<td class="label"><s:text name="label.title" />:</td>
			<td colspan="3"><s:textfield name="description.title" value="%{description.title}" label="Title" /></td>
		</tr>
		<tr>
			<!--
				<td class="label"><s:text name="label.id" />:</td>
				<td><s:property value="description.id" /> @ <s:property value="description.version" /> Status: <s:property value="description.status" /></td>
				-->
			<td class="label"><s:text name="label.date" />:</td>
			<td><iita:datepicker format="dd/MM/yyyy" name="description.date" value="%{description.date}" /></td>
			<td class="label"><s:text name="transaction.type" />:</td>
			<td class="identifying"><s:property value="description.transactionType" /></td>
		</tr>
		<tr>
			<td class="label"><s:text name="label.description" />:</td>
			<td colspan="3"><s:textarea name="description.description" value="%{description.description}" label="Description" /></td>
		</tr>
		<tr>
			<td />
			<td colspan="3"><s:submit value="Store" /> <s:submit name="redirect-action:index" value="To list" /></td>
		</tr>
	</table>
	<s:hidden name="id" value="%{id}" />
	<s:hidden name="description.version" value="%{description.version}" />

	<h2>Inventory update list</h2>
	<s:if test="description.lots.size == 0">
		<p>No lots have been added to the list for stock update.</p>
	</s:if>
	<s:else>
		<table class="data-listing" id="inventorytable">
			<colgroup>
				<col width="200" />
				<col width="120" />
				<col />
				<col width="80" />
				<col width="130" />
				<col width="130" />
				<col width="110" />
			</colgroup>
			<thead>
				<tr>
					<td>Item</td>
					<td class="ar">Quantity</td>
					<td />
					<td>Type</td>
					<td>Location</td>
					<td>Container</td>
					<td>Last modified</td>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="description.sortedLots" status="status">
					<tr>
						<td><b><s:property value="lot.item.name" /></b> <s:property value="lot.introductionDate" /></td>
						<td class="ar"><s:textfield name="description.sortedLots[%{#status.index}].quantity" value="%{quantity}" theme="simple" cssStyle="width: 50px;" /> <s:property
							value="scale" /></td>
						<td>	<s:if test="lot.line!=null">(<s:property value="lot.line" />)</s:if>
							<s:if test="%{lot instanceof org.iita.inventory.model.InVitroLot}">
							<s:property value="lot.regenerationDate" />
							<s:if test="lot.virusFree">
								<b>VF</b>
							</s:if>
						</s:if></td>
						<td><s:property value="lot.item.itemType.name" /></td>
						<td></td>
						<td><s:property value="lot.container.name" /></td>
						<td><s:date name="lot.dateLastModified" format="%{getText('date.format')}" /></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</s:else>
</s:form>
</body>
</html>