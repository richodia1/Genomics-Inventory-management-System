<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title><s:property value="trial.name" /></title>
</head>
<body iita:help="inventory/trials">
<table class="inputform" id="lotprofile_editor">
	<colgroup>
		<col width="200" />
		<col />
		<col width="200" />
		<col />
	</colgroup>
	<tr>
		<td>Trial:</td>
		<td colspan="3"><big><s:property value="trial.name" /></big></td>
	</tr>
	<tr>
		<td>Date of trial:</td>
		<td colspan="3"><iita:date name="trial.date" format="dd/MM/yyyy HH:mm" /></td>
	</tr>
	<tr>
		<td>Trait group:</td>
		<td colspan="3"><a href="<s:url action="trial/traitgroup" />?id=<s:property value="trial.traitGroup.id" />"><s:property value="trial.traitGroup.title" /></a></td>
	</tr>
	<tr>
		<td>Description:</td>
		<td colspan="3"><s:property value="trial.description" /></td>
	</tr>
	<tr>
		<td class="label">Created:</td>
		<td><iita:date name="trial.createdDate" format="dd/MM/yyyy" /> by <s:property value="trial.createdBy" /></td>
		<td class="label">Updated:</td>
		<td><iita:date name="trial.lastUpdated" format="dd/MM/yyyy" /> by <s:property value="trial.lastUpdatedBy" /></td>
	</tr>
	<tr>
		<td />
		<td><s:if test="trial.status==@org.iita.trial.model.Trial$TrialStatus@OPEN">
			<s:form method="post" action="trial/edit!close">
				<s:hidden name="id" value="%{trial.id}" />
				<s:submit value="Close trial" />
				<s:submit value="Remove trial" cssClass="button-delete" onclick="javascript: return confirm('Are you sure you want to delete?');" action="trial/edit!remove" />
			</s:form>
		</s:if><s:elseif test="trial.status==@org.iita.trial.model.Trial$TrialStatus@CLOSED">
			<s:form method="post" action="trial/edit!open">
				<s:hidden name="id" value="%{trial.id}" />
				<s:submit value="Open trial" />
			</s:form>
		</s:elseif>
		<s:form method="post" action="trial/profile">
			<s:hidden name="id" value="%{trial.id}" />
			<s:hidden name="startAt" value="%{startAt}" />
			<s:submit action="trial/profile!download" value="Download XLS" />
		</s:form>
		<s:form method="post" action="trial/edit">
			<s:hidden name="id" value="%{trial.id}" />
			<s:hidden name="startAt" value="%{paged.startAt}" />
			<s:submit action="trial/edit" value="Edit results" />
			</s:form>
		</td>
	</tr>
</table>

<h2>Observations (wide data format)</h2>
<s:if test="trial.status==@org.iita.trial.model.Trial$TrialStatus@OPEN">
	<div class="actionMessage"><s:form method="post" action="trial/profile!addEntity">
		<s:hidden name="id" value="%{trial.id}" />
	Scan lots to add to trial: <s:textfield name="barcode" cssClass="autofocus" />
		<s:submit value="Add to trial" />
	</s:form>
	<iita:collapse id="uploadTrial" closedHeading="Update trial data using XLS file">
		<iita:fileupload action="trial/edit!upload" queryParams="id=${trial.id}" value="Update trial data" />
	</iita:collapse>
	<iita:collapse id="addstuff" closedHeading="Bulk add lots to selection" heading="Close importer">
	<s:form method="post" action="trial/profile!addBulk">
		<s:hidden name="id" value="%{trial.id}" />
		<table class="inputform">
			<colgroup>
				<col width="200" />
				<col />
			</colgroup>
			<tr>
				<td>Paste Lot IDs:<br />or Scan barcodes:</td>
				<td><s:textarea id="lotIds" name="lotIds" /></td>
			</tr>
			<tr>
				<td>Num. Tested:</td>
				<td><s:textfield id="numTested" name="numTested" /></td>
			</tr>
			<tr>
				<td />
				<td><s:submit value="This is Lot IDs" /> <s:submit action="trial/profile!addBar" value="This is barcodes" /> <s:submit action="trial/showform!addTrial" onclick="return validateEntries();" value="Generate trail entry form" /></td>
			</tr>
		</table>
	</s:form>
</iita:collapse>
	<iita:collapse id="setDefault" closedHeading="Set default values">
		<s:form method="post" action="trial/edit!setdefaults">
			<s:hidden name="id" value="%{trial.id}" />
			<s:include value="defaults.jsp" />
			<s:submit value="Set all values" />
		</s:form>
	</iita:collapse>
	
	</div>
</s:if>

<s:if test="showForm!=true">
	<s:if test="paged!=null">
		<s:push value="paged">
			<s:include value="/WEB-INF/jsp/paging.jsp">
				<s:param name="href" value="%{'id=' + trial.id}" />
		</s:include>
		</s:push>
	</s:if>
	
	<s:form method="post" action="trial/profile!removeEntities">
		<s:hidden name="id" value="%{trial.id}" />
		<table class="data-listing">
			<s:iterator value="paged.results" status="rowIndex">
				<s:if test="#rowIndex.index==0">
					<thead>
						<tr>
							<s:iterator status="columnIndex">
								<s:if test="#columnIndex.index==0">
									<td></td>
									<s:if test="trial.status==@org.iita.trial.model.Trial$TrialStatus@OPEN">
									<td><input type="checkbox" onclick="javascript: var chk=this.checked; $$('input[name=entityIds]').each(function(x) { x.checked=chk; });" /></td>
									</s:if>
								</s:if><s:else>
									<td><big><s:property value="title" /> [<s:property value="var" />]</big>
									<s:if test="description!=null">
										<br />
										<s:property value="description" />
									</s:if></td>
								</s:else>
							</s:iterator>
						</tr>
					</thead>
				</s:if>
				<s:else>
					<tr id="ent<s:property value="top[0].id" />">
						<s:iterator status="columnIndex">
							<s:if test="#columnIndex.index==0">
								<td class="identifying"><a href="<s:url action="index" namespace="/lot" />?id=<s:property value="id" />"><s:property value="item.name" /></a></td>
								<s:if test="trial.status==@org.iita.trial.model.Trial$TrialStatus@OPEN">
									<td><input type="checkbox" name="entityIds" value="<s:property value="id" />" /></td>
								</s:if>
							</s:if>
							<s:else>
								<td><s:if test="top==null">
									<em>Not measured</em>
								</s:if><s:else>
									<s:property value="paged.results[0][#columnIndex.index].decode(top)" />
									<s:property value="paged.results[0][#columnIndex.index].uom" />
								</s:else></td>
							</s:else>
						</s:iterator>
					</tr>
				</s:else>
			</s:iterator>
		</table>
		<s:if test="trial.status==@org.iita.trial.model.Trial$TrialStatus@OPEN">
			<s:submit value="Remove selected rows" />
		</s:if>
	</s:form>
	<script type="text/javascript">
	if (window.location.hash!=null) {
		var mark=$(window.location.hash.substring(1));
		if (mark!=null)
			Element.addClassName(mark, "row-mark");
	}
	</script>
</s:if>
<s:else>
	<s:form id="formEntries" method="post" action="trial/profile!addTrailEntries">
	<s:hidden name="id" value="%{trial.id}" />
	<table class="data-listing">
		<thead>
			<tr> 
				<td>ItemName</td>
				<td>LotID</td>
				<td>Barcode</td>
				<td>No. Tested</td>
				<td>Germ 6d</td>
				<td>Viability 10d</td>
				<td>% 6d</td>
				<td>% 10d</td>
			</tr>
		</thead>
		<tbody>
			<s:iterator status="rowIndex" value="lotList.results">
				<s:if test="#rowIndex.index!=0">
					<tr>
						<s:iterator status="columnIndex">
							<s:if test="#columnIndex.index==0">
								<td class="identifying"><a href="<s:url action="index" namespace="/lot" />?id=<s:property value="id" />"><s:property value="item.name" /></a><s:hidden name="item_id" value="%{item.id}" /></td>
								<td><s:property value="id" /><s:hidden name="lot_id" value="%{id}" /></td>
								<td><s:property value="barCode.id" /><s:hidden name="barCode_id" value="%{barCode.id}" /></td>
							</s:if>	
						</s:iterator>						
						<td><s:textfield readonly="true" id="num_tested_%{#rowIndex.index-1}" name="num_tested" value="%{numt}" /></td>
						<td><s:textfield id="germ_6d_%{#rowIndex.index-1}" name="germ_6d" value="%{germ_6d[#rowIndex.index-1]}" /></td>
						<td><s:textfield id="germ_10d_%{#rowIndex.index-1}" name="germ_10d" value="%{germ_10d[#rowIndex.index-1]}" /></td>
						<td><s:textfield readonly="true" id="perc_6d_%{#rowIndex.index-1}" name="perc_6d" value="%{perc_6d[#rowIndex.index-1]}" /></td>
						<td><s:textfield readonly="true" id="perc_10d_%{#rowIndex.index-1}" name="perc_10d" value="%{perc_10d[#rowIndex.index-1]}" /></td>
					</tr>
				</s:if>
			</s:iterator>
		</tbody>
	</table>
	<s:submit value="Submit Viability Test" onclick="if(confirm('Submit viability?')){return true;}else{return false;}" />
	</s:form>
</s:else>
<script>
Event.observe(window, 'load', function() {
	var numtested = Array.prototype.slice.call(document.querySelectorAll('input[id^="num_tested_"]'));
	var germ6d = Array.prototype.slice.call(document.querySelectorAll('input[id^="germ_6d_"]'));
	var germ10d = Array.prototype.slice.call(document.querySelectorAll('input[id^="germ_10d_"]'));
	
	for(var indx=0;indx<germ6d.length;++indx){
		Event.observe(germ6d[indx], "blur", function(ev) {
			compute6dViability(ev);
		});
		Event.observe(germ6d[indx], "keypress", function(ev) {
			cancelEnter(ev);
		});
	}
	
	for(var indx=0;indx<germ10d.length;++indx){
		Event.observe(germ10d[indx], "blur", function(ev) {
			compute10dViability(ev);
		});
		Event.observe(germ10d[indx], "keypress", function(ev) {
			cancelEnter(ev);
		});
	}
});

function validateEntries(){
	var lotIds = $("lotIds").value;
	var numT = $("numTested").value;
	
	if(lotIds==""){
		alert("Barcodes must be entered!");
		$("lotIds").focus();
		return false;
	}
	
	if(numT==""){
		alert("Number tested must be entered!");
		$("numTested").focus();
		return false;
	}
	
	return true;
}

function compute6dViability(event){
	var germ6d = Array.prototype.slice.call(document.querySelectorAll('input[id^="germ_6d_"]'));
	
	for(var indx=0;indx<germ6d.length;++indx){		
		if($("germ_6d_"+indx).value!=="" && $("num_tested_"+indx).value!==""){
			
			if(($("germ_6d_"+indx).value * 1) > ($("num_tested_"+indx).value * 1)){
				alert("6d germination value greater than number tested!");
				$("germ_6d_"+indx).value="";
			}
			
			if((($("germ_6d_"+indx).value * 1) + ($("germ_10d_"+indx).value * 1)) > ($("num_tested_"+indx).value * 1)){
				alert("(6d + 10d) germination value greater than number tested!");
				$("germ_6d_"+indx).value="";
			}
			
			if(!isNaN($("germ_6d_"+indx).value) && !isNaN($("num_tested_"+indx).value)){
				$("perc_6d_"+indx).value = Math.round(($("germ_6d_"+indx).value/$("num_tested_"+indx).value)*100 * 100)/100;
			}
		}
	}
}

function compute10dViability(event){
	var germ10d = Array.prototype.slice.call(document.querySelectorAll('input[id^="germ_10d_"]'));
	for(var indx=0;indx<germ10d.length;++indx){
		if($("germ_10d_"+indx).value!=="" && $("num_tested_"+indx).value!==""){
			
			if(($("germ_10d_"+indx).value * 1) > ($("num_tested_"+indx).value * 1)){
				alert("10d germination value greater than number tested!");
				$("germ_10d_"+indx).value="";
			}
			
			if((($("germ_10d_"+indx).value * 1) + ($("germ_6d_"+indx).value * 1)) > ($("num_tested_"+indx).value * 1)){
				alert("(10d + 6d) germination value greater than number tested!");
				$("germ_10d_"+indx).value="";
			}
			
			if(!isNaN($("germ_10d_"+indx).value) && !isNaN($("num_tested_"+indx).value)){
				$("perc_10d_"+indx).value = Math.round(($("germ_10d_"+indx).value/$("num_tested_"+indx).value)*100 * 100)/100;
			}
		}
	}
}


//Will cancel the enter-key event
function cancelEnter(event)
{
	if ( event.keyCode == 10 || event.keyCode == 13 || event.which == '10' || event.which == '13' )
	{
		moveNext(event);
		event.preventDefault();
	}
}

function moveNext(event){
	var res = event.target.id.split("_");
	if(res[1]!=null){
		if(res[2]!=null){
			if(res[1]=="6d"){
				compute6dViability(event);
				document.getElementById(res[0] + "_10d_" + res[2]).focus();
			}else if(res[1]=="10d"){
				compute10dViability(event);
				if(document.getElementById(res[0] + "_6d_" + ((res[2]*1)+1))!=null){
					document.getElementById(res[0] + "_6d_" + ((res[2]*1)+1)).focus();
				}
			}
		}
	}
}

function ValidatePassKey(tb){
	var germ6d = Array.prototype.slice.call(document.querySelectorAll('input[id^="germ_6d_"]'));
}
</script>
</body>
</html>