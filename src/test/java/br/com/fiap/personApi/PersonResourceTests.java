package br.com.fiap.personApi;

import br.com.fiap.personApi.repository.Person;
import br.com.fiap.personApi.repository.PersonRepository;
import br.com.fiap.personApi.resource.PersonRequest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class PersonResourceTests {

    @Autowired
    private PersonRepository personRepository;

    @LocalServerPort
    private Integer port;

    @Before
    public void setup() {

        stubCreatePerson();

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = this.port;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("Content-Type", "application/json")
                .build();
    }

    @Test
    public void shouldFindPersonById() {

        RestAssured.get("/people/1")
                .then()
                .assertThat()
                .statusCode(200)
                .body("name", Matchers.is("Danilo"))
                .body("lastName", Matchers.is("Vitoriano"))
                .body("gender", Matchers.is("Male"))
                .body("age", Matchers.is(38));
    }

    private void stubCreatePerson() {
        Person person = new Person();
        person.setName("Danilo");
        person.setLastName("Vitoriano");
        person.setGender("Male");
        person.setAge(38);

        personRepository.save(person);
    }

    @Test
    public void cannotFindPersonByIdWhenIdIsNotFound() {

        RestAssured.get("/people/300")
                .then()
                .assertThat()
                .statusCode(404)
                .body("messageError", Matchers.is("Person Not Found."));
    }

    @Test
    public void shouldCreatePerson(){

        PersonRequest personRequest = new PersonRequest();

        personRequest.setName("Danilo");
        personRequest.setLastName("Vitoriano");
        personRequest.setGender("Male");
        personRequest.setAge(38);

        RestAssured.given()
                .body(personRequest)
                .post("/people")
                .then()
                .assertThat()
                .statusCode(201)
                .body("personId", Matchers.any(Integer.class));
    }

    @Test
    public void cannotCreatePersonWhenGenderIsInvalid(){

        PersonRequest personRequest = new PersonRequest();

        personRequest.setName("Danilo");
        personRequest.setLastName("Vitoriano");
        personRequest.setGender("Animal");
        personRequest.setAge(38);

        RestAssured.given()
                .body(personRequest)
                .post("/people")
                .then()
                .assertThat()
                .statusCode(422)
                .body("messageError", Matchers.is("Gender Is Invalid."));
    }

}
