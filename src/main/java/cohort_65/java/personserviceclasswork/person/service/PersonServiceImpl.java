package cohort_65.java.personserviceclasswork.person.service;

import cohort_65.java.personserviceclasswork.person.dao.PersonRepository;
import cohort_65.java.personserviceclasswork.person.dto.*;
import cohort_65.java.personserviceclasswork.person.dto.exception.PersonNotFoundException;
import cohort_65.java.personserviceclasswork.person.model.Address;
import cohort_65.java.personserviceclasswork.person.model.Child;
import cohort_65.java.personserviceclasswork.person.model.Employee;
import cohort_65.java.personserviceclasswork.person.model.Person;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, CommandLineRunner {
    final PersonRepository personRepository;
    final ModelMapper modelMapper;

    @Override
    @Transactional
    public PersonDto addPerson(PersonDto newPersonDto) {
        if (personRepository.existsById(newPersonDto.getId())) {
            return null;
        }
        Person person = modelMapper.map(newPersonDto, Person.class);
        person = personRepository.save(person);
        return mapToCorrectDto(person);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonDto findPersonById(Integer id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        return mapToCorrectDto(person);
    }

    @Override
    public PersonDto deletePersonById(Integer id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        personRepository.delete(person);
        return mapToCorrectDto(person);
    }

    @Override
    public PersonDto updatePersonName(Integer id, String name) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        person.setName(name);
        personRepository.save(person);
        return mapToCorrectDto(person);

    }

    @Override
    @Transactional
    public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        if (addressDto == null) {
            return null;
        }
        person.setAddress(modelMapper.map(addressDto, Address.class));
        person = personRepository.save(person);
        return mapToCorrectDto(person);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonDto[] findPersonByCity(String city) {
        return personRepository.findByAddressCityIgnoreCase(city)
                .stream()
                .map(this::mapToCorrectDto)
                .toArray(PersonDto[]::new);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonDto[] findPersonByName(String name) {
        return personRepository.findByNameIgnoreCase(name)
                .stream()
                .map(this::mapToCorrectDto)
                .toArray(PersonDto[]::new);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonDto[] findPersonsBetweenAge(Integer minAge, Integer maxAge) {
        LocalDate startAge = LocalDate.now().minusYears(maxAge);
        LocalDate endAge = LocalDate.now().minusYears(minAge);
        return personRepository.findByBirthDateBetween(startAge, endAge)
                .stream()
                .map(this::mapToCorrectDto)
                .toArray(PersonDto[]::new);
    }

    @Override
    public Iterable<CityPopulationDto> getCityPopulation() {
        return personRepository.getCityPopulation();
    }

    @Override
    public Iterable<EmployeeDto> findEmployeeBySalary(Integer min, Integer max) {
        return personRepository.findEmployeesWithSalaryBetween(min, max).stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class)).toList();
    }

    @Override
    public Iterable<ChildDto> findAllChildren() {
        return personRepository.getAllChildren().stream().map(child -> modelMapper.map(child, ChildDto.class)).toList();
    }


    private PersonDto mapToCorrectDto(Person person) {
        if (person instanceof Employee) {
            return modelMapper.map(person, EmployeeDto.class);
        }
        if (person instanceof Child) {
            return modelMapper.map(person, ChildDto.class);
        } else {
            return modelMapper.map(person, PersonDto.class);
        }
    }


    @Override

    public void run(String... args) throws Exception {
        if (personRepository.count() == 0) {
            Person person = new Person(
                    1000, "John", LocalDate.now().minusYears(20),
                    new Address("Berlin", "Kant str.", 33));
            Child child = new Child(
                    1001, "Johny", LocalDate.now().minusYears(5),
                    new Address("Berlin", "Kant str.", 33), "Kindergarten");
            Employee employee = new Employee(1002, "John", LocalDate.now().minusYears(20),
                    new Address("Berlin", "Kant str.", 33), "Apple", 8000);
            personRepository.save(person);
            personRepository.save(child);
            personRepository.save(employee);
        }

    }

}
