<%@ include file="/common/taglibs.jsp"%>

<table class="data-listing">
		<colgroup>
			<col width="120" />
			<col />
			<col width="150" />
			<col width="150" />
			<col width="150" />
			<col width="100" />
		</colgroup>
		<thead>
			<tr>
				<td>Item</td>
				<td />
				<td>Location</td>
				<td>Quantity</td>
				<td>Current</td>
				<td style="text-align: right;">Tools</td>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="description.sortedLots" status="status">
				<tr <s:if test="#readonly==false and description.transactionType+''=='OUT' and lot.quantity - quantity < 0 and description.affectingInventory">class="row-error"</s:if>>
					<td class="identifying"><s:url id="lotUrl" namespace="/lot" action="index" includeParams="none">
						<s:param name="id" value="lot.id" />
					</s:url><s:a href="%{lotUrl}">
						<s:property value="lot.item.name" />
					</s:a></td>
					<td>
						<div style="float:right">
							<div class="select-tool"><a x:id="<s:property value="lot.id" />" class="btn_select" href="#" title="Add to current selection list">Select</a></div>
							<div class="unselect-tool"><a x:id="<s:property value="lot.id" />" class="btn_unselect" href="#" title="Remove from current selection list">Remove</a></div>
						</div>
						<s:if test="lot.line!=null && lot.line.length()>0">
							(<s:property value="lot.line" />)
						</s:if>
					</td>
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
			<tr><td colspan="6"><s:property value="description.lots.size" /> item(s) in list</td></tr>
		</tfoot>
	</table>