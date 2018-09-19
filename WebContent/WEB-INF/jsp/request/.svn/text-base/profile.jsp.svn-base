<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Order information</title>
</head>
<body iita:help="inventory/requests">
<%@ include file="navigation.jsp" %>

<table>
<colgroup><col /><col width="350" /></colgroup>
<tr><td style="padding-right: 10px">

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
	<s:if test="requester!=null">
		<tr>
			<td><label>Requester:</label></td>
			<td><s:property value="order.requester.fullName" /></td>
		</tr>
	</s:if>
	<tr>
		<td><label>Last, first name:</label></td>
		<td><s:property value="order.lastName" /> <s:property value="order.firstName" /></td>
	</tr>
	<s:if test="order.mail!=null">
	<tr>
		<td><label>Mail:</label></td>
		<td><a href="mailto:<s:property value="order.mail" />?subject=Re: Germplasm request"><s:property value="order.mail" /></a></td>
	</tr>
	</s:if>
	<tr>
		<td><label>Organization:</label></td>
		<td><s:property value="order.organization" /> <em><s:property value="order.organizationCategory" /></em></td>
	</tr>
	<tr>
		<td><label>Shipping address:</label></td>
		<td><pre><s:property value="order.shippingAddress" /></pre></td>
	</tr>
	<tr>
		<td><label>Country:</label></td>
		<td><s:property value="order.country" /></td>
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
	<tr>
		<td><label>Contact info (Full name):</label></td>
		<td><s:property value="order.contactPerson" /></td>
	</tr>
	<tr>
		<td><label>Contact info (Phone):</label></td>
		<td><s:property value="order.contactInfo" /></td>
	</tr>
	<s:if test="order.shippingCost!=null">
	<tr>
		<td><label>Shipping cost:</label></td>
		<td><s:text name="format.number"><s:param value="order.shippingCost" /></s:text> USD</td>
	</tr>
	<tr>
		<td><label>SMTA option:</label></td>
		<td><s:property value="order.agreementOption" /> <s:if test="order.smta611"><b>Opted for option 6.11</b></s:if></td>
	</tr>
	<tr>
		<td><label>Provider:</label></td>
		<td><s:property value="order.providerName" />, <s:property value="order.providerOrganization" /></td>
	</tr>
	</s:if>
	<s:if test="order.createdDate!=null">
		<tr>
			<td><label>Audit:</label></td>
			<td><s:date name="order.createdDate" format="dd/MM/yyyy" /> by <b><s:property value="order.createdBy" /></b>
				last updated <s:date name="order.lastUpdated" format="dd/MM/yyyy" /> by <b><s:property value="order.lastUpdatedBy" /></b></td>
		</tr>
	</s:if>
	<tr>
		<td />
		<td>
		<div class="button-bar">
			<iita:authorize ifAnyGranted="ROLE_ORDER, ROLE_BREEDER">
				<input type="button" onClick="javascript: window.location.href='<s:url action="request/edit" />?id=<s:property value="order.id" />';" value="Edit order" />
				<s:if test="order.items.size()>0">
					<input type="button" onClick="javascript: window.location.href='<s:url action="request/assign-lots" />?id=<s:property value="order.id" />';" value="Assign lots" />
				</s:if>
				<iita:authorize ifAnyGranted="ROLE_ORDER">
					<input type="button" onClick="javascript: window.location.href='<s:url action="request/order!process" />?id=<s:property value="order.id" />';" value="Create distribution" />
					<input type="button" onClick="javascript: window.location.href='<s:url action="request/order!reject" />?id=<s:property value="order.id" />';" value="Reject" />
					<input type="button" onClick="javascript: window.location.href='<s:url action="request/order!split" />?id=<s:property value="order.id" />';" value="Split" />
				</iita:authorize>
			</iita:authorize>
			<input type="button" onClick="javascript: window.location.href='<s:url action="request/index" />';" value="To list" />
		</div>
		</td>
	</tr>
</table>
</td>
<td>
<s:if test="order.updates.size()>0">
<h2>Distributions</h2>
	<s:iterator value="order.updates">
	<div><b><a href="<s:url action="request/order!smta" />?id=<s:property value="[1].order.id" />&amp;bulk=<s:property value="id" />">SMTA</a></b> <a href="<s:url namespace="/update" action="update!edit" />?id=<s:property value="id" />"><iita:date name="date" format="dd/MM/yyyy" /> <s:property value="title" /></a></div>
	</s:iterator>
</s:if>

<h2>Tags</h2>
<s:iterator value="order.tags">
	<s:include value="/WEB-INF/jsp/tags/tag-readonly.jsp" />
</s:iterator>

<!-- Order files -->
<h2>Order files</h2>
<iita:authorize ifAnyGranted="ROLE_ORDER">
<div>
	<iita:filedropzone action="request/files!upload" queryParams="id=${order.id}">
		<div style="margin: 5px;">You can attach files by dragging-and-dropping them right HERE! The <b>copy</b> icon will appear.</div>
	</iita:filedropzone>
</div>
</iita:authorize>

<s:if test="files.size>0">
<ul class="file-list">
	<s:iterator value="files">
		<li class="${file.directory?'directory':'file'}">
		<s:if test="file.directory"><a href="<s:url action="request/files" />?id=<s:property value="order.id" />&directory=<s:property value="fileName" />"><s:property value="fileName" /></a></s:if>
		<s:else><a href="<s:url action="request/files!download" />?id=<s:property value="order.id" />&file=<s:property value="fileName" />"><s:property value="fileName" /></a></s:else></li>
	</s:iterator>
</ul>
</s:if>


</td></tr>
</table>

<s:if test="order.id!=null">
	<!-- Only rendered if order is persisted -->
	<h2>Ordered items</h2>	
	<table class="data-listing">
		<colgroup>
			<col width="60" />
			<col width="150" />
			<col width="200" />
			<col />
			<col width="200" />
		</colgroup>
		<thead>
			<tr>
				<td>Status</td>
				<td>Item</td>
				<td>Qty</td>
				<td>Lot</td>
				<td>Available</td>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="order.items">
				<tr>
					<td><s:property value="status" /></td>
					<td class="identifying"><s:if test="item!=null"><s:property value="item.name" /></s:if><s:else><em><s:property value="itemName" />???</em></s:else></td>
					<td><s:property value="quantity" /> <s:property value="scale" /></td>
					<td><s:if test="lot==null">
						<i>Lot not selected</i>
					</s:if><s:else>
						<s:property value="lot.item.name" />
						<s:property value="lot.location.pathNames" />
					</s:else></td>
					<td><s:if test="lot!=null">
						<s:property value="lot.quantity" />
						<s:property value="lot.scale" />
					</s:if></td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="2"><s:property value="order.items.size()" /> Items</td>
				<td />
				<td />
				<td />
			</tr>
		</tfoot>
	</table>
</s:if>
</body>
</html>