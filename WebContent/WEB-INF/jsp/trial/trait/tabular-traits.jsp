<%@ include file="/common/taglibs.jsp"%>

<table class="data-listing">
<colgroup><col width="150" /><col /><col width="100" /><col width="100" /><col width="100" /></colgroup>
	<thead>
		<tr>
			<td>Title</td>
			<td>Description</td>
			<td>Var</td>
			<td>UOM</td>
			<td>Coded</td>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="top">
			<tr>
				<td><a href="<s:url action="trial/trait" />?id=<s:property value="id" />"><s:property value="title" /></a></td>
				<td><s:property value="description" /></td>
				<td class="identifying"><s:property value="var" /></td>
				<td><s:property value="uom" /></td>
				<td><s:property value="coded" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>