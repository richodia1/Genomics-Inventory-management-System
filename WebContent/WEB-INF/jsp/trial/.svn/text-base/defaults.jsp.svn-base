<%@ include file="/common/taglibs.jsp"%>

<s:push value="paged.results[0]">
<table class="data-listing">
		<thead>
			<tr>
				<s:iterator status="columnIndex">
					<td><s:if test="#columnIndex.index==0">

					</s:if><s:else>
						<s:hidden name="data[0][%{#columnIndex.index}]" value="%{id}" />
						<big><s:property value="title" /></big>
						<s:if test="description!=null">
							<br />
							<s:property value="description" />
						</s:if>
					</s:else></td>
				</s:iterator>
			</tr>
		</thead>
		<tbody>
			<tr>
				<s:iterator status="columnIndex">
					<s:if test="#columnIndex.index==0">
					<td class="identifying">Default values</td>
					</s:if>
					<s:else>
					<td><s:if test="paged.results[0][#columnIndex.index].coded">
						<s:select name="data[1][%{#columnIndex.index}]" value="" list="paged.results[0][#columnIndex.index].coding" listKey="value" listValue="coding"
							headerKey="" headerValue="Not measured" />
					</s:if> <s:else>
						<s:textfield name="data[1][%{#columnIndex.index}]" value="" cssClass="numeric-input" />
						<s:property value="paged.results[0][#columnIndex.index].uom" />
					</s:else></td>
					</s:else>
				</s:iterator>
			</tr>
		</tbody>
	</table>
</s:push>