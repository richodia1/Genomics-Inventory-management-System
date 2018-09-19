<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Order information</title>
</head>
<body>
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
		<td><label>Organization:</label></td>
		<td><s:property value="order.organization" /></td>
	</tr>
	<tr>
		<td><label>Shipping address:</label></td>
		<td><s:property value="order.shippingAddress" /></td>
	</tr>
	<tr>
		<td><label>Order date:</label></td>
		<td><s:date name="order.orderDate" format="dd/MM/yyyy" /></td>
	</tr>
	<tr>
		<td><label>Purpose:</label></td>
		<td><s:property value="order.purpose" /></td>
	</tr>
	<tr>
		<td><label>Description:</label></td>
		<td><s:property value="order.description" /></td>
	</tr>
	<s:if test="order.createdDate!=null">
		<tr>
			<td><label>Created:</label></td>
			<td><s:date name="order.createdDate" format="dd/MM/yyyy" /> by <b><s:property value="order.createdBy" /></b></td>
		</tr>
		<tr>
			<td><label>Last update:</label></td>
			<td><s:date name="order.lastUpdated" format="dd/MM/yyyy" /> by <b><s:property value="order.lastUpdatedBy" /></b></td>
		</tr>
	</s:if>
	<tr>
		<td />
		<td>
		<div class="button-bar">
			<input type="button" onClick="javascript: window.location.pathname='<s:url action="order/edit" />?id=<s:property value="order.id" />';" value="Edit order" />
			<input type="button" onClick="javascript: window.location.pathname='<s:url action="order/assign-lots" />?id=<s:property value="order.id" />';" value="Assign lots" />
		</div>
		</td>
	</tr>
</table>

<!-- Order files -->
<h2>Task files</h2>
<div>
	<iita:filedropzone action="order/files!upload" queryParams="id=${order.id}">
		<div style="margin: 5px;">You can attach files by dragging-and-dropping them right HERE! The <b>copy</b> icon will appear.</div>
	</iita:filedropzone>
</div>
<s:if test="files.size>0">
<ul class="file-list">
	<s:iterator value="files">
		<li class="${file.directory?'directory':'file'}">
		<s:if test="file.directory"><a href="<s:url action="order/files" />?id=<s:property value="order.id" />&directory=<s:property value="fileName" />"><s:property value="fileName" /></a></s:if>
		<s:else><a href="<s:url action="order/files!download" />?id=<s:property value="order.id" />&file=<s:property value="fileName" />"><s:property value="fileName" /></a></s:else></li>
	</s:iterator>
</ul>
</s:if>


<s:if test="order.id!=null">
	<!-- Only rendered if order is persisted -->
	<h2>Ordered items</h2>
	<iita:collapse id="paste_names" heading="Hide form" closedHeading="Add items by pasting">
		<s:form method="post" action="order/order!pasteNames">
			<s:hidden name="id" value="%{order.id}" />
			<table class="inputform">
				<tr>
					<td><s:textarea name="accessionNames" /></td>
				</tr>
				<tr>
					<td><s:submit value="Add items" /></td>
				</tr>
			</table>
		</s:form>
	</iita:collapse>
	<table class="data-listing">
		<colgroup>
			<col width="300" />
			<col width="200" />
			<col />
			<col width="200" />
		</colgroup>
		<thead>
			<tr>
				<td>Item</td>
				<td>Qty</td>
				<td>Lot</td>
				<td>Available</td>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="order.items">
				<tr>
					<td><s:property value="item.name" /></td>
					<td><s:property value="quantity" /> <s:property value="scale" /></td>
					<td><s:if test="lot==null">
						<i>Lot not selected</i>
					</s:if><s:else>
						<s:property value="lot.item.name" />
						<s:property value="lot.id" />
					</s:else></td>
					<td><s:if test="lot!=null">
						<s:property value="lot.quantity" />
						<s:property value="lot.scale" />
					</s:if></td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<td><s:property value="order.items.size()" /> Items</td>
		</tfoot>
	</table>
</s:if>
</body>
</html>