package br.com.fiap.personApi.service;

import br.com.fiap.personApi.resource.PersonCreateResponse;
import br.com.fiap.personApi.resource.PersonRequest;
import br.com.fiap.personApi.resource.PersonResponse;

public interface PersonService {

    PersonResponse findById(Integer id);

    PersonCreateResponse create(PersonRequest personRequest);
}
