${signature("cdMethod")}
<#assign name = cdMethod.getCDParameter(0)>
<#assign arguments = cdMethod.getCDParameter(1)>
<#assign flags = cdMethod.getCDParameter(2)>
        return Optional.ofNullable(
            commands.computeIfAbsent(
                ${name.getName()},
                $name -> {
                    throw CommandException.of(Errors.UNKNOWN_COMMAND, $name);
                }
            )
        ).map(_function -> _function.apply(${arguments.getName()}, ${flags.getName()}));