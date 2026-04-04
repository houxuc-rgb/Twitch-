package com.laioffer.twitch.hello;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {


    @GetMapping("/hello")
    public Person sayHello(@RequestParam(defaultValue = "Guest") String name) {
        return new Person(name, "LaiOffer",
                new Address("123 Main St", null, null, null),
                new Book("1984", "George Orwell"));
    }
}