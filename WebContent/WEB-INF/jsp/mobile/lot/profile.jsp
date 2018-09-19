<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title><s:property value="lot.item.name" /></title>
</head>
<body>
<s:form namespace="/mobile" action="lot" method="GET">
	<s:include value="../scanbarcode.jsp" />
</s:form>

<h1><a href="<s:url action="item" />?id=<s:property value="lot.item.id" />"><s:property value="lot.item.name" /></a></h1>
<table class="data-listing">
	<tbody>
		<tr>
			<td>Quantity</td>
			<td><b><s:property value="lot.quantity" /> <s:property value="lot.scale" /></b></td>
		</tr>
		<tr>
			<td>Container</td>
			<td><b><s:property value="lot.container.name" /></b></td>
		</tr>
		<tr>
			<td>Total Qty</td>
			<td><b><s:property value="totalLocationQuantity" /> <s:property value="lot.scale" /></b> in same loc.</td>
		</tr>
		<s:if test="lot.line!=null">
		<tr>
			<td>Line</td>
			<td><b><s:property value="lot.line" /></b></td>
		</tr>
		</s:if>
		<s:if test="lot.parent1">
			<tr>
				<td>Parent</td>
				<td><s:property value="%{getText('lot.parentType.' + lot.parent1type)}" /> <a href="<s:action name="lot" />?id=<s:property value="lot.parent1.id" />">Lot
				<s:property value="lot.parent1.id" /></a></td>
			</tr>
		</s:if>
		<s:if test="lot instanceof org.iita.inventory.model.SeedLot">
			<s:if test="lot.weight!=null">
				<tr>
					<td>Weight</td>
					<td><b><s:property value="lot.weight" /> g</b></td>
				</tr>
			</s:if>
			<tr>
				<td>100 seed Weight</td>
				<td><b><s:property value="lot.weight100" /> g</b></td>
			</tr>
			<tr>
				<td>Moisture content</td>
				<td><b><s:text name="format.percent">
					<s:param value="lot.moistureContent * 100" />
				</s:text></b></td>
			</tr>
		</s:if>
		<s:elseif test="lot instanceof org.iita.inventory.model.InVitroLot">
			<tr>
				<td>Virus free</td>
				<td><b><s:property value="lot.virusFree" /></b></td>
			</tr>
			<tr>
				<td>Subcultured</td>
				<td><b><iita:date name="%{lot.regenerationDate}" format="dd/MM/yyyy HH:mm" /></b></td>
			</tr>
			<tr>
				<td>Introduction</td>
				<td><b><iita:date name="%{lot.introductionDate}" format="dd/MM/yyyy" /></b></td>
			</tr>
			<s:if test="lot.origin!=null">
				<tr>
					<td>Origin</td>
					<td><b><s:property value="lot.origin}" /></b></td>
				</tr>
			</s:if>
		</s:elseif>
		<s:else>
			<tr>
				<td colspan="2">Contact support <b><s:property value="lot.class.name" /></b></td>
			</tr>
		</s:else>


		<tr>
			<td>Location</td>
			<td><s:if test="lot.location!=null">
				<s:property value="lot.location.pathNames" />
			</s:if><s:else>NOT ASSIGNED</s:else></td>
		</tr>

		<s:if test="lot.children1!=null && lot.children1.size>0">
			<tr>
				<td>Children</td>
				<td><s:iterator value="lot.children1">
					<a href="<s:action name="lot" />?id=<s:property value="id" />">Lot <s:property value="id" /></a>
				</s:iterator></td>
			</tr>
		</s:if>

	</tbody>
</table>

<div style="margin-top: 10px;">Last updated <s:date name="lot.lastUpdated" format="dd/MM/yyyy HH:mm" /> by <s:property value="lot.lastUpdatedBy" /></div>

</body>
</html>