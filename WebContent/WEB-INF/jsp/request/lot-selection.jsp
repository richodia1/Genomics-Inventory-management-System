<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Lot selection for <s:property value="order.title" /></title>
<style type="text/css">
tr.lot-selected { background-color: #EACE7C; }
</style>
</head>
<body iita:help="inventory/requests">

<table class="inputform">
	<colgroup>
		<col width="200" />
		<col />
	</colgroup>
	<tr>
		<td><label>State:</label></td>
		<td><s:property value="order.state" /></td>
	</tr>
	<tr>
		<td><label>Title:</label></td>
		<td><s:property value="order.title" /></td>
	</tr>
	<tr>
		<td><label>Last, first name:</label></td>
		<td><s:property value="order.lastName" /> <s:property value="order.firstName" /></td>
	</tr>
	<tr>
		<td />
		<td></td>
	</tr>
</table>

<h2>Select lots</h2>

<s:form action="request/assign-lots!update" namespace="/" method="POST">
	<s:hidden name="id" value="%{order.id}" />
	<table class="data-listing">
		<colgroup>
			<col width="150" />
			<col width="200" />
			<col />
		</colgroup>
		<thead>
			<tr>
				<td>Item</td>
				<td>Qty</td>
				<td>Available lots</td>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="order.items">
				<s:if test="status==@org.iita.inventory.model.OrderItem$OrderItemStatus@PENDING && item!=null">
				<tr>
					<td><s:property value="item.name" /><s:hidden name="itemId" value="%{id}" /></td>
					<td><s:textfield name="quantity" value="%{quantity}" cssStyle="width: 80px;" /> <s:property value="scale" /></td>
					<td>
						<%--<s:select name="lotId" value="%{lot.id}" list="availableLots[item]" listKey="id" listValue="%{getItem().getName() + ' ' + getQuantity() + ' ' + getScale() + ' @ ' + getLocation().getName()}" emptyOption="true" cssStyle="width: 100%" />
						<s:hidden name="lotId" value="%{lot.id}" id="select_%{id}" />--%>
						<table><colgroup><col width="100px" /><col /><col width="20px" /><col width="145px" /><col width="145px" /></colgroup>
						<s:iterator value="availableLots[item]">
						<s:if test="quantity > 0">
						<tr class="lot-selector<s:if test="[1].lot.id==id"> lot-selected</s:if>" iita:lotid="${id}">
							<td><s:hidden name="lotId" value="%{id}" id="select_%{[1].id}" />
							<s:property value="quantity" /> <s:property value="scale" /></td>
							<td><s:if test="location==null"><em>No location</em></s:if><s:else><s:property value="location.pathNames" /></s:else></td>
							<s:if test="top instanceof org.iita.inventory.model.SeedLot">
								<td><s:property value="yearProcessed" /></td>
								<td><s:if test="germination!=null"><s:property value="germination" />% </s:if><iita:date format="dd/MM/yyyy" name="germinationDate" /></td>
								<td><s:set name="newLastViabiliy" value="[1].getTraitLastValue('VIABILITY', top)" />
									<s:if test="#newLastViabiliy!=null">
									<s:property value="#newLastViabiliy.trait.decode(#newLastViabiliy.value)" /> <s:property value="#newLastViabiliy.trait.uom" /> <iita:date format="dd/MM/yyyy" name="#newLastViabiliy.date" />
									</s:if>
								</td>
							</s:if>
						</tr>
						</s:if>
						</s:iterator>
						</table>
					</td>					
				</tr>
				</s:if>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<td><s:property value="order.items.size()" /> Items</td>
				<td /><td /><td />
			</tr>
		</tfoot>
	</table>

	<div class="button-bar"><s:submit value="Update" /></div>
</s:form>

<script type="text/javascript">
Event.observe(window, 'load', function() {	
	var matching = $$(".lot-selector");
	for (var i=0; i<matching.length; i++)
		Event.observe($(matching[i]), 'click', function(e) { 
			var selected=Element.hasClassName(this, 'lot-selected');
			this.parentNode.immediateDescendants().each(function(c) { Element.removeClassName(c, 'lot-selected'); }); 
			this.parentNode.parentNode.parentNode.firstDescendant().value=null;
			if (!selected) { this.parentNode.parentNode.parentNode.firstDescendant().value=this.getAttribute('iita:lotid'); Element.addClassName(this, 'lot-selected'); } 
		});
});
</script>
</body>
</html>