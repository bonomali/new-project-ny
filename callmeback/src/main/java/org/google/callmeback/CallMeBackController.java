package org.google.callmeback;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
 
@Controller
public class CallMeBackController {
 
  /**
   * Redirects URLs that are handled by React routing
   *
   * The client-side routing that React is doing is purely client-side. When you
   * navigate from one route to another (such as /home to /reservations) within
   * React, the server isn't involved at all. React interprets those routes
   * internally.
   *
   * However, when you either refresh a route (such as /reservations) or
   * navigate directly to that route from a link outside of React, the server
   * (not React) sees a url like /reservations and doesn't know what to do with
   * it. It returns 404 Not Found. This mapping tells the server, "Hey, when you
   * see a path like /reservations, just redirect to "/". This means that
   * index.html gets served, React gets a chance to boot, and then it takes over
   * handling of the /reservations route.
   *
   * IMPORTANT: This mapping must be kept in sync with the routes in Routes.jsx.
   * Whenever you add or modify a route there, you must also make the change
   * here.
   *
   * @return the view name for index.html
   */
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
