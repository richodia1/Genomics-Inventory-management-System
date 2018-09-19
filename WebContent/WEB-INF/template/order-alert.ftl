
Dear Genebank,

A breeder request has been raised by <#if order.firstName??>
of ${order.firstName} </#if> <#if order.lastName??>
of ${order.lastName} </#if>:

<#if order.contactPerson??>
CONTACT DETAILS
============================
${order.contactPerson!''}
${order.contactInfo!''}

</#if>

REQUEST #${order.id} : ${order.title} DETAILS
============================

<#if order.shippingAddress??>Shipping address
----------------
${order.firstName!''} ${order.lastName!''}
${order.organization}
${order.shippingAddress!''}
${order.country!''}
${order.mail!''}

<#else>
Name: ${order.firstName!''} ${order.lastName!''}
Email: ${order.mail!''}
Organization: ${order.organization}
Country: ${order.country!''}
Mail: ${order.mail!''}
</#if>
<#if order.items??>Requested accessions
--------------------
<#list order.items as item>${item.item.name}
</#list></#if>


<#include "genebank-footer.ftl">