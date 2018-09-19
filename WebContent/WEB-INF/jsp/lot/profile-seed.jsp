<%@ include file="/common/taglibs.jsp"%>
<tr>
	<td>Weight:</td>
	<td colspan="3"><s:property value="lot.weight" /> g</td>
</tr>
<tr>
	<td>100 seed weight:</td>
	<td><s:property value="lot.weight100" /> g</td>
	<td class="label">Moisture content:</td>
	<td><s:property value="lot.moistureContent" /> %FWB</td>
</tr>
<tr>
	<td>Regeneration location:</td>
	<td><s:property value="lot.fieldLocation" /></td>
	<td class="label">Planting date:</td>
	<td><s:property value="lot.plantingDate" /></td>
</tr>
<tr>
	<td>Germination:</td>
	<td><s:property value="lot.germination" /> %</td>
	<td class="label">Germination date:</td>
	<td><iita:date format="dd/MM/yyyy" name="lot.germinationDate" /></td>
</tr>
<tr>
	<td>Harvest date:</td>
	<td><iita:date format="dd/MM/yyyy" name="lot.harvestDate" /></td>
	<td class="label">Year processed:</td>
	<td><s:property value="lot.yearProcessed" /></td>
</tr>
<tr>
	<td>GHU RefCode:</td>
	<td colspan="3"><s:property value="lot.referenceCode" /></td>
</tr>

<s:set name="newLastViabiliy" value="getTraitLastValue('VIABILITY', lot)" />
<s:if test="#newLastViabiliy!=null">
<tr>
	<td>Viability % at last test:</td>
	<td><s:property value="#newLastViabiliy.trait.decode(#newLastViabiliy.value)" /> <s:property value="#newLastViabiliy.trait.uom" /></td>
	<td class="label">Date for last viability test:</td>
	<td><iita:date format="dd/MM/yyyy" name="#newLastViabiliy.date" /></td>
</tr>
</s:if>
<%--
<s:if test="lot.lastViability!=null">
<tr>
	<td>Last viability:</td>
	<td><s:property value="%{lot.lastViability.viability}" /> %</td>
	<td class="label">Last viability date:</td>
	<td><iita:date format="dd/MM/yyyy" name="lot.lastViability.testDate" /></td>
</tr>
</s:if>--%>
<tr>
	<td>Conformity:</td>
	<td><s:if test="lot.conformity==null">Not specified</s:if><s:else><b><s:if test="lot.conformity">Lot is conform</s:if><s:else>Lot is not conform</s:else></b></s:else></td>
</tr>