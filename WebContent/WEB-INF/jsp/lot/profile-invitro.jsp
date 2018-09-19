<%@ include file="/common/taglibs.jsp"%>
<tr>
	<td><label>Subculture date:</label></td>
	<td><iita:date format="dd/MM/yyyy" name="lot.regenerationDate" /></td>
	<td><label>Introduction date:</label></td>
	<td><iita:date format="dd/MM/yyyy" name="lot.introductionDate" /></td>
</tr>
<tr>
	<td><label>Virus free</label></td>
	<td><s:property value="lot.virusFree" /></td>
	<td><label>Origin Geo location:</label></td>
	<td><s:property value="lot.origin" /></td>
</tr>

<tr>
	<td><label>Origin Explant:</label></td>
	<td><s:property value="lot.originExplant" /></td>
	<td><label>Status:</label></td>
	<td><s:property value="lot.invitroStatus" /></td>
</tr>