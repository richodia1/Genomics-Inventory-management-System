<%@ include file="/common/taglibs.jsp"%>
<table class="data-listing">
	<colgroup>
		<col width="140" />
		<col width="150" />
		<col width="140" />
		<col />
	</colgroup>
	<thead>
		<tr>
			<td>Date</td>
			<td>Trait</td>
			<td>Value</td>
			<td>Trial, Trait</td>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="top">
			<tr>
				<td><iita:date name="date" format="dd/MM/yyyy HH:mm" /></td>
				<td class="identifying"><s:property value="trait.title" /></td>
				<td><s:if test="value==null"><em>Result pending</em></s:if><s:else><b><s:property value="trait.decode(value)" /> <s:property value="trait.uom" /></b></s:else></td>
				<td><s:if test="trial!=null"><a href="<s:url action="trial/profile" namespace="/" />?id=<s:property value="trial.id" />#ent<s:property value="entity.id" />"><s:property value="trial.name" /></a>: </s:if><s:property value="trait.description" /></td>
			</tr>
		</s:iterator>
	</tbody>
</table>