package rest;

import model.JocUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.IJocRepository;
import repository.IJocUserRepository;
import utils.DTO;
import utils.Pair;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/spanzuratoare")
public class Controller {
    @Autowired
    private IJocUserRepository jocUserRepository;

    @RequestMapping(value="/{idJoc}/litere",method = RequestMethod.GET)
    public ResponseEntity<?> getBy(@RequestParam String idJucator,@PathVariable Integer idJoc) {
        DTO j=jocUserRepository.getDTO(idJoc,idJucator);
        if(j==null)
            return new ResponseEntity<>("Nu exista", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(j, HttpStatus.OK);
    }


}