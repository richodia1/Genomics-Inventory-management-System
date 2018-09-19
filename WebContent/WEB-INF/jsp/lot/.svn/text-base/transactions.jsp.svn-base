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
		<s:iterator value="lot.transactions" status="status">
			<tr style="<s:if test="quantity<0">color: Red;</s:if>">
				<td class="ar"><s:date name="date" format="dd/MM/yyyy HH:mm" /></td>
				<td class="identifying"><s:property value="subtype" /></td>
				<td class="ar"><s:text name="format.number">
					<s:param value="quantity" />
				</s:text> <s:property value="scale" /></td>
				<td><b><s:property value="lastUpdatedBy" /></b> <s:if test="rel!=null">
					<s:if test="source+''=='BULK'">
						<a href="<s:url namespace="/update" action="update!edit" />?id=<s:property value="rel" />">Bulk inventory update</a>
					</s:if>
					<s:else>
						<s:property value="source" />
						<s:property value="rel" />
					</s:else>
				</s:if><s:else>
					<s:property value="source" />
				</s:else>
				</td>
			</tr>
		</s:iterator>
	</tbody>
</table>