package cn.edu.bnu.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Created by dave on 16/5/16.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("app")
            .genericModelSubstitutes(ResponseEntity.class, DeferredResult.class)
            .useDefaultResponseMessages(false)
            .forCodeGeneration(true)
            .select()
            .paths(PathSelectors.ant("/api/**"))
            .build()
            .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("dave", "https://github.com/dchuangincar", "dchuang@qq.com");
        ApiInfo apiInfo = new ApiInfo("App API接口",
            "BNU-cms",
            "0.1",
            "https://github.com/dchuangincar",
            contact,
            "GPL v3",
            "https://www.gnu.org/copyleft/gpl.html"
        );
        return apiInfo;
    }

}
