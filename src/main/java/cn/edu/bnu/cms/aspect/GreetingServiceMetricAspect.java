package cn.edu.bnu.cms.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

/**
 * Created by dave on 16/7/1.
 */
@Aspect
@Component
public class GreetingServiceMetricAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingServiceMetricAspect.class);
    @Autowired CounterService counterService;

    @AfterReturning(pointcut = "execution(* cn.edu.bnu.cms.service.GreetingService.getGreeting(int)) && args(number)",
            argNames = "number")
    public void afterCallingGetGreeting(int number) {
        LOGGER.debug("Triggered after calling getGreeting()");
        counterService.increment("counter.calls.get_greeting");
        counterService.increment("counter.calls.get_greeting." + number);
    }

    @AfterThrowing(pointcut = "execution(* cn.edu.bnu.cms.service.GreetingService.getGreeting(int))", throwing = "e")
    public void afterGetGreetingThrowsException(NoSuchElementException e) {
        LOGGER.debug("Triggered after getGreeting() throws exception: " + e.getMessage());
        counterService.increment("counter.errors.get_greeting");
    }

}
