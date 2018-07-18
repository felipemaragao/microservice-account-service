package br.com.mv.microservice.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.mv.microservice.model.Users;
import br.com.mv.microservice.repository.UserRepository;
import br.com.mv.microservice.service.UserService;
import br.com.mv.microservice.service.exception.EmailExistenteException;
import br.com.mv.microservice.service.exception.LoginExistenteException;


@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	    @Autowired
	    private UserService userService;
	    
	    @Autowired
	    private UserRepository userRepository;
		
		private Collection<? extends GrantedAuthority> getAuthorities(Users users) {
			Set<SimpleGrantedAuthority> authorities = new HashSet<>();
			users.getAuthorities().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getName().toUpperCase())));
			return authorities;
		}
	 
		
		
		 
		 
		 @PutMapping("/{username}")
		 @ExceptionHandler({EmailExistenteException.class})
		 public ResponseEntity<Users> atualizarEmail(@PathVariable String username, @Valid @RequestBody Users users){
				Users userSalvo = userService.atualizarEmail(username, users);
				return ResponseEntity.ok(userSalvo);
		}
			
	
	    @PostMapping
	    @ExceptionHandler({LoginExistenteException.class, EmailExistenteException.class})
	    public ResponseEntity<Users> save(@RequestBody  Users users){
	        return ResponseEntity.ok(userService.salvar(users));
	    }

    	    
	    
	    @GetMapping("/username/{username}")  
		public ResponseEntity<Users> buscarPeloCodigo(@PathVariable String username){
			Optional<Users> usuarioRetornado  = userRepository.findByUsername(username);
			if (usuarioRetornado.isPresent()) {
				return ResponseEntity.ok(usuarioRetornado.get());
			}else {
				return ResponseEntity.notFound().build();
			}
	    }
	    
	    @GetMapping
		public Page<Users> pesquisar(@RequestParam(required = false, defaultValue = "%") String username, Pageable pageable) {
			return userRepository.findByUsernameContaining(username, pageable);
		}
	    
	    

	    @RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
	    public ResponseEntity<?> delete(@PathVariable("username") String username){
	        userService.delete(username);
	        return ResponseEntity.ok().build();
	    }
	}
