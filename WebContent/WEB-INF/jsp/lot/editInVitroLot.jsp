<%@ include file="/common/taglibs.jsp"%>
<tr>
	<td class="label">Container:</td>
	<td><s:select name="lot.container" value="%{lot.container.id}" list="containerTypes" listKey="id" listValue="name" headerKey="" headerValue="[Not specified]" /></td>
</tr>
<tr>
	<td class="tdLabel"><label>Subculture date:</label></td>
	<td><s:textfield name="lot.regenerationDate" value="%{lot.regenerationDate}" cssStyle="width: auto;" /></td>
	<td class="tdLabel"><label>Introduction date:</label></td>
	<td><s:textfield name="lot.introductionDate" value="%{lot.introductionDate}" cssStyle="width: auto;" /></td>
</tr>
<tr>
	<td class="tdLabel"><label>Virus free</label></td>
	<td><s:radio list="#{'':'Not tested','true':'Virus free','false':'Not virus free'}" name="lot.virusFree" value="%{lot.virusFree}" /></td>
	<td class="tdLabel"><label>Origin Geo location:</label></td>
	<td><s:select list="#{'Brazillian':'Brazillian','CIAT':'CIAT','Columbia':'Columbia'}" emptyOption="true" name="lot.origin" value="%{lot.origin}" /></td>
</tr>

<tr>
	<td class="tdLabel"><label>Origin Explant:</label></td>
	<td><s:select list="#{'Meristem':'Meristem','Embryo Rescue Clones':'Embryo Rescue Clones','Nodal Cuttings':'Nodal Cuttings'}" emptyOption="true" name="lot.originExplant" value="%{lot.originExplant}" /></td>
	<td class="tdLabel"><label>Status:</label></td>
	<td><s:select list="#{'Old breeder line':'Old breeder line','New breeder line':'New breeder line'}" emptyOption="true" name="lot.invitroStatus" value="%{lot.invitroStatus}" /></td>
</tr>