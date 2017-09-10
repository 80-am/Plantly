package com.example.plantly.Controller;

import com.example.plantly.Domain.Plant;
import com.example.plantly.Domain.User;
import com.example.plantly.Domain.UserPlant;
import com.example.plantly.Repository.DBRepository;
import com.sun.org.apache.xerces.internal.util.HTTPInputSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class DBController {
    @Autowired
    private DBRepository DBConnection;

    @GetMapping("/")
    public String homepage() {
        return "index";
    }
    
    @GetMapping("/about")
	public String about() {
		return "about";
	}

    @PostMapping("/signup")
    public String signup(Model model, @RequestParam String email, @RequestParam String firstname, @RequestParam String lastname, @RequestParam String password) {
        List<User> allUsers = DBConnection.getAllUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getEmail().equals(email)) {
                model.addAttribute("info", "User with this email already exists");
                return "redirect:/";
            }
        }
        DBConnection.addUser(email, firstname, lastname, password);
        return "redirect:/";
    }

    @PostMapping("/user")
    public ModelAndView loggedin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        boolean userExists = DBConnection.userExists(email, password);
        User user = DBConnection.getCurrentUser(email, password);

        if(userExists) {
            List<UserPlant> userPlantList = DBConnection.getUserPlantsInfo(user.getUserId());
            session.setAttribute("user", user);
            return new ModelAndView("userpage").addObject("userPlansList", userPlantList);
        }
        model.addAttribute("info", "Wrong password or email try again");
        return new ModelAndView("/");

    }

    @GetMapping("/user")
    public ModelAndView userpage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            List<UserPlant> userPlantList = DBConnection.getUserPlantsInfo(user.getUserId());
            return new ModelAndView("userpage").addObject("userPlansList", userPlantList);
        }
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/changePassword")
    public String passwordChangeHTML(){
        return "changePassword";
    }


    @PostMapping("/passwordVerification")
    public String /*ModelAndView*/ passwordVerification(@RequestParam String newPassword, @RequestParam String oldPassword, HttpSession session, Model model) {
        if(session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (user.getPassword().equals(oldPassword)) {
                DBConnection.changePassword(user.getUserId(), newPassword);
                model.addAttribute("info", "Password has been changed");
                return "changePassword";
                //return new ModelAndView("changePassword").addObject("info", "Password has been changed");
            } else {
                model.addAttribute("info", "Wrong password!");
                return "changePassword";
                //return new ModelAndView("changePassword").addObject("info", "Incorrect old password!");
            }
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session, HttpServletResponse res) {
        {
            session.invalidate();
            Cookie cookie = new Cookie("jsessionid", "");
            cookie.setMaxAge(0);
            res.addCookie(cookie);
            return new ModelAndView("redirect:/");
        }
    }

    @GetMapping("/plantinfo/{plantSpecies}")
    public ModelAndView plantinfo(@PathVariable String plantSpecies) {
        Plant plant = DBConnection.getPlantByPlantSpecies(plantSpecies); // get plant from Plants database using plantSpecies
        return new ModelAndView("plantinfo").addObject("plant", plant);
    }

    @GetMapping("/addplant")
    public String addplant(){
        return "addplant";
    }

    @PostMapping("/addUserPlant")
    public String addUserPlant(@RequestParam String nickName, @RequestParam String plantSpecies, @RequestParam int userId, HttpSession session){
        session.setAttribute("warning", "ok");
        boolean nickNameExists = DBConnection.nickNameAlreadyExists(nickName, userId);
        if(!nickNameExists){
            DBConnection.addPlantToUserPlants(nickName, "needs a image URL", userId, plantSpecies);
            return "redirect:/user";
        }
        session.setAttribute("warning", "Nickname already exists!");
        return "addplant";
    }

    @RequestMapping(path = "/GET", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getData(){
        return DBConnection.getPlantName();
    }
}

