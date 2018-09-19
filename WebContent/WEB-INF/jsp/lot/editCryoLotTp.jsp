<%@ include file="/common/taglibs.jsp"%>
<tr>
	<td class="tdLabel"><label>Introduction date (MTS):</label></td>
	<td><iita:datepicker name="lot.introductionDate" value="%{lot.introductionDate}" /></td>
</tr>
<tr>
<td class="tdLabel"><label>Cryo date:</label></td>
	<td><iita:datepicker name="lot.cryoDate" value="%{lot.cryoDate}" /></td>
</tr>
<tr>
	<td class="tdLabel"><label>Health Status:</label></td>
<td><s:textfield name="lot.healthstatus" value="%{lot.healthstatus}" cssStyle="width: auto;" /></td>
</tr>
<tr>
<td class="tdLabel"><label>Remarks:</label></td>
	<td><s:textfield name="lot.remarks" value="" cssStyle="width: auto;" /></td>
</tr>
<tr>
	<td class="tdLabel"><label>Number of meristerm in Temp= 36 (12*3 vials)</label></td>
	<td><s:textfield name="lot.numbermeristermtemp" value="" cssStyle="width: auto;" /></td>
</tr>
<tr>
	<td class="tdLabel"><label>Viability test date (3 weeks after cryo):</label></td>
	<td><iita:datepicker name="lot.viabcultureDate" value="%{lot.viabcultureDate}" /></td>
	<td class="tdLabel"><label>Quantity tested for viability (Meristerm):</label></td>
<td><s:textfield name="lot.qtytestedforviability" value="" cssStyle="width: auto;" /></td>
</tr>
<tr>
	<td class="tdLabel"><label>Observation date (12 weeks after test):</label></td>
	<td><iita:datepicker name="lot.observationdate" value="%{lot.observationdate}" /></td>
	<td class="tdLabel"><label>Regrowth rate:</label></td>
<td><s:textfield name="lot.regrowthrate" value="" cssStyle="width: auto;" />%</td>
</tr>