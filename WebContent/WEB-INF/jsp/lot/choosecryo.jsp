<%@ include file="/common/taglibs.jsp"%>
<s:form  method="POST">
<table class="inputform">
<tr>
	<td class="tdLabel"><label>Select Cryo Phase:</label></td>
	<td>
	<s:radio list="#{'':'Null','temp':'TEMP','lts':'LTS','sd':'SD'}" name="cryophase" value="%{cryophase}" onclick="javascript: window.location='%{virtualDirectory}/lot/register.jspx?item='+%{lot.item.id}+'&lotType=cryolot&cryophase=' + this.value;" />
	</td>
</tr>
</table>
</s:form>