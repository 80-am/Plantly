package com.example.plantly.Controller;

import com.example.plantly.Domain.Plant;
import com.example.plantly.Domain.User;
import com.example.plantly.Domain.UserPlant;
import com.example.plantly.Repository.DBRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
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
    public ModelAndView signup(Model model, @RequestParam String email, @RequestParam String firstname, @RequestParam String lastname, @RequestParam String password, HttpSession session) {
        List<User> allUsers = DBConnection.getAllUsers();
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getEmail().equals(email)) {
                return new ModelAndView("index").addObject("infoSignup", "User already exists!");
            }
        }
        DBConnection.addUser(email, firstname, lastname, password);
        User user = DBConnection.getCurrentUser(email, password);
        List<UserPlant> userPlantList = DBConnection.getUserPlantsInfo(user.getUserId());
        session.setAttribute("user", user);
        session.setAttribute("userPlantsList", userPlantList);
        return new ModelAndView("userpage");
    }

    @PostMapping("/user")
    public ModelAndView loggedin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        boolean userExists = DBConnection.userExists(email, password);
        User user = DBConnection.getCurrentUser(email, password);

        if(userExists) {
            List<UserPlant> userPlantList = DBConnection.getUserPlantsInfo(user.getUserId());
            session.setAttribute("user", user);
            session.setAttribute("userPlantsList", userPlantList);
            List<LocalDate> listOfWDays = DBConnection.getAllWDays(user.getUserId());
            System.out.println(listOfWDays);

            LocalDate from = LocalDate.now();
            LocalDate to = null;
            long diff = 0;

            for(int i=0; i<userPlantList.size(); i++) {
                to = userPlantList.get(i).waterDate.toLocalDateTime().toLocalDate();
                diff = ChronoUnit.DAYS.between(LocalDate.parse(from.toString()),LocalDate.parse(to.toString()));
                System.out.println(diff + " " +  from + " " + to);
                userPlantList.get(i).daysLeft = diff;
            }









            return new ModelAndView("userpage");
        }
        return new ModelAndView("index").addObject("infoLogin", "Invalid email or password!");

    }

    @GetMapping("/user")
    public ModelAndView userpage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            User user = (User)session.getAttribute("user");
            List<UserPlant> userPlantList = DBConnection.getUserPlantsInfo(user.getUserId());

            LocalDate from = LocalDate.now();
            LocalDate to = null;
            long diff = 0;

            for(int i=0; i<userPlantList.size(); i++) {
                to = userPlantList.get(i).waterDate.toLocalDateTime().toLocalDate();
                diff = ChronoUnit.DAYS.between(LocalDate.parse(from.toString()),LocalDate.parse(to.toString()));
                System.out.println(diff + " " +  from + " " + to);
                userPlantList.get(i).daysLeft = diff;
            }

            session.setAttribute("userPlantsList", userPlantList);
            return new ModelAndView("userpage").addObject("userPlantsList", userPlantList);
        }
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/changePassword")
    public String passwordChangeHTML(){
        return "changePassword";
    }


    @PostMapping("/passwordVerification")
    public /*String*/ ModelAndView passwordVerification(@RequestParam String newPassword, @RequestParam String oldPassword, HttpSession session, Model model) {
        if(session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (user.getPassword().equals(oldPassword)) {
                DBConnection.changePassword(user.getUserId(), newPassword);
                //model.addAttribute("info", "Password has been changed");
                //return "changePassword";
                return new ModelAndView("changePassword").addObject("info", "Password has been changed");
            } else {
                //model.addAttribute("info", "Wrong password!");
                //return "changePassword";
                return new ModelAndView("changePassword").addObject("info", "Incorrect old password!");
            }
        }
        return new ModelAndView("userpage");
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
    public ModelAndView plantinfo(@PathVariable String plantSpecies, HttpSession session) {
        if(session.getAttribute("user") != null) {
            Plant plant = DBConnection.getPlantByPlantSpecies(plantSpecies); // get plant from Plants database using plantSpecies
            return new ModelAndView("plantinfo").addObject("plant", plant);
        }
        return new ModelAndView("index");
    }

    @GetMapping("/addplant")
    public String addplant(){
        return "addplant";
    }

    @PostMapping("/updateSql")
    public ModelAndView updateDates(HttpSession session) {
        User user = (User)session.getAttribute("user");
        
        return new ModelAndView("userpage");
    }

    @PostMapping("/addUserPlant")
    public ModelAndView addUserPlant(@RequestParam String nickName, @RequestParam String plantSpecies, @RequestParam int userId, HttpSession session){
        boolean nickNameExists = DBConnection.nickNameAlreadyExists(nickName, userId);

        int plantID = DBConnection.getPlantIdFromPlants(plantSpecies);
        int wdays = DBConnection.getWateringDays(plantID);

        LocalDate regdate = LocalDate.now();
        LocalDate futureDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis()).toLocalDate().plusDays(wdays);



        if(!nickNameExists){

            DBConnection.addPlantToUserPlants(nickName, "needs a image URL", userId, plantSpecies, java.sql.Date.valueOf(regdate), java.sql.Date.valueOf(futureDate));
            List<UserPlant> userPlantList = DBConnection.getUserPlantsInfo(userId);

            LocalDate from = LocalDate.now();
            LocalDate to = null;
            long diff = 0;

            for(int i=0; i<userPlantList.size(); i++) {
                to = userPlantList.get(i).waterDate.toLocalDateTime().toLocalDate();
                diff = ChronoUnit.DAYS.between(LocalDate.parse(from.toString()),LocalDate.parse(to.toString()));
                System.out.println(diff + " " +  from + " " + to);
                userPlantList.get(i).daysLeft = diff;
            }
            session.setAttribute("userPlantsList", userPlantList);
            return new ModelAndView("userpage");


        }
        return new ModelAndView("userpage").addObject("warning", "Nickname already exists!");
    }

    @RequestMapping(path = "/GET", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getData(){
        return DBConnection.getPlantName();
    }

    @RequestMapping(path = "/GETD", method = RequestMethod.GET)
    @ResponseBody
    public List<Integer> getDAYS(HttpSession session){
        User user = (User)session.getAttribute("user");

        return DBConnection.getDays(user.getUserId());
    }

    @GetMapping("/deletePlant/{nickName}")
    public String deletePlant(@PathVariable String nickName, HttpSession session){
        User user =  (User) session.getAttribute("user");
        DBConnection.deletePlantFromUserPlants(nickName, user.getUserId());
        return "redirect:/user";
    }

    @GetMapping ("/updateWateringDays/{usersPlantsID}/{plantSpecies}")
    public String updateDates(@PathVariable String usersPlantsID, @PathVariable String plantSpecies) {
       // LocalDate wateredDay = DBConnection.getWateredDay(usersPlantsID);
        LocalDate wateredDay = LocalDate.now();
        int plantID = DBConnection.getPlantIdFromPlants(plantSpecies);
        int wdays = DBConnection.getWateringDays(plantID);
        LocalDate futureDate = wateredDay.plusDays(wdays);
        DBConnection.updateDates(usersPlantsID, wateredDay, futureDate);
        return "redirect:/user";
    }

    @GetMapping("/clock")
    public String testClock(){
        return  "clock";
    }


}

