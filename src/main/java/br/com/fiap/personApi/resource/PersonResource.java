package br.com.fiap.personApi.resource;

import br.com.fiap.personApi.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

@RestController
public class PersonResource {

    @Autowired
    private PersonService personService;

    @GetMapping("/people/{id}")
    public ResponseEntity<PersonResponse> findPersonById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(personService.findById(id));
    }

    @PostMapping("/people")
    public ResponseEntity<PersonCreateResponse> createPerson(@RequestBody PersonRequest personRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.create(personRequest));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(HttpServerErrorException hsee) {
        return ResponseEntity.status(hsee.getStatusCode()).body(new ErrorResponse(hsee.getStatusText()));
    }
}