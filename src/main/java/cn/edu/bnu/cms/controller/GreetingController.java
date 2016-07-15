package cn.edu.bnu.cms.controller;

import cn.edu.bnu.cms.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

/**
 * Created by dave on 16/7/1.
 */
@RestController
public class GreetingController {
    @Autowired GreetingService greetingService;

    @RequestMapping(value = "/greeting/{number}", method = RequestMethod.GET)
    public String getGreeting(@PathVariable int number) {
        return greetingService.getGreeting(number);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoSuchElementException(NoSuchElementException e) {
        return e.getMessage();
    }
}
