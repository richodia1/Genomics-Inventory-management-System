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
	<td class="tdLabel"><label>Number of meristem in LTS =120(12*10vials)</label></td>
	<td><s:textfield name="lot.numbermistermlts" value="" cssStyle="width: auto;" /></td>
</tr>
<tr>
	<td class="tdLabel"><label>Regrowth validation rate:</label></td>
	<td><s:textfield name="lot.regrowthvalidationrate" value="" cssStyle="width: auto;" />%</td>
</tr>
