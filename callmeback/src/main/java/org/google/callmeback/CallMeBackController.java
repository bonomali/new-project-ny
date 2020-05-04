package org.google.callmeback;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
 
@Controller
public class CallMeBackController {
 
    @RequestMapping(value = {
      "/home",
      "/thankyou",
      "/cancel",
      "/cancelconfirmation",
      "/reservations/**"
    })
    public String index() {
        return "/";
    }
}
