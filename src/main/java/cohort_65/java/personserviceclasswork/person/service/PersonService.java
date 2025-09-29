package cohort_65.java.personserviceclasswork.person.service;

import cohort_65.java.personserviceclasswork.person.dto.AddressDto;
import cohort_65.java.personserviceclasswork.person.dto.CityPopulationDto;
import cohort_65.java.personserviceclasswork.person.dto.PersonDto;

public interface PersonService {

    boolean addPerson(PersonDto newPersonDto);

    PersonDto findPersonById(Integer id);

    PersonDto deletePersonById(Integer id);

    PersonDto updatePersonName(Integer id, String name);

    PersonDto updatePersonAddress(Integer id, AddressDto addressDto);

    PersonDto[] findPersonByCity(String city);

    PersonDto[] findPersonByName(String name);

    PersonDto[] findPersonsBetweenAge(Integer from, Integer to);

    Iterable<CityPopulationDto> getCityPopulation();}
