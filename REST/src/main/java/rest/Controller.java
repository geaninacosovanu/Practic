package rest;

import model.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.IRezultatRepository;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/concurs/rezultate")
public class Controller {
    @Autowired
    private IRezultatRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getById(@RequestParam String user) {
        List<Participant> all = repository.getParticipantiByUser(user);
        return new ResponseEntity<List<Participant>>(all, HttpStatus.OK);
    }


}
