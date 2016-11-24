
<#if userList??>
	<#list userList as item>
	  ${item.id ! 'id'}-------${item.name ! 'name'}-------${item.age ! 'age'}</br>
	</#list>
</#if>