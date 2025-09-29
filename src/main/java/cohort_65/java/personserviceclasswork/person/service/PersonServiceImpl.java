package cohort_65.java.personserviceclasswork.person.service;

import cohort_65.java.personserviceclasswork.person.dao.PersonRepository;
import cohort_65.java.personserviceclasswork.person.dto.AddressDto;
import cohort_65.java.personserviceclasswork.person.dto.CityPopulationDto;
import cohort_65.java.personserviceclasswork.person.dto.PersonDto;
import cohort_65.java.personserviceclasswork.person.dto.exception.PersonNotFoundException;
import cohort_65.java.personserviceclasswork.person.model.Address;
import cohort_65.java.personserviceclasswork.person.model.Person;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    final PersonRepository personRepository;
    final ModelMapper modelMapper;

    @Override
    public boolean addPerson(PersonDto newPersonDto) {
        if (personRepository.existsById(newPersonDto.getId())) {
            return false;
        }
        personRepository.save(modelMapper.map(newPersonDto, Person.class));
        return true;
    }

    @Override
    public PersonDto findPersonById(Integer id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    public PersonDto deletePersonById(Integer id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        personRepository.delete(person);
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    public PersonDto updatePersonName(Integer id, String name) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        person.setName(name);
        personRepository.save(person);
        return modelMapper.map(person, PersonDto.class);

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
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    public PersonDto[] findPersonByCity(String city) {
        return personRepository.findByAddressCityIgnoreCase(city)
                .stream()
                .map(person -> modelMapper.map(person, PersonDto.class))
                .toArray(PersonDto[]::new);
    }

    @Override
    public PersonDto[] findPersonByName(String name) {
        return personRepository.findByNameIgnoreCase(name)
                .stream()
                .map(person -> modelMapper.map(person, PersonDto.class))
                .toArray(PersonDto[]::new);
    }

    @Override
    public PersonDto[] findPersonsBetweenAge(Integer minAge, Integer maxAge) {
        LocalDate startAge = LocalDate.now().minusYears(maxAge);
        LocalDate endAge = LocalDate.now().minusYears(minAge);
        return personRepository.findByBirthDateBetween(startAge, endAge)
                .stream()
                .map(person -> modelMapper.map(person, PersonDto.class))
                .toArray(PersonDto[]::new);
    }

    @Override
    public Iterable<CityPopulationDto> getCityPopulation() {
        return personRepository.getCityPopulation();
    }

}
