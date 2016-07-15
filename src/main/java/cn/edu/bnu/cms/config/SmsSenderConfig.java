package cn.edu.bnu.cms.config;

import cn.edu.bnu.cms.beans.SmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by dave on 16/7/8.
 */
@Configuration
public class SmsSenderConfig {
    @Autowired
    private Environment env;

    @Bean
    public SmsSender getSmsSender() {
        return new SmsSender(env.getProperty("app.sms.appKey"),
                env.getProperty("app.sms.appSecret"),
                env.getProperty("app.sms.templateId"),
                env.getProperty("app.sms.signature"));
    }
}
