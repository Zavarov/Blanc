${signature("cdConstructor", "commands")}
        super(<#rt>
<#list cdConstructor.getCDParameterList() as cdParameter>
            ${cdParameter.getName()}<#t>
            <#if cdParameter?has_next>,</#if><#t>
</#list>
        );<#lt>

        <#list commands as command>
            ${tc.include("command.builder.Command", command)}
        </#list>