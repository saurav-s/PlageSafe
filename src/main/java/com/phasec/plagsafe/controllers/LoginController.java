package com.phasec.plagsafe.controllers;
import com.phasec.plagsafe.services.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for login functionality
 */
import com.phasec.plagsafe.models.UserObject;


@RestController
public class LoginController {

	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	
    @Autowired
    LoginService service;

    /**
     * default login page mapping
     * @param model ModelMap for the shouLoginPage method
     * @return the default login string
     */
    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String showLoginPage(ModelMap model){
        return "login";
    }

    /**
     * api for login credential checks
     * @param model ModelMap for the shouLoginPage method
     * @param name the name of the logged in user
     * @param password the password of the logged in user
     * @return the User
     */
    @RequestMapping(value="/logincheck", method = RequestMethod.GET)
    public UserObject showWelcomePage(ModelMap model, @RequestParam String name, @RequestParam String password){
        logger.info("User {} is trying to authenticate ",name);
        UserObject validateUser = service.validateUser(name, password);
        logAuthActivity(name, validateUser);
        return validateUser;
    }

	/**
	 * @param name
	 * @param validateUser
	 */
	private void logAuthActivity(String name, UserObject validateUser) {
		if(validateUser == null) {
        		logger.warn("Authentication failure for user {}",name);
        }else {
        		logger.info("User {} is authenticated successfully",name);
        }
	}

}