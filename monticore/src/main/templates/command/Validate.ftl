${signature("permissions", "ranks", "requiresAttachment")}
<#list permissions as permission>
    ${tc.include("command.validate.CheckPermission", permission)}
</#list>
<#list ranks as rank>
    ${tc.include("command.validate.CheckRank", rank)}
</#list>
<#list permissions as permission>
    ${tc.include("command.validate.CheckPermission", permission)}
</#list>
<#if requiresAttachment>
    ${tc.include("command.validate.CheckAttachment")}
</#if>