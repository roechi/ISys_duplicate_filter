package isys.duplicatefilter.controllers;

import isys.duplicatefilter.services.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class helloController {

    @Autowired
    private UpdateService service;

    @RequestMapping(path = "/hello")
    public String sayHello() {
        return "hello";
    }

    @GetMapping(path = "/update")
    public String update() {
        service.updateArticles();
        return "Done updating.";
    }
}
