<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Request information</title>
</head>
<body iita:help="inventory/requests">
<s:form action="request/edit!update" namespace="/" method="POST">
	<s:hidden name="id" value="%{order.id}" />
	<s:hidden name="order.version" value="%{order.version}" />
	<table class="inputform" iita:helptoc="inventory/requests#germplasm-request">
		<colgroup>
			<col width="200" />
			<col />
		</colgroup>
		<tr>
			<td><label>State:</label></td>
			<td><s:select name="order.state" value="%{order.state}" list="@org.iita.inventory.model.Order$OrderState@values()" /></td>
		</tr>
		<tr>
			<td><label>Title:</label></td>
			<td><s:textfield name="order.title" value="%{order.title}" /></td>
		</tr>
		<tr>
			<td><label>Last, first name:</label></td>
			<td><s:textfield name="order.lastName" value="%{order.lastName}" cssStyle="width: 45%" /> <s:textfield name="order.firstName"
				value="%{order.firstName}" cssStyle="width: 45%" /></td>
		</tr>
		<tr>
			<td><label>E-Mail:</label></td>
			<td><s:textfield name="order.mail" value="%{order.mail}" /></td>
		</tr>
		<tr>
			<td><label>Organization:</label></td>
			<td><s:textfield name="order.organization" value="%{order.organization}" /></td>
		</tr>
		<tr>
			<td><label>Organization category:</label></td>
			<td><s:select name="order.organizationCategory" value="%{order.organizationCategory}"
				list="{'CGIAR Centre','Commercial company','Farmer','Genebank','National agric. research (NARS)','Non-governmental organization (NGO)','Regional organization','University','Individual other than farmers'}" /></td>
		</tr>
		<tr>
			<td><label>Shipping address:</label></td>
			<td><s:textarea name="order.shippingAddress" value="%{order.shippingAddress}" /></td>
		</tr>
		<tr>
			<td><label>Country:</label></td>
			<td><s:textfield name="order.country" value="%{order.country}" /></td>
		</tr>
		<tr>
			<td><label>Order date:</label></td>
			<td><iita:datepicker name="order.orderDate" value="%{order.orderDate}" format="dd/MM/yyyy" /></td>
		</tr>
		<tr>
			<td><label>Purpose:</label></td>
			<td><s:textfield name="order.purpose" value="%{order.purpose}" /></td>
		</tr>
		<tr>
			<td><label>Description:</label></td>
			<td><s:textarea name="order.description" value="%{order.description}" /></td>
		</tr>
		<tr>
			<td><label>Contact info (Full name):</label></td>
			<td><s:textfield name="order.contactPerson" value="%{order.contactPerson}" /></td>
		</tr>
		
		<tr>
			<td><label>Contact info (phone):</label></td>
			<td><s:textfield name="order.contactInfo" value="%{order.contactInfo}" /></td>
		</tr>
		<tr>
			<td><label>Shipping cost:</label></td>
			<td><s:textfield name="order.shippingCost" value="%{order.shippingCost}" cssClass="numeric-input" /> USD</td>
		</tr>
		<tr>
			<td><label>SMTA option:</label></td>
			<td><s:select name="order.agreementOption" value="%{order.agreementOption}" list="{'Signature','Shrink-wrap','Click-wrap'}" /></td>
		</tr>
		<tr>
			<td />
			<td><s:checkbox name="order.smta611" value="%{order.smta611}" label="SMTA Option 6.11" /> Opt-in for payment under Option 6.11</td>
		</tr>
		<tr>
			<td><label>Provider name:</label></td>
			<td><s:textfield name="order.providerName" value="%{order.providerName==null || ''==order.providerName?'Dr. Michael Abberton':order.providerName}" /></td>
		</tr>
		<tr>
			<td><label>Provider organization:</label></td>
			<td><s:textfield name="order.providerOrganization"
				value="%{order.providerOrganization==null || ''==order.providerOrganization?'International Institute of Tropical Agriculture, IITA, Ibadan':order.providerOrganization}" /></td>
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
			<div class="button-bar"><s:submit value="Update" /> <input type="button"
				onClick="javascript: window.location.href='<s:url action="request/order" />?id=<s:property value="order.id" />';" value="Back" /></div>
			</td>
		</tr>
	</table>
</s:form>

<s:if test="order.id!=null && !getUser().hasRole('ROLE_BREEDER')">
	<!-- Tags -->
	<s:form method="post" action="request/tags!update">
		<s:hidden name="id" value="%{order.id}" />
		<table class="inputform">
			<colgroup>
				<col width="200" />
				<col />
				<col />
			</colgroup>
			<tr>
				<td />
				<td>
				<h3 class="clearfloat">SMTA</h3>
				<ul class="taglist" id="taglist1">
					<s:iterator
						value="getAllTagsForCategory({'SMTA: Pending response', 'SMTA: Sent to requestor', 'SMTA: No reply', 'SMTA: Rejected', 'SMTA: Intention to sign', 'SMTA: Signed'})">
						<s:include value="/WEB-INF/jsp/tags/builder-radio-tag.jsp" />
					</s:iterator>
				</ul>
				</td>
				<td>
				<h3 class="clearfloat">Import permit</h3>
				<ul class="taglist" id="taglist2">
					<s:iterator
						value="getAllTagsForCategory({'Import permit: Requested', 'Import permit: Not provided', 'Import permit: Provided', 'Import permit: Not required'})">
						<s:include value="/WEB-INF/jsp/tags/builder-radio-tag.jsp" />
					</s:iterator>
				</ul>
				</td>
			</tr>
			<tr>
				<td />
				<td>
				<h3 class="clearfloat">Plant Quarantine Certificate</h3>
				<ul class="taglist" id="taglist2">
					<s:iterator value="getAllTagsForCategory({'PQC: Requested to IGH', 'PQC: Provided by IGH and matching IP', 'PQC: Provided by IGH not matching IP'})">
						<s:include value="/WEB-INF/jsp/tags/builder-radio-tag.jsp" />
					</s:iterator>
				</ul>
				</td>
				<td>
				<h3 class="clearfloat">Specific Request</h3>
				<ul class="taglist" id="taglist2">
					<s:iterator
						value="getAllTagsForCategory({'Specific: No specific requirement', 'Specific: Specific requests not completed', 'Specific: Specific requests completed'})">
						<s:include value="/WEB-INF/jsp/tags/builder-radio-tag.jsp" />
					</s:iterator>
				</ul>
				</td>
			</tr>
			<tr>
				<td />
				<td>
				<h3 class="clearfloat">Delivery mode</h3>
				<ul class="taglist" id="taglist2">
					<s:iterator value="getAllTagsForCategory({'Delivery: DHL', 'Delivery: Visit', 'Delviery: Drop-off'})">
						<s:include value="/WEB-INF/jsp/tags/builder-radio-tag.jsp" />
					</s:iterator>
				</ul>
				</td>
				<td>
				<h3 class="clearfloat">Shipping Cost</h3>
				<ul class="taglist" id="taglist2">
					<s:iterator
						value="getAllTagsForCategory({'Shipping: Reply not yet provided', 'Shipping: Covered by GRC', 'Shipping: Recipient covers cost', 'Shipping: Recipient did not agree to cover cost'})">
						<s:include value="/WEB-INF/jsp/tags/builder-radio-tag.jsp" />
					</s:iterator>
				</ul>
				</td>
			</tr>
			<tr>
				<td />
				<td>
				<h3 class="clearfloat">Production Cost</h3>
				<ul class="taglist" id="taglist2">
					<s:iterator
						value="getAllTagsForCategory({'Production: Reply not yet provided', 'Production: Covered by GRC', 'Production: Recipient covers cost', 'Production: Recipient did not agree to cover cost'})">
						<s:include value="/WEB-INF/jsp/tags/builder-radio-tag.jsp" />
					</s:iterator>
				</ul>
				</td>
				<td>
				<h3 class="clearfloat">Stock</h3>
				<ul class="taglist" id="taglist2">
					<s:iterator value="getAllTagsForCategory({'Stock: Available', 'Stock: For regeneration', 'Stock: In regeneration', 'Stock: Not available'})">
						<s:include value="/WEB-INF/jsp/tags/builder-radio-tag.jsp" />
					</s:iterator>
				</ul>
				</td>
			</tr>
			<tr>
				<td />
				<td>
				<h3 class="clearfloat">Source of Request</h3>
				<ul class="taglist" id="taglist2">
					<s:iterator value="getAllTagsForCategory({'Source: Phone', 'Source: Internet', 'Source: Visit', 'Source: Other'})">
						<s:include value="/WEB-INF/jsp/tags/builder-radio-tag.jsp" />
					</s:iterator>
				</ul>
				</td>
			</tr>
			<tr>
				<td />
				<td colspan="2">
				<div class="button-bar"><s:submit value="Save tags" /></div>
				</td>
			</tr>
		</table>
	</s:form>

	<table class="inputform">
		<colgroup>
			<col width="200" />
			<col />
		</colgroup>
		<tr>
			<td />
			<td>
			<h2>Add new tag</h2>
			<div class="actionMessage">To create categorized tags, please use format: <i>Category</i><b>:</b> <i>Tag</i></div>
			<s:bean name="org.iita.inventory.model.OrderTag">
				<s:param name="entity" value="[1].order" />
				<s:include value="/WEB-INF/jsp/tags/tag-form.jsp" />
			</s:bean></td>
		</tr>
	</table>

	<style type="text/css">
li.selected-tag {
	font-weight: bold;
	background-color: #FFF3CF;
}
</style>
</s:if>
</body>
</html>