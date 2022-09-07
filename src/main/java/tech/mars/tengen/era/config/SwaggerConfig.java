package tech.mars.tengen.era.config;


import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;


/**
 * swagger config for open api.
 *
 * @author majunyang
 */
@Configuration
@ConditionalOnExpression("${swagger.enabled:true}")
public class SwaggerConfig  {


    @Bean
    public GroupedOpenApi userApi(){
        String[] paths = { "/**" };
        String[] packagedToMatch = { "tech.mars.tengen.era.controller" };
        return GroupedOpenApi.builder().group("User model")
                .pathsToMatch(paths)
                .addOperationCustomizer((operation, handlerMethod) -> operation.addParametersItem(new HeaderParameter()
                        .name("")
                        .example("")
                        .description("")
                        .schema(new StringSchema()._default("BR")
                                .name("groupCode").description(""))))
                .packagesToScan(packagedToMatch).build();
    }
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User model")
                        .version("1.0")
                        .description( "")
                        .termsOfService("")
                        .license(new License().name("Apache 2.0")
                                .url("")));
    }


}