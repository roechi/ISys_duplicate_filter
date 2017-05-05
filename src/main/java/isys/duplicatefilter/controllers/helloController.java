package isys.duplicatefilter.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class helloController {

    @RequestMapping(path = "/hello")
    public String sayHello() {
        return "hello";
    }
}
