<%@ include file="/common/taglibs.jsp"%>
<tr>
	<td class="label">Container:</td>
	<td><s:select name="lot.container" value="%{lot.container.id}" list="containerTypes" listKey="id" listValue="name" headerKey="" headerValue="[Not specified]" /></td>
</tr>

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
	<td class="tdLabel"><label>Select Cryo Phase:</label></td>
	<td>
	<s:if test="cryophase!=null"> 
		<s:radio list="#{'':'Null','temp':'TEMP','lts':'LTS','sd':'SD'}" name="lot.cryophase" value="%{cryophase}" onclick="javascript: window.location='%{virtualDirectory}/lot/register.jspx?item='+%{lot.item.id}+'&lotType=cryolot&cryophase=' + this.value;" />
	</s:if>
	<s:else>
		<s:radio list="#{'':'Null','temp':'TEMP','lts':'LTS','sd':'SD'}" name="lot.cryophase" value="%{lot.cryophase}" onclick="javascript: window.location='%{virtualDirectory}/lot/register.jspx?item='+%{lot.item.id}+'&lotType=cryolot&cryophase=' + this.value;" />
	</s:else>
	<br />CRYOPHASE 1: <s:property value="cryophase" /><br />
	CRYOLOT 2: <s:property value="lotType" /><br />
	ITEM 3: <s:property value="lot.item" /><br />
	</td>
</tr>
		<s:if test="lot.item!=null && lotType.equals('cryolot') && cryophase.equals('temp')"> 
			<%@ include file="editCryoLotTp.jsp"%>
		</s:if>
			<s:elseif test="lot.item!=null && lotType.equals('cryolot') && cryophase.equals('lts')">
				<%@ include file="editCryoLotLts.jsp"%>
			</s:elseif>
			<s:elseif test="lot.item!=null && lotType.equals('cryolot') && cryophase.equals('sd')">
				<%@ include file="editCryoLotSd.jsp"%>
			</s:elseif>
			<s:else>
				<!-- GENERIC -->
			</s:else>