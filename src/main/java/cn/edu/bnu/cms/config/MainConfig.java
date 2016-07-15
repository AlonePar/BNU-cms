package cn.edu.bnu.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Created by dave on 16/7/8.
 */
@Configuration
public class MainConfig {
    @Autowired
    private Environment env;

    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(env.getProperty("app.mail.host"));
        sender.setPort(Integer.valueOf(env.getProperty("app.mail.port")));
        sender.setUsername(env.getProperty("app.mail.username"));
        sender.setPassword(env.getProperty("app.mail.password"));
        return sender;
    }
}
