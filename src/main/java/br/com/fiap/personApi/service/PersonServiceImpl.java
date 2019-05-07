package br.com.fiap.personApi.service;

import br.com.fiap.personApi.repository.Person;
import br.com.fiap.personApi.repository.PersonRepository;
import br.com.fiap.personApi.resource.PersonCreateResponse;
import br.com.fiap.personApi.resource.PersonRequest;
import br.com.fiap.personApi.resource.PersonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public PersonResponse findById(Integer id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new HttpServerErrorException(NOT_FOUND, "Person Not Found."));

        PersonResponse personResponse = new PersonResponse();
        personResponse.setName(person.getName());
        personResponse.setLastName(person.getLastName());
        personResponse.setGender(person.getGender());
        personResponse.setAge(person.getAge());

        return personResponse;
    }

    @Override
    public PersonCreateResponse create(PersonRequest personRequest) {

        validateGender(personRequest.getGender());

//        String gender = personRequest.getGender().toUpperCase();
//        if(gender.equals("MALE") || gender.equals("FEMALE")){
//            return null;
//        } else {
//            throw new HttpServerErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "Gender Is Invalid.");
//        }

        Person person = new Person();
        person.setName(personRequest.getName());
        person.setLastName(personRequest.getLastName());
        person.setGender(personRequest.getGender());
        person.setAge(personRequest.getAge());

        Person createdPerson = personRepository.save(person);

        PersonCreateResponse personCreateResponse = new PersonCreateResponse();
        personCreateResponse.setPersonId(createdPerson.getId());
        return personCreateResponse;

    }

    private void validateGender(String gender) {
        Stream.of("MALE","FEMALE")
                .filter(s -> s.equals(gender.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new HttpServerErrorException(UNPROCESSABLE_ENTITY, "Gender Is Invalid."));
    }
}
