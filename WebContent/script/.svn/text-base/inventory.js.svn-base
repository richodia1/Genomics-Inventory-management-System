IITA.Inventory = Class.create();
IITA.Inventory.AjaxService = new IITA.AjaxRPC("/inventory/ajax/service.jspx");
IITA.Inventory.addToSelection = function(id, element) {
	var el = $(element);
	IITA.Inventory.AjaxService.addToSelection(parseInt(id), function(x) {
		if (x.responseJSON.result == true)
			el.addClassName("accession-selected");
	});
};
IITA.Inventory.removeFromSelection = function(id, element) {
	var el = $(element);
	IITA.Inventory.AjaxService.removeFromSelection(parseInt(id), function(x) {
		if (x.responseJSON.result == true)
			el.removeClassName("accession-selected");
	});
};
// IITA.Inventory.addAll = function() {
// IITA.Inventory.AjaxService.addManyToSelection([<s:iterator value="top"
// status="status"><s:property value="id" /><s:if
// test="!#status.last">,</s:if></s:iterator>], function(x) {
// if (x.responseJSON.result==true) {
// var rows=$("accession-tabular").children;
// for (var i=rows.length-1; i>=0; i--)
// rows[i].addClassName("accession-selected");
// }
// });
// }
Event.observe(window, 'load', function() {
	$A(document.getElementsByClassName("btn_select")).each(
			function(value, index) {
				var ic = value;
				Event.observe(ic, "click", function(ev) {
					//				alert("add: " + ic + "\n" + ic.parentNode.parentNode);
						IITA.Inventory.addToSelection(ic.getAttribute("x:id"),
								ic.parentNode.parentNode.parentNode);
						Event.stop(ev);
					});
			});

	$A(document.getElementsByClassName("btn_unselect")).each(
			function(value, index) {
				var ic = value;
				Event.observe(ic, "click", function(ev) {
					//				alert("remove: " + ic + "\n" + ic.parentNode.parentNode);
						IITA.Inventory.removeFromSelection(ic
								.getAttribute("x:id"),
								ic.parentNode.parentNode.parentNode);
						Event.stop(ev);
					});
			});
});