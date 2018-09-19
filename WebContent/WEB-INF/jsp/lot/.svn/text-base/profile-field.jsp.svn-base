<%@ include file="/common/taglibs.jsp"%>
<tr>
	<td class="label"><label>Planting date:</label></td>
	<td><iita:date format="dd/MM/yyyy" name="lot.plantingDate" /></td>
</tr>
<s:if test="lot.pegNumber!=null">
	<tr>
		<td class="label"><label>Peg number:</label></td>
		<td><s:property value="lot.pegNumber" /></td>
	</tr>
</s:if>
<s:if test="lot.latitude!=null || lot.longitude!=null">
	<tr>
		<td class="label"><label>Geoposition:</label></td>
		<td>(<s:property value="lot.latitude" />, <s:property value="lot.longitude" />)</td>
		<s:if test="lot.geoposDate!=null">
			<td class="label"><label>Geoposition date:</label></td>
			<td><iita:date format="dd/MM/yyyy" name="lot.geoposDate" /></td>
		</s:if>
	</tr>
</s:if>