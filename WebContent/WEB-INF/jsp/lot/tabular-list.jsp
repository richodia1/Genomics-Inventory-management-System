<%@ include file="/common/taglibs.jsp"%>

<table class="data-listing">
	<colgroup>
		<col width="50" />
		<s:if test="getUser().hasRole('ROLE_ADMIN')">
			<col width="30" />
		</s:if>
		<col width="120" />
		<col width="130" />
		<col />
		<col />
		<col width="80" />
		<col width="130" />
		<col width="110" />
	</colgroup>
	<thead>
		<tr>
			<td class="ar">#</td>
			<s:if test="getUser().hasRole('ROLE_ADMIN')">
				<td class="ar"><input type="button" id="delAll" value="Del" onClick="deleteAll();" /> <input type="checkbox" id="toggle" value="select" onClick="checkAllCheckBoxes();" /></td>
			</s:if>
			<td class="ar">Quantity</td>
			<td>Item</td>
			<td>Line</td>
			<td />
			<td>Type</td>
			<td>Location</td>
			<td>Last updated</td>
		</tr>
	</thead>
	<tbody>
		<s:set name="selectedList" value="selectionService.selectedList" />
		<s:iterator value="top" status="status">
				<tr class="lotstatus-<s:property value="status" /><s:if test="#selectedList.contains(id)"> accession-selected</s:if>">
					<td class="ar">
					<s:if test="startAt!=null">
						<s:property value="#status.index + startAt + 1" />
					</s:if><s:else>
						<s:property value="#status.index + 1" />
					</s:else></td>
					<td class="ar"><input type="checkbox" id="checkAll_<s:property value="#status.index + 1" />" name="lotIds[]" value="${id}" /></td>
					<td class="ar"><s:if test="status==-3">
						Missing
					</s:if><s:elseif test="status==0">
						<s:property value="quantity" />
						<s:property value="scale" />
						<b>?</b>
					</s:elseif> <s:elseif test="status==-100">
						Deleted
					</s:elseif> <s:else>
						<s:property value="quantity" />
						<s:property value="scale" />
					</s:else></td>
					<td class="identifying">
					 <b><s:if test="id!=null"><a href="<c:url value='/lot/${id}' />">
						<s:property value="item.name" /></a></s:if><s:else><s:property value="item.name" /></s:else>
					</b></td>
					<td><s:if test="line!=null">
							(<s:property value="line" />)
						</s:if>
						<s:if test="%{top instanceof org.iita.inventory.model.InVitroLot}">
						<s:if test="regenerationDate!=null">
							<s:date format="dd/MM/yyyy" name="regenerationDate" />
						</s:if>
						<s:if test="virusFree!=null && virusFree==true">
							VF
						</s:if>
						<s:if test="originExplant!=null">
							<s:property value="originExplant" />
						</s:if>
						<s:if test="invitroStatus!=null">
							<s:if test="originExplant!=null">
								| <s:property value="invitroStatus" />
							</s:if>
							<s:else>
								<s:property value="invitroStatus" />
							</s:else>
						</s:if>
					</s:if> <s:if test="%{top instanceof org.iita.inventory.model.SeedLot}">
						<s:if test="yearProcessed!=null">
							<s:property value="yearProcessed" />
						</s:if>
				</s:if></td>
				<td class="ar">
				<div class="select-tool"><a x:id="<s:property value="id" />" class="btn_select" href="#" title="Add to current selection list">Select</a></div>
				<div class="unselect-tool"><a x:id="<s:property value="id" />" class="btn_unselect" href="#" title="Remove from current selection list">Remove</a></div>
				</td>
				<td><s:property value="item.itemType.name" /></td>
				<td><a href="<c:url value='/browse/location/${location.id}' />"> <s:property value="location.name" /> </a></td>
				<td><s:date name="lastUpdated" format="%{getText('date.format')}" /></td>
			</tr>			
		</s:iterator>
	</tbody>
</table>
<script>
	function checkAllCheckBoxes(){
		var checkboxes = document.getElementsByName('lotIds[]');
        var button = document.getElementById('toggle');

        if(button.value == 'select'){
            for (var i in checkboxes){
                checkboxes[i].checked = 'FALSE';
            }
            button.value = 'deselect';
        }else{
            for (var i in checkboxes){
                checkboxes[i].checked = '';
            }
            button.value = 'select';
        }
	}
	
	function deleteAll(){
		if(confirm('Delete all selected lot(s) and their corresponding item(s)?')){
			var form = document.createElement("form");
			var checkboxes = document.getElementsByName('lotIds[]');
			var locid = document.getElementById('locid');
			
			var strCtrl = "";
			
			for(var i in checkboxes){
				//alert("i" + i);
				//alert("checkboxes[i]: " + checkboxes[i]);
				//alert("checkboxes[i].value: " + checkboxes[i].value);
				if(strCtrl==""){
					if(checkboxes[i].value!="" && checkboxes[i].value != undefined)
						strCtrl = checkboxes[i].value;
				}else{
					if(checkboxes[i].value!="" && checkboxes[i].value != undefined)
						strCtrl = strCtrl + "," + checkboxes[i].value;
				}
			}
			
	        form.setAttribute("method", "post");
	
	        form.setAttribute("action", "/inventory/browse/removeselectedlots.jspx");
	
	        form.setAttribute("target", "_parent");
	
	        var hiddenField = document.createElement("input");
	
	        hiddenField.setAttribute("name", "lotIds");
	
	        hiddenField.setAttribute("value", strCtrl );
	
	        hiddenField.setAttribute("type", "hidden");
	
	        form.appendChild(hiddenField);
	        
	        var hiddenField2 = document.createElement("input");
	    	
	        hiddenField2.setAttribute("name", "locid");
	
	        hiddenField2.setAttribute("value", locid.value );
	
	        hiddenField2.setAttribute("type", "hidden");
	
	        form.appendChild(hiddenField2);
	
	        document.body.appendChild(form);
			
	        //alert("lotIds: " + strCtrl);
	        //alert("locid: " + locid.value);
	        form.submit();
		}
	}
</script>