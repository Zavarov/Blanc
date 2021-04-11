${signature("commands", "resolver", "command", "factory", "parameters", "requiresGuild")}
<#assign arguments = "$arguments">
<#assign flags = "$flags">
        ${commands.getName()}.put("${command}", (${arguments}, ${flags}) -> {
            //Check fails with optional arguments
            //Preconditions.checkArgument(<#rt>
            //    ${arguments}.size() >= ${parameters?size},<#t>
            //    "This command requires at least ${parameters?size} argument<#if parameters?size != 1>s</#if>."<#t>
            //);<#lt>
            return ${factory}.create(
            <#list parameters as parameter, parameterName>
                ${
                    tc.includeArgs(
                        "command.builder.resolve.Resolve",
                        parameter,
                        [resolver.getName(), parameterName, arguments, parameter?index]
                    )
                }<#t>
                ,<#lt>
            </#list>
                //The context is provided in the other builder methods.
                null, //Author
                null, //MessageChannel
            <#if requiresGuild>
                null, //Guild
            </#if>
                null, //Shard
                null, //Message
                ${flags}
            );
        });