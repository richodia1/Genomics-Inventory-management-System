<%@ include file="/common/taglibs.jsp"%>

<table class="data-listing">
	<colgroup>
		<col width="50" />
		<col width="120" />
		<col />
		<col width="120" />
	</colgroup>
	<thead>
		<tr>
			<td class="ar">#</td>
			<td class="ar">Last updated</td>
			<td>Name</td>
			<td />
		</tr>
	</thead>
	<tbody>
		<s:iterator value="top" status="status">
			<tr>
				<td class="ar"><s:property value="#status.index + 1" />.</td>
				<td><s:date name="lastUpdated" format="%{getText('date.format')}" /></td>
				<td><a href="variables.jspx?id=<s:property value="%{id}" />"><s:property value="name" /></a></td>
				<td><a href="<s:url action="variables!remove" />?id=<s:property value="%{id}" />" onClick="if(confirm('Remove anyway?')){return true;}else{return false;}">Remove</a></td>
			</tr>
		</s:iterator>
	</tbody>
</table>