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
	<td class="tdLabel"><label>Number of meristerm in SD=84(12*7vials)</label></td>
	<td><s:textfield name="lot.numbmrtmsd" value="" cssStyle="width: auto;" /></td>
</tr>
<tr>
	<td class="tdLabel"><label>Regrowth validation rate:</label></td>
	<td><s:textfield name="lot.regrowthvalidationrate" value="" cssStyle="width: auto;" />%</td>
</tr>
<tr>
	<td class="tdLabel"><label>SD verification date (after 10 years):</label></td>
	<td><iita:datepicker name="lot.sdverificationdate" value="%{lot.sdverificationdate}" /></td>
	<td class="tdLabel"><label>SD Verification quantity (number of meristerms is 12)</label></td>
<td><s:textfield name="lot.sdverificationqty" value="" cssStyle="width: auto;" /></td>
</tr>
<tr>
<td class="tdLabel"><label>SD verification rate:</label></td>
	<td><s:textfield name="lot.sdverificationrate" value="" cssStyle="width: auto;" />%</td>
</tr>
<tr>
<td class="tdLabel"><label>Verification Remarks:</label></td>
	<td><s:textfield name="lot.verificationremarks" value="" cssStyle="width: auto;" /></td>
</tr>