<%@ include file="/common/taglibs.jsp"%>

<table class="data-listing">
<colgroup><col width="100" /><col width="100" /><col /></colgroup>
	<thead>
		<tr>
			<td>Status</td>
			<td>Date</td>
			<td>Title</td>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="top">
			<tr>
				<td><s:property value="status" /></td>
				<td><iita:date name="date" format="dd/MM/yyyy" /></td>
				<td class="identifying"><a href="<s:url action="trial/profile" namespace="/" />?id=<s:property value="id" />"><s:property value="name" /></a></td>
			</tr>
		</s:iterator>
	</tbody>
</table>