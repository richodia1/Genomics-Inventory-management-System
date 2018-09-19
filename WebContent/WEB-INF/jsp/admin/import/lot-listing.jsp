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
		<s:iterator value="#lots" status="status">
			<tr>
				<td><s:property value="item.name" /></td>
				<td><s:property value="item.prefix" /></td>
				<td><s:property value="location.name" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>
<s:if test="#lots.size==0">
	<p>No lots have been filed</p>
</s:if>