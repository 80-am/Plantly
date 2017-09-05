package com.example.plantly;

import com.example.plantly.Repository.DBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
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


	@GetMapping("/login")
	public String login() {
		return "login";
	}





}


