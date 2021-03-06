package com.codingdojo.cynthia.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codingdojo.cynthia.modelos.Event;
import com.codingdojo.cynthia.modelos.Message;
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
			
			/*lista de eventos en mi estado*/
			String miEstado = currentUser.getState(); //Obtenemos estado del usuario
			List<Event> eventos_miestado = servicio.eventos_estado(miEstado); //Lista de eventos
			
			List<Event> eventos_otrosedos = servicio.eventos_otros(miEstado); //Lista de eventos fuera de mi estado
			
			model.addAttribute("eventos_miestado", eventos_miestado);
			model.addAttribute("eventos_otrosedos", eventos_otrosedos);
			
			return "dashboard.jsp";
		}
		
		servicio.save_event(event);
		return "redirect:/dashboard";
	}
	
	@GetMapping("/join/{event_id}")
	public String join_event(@PathVariable("event_id") Long event_id, HttpSession session){
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session");
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		servicio.save_event_user(currentUser.getId(), event_id);
		
		return "redirect:/dashboard";
		
	}
	
	@GetMapping("/remove/{event_id}")
	public String remove_event(@PathVariable("event_id") Long event_id, HttpSession session) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session");
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		servicio.remove_event_user(currentUser.getId(), event_id);
		
		return "redirect:/dashboard";
	}
	
	@GetMapping("/{event_id}")
	public String show_event(@PathVariable("event_id") Long event_id, HttpSession session,
							 Model model, @ModelAttribute("message") Message message) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session");
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		Event event_edit = servicio.find_event(event_id);
		model.addAttribute("evento", event_edit);
		
		return "show.jsp";
	}
	
	@PostMapping("/message")
	public String message(@Valid @ModelAttribute("message") Message message, BindingResult result,
						  HttpSession session, Model model) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session");
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		Long event_id = message.getEvent().getId();
		Event event_edit = servicio.find_event(event_id);
		
		if(result.hasErrors()) {
			model.addAttribute("evento", event_edit);
			return "show.jsp";
		}
		
		servicio.save_message(message);
		
		return "redirect:/events/"+event_id;
		
		
	}
	
	@GetMapping("/edit/{event_id}")
	public String edit_event(@PathVariable("event_id") Long event_id, HttpSession session, Model model) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session");
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		Event evento = servicio.find_event(event_id);
		
		if(evento == null || !evento.getPlanner().getId().equals(currentUser.getId())) {
			return "redirect:/dashboard";
		}
		
		model.addAttribute("evento",evento);
		model.addAttribute("states", State.States);
		
		return "edit.jsp";
	}
	
	@PutMapping("/update")
	public String update_event(@Valid @ModelAttribute("evento") Event evento, 
							   BindingResult result, HttpSession session, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("states", State.States);
			return "edit.jsp";
		}
		
		Event thisEvent = servicio.find_event(evento.getId());
		evento.setAttendees(thisEvent.getAttendees());
		servicio.save_event(evento);
		
		return "redirect:/dashboard";
	}
	
	@DeleteMapping("/delete/{event_id}")
	public String delete_event(@PathVariable("event_id") Long event_id, HttpSession session) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session");
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		Event evento = servicio.find_event(event_id);
		if(evento == null || !evento.getPlanner().getId().equals(currentUser.getId())) {
			return "redirect:/dashboard";
		}
		
		servicio.delete_event(event_id);
		
		return "redirect:/dashboard";
	}
	
	
	
}
