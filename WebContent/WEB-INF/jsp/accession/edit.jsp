<%@ include file="/common/taglibs.jsp"%>

<s:if test="accession==null || accession.id == null">
	<s:set name="title" value="%{'Add new accession'}" />
</s:if>
<s:else>
	<s:set name="title" value="%{'Update accession ' + accession.prefix + '-' + accession.accessionIdentifier}" />
</s:else>

<html>
<head>
<title><s:property value="#title" /></title>
</head>
<body>
<s:action name="crop!list" id="allCrops" />

<s:form namespace="/accession" action="crud!save" method="post">
	<s:textfield name="accession.prefix" value="%{accession.prefix}" label="Prefix" size="5" />
	<s:textfield name="accession.accessionIdentifier" value="%{accession.accessionIdentifier}" label="Accession identifier" size="10" />
	<s:textfield name="accession.name" value="%{accession.name}" label="Cultivar name" size="20" />
	
	<s:select name="accession.crop.id" value="%{accession.crop.id}" list="%{#allCrops.crops}" listKey="id" listValue="name" />
	<s:hidden name="accession.id" value="%{accession.id}" />
	<s:hidden name="accession.version" value="%{accession.version}" />
	<s:submit value="Submit" />
	<s:submit value="Cancel" name="redirect-action:accessions" />
</s:form>
</body>
</html>