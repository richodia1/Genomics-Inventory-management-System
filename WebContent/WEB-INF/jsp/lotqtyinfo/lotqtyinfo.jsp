<%@ include file="/common/taglibs.jsp"%>
<s:if test="paged.results!=null && paged.results.size>0">
	<s:include value="/WEB-INF/jsp/paging.jsp">
		<s:param name="page" value="paged.page" />
		<s:param name="pages" value="paged.pages" />
		<s:param name="pageSize" value="paged.pageSize" />
		<s:param name="href" value="%{''}" />
	</s:include>
	<s:push value="paged.results">
		<table class="data-listing">
			<colgroup>
				<col />
				<col />
				<col />
				<col />
				<col />
				<col />
			</colgroup>
			<thead>
				<tr>
					<td>#</td>
					<td>Acc Name</td>
					<td>Scale</td>
					<td>Type</td>
					<td>Qty</td>
					<td>Location</td>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="top" status="status">
					<tr>
						<td class="ar"><s:property value="#status.count + paged.startAt" /></td>
						<td><s:property value="top[0]" /></td>
						<td><s:property value="top[1]" /></td>
						<td><s:property value="top[2]" /></td>
						<td><s:property value="top[3]" /></td>
						<td><s:property value="top[4]" /></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</s:push>
	<s:include value="/WEB-INF/jsp/paging.jsp">
		<s:param name="page" value="paged.page" />
		<s:param name="pages" value="paged.pages" />
		<s:param name="pageSize" value="paged.pageSize" />
		<s:param name="href" value="%{''}" />
	</s:include>
</s:if>
<s:else>
	<p>No matching trials found.</p>
</s:else>