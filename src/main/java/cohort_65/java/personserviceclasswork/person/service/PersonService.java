package cohort_65.java.personserviceclasswork.person.service;

import cohort_65.java.personserviceclasswork.person.dto.*;

public interface PersonService {

    PersonDto addPerson(PersonDto newPersonDto);

    PersonDto findPersonById(Integer id);

    PersonDto deletePersonById(Integer id);

    PersonDto updatePersonName(Integer id, String name);

    PersonDto updatePersonAddress(Integer id, AddressDto addressDto);

    PersonDto[] findPersonByCity(String city);

    PersonDto[] findPersonByName(String name);

    PersonDto[] findPersonsBetweenAge(Integer from, Integer to);

    Iterable<CityPopulationDto> getCityPopulation();

    Iterable<EmployeeDto> findEmployeeBySalary(Integer min, Integer max);

    Iterable<ChildDto> findAllChildren();

    void run(String ...args) throws Exception ;
}
