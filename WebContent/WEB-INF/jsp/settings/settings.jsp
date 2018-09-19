<%@ include file="/common/taglibs.jsp"%>
<s:form method="post" action="settings!update">
<s:checkbox name="showHiddenLots" value="%{showHiddenLots}" onchange="javascript: this.form.submit(); return false;" /> <label for="settings_showHiddenLots">Display hidden lots</label>
</s:form>