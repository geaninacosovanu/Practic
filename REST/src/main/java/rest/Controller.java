package rest;

import model.CopilDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.ITrecereRepository;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/copii")
public class Controller {
    @Autowired
    private ITrecereRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getById(@RequestParam Integer punct) {
        List<CopilDTO> all = repository.getCopiiByPunct(punct);
        return new ResponseEntity<List<CopilDTO>>(all, HttpStatus.OK);
    }


}