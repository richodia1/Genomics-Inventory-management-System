<%@ include file="/common/taglibs.jsp"%>

<s:form action="update" namespace="/lot" method="post">
	<s:hidden name="referer" value="%{referer}" />
	<s:hidden name="id" value="%{lot.id}" />
	<table class="inputform">
		<colgroup>
			<col width="200" />
			<col />
			<col width="200" />
			<col />
		</colgroup>
		<tr>
			<td>Item:</td>
			<td colspan="3"><big><s:property value="lot.item.name" /></big> View <a href="<c:url value="/item/${lot.item.itemType.shortName}/${lot.item.name}" />">all
			lot information for <b><s:property value="lot.item.name" /></b></a>.</td>
		</tr>
		<s:if test="lot.item.notes!=null && lot.item.notes.length()>0">
			<tr>
				<td>Item Notes:</td>
				<td colspan="3"><s:property value="lot.item.notes" /></td>
			</tr>
		</s:if>
		<tr>
			<td>Current quantity:</td>
			<td><s:textfield name="lot.quantity" value="%{lot.quantity}" cssClass="numeric-input" /> <s:select name="lot.scale" list="lot.uoms"
				listValue="%{getText('lot.scale.' + toString())}" /></td>
			<s:if test="lot.initialQuantity!=null">
				<td class="label">Initial quantity:</td>
				<td><s:property value="lot.initialQuantity" /> <s:property value="lot.initialScale" /></td>
			</s:if>
		</tr>
		<tr>
		<td class="tdLabel"><label>Line:</label></td>
			<td><s:textfield name="lot.line" value="%{lot.line}" cssStyle="width:auto;" /></td>
			<td></td><td></td>
		</tr>
		<s:if test="lot instanceof org.iita.inventory.model.SeedLot">
			<%@ include file="editSeedLot.jsp"%>
		</s:if>
		<s:elseif test="lot instanceof org.iita.inventory.model.InVitroLot">
			<%@ include file="editInVitroLot.jsp"%>
		</s:elseif>
		<s:elseif test="lot instanceof org.iita.inventory.model.FieldLot">
			<%@ include file="editFieldLot.jsp"%>
		</s:elseif>
		<s:elseif test="lot instanceof org.iita.inventory.model.OtherLot">
			
		</s:elseif>
		<s:else>
			<!-- GENERIC -->
		</s:else>
		<tr>
			<td>Location:</td>
			<td colspan="3"><s:if test="lot.location!=null">
				<a href="<c:url value="/browse/location/${lot.location.id}" />"><s:property value="lot.location.pathNames" /></a>
			</s:if> <s:else>
				<span class="important">Location not assigned, use <a href="<c:url value="/update/migrate/" />">lot migration</a> to assign lot to a location.</span>
			</s:else></td>
		</tr>
		<tr>
			<td class="label">Location detail:</td>
			<td colspan="3"><s:textfield name="lot.locationDetail" value="%{lot.locationDetail}" /></td>
		</tr>
		<tr>
			<td class="label">Notes:</td>
			<td colspan="3"><s:textarea name="lot.notes" value="%{lot.notes}" cssClass="sizable-textarea" /></td>
		</tr>
		<tr>
			<td />
			<td><s:submit value="Update" /></td>
		</tr>
	</table>
</s:form>