package cn.edu.bnu.cms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * Created by dave on 16/7/1.
 */
@Service
public class GreetingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingService.class);

    private static final String[] GREETINGS = {"Yo!", "Hello", "Good day", "Hi", "Hey"};

    public String getGreeting(int number) {
        LOGGER.debug("Get greeting #{}", number);
        if (number < 1 || number > GREETINGS.length) {
            throw new NoSuchElementException(String.format("No greeting #%d", number));
        }

        return GREETINGS[number - 1];
    }
}
