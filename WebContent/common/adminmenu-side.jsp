<%@ include file="/common/taglibs.jsp"%>

<h2>Application</h2>
<ul>
	<li><a href="<s:url namespace="/admin" action="users" />">Users</a></li>
	<li><a href="<s:url namespace="/admin" action="user/roles" />">Roles</a></li>
	<li><a href="<s:url namespace="/admin" action="user/user!input" />">Add user</a></li>
	<li><a href="<s:url namespace="/admin" action="browse" />">Browse files</a></li>
	<li><a href="<s:url namespace="/admin" action="template/index" />">Templates</a></li>
</ul>

<h2>Tools</h2>
<ul>
	<li><a href="<s:url namespace="/admin" action="log/view" />">Log browser</a></li>
	<li><a href="<s:url namespace="/admin" action="log" />">Log configuration</a></li>
	<li><a href="<s:url namespace="/admin" action="schedule/index" />">Scheduled jobs</a></li>
	<li><a href="<s:url namespace="/admin" action="lucene/reindex" />">Lucene reindex</a></li>
	<li><a href="<s:url namespace="/admin" action="schema" />">Schema</a></li>
</ul>

<h2>Administrative</h2>
<ul>
	<li><a href="<s:url namespace="/admin" action="applock" />">Block access to application</a></li>
	<li><a href="<s:url namespace="/admin" action="java-status" />">JRE status</a></li>
</ul>

<h2>Inventory Tools</h2>
<ul>
<li><a href="<s:url namespace="/admin" action="print/index" />">Printers</a></li>
<li><a href="<s:url namespace="/admin" action="scale/index" />">Balance Scale</a></li>
<li><a href="<s:url namespace="/admin" action="import" />">Import</a></li>
<li><a href="<s:url namespace="/admin" action="xls-update" />">Update lot data using XLS file</a></li>
<li><a href="<s:url namespace="/admin" action="xls-import" />">Update or import data using XLS file</a></li>
<li><a href="<s:url namespace="/admin" action="xlslot-import" />">Import Missing Items</a></li>
</ul>