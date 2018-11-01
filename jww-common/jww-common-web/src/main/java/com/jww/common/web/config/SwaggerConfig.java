package com.jww.common.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger接口文档配置
 *
 * @author wanyong
 * @date 2018-11-1 14:57
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 是否开启swagger，正式环境一般是需要关闭的，可根据springboot的多环境配置进行设置
     */
    @Value(value = "${swagger.enabled:true}")
    Boolean swaggerEnabled;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                // 是否开启
                .enable(swaggerEnabled).select()
                // 扫描的路径包
                .apis(RequestHandlerSelectors.basePackage("com.jww.**.controller"))
                // 指定路径处理PathSelectors.any()代表所有的路径
                .paths(PathSelectors.any()).build().pathMapping("/");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("JWW接口文档")
                .description("JWW | JWW")
                // 作者信息
                .contact(new Contact("JWW", "https://www.jww.com", "jww@jww.com"))
                .version("2.0.0")
                .build();
    }
}