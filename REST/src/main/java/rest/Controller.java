package rest;

import model.DTO;
import model.Joc;
import model.JocUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.IJocRepository;
import repository.IJocUserRepository;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/razboi")
public class Controller {
    @Autowired
    private IJocUserRepository jocUserRepository;
    @Autowired
    private IJocRepository jocRepository;

    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Joc joc=jocRepository.findOne(id);
        if(id!=null) {
            List<JocUser> all = jocUserRepository.getDetalii(id);
            return new ResponseEntity<>(new DTO(joc.getId(),all,joc.getPunctaj()), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }

}