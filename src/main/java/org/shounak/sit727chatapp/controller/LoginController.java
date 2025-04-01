package org.shounak.sit727chatapp.controller;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.shounak.sit727chatapp.model.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LoginController {
//    private final DataRepository repository;

    @GetMapping("/home")
    public String greeting(Model model, HttpServletRequest request, HttpServletResponse response) {
//		Data userData = (Data) request.getSession().getAttribute("user");
        Data userData = (Data) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        model.addAttribute("name", userData.getUsername());
//        model.addAttribute("email", userData.getEmail());
        return "index";
    }

    @RequestMapping({"/login", "/"})
    public String index(@RequestParam(required = false) String error, @RequestParam(required = false) String errorMessage, Model model, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isNotBlank(error) && StringUtils.isNotBlank(errorMessage)) {
            model.addAttribute("errorMessage", errorMessage);
        }
        return "login";
    }

    @RequestMapping("/custom-logout")
    public String logOut(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            invalidateCookie(request, response);
            return "login";
        }
        return "redirect:/login";
    }

    private void invalidateCookie(HttpServletRequest request, HttpServletResponse response) {
        // Get the JSESSIONID cookie from the request
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID")) {
                    // Set the max age to 0 to delete the cookie
                    cookie.setMaxAge(0);
                    // Optionally, set the path to match where the cookie was set
                    cookie.setPath("/");
                    // Add the cookie to the response to remove it from the client
                    response.addCookie(cookie);
                }
            }
        }
    }

//    @RequestMapping(value = "/auth",method = RequestMethod.POST)
//    public String authenticateTheUser(
//            LoginRequest loginRequest,
//            Model model,
//            HttpServletRequest request,
//            HttpServletResponse response) {
//
//        log.info("Authenticating user: " + loginRequest.getUsername());
//        Optional<Data> userDataByName = repository.findByName(loginRequest.getUsername());
//        if (userDataByName.isEmpty()) {
//            log.warn("User not found: " + loginRequest.getUsername());
//            //Logic for when the client doesn't exist
//            model.addAttribute("errorMessage", "Wrong username");
//            return "login";
//        }
//        Optional<String> userDataByNameAndPassword = repository.findDataPasswordByName(loginRequest.getUsername());
//        if (userDataByNameAndPassword.isEmpty() || !userDataByNameAndPassword.get().equals(loginRequest.getPassword())) {
//            log.warn("Password not Correct: " + loginRequest.getPassword());
//            //Logic for when the client doesn't exist
//            model.addAttribute("errorMessage", "Wrong password");
//            return "login";
//        }
//        Optional<Data> userData = repository.findByNameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
//        //We handle the logic to the Other Controllers
//        Cookie foo = new Cookie("shounak_app", "YOU_USED_SHOUNAK_APP");
//        foo.setHttpOnly(true);
//        response.addCookie(foo);
//
//        HttpSession session = request.getSession();
//        session.setAttribute("user", userData.orElse(new Data()));
//        return "redirect:/home";
//    }

}
