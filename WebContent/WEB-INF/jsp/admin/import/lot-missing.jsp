<%@ include file="/common/taglibs.jsp"%>
<table class="data-listing">
	<colgroup>
		<col />
		<col />
		<col />
		<col />
	</colgroup>
	<thead>
		<tr>
			<td>Item Name</td>
			<td>Prefix</td>
			<td>Location</td>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="top" status="status">
			<tr>
				<td><s:property value="top[0]" /></td>
				<td><s:property value="top[1]" /></td>
				<td><s:property value="top[2]" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>