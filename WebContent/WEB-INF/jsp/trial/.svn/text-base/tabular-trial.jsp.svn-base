<%@ include file="/common/taglibs.jsp"%>

<table class="data-listing">
<colgroup><col width="100" /><col width="100" /><col width="120" /><col /><col width="150" /></colgroup>
	<thead>
		<tr>
			<td>User</td>
			<td>Status</td>
			<td>Date</td>
			<td>Title</td>
			<td>Traits</td>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="top">
			<tr>
				<td><s:property value="createdBy" /></td>
				<td><s:property value="status" /></td>
				<td><iita:date name="date" format="dd/MM/yyyy" /></td>
				<td class="identifying"><a href="<s:url action="trial/profile" namespace="/" />?id=<s:property value="id" />"><s:property value="name" /></a></td>
				<td><s:property value="traitGroup.title" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>