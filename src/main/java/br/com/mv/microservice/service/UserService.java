package br.com.mv.microservice.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import br.com.mv.microservice.model.Users;
import br.com.mv.microservice.repository.UserRepository;
import br.com.mv.microservice.service.exception.EmailExistenteException;
import br.com.mv.microservice.service.exception.LoginExistenteException;

@Service
public class UserService {
	
	 @Autowired
	    private UserRepository userRepository;
	 
	 
	

	    public Users salvar(@Validated Users users) {
	    	if (!verificaLoginEmailJaCadastrado(users)){
	    		 return userRepository.save(users);   		
	    	}
	    	return null;
	    	
	    	
	    }
	    
	    public boolean verificaLoginEmailJaCadastrado(Users users) {
	    	
	    	Optional<Users> user = userRepository.findByUsername(users.getUsername());
			if (user.isPresent()) {
	    		    		throw new LoginExistenteException("Login já cadastrado");
	    	} 
			user = userRepository.findByEmail(users.getEmail());
			if (user.isPresent()) {
	    		    		throw new EmailExistenteException("E-mail já cadastrado");
	    	} 
	    		    	
	    	return false;
	    }
	    
		public Users atualizarEmail(String username, Users user){		
			
			Users usersSalvo = findByUsername(username);
			Optional<Users> userEmail = userRepository.findByEmail(user.getEmail());
			if (userEmail.isPresent() && ! (usersSalvo.getUsername()==userEmail.get().getUsername())){
				throw new EmailExistenteException("E-mail já cadastrado");
			}else {
				usersSalvo.setEmail(user.getEmail());
				return userRepository.saveAndFlush(usersSalvo);
			}
		}
		
		 public Users findByEmail(String email){
		    	Optional<Users> user = userRepository.findByEmail(email);
				if (!user.isPresent()) {
					throw new EmptyResultDataAccessException(1);
				}
				return user.get();
				    	
		    }

	    public Users findByUsername(String username){
	    	Optional<Users> user = userRepository.findByUsername(username);
			if (!user.isPresent()) {
				throw new EmptyResultDataAccessException(1);
			}
			return user.get();		    	
	    }

	    public Iterable<Users> findAll(){
	        return userRepository.findAll();
	    }

	    public void delete(String username) {
	    	userRepository.delete(username);
	    }
	}