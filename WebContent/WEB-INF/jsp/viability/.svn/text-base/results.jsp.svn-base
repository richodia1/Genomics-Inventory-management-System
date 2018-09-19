<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Viability results

<!-- 

	NO LONGER USED!!!!  
	
-->

</title>
</head>
<body>

<s:form namespace="/update" action="viability!save" method="POST">
	<table class="inputform">
		<colgroup>
			<col width="200" />
			<col />
		</colgroup>
		<tr>
			<td class="tdLabel">Test date:</td>
			<td><iita:datepicker name="testDate" value="%{testDate}" format="dd/MM/yyyy" /></td>
		</tr>
		<tr>
			<td class="tdLabel"><s:text name="transaction.type" />:</td>
			<td class="identifying"><s:property value="bulk.transactionType" /> <s:property value="bulk.transactionSubtype" /></td>
		</tr>
		<tr>
			<td class="tdLabel"><s:text name="label.title" />:</td>
			<td><s:property value="bulk.title" /></td>
		</tr>
		<s:if test="bulk.description!=null && bulk.description.length()>0">
		<tr>
			<td class="tdLabel"><s:text name="label.description" />:</td>
			<td><s:property value="bulk.description" /></td>
		</tr>
		</s:if>
		<tr>
			<td />
			<td><s:submit value="Store" /></td>
		</tr>
	</table>
	<s:hidden name="id" value="%{bulk.id}" />

	<h2>Viability results</h2>
	<s:if test="bulk.lots.size == 0">
		<p>No lots have been added to the list for stock update.</p>
	</s:if>
	<s:else>
		<table class="data-listing" id="inventorytable">
			<colgroup>
				<col width="200" />
				<col width="120" />
				<col width="120" />
				<col />
				<col width="160" />
				<col width="160" />
				<col width="110" />
			</colgroup>
			<thead>
				<tr>
					<td>Item</td>
					<td>Barcode</td>
					<td class="ar">Viability %</td>
					<td />
					<td>Type</td>
					<td>Container</td>
					<td>Last modified</td>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="bulk.sortedLots" status="status">
					<tr>
						<td><b><a href="<c:url value="/lot/" /><s:property value="lot.id" />"><s:property value="lot.item.name" /></a></b></td>
						<td><s:property value="lot.barCode.id" /></td>
						<td class="ar"><s:hidden name="viability[%{#status.index}].lotId" value="%{lot.id}" /><s:textfield name="viability[%{#status.index}].viability" value="%{viability[#status.index].viability}" cssClass="numeric-input" /> %</td>
						<td />
						<td><s:property value="lot.item.itemType.name" /></td>
						<td><s:property value="lot.container.name" /></td>
						<td><s:date name="lot.lastUpdated" format="%{getText('date.format')}" /></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</s:else>
</s:form>
</body>
</html>