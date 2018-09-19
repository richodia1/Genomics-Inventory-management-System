<%@ include file="/common/taglibs.jsp"%>

<table class="data-listing">
	<colgroup>
		<col width="140" />
		<col width="150" />
		<col width="140" />
		<col />
	</colgroup>
	<thead>
		<tr>
			<td class="ar">Date &amp; Time</td>
			<td>Type</td>
			<td class="ar">Quantity</td>
			<td>Source</td>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="transactions" status="status">
			<tr style="<s:if test="quantity<0">color: Red;</s:if>">
				<td class="ar"><s:date name="date" format="dd/MM/yyyy HH:mm" /></td>
				<td class="identifying"><s:property value="subtype" /></td>
				<td class="ar"><s:text name="format.number">
					<s:param value="quantity" />
				</s:text> <s:property value="scale" /></td>
				<td><b><s:property value="lastUpdatedBy" /></b> 
					<s:property value="source" />
				</td>
			</tr>
		</s:iterator>
	</tbody>
</table>