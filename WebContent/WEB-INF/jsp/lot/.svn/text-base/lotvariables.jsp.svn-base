<%@ include file="/common/taglibs.jsp"%>

<table class="data-listing">
	<colgroup>
		<col width="140" />
		<col width="150" />
		<col />
		<col width="140" />
		<col width="140" />	
		<col width="50" />	
	</colgroup>
	<thead>
		<tr>
			<td class="ar">Last updated</td>
			<td>Updated By</td>
			<td>Variable Name</td>
			<td class="ar">Quantity</td>
			<td class="ar">Date Processed</td>
			<td />
		</tr>
	</thead>
	<tbody>
		<s:iterator value="lotVariables" status="status">
			<tr style="<s:if test="quantity<1">color: Red;</s:if>">
				<td><s:if test="lastUpdated!=null"><s:date name="lastUpdated" format="dd/MM/yyyy HH:mm" /></s:if><s:else><s:date name="createdDate" format="dd/MM/yyyy HH:mm" /></s:else></td>
				<td><s:if test="lastUpdatedBy!=null"><s:property value="lastUpdatedBy" /></s:if><s:else><s:property value="createdBy" /></s:else></td>
				<td class="identifying"><a href="index.jspx?id=<s:property value="%{lot.id}" />&amp;lotVarid=<s:property value="%{id}" />"><s:property value="variable.name" /></a></td>
				<td class="ar"><s:property value="quantity" /></td>
				<td class="ar"><s:date name="variabledate" format="dd/MM/yyyy" /></td>
				<td><a href="<s:url action="index!removeLotVariable" />?id=<s:property value="%{lot.id}" />&amp;lotVarid=<s:property value="%{id}" />" onClick="if(confirm('Remove anyway?')){return true;}else{return false;}">Remove</a></td>
			</tr>
			
			<s:if test="transactions.size>0">
			<tr>
				<td colspan="2" />
				<td colspan="4">
				<h2>Transactions</h2>
				<%@ include file="transactions-lotvariables.jsp"%>
				</td>
			</tr>
			</s:if>
		</s:iterator>
	</tbody>
</table>