package cohort_65.java.personserviceclasswork.person.service;

import cohort_65.java.personserviceclasswork.person.dto.ChildDto;
import cohort_65.java.personserviceclasswork.person.dto.EmployeeDto;
import cohort_65.java.personserviceclasswork.person.dto.PersonDto;
import cohort_65.java.personserviceclasswork.person.model.Child;
import cohort_65.java.personserviceclasswork.person.model.Employee;
import cohort_65.java.personserviceclasswork.person.model.Person;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonModelDtoMapper {
    final ModelMapper modelMapper;

    public Person mapToModel(PersonDto personDto) {
        if (personDto instanceof EmployeeDto) {
            return modelMapper.map(personDto, Employee.class);
        }
        if (personDto instanceof ChildDto) {
            return modelMapper.map(personDto, Child.class);
        }
        return modelMapper.map(personDto, Person.class);
    }

    public PersonDto mapToDto(Person person) {
        if (person instanceof Employee) {
            return modelMapper.map(person, EmployeeDto.class);
        }
        if (person instanceof Child) {
            return modelMapper.map(person, ChildDto.class);
        }
        return modelMapper.map(person, PersonDto.class);
    }
}
