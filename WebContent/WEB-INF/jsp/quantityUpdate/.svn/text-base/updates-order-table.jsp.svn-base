<%@ include file="/common/taglibs.jsp"%>

<table class="data-listing">
		<colgroup>
			<col width="120" />
			<col />
			<col width="150" />
			<col width="150" />
			<col width="100" />
		</colgroup>
		<thead>
			<tr>
				<td>Item</td>
				<td>Location</td>
				<td>Quantity</td>
				<td>Current</td>
				<td style="text-align: right;">Tools</td>
			</tr>
		</thead>
		<tbody>
			<!-- Find requested but missing items -->
			<s:if test="description.status==0">
			<s:iterator value="description.order.findMissing(description)" status="status">
				<s:if test="lot!=null">
				<tr>
					<td class="identifying"><a href="<s:url namespace="/lot" action="index" />?id=<s:property value="lot.id" />"><s:property value="lot.item.name" /></a></td>
					<td><a href="<s:url action="index" namespace="/browse" />?locid=<s:property value="lot.location.id" />"><s:property value="lot.location.pathNames" /></a></td>
					<td><s:property value="quantity" /> <s:property value="lot.scale" /></td>
					<td><s:property value="lot.quantity" /> <s:property value="lot.scale" /></td>
					<td style="text-align: right"><b>Pending</b></td>
				</tr>
				</s:if>
			</s:iterator>
			</s:if>
			<s:iterator value="description.sortedLots" status="status">
				<tr <s:if test="#readonly==false and description.transactionType+''=='OUT' and lot.quantity - quantity < 0 and description.affectingInventory">class="row-error"</s:if>>
					<td class="identifying"><a href="<s:url namespace="/lot" action="index" />?id=<s:property value="lot.id" />"><s:property value="lot.item.name" /></a></td>
					<td><s:url id="locationUrl" namespace="/browse" action="index" includeParams="none">
						<s:param name="locid" value="lot.location.id" />
					</s:url> <s:a href="%{locationUrl}">
						<s:property value="lot.location.name" />
					</s:a></td>
					<td><s:property value="quantity" /> <s:property value="scale" /></td>
					<td><s:property value="lot.quantity" /> <s:property value="lot.scale" /></td>
					<td style="text-align: right"><s:if test="description.status==0">
						<s:url id="editUrl" namespace="/update" action="update!edit">
							<s:param name="id" value="%{description.id}" />
							<s:param name="itemid" value="%{id}" />
						</s:url>
						<s:a href="%{editUrl}">Edit</s:a>
						<s:url id="removeUrl" namespace="/update" action="update!removeItem">
							<s:param name="id" value="%{description.id}" />
							<s:param name="itemid" value="%{id}" />
						</s:url>
						<s:a href="%{removeUrl}">Remove</s:a>
					</s:if><s:else>-</s:else></td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr><td colspan="5"><s:property value="description.lots.size" /> item(s) in list</td></tr>
		</tfoot>
	</table>