<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>Search results '<s:property value="q" />'</title>
</head>
<body>
<p>Found <b><s:property value="totalHits" /></b> matching accession
records.</p>

<s:include value="searchResultsPaging.jsp">
	<s:param name="page" value="currentPage" />
	<s:param name="pages" value="allPages" />
	<s:param name="pageSize" value="pageSize" />
	<s:param name="queryString" value="q" />
</s:include>


<div style="float: left; width: 100%;"><s:iterator value="results"
	status="status">
	<div class="accessionbox">
	<div style="overflow: hidden; height: 8em;"><b><a
		href="<s:url includeParams="none" action="browse" />/TVu-323"><s:property />
	TVu-323</a></b><br />
	Vigna unguiculata <br />
	<b>United States of America</b> <br />
	TVu-323 TVu-323 C 5728-17<br />
	DR R.P. MURPHY, FDAR,NIG.</div>
	<div style="margin: 10px 0px 5px;"><a
		href="<s:url includeParams="none" action="browse" />/TVu-323">Detailed view</a> <a
		href="<s:url includeParams="none" action="selection" method="add" />?id=5779">Select accession</a></div>
	</div>
</s:iterator></div>

<s:include value="searchResultsPaging.jsp">
	<s:param name="page" value="currentPage" />
	<s:param name="pages" value="allPages" />
	<s:param name="pageSize" value="pageSize" />
	<s:param name="queryString" value="q" />
</s:include>


</body>
</html>
