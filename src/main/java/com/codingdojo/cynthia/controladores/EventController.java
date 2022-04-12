package com.codingdojo.cynthia.controladores;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codingdojo.cynthia.modelos.Event;
import com.codingdojo.cynthia.modelos.State;
import com.codingdojo.cynthia.modelos.User;
import com.codingdojo.cynthia.servicios.AppService;

@Controller
@RequestMapping("/events")
public class EventController {
	
	@Autowired
	private AppService servicio;
	
	@PostMapping("/create")
	public String create_event(@Valid @ModelAttribute("event") Event event,
							   BindingResult result, Model model, HttpSession session) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session");
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		if(result.hasErrors()) {
			
			User myUser = servicio.find_user(currentUser.getId());
			
			model.addAttribute("states", State.States);
			model.addAttribute("user", myUser);
			return "dashboard.jsp";
		}
		
		servicio.save_event(event);
		return "redirect:/dashboard";
	}
	
}
