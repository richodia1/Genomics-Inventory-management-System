<%@ include file="/common/taglibs.jsp"%>
<%-- Not used anymore! --%>
<table class="data-listing">
	<colgroup>
		<col width="10%" />
		<col />
	</colgroup>
	<thead>
		<tr>
			<td>Date</td>
			<td>Viability</td>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="lot.viabilityResults" status="status">
			<tr>
				<td><s:date name="testDate" format="dd/MM/yyyy" /></td>
				<td><s:property value="viability" /> %</td>
			</tr>
		</s:iterator>
	</tbody>
</table>