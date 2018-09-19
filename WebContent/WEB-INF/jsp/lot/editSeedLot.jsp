<%@ include file="/common/taglibs.jsp"%>
<tr>
	<td class="label">Container:</td>
	<td><s:select name="lot.container" value="%{lot.container.id}" list="containerTypes" listKey="id" listValue="name" headerKey="" headerValue="[Not specified]" /></td>
	<%--<td class="label">Barcode:</td>
			<td>Use selection screen after storing lot information</td> --%>
</tr>
<tr>
	<td class="label">Weight:</td>
	<td colspan="2"">
		<s:textfield name="lot.weight" value="%{lot.weight}" cssClass="numeric-input" /> g
		<s:submit value="Read balance" action="update!readBalance" />
	</td>
</tr>
<tr>
	<td class="label">100 seed weight:</td>
	<td><s:textfield name="lot.weight100" value="%{lot.weight100}" cssClass="numeric-input" /> g</td>
	<td class="label">Moisture content:</td>
	<td><s:textfield name="lot.moistureContent" value="%{lot.moistureContent}" cssClass="numeric-input" /> %FWB</td>
</tr>
<tr>
	<td class="label">Regeneration location:</td>
	<td><s:textfield name="lot.fieldLocation" value="%{lot.fieldLocation}" /></td>
	<td class="label">Planting date:</td>
	<td><iita:datepicker name="lot.plantingDate" value="%{lot.plantingDate}" cssClass="numeric-input" /></td>
</tr>
<tr>
	<td class="label">Germination:</td>
	<td><s:textfield name="lot.germination" value="%{lot.germination}" cssClass="numeric-input" /> %</td>
	<td class="label">Germination date:</td>
	<td><iita:datepicker name="lot.germinationDate" value="%{lot.germinationDate}" cssClass="numeric-input" /></td>
</tr>
<%-- 
<s:if test="lot.lastViability!=null">
	<tr>
		<td class="label">Last viability:</td>
		<td><s:property value="%{lot.lastViability.viability}" /> %</td>
		<td class="label">Last viability date:</td>
		<td><s:property value="%{lot.lastViability.testDate}" /></td>
	</tr>
</s:if>
--%>
<tr>
	<td class="label">Harvest date:</td>
	<td><iita:datepicker name="lot.harvestDate" value="%{lot.harvestDate}" /></td>
	<td class="label">Year processed:</td>
	<td><s:textfield name="lot.yearProcessed" value="%{lot.yearProcessed}" cssClass="numeric-input" /></td>
</tr>
<tr>
	<td class="label">Conformity:</td>
	<td><s:select name="lot.conformity" value="%{lot.conformity}" list="#{'true': 'Conform yes', 'false':'Not conform'}" headerKey="" headerValue="-- Unspecified" /></td>
</tr>