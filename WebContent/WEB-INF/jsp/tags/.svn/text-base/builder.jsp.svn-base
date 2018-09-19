<%@ include file="/common/taglibs.jsp"%>

<s:iterator value="categories" status="status">
<h3 class="clearfloat"><s:property /> tags</h3>
<ul class="taglist" id="taglist<s:property value="#status.index" />">
<s:iterator value="[1].getTagsForCategory(top)">
	<s:set name="myTag" value="[2].findTag(top)" />
	<s:if test="#myTag!=null">
	<li class="selected-tag"><input type="checkbox" name="usedTag" value="${top}" checked="checked" /><s:textfield name="tagValue['%{top}']" value="%{#myTag.percentage}" cssStyle="width: 50px;" />% <span style="margin-left: 20px;"><s:property /></span></li>
	</s:if>
	<s:else>
	<li><input type="checkbox" name="usedTag" value="${top}" /><s:textfield name="tagValue['%{top}']" cssStyle="width: 50px;" />% <span style="margin-left: 20px;"><s:property /></span></li>
	</s:else>
</s:iterator>
</ul>
<div class="clearfloat">
Sum of tag values: <b><span id="sumtaglist<s:property value="#status.index" />">0</span> %</b>
</div>
</s:iterator>

<h3 class="clearfloat">Non categorized tags</h3>
<ul class="taglist" id="taglistroot">
<s:iterator value="getTagsForCategory(null)">
	<s:set name="myTag" value="[1].findTag(top)" />
	<li class="selected-tag"><input type="checkbox" name="usedTag" value="${top}" checked="checked" /><s:textfield name="tagValue['%{top}']" value="%{#myTag.percentage}" cssStyle="width: 50px;" />% <span style="margin-left: 20px;"><s:property /></span></li>
</s:iterator>
</ul>

<div class="clearfloat"></div>

<script type="text/javascript">
Event.observe(window, "load", function() {
	$$('ul.taglist').each(function(e) {
		var summer=$('sum' + e.id);
		if (summer) {
			var sum=0;
			Element.select(e, 'li').each(function(x) {
				var d=x.descendants();
				if (d[0].checked && d[2].value)
					try { 
						var v=parseFloat(d[2].value); 
						sum += v; 
					} catch (e) {}
			});
			summer.innerHTML=sum;

			Event.observe(e, "change", function(ev) {
				// sum them up
				//alert(ev.element());
				var summer=$('sum' + e.id);
				if (!summer) return;
				var sum=0;
				Element.select(e, 'li').each(function(x) {
					var d=x.descendants();
					if (d[0].checked && d[2].value)
						try { 
							var v=parseFloat(d[2].value); 
							sum += v; 
						} catch (e) {}
				});
				summer.innerHTML=sum;
			});
		}
	});
	$$('input[name="usedTag"]').each(function(e) {
		if (e.checked)
			Element.addClassName(e.parentNode, "selected-tag");
		Event.observe($(e), "click", function(ev) {
			if (ev.element().checked)
				Element.addClassName(ev.element().parentNode, "selected-tag");
			else
				Element.removeClassName(ev.element().parentNode, "selected-tag");
		});
	});
});
</script>
<style type="text/css">
li.selected-tag {
	font-weight: bold;
	background-color: #FFF3CF; 
}
</style>