${signature("resolver", "parameter", "source", "index")}
                ${resolver}.resolveMany(<#rt>
                    ${source}.subList(${index}, ${source}.size()),<#t>
                    ${resolver}::resolve${parameter}<#t>
                )<#lt>