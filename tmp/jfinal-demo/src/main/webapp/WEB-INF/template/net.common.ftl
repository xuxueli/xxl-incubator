<#macro pager pageNumber totalPage baseUrl>
	<!--pre-->
	<#if pageNumber gt 1><a href="${baseUrl}?pageNumber=${pageNumber - 1}" >上页</a>
	<#else>上页</#if>
	
	<!--every pre-->
	<#if pageNumber-1 gte 5>
		<a href="${baseUrl}?pageNumber=1" >1</a>
		<a href="${baseUrl}?pageNumber=2" >2</a>
		<a>...</a>
		<a href="${baseUrl}?pageNumber=${pageNumber-2}" >${pageNumber-2}</a>
		<a href="${baseUrl}?pageNumber=${pageNumber-1}" >${pageNumber-1}</a>
	<#elseif 1 lte (pageNumber-1) >
		<#list 1..(pageNumber-1) as item>
			<a href="${baseUrl}?pageNumber=${item}" >${item}</a>
		</#list>
	</#if>
	
	<!--every now-->
	${pageNumber}
	
	<!--every next-->
	<#if totalPage-pageNumber gte 5>
		<a href="${baseUrl}?pageNumber=${pageNumber}" >${pageNumber+1}</a>
		<a href="${baseUrl}?pageNumber=${pageNumber}" >${pageNumber+2}</a>
		<a>...</a>
		<a href="${baseUrl}?pageNumber=${totalPage-1}" >${totalPage-1}</a>
		<a href="${baseUrl}?pageNumber=${totalPage}" >${totalPage}</a>
	<#elseif (pageNumber+1) lte totalPage >
		<#list (pageNumber+1)..totalPage as item>
			<a href="${baseUrl}?pageNumber=${item}" >${item}</a>
		</#list>
	</#if>
	
  	<!--next-->
  	<#if pageNumber lt totalPage><a href="${baseUrl}?pageNumber=${pageNumber+1}" >下页</a>
  	<#else>下页</#if>
</#macro>