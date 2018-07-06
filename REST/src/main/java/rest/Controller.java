package rest;

import model.JocUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.IJocUserRepository;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/joc")
public class Controller {
    @Autowired
    private IJocUserRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getById(@RequestParam String user,@RequestParam Integer idJoc) {
        List<JocUser> all = repository.getDetalii(idJoc,user);
        return new ResponseEntity<List<JocUser>>(all, HttpStatus.OK);
    }


}