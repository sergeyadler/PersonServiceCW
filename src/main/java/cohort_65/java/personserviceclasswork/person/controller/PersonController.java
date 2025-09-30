package cohort_65.java.personserviceclasswork.person.controller;

import cohort_65.java.personserviceclasswork.person.dto.AddressDto;
import cohort_65.java.personserviceclasswork.person.dto.CityPopulationDto;
import cohort_65.java.personserviceclasswork.person.dto.PersonDto;
import cohort_65.java.personserviceclasswork.person.dto.ChildDto;
import cohort_65.java.personserviceclasswork.person.dto.EmployeeDto;
import cohort_65.java.personserviceclasswork.person.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
    final PersonService personService;
    @PostMapping
    public PersonDto addPerson(@RequestBody PersonDto personDto) {
        return personService.addPerson(personDto);

    }
    @GetMapping("/{id}")
    public PersonDto  findPersonById(@PathVariable Integer id){
        return personService.findPersonById(id);

    }
    @DeleteMapping("/{id}")
    public PersonDto  deletePersonById(@PathVariable Integer id){
        return personService.deletePersonById(id);

    }
    @PutMapping("/{id}/name/{name}")
    public PersonDto updatePersonName(@PathVariable Integer id, @PathVariable String name){
        return personService.updatePersonName(id,name);
    }
    @PutMapping("/{id}/address")
    public PersonDto updatePersonAddress(@PathVariable Integer id, @RequestBody AddressDto addressDto){
        return personService.updatePersonAddress(id,addressDto);
    }

    @GetMapping("/city/{city}")
    public PersonDto[] findPersonByCity(@PathVariable String city) {
        return personService.findPersonByCity(city);
    }
    @GetMapping("/name/{name}")
    public PersonDto[] findPersonByName(@PathVariable String name){
        return personService.findPersonByName(name);
    }
    @GetMapping("/ages/{age1}/{age2}")
    public PersonDto[] findPersonsBetweenAge(@PathVariable Integer age1, @PathVariable Integer age2) {
        return personService.findPersonsBetweenAge(age1,age2);
    }

    @GetMapping("/population/city")
    public Iterable<CityPopulationDto> getCityPopulation() {
        return personService.getCityPopulation();
    }

    @GetMapping("/salary/{min}/{max}")
    public Iterable<EmployeeDto> findEmployeeBySalary(@PathVariable Integer min, @PathVariable Integer max) {
        return personService.findEmployeeBySalary(min,max);
    }


    @GetMapping("/children")
    public Iterable<ChildDto> findAllChildren() {
        return personService.findAllChildren();
    }
}
