<%@ include file="/common/taglibs.jsp"%>

<table class="data-listing">
	<colgroup>
		<col width="140" />
		<col width="50%" />
		<col />
	</colgroup>
	<thead>
		<tr>
			<td>Date</td>
			<td>Location</td>
			<td>Reason</td>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="lot.migrations" status="status">
			<tr>
				<td><a href="${virtualDirectory}/update/migrate/${newLocationId}" title="View migrations to this location"><iita:date name="migrationDate" format="dd/MM/yyyy HH:mm" /></a></td>
				<td>
					<a href="<s:url namespace="/browse" action="index" includeParams="none"><s:param name="locid" value="%{oldLocationId}" /></s:url>"><s:property value="oldLocationName" /></a>
					<br />
					<a href="<s:url namespace="/browse" action="index" includeParams="none"><s:param name="locid" value="%{newLocationId}" /></s:url>"><b><s:property value="newLocationName" /></b></a>
				</td>
				<td><b><s:property value="lastUpdatedBy" /></b> <s:property value="reason" />
				</td>
			</tr>
		</s:iterator>
	</tbody>
</table>