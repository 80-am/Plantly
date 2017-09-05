package com.example.plantly;

import com.example.plantly.Domain.User;

import com.example.plantly.Repository.DBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SpringBootApplication
public class PlantlyApplication {

	@Autowired
	private DBRepository DBConnection;

	public static void main(String[] args) {
		SpringApplication.run(PlantlyApplication.class, args);
	}

	@GetMapping("/")
	public String homepage() {
		return "index";
	}



	@GetMapping("/signup")
	public String login() {
		return "signup";
	}

	@PostMapping("/login")
	public String signup(Model model, @RequestParam String email, @RequestParam String firstname, @RequestParam String lastname, @RequestParam String password)
	{
			List<User> allUsers = DBConnection.getAllUsers();
		System.out.println(allUsers);

			for(int i =0; i<allUsers.size(); i++) {
				if(allUsers.get(i).getEmail().equals(email)){
					model.addAttribute("info", "User with this email already exists");
					return "login";
				}
			}

			DBConnection.addUser(email, firstname, lastname, password);
			return "login";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

}


