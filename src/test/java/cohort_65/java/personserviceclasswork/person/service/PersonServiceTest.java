package cohort_65.java.personserviceclasswork.person.service;

import cohort_65.java.personserviceclasswork.person.dao.PersonRepository;
import cohort_65.java.personserviceclasswork.person.dto.*;
import cohort_65.java.personserviceclasswork.person.model.Address;
import cohort_65.java.personserviceclasswork.person.model.Child;
import cohort_65.java.personserviceclasswork.person.model.Employee;
import cohort_65.java.personserviceclasswork.person.model.Person;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PersonServiceTest {

    @Autowired
    PersonService personService;

    @Autowired
    ModelMapper modelMapper;

    @MockitoBean
    PersonRepository personRepository;

    // ---------- helpers ----------
    private Person person(Integer id) {
        return new Person(id, "Alice", LocalDate.now().minusYears(30),
                new Address("Berlin","Kant",10));
    }
    private Child child(Integer id) {
        return new Child(id, "Bobby", LocalDate.now().minusYears(8),
                new Address("Berlin","Kant",10), "Kita #1");
    }
    private Employee employee(Integer id, int salary) {
        return new Employee(id, "Carol", LocalDate.now().minusYears(25),
                new Address("Munich","Ring",7), "Apple", salary);
    }
    private PersonDto personDto(Integer id) {
        PersonDto dto = new PersonDto();
        return modelMapper.map(person(id), PersonDto.class);
    }
    private AddressDto addressDto(String city, String street, int building) {
        AddressDto dto = new AddressDto();
        // ModelMapper STRICT с private доступом — поля проставятся через рефлексию
        modelMapper.map(new Address(city, street, building), dto);
        return dto;
    }

    // ---------- tests ----------

    @Test
    void addPerson() {
        // given
        PersonDto dto = personDto(100);
        when(personRepository.save(any(Person.class))).thenAnswer(inv -> inv.getArgument(0)); // вернет тот же самый объект который передали в метод

        // when
        boolean ok = personService.addPerson(dto);

        // then
        assertTrue(ok);
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void findPersonById() {
        // given
        when(personRepository.findById(101)).thenReturn(Optional.of(person(101)));

        // when
        PersonDto out = personService.findPersonById(101);

        // then
        assertNotNull(out);
        assertEquals(101, out.getId());
        assertEquals("Alice", out.getName());
    }

    @Test
    void deletePersonById() {
        // given
        Person p = person(102);
        when(personRepository.findById(102)).thenReturn(Optional.of(p));

        // when
        PersonDto deleted = personService.deletePersonById(102);

        // then
        assertNotNull(deleted);
        assertEquals(102, deleted.getId());
        verify(personRepository, times(1)).delete(p);
    }

    @Test
    void updatePersonName() {
        // given
        Person p = person(103);
        when(personRepository.findById(103)).thenReturn(Optional.of(p));
        when(personRepository.save(any(Person.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        PersonDto updated = personService.updatePersonName(103, "NewName");

        // then
        assertNotNull(updated);
        assertEquals("NewName", updated.getName());
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void updatePersonAddress() {
        // given
        Person p = person(104);
        when(personRepository.findById(104)).thenReturn(Optional.of(p));
        when(personRepository.save(any(Person.class))).thenAnswer(inv -> inv.getArgument(0));

        AddressDto addr = addressDto("Hamburg", "Haupt", 77);

        // when
        PersonDto updated = personService.updatePersonAddress(104, addr);

        // then
        assertNotNull(updated);
        assertEquals("Hamburg", updated.getAddress().getCity());
        assertEquals("Haupt", updated.getAddress().getStreet());
        assertEquals(77, updated.getAddress().getBuilding());
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void findPersonByCity() {
        // given
        when(personRepository.findByAddressCityIgnoreCase("Berlin"))
                .thenReturn(List.of(person(201), child(202), employee(203, 5000)));

        // when
        PersonDto[] res = personService.findPersonByCity("Berlin");

        // then
        assertNotNull(res);
        assertEquals(3, res.length);
        // проверим полиморфный маппинг
        assertTrue(res[1] instanceof ChildDto || res[2] instanceof EmployeeDto);
    }

    @Test
    void findPersonByName() {
        // Репозиторий в файле обрезан, но обычно это:
        // List<Person> findByNameIgnoreCase(String name);
        when(personRepository.findByNameIgnoreCase("Alice"))
                .thenReturn(List.of(person(301), person(302)));

        PersonDto[] res = personService.findPersonByName("Alice");

        assertNotNull(res);
        assertEquals(2, res.length);
        assertEquals("Alice", res[0].getName());
    }

    @Test
    void findPersonsBetweenAge() {
        // Обычно сервис конвертит возраст в диапазон birthDate (now - to ... now - from)
        // Мокаем репозиторий по дате рождения:
        // List<Person> findByBirthDateBetween(LocalDate from, LocalDate to);
        when(personRepository.findByBirthDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(person(401), person(402)));

        PersonDto[] res = personService.findPersonsBetweenAge(20, 30);

        assertNotNull(res);
        assertEquals(2, res.length);
        verify(personRepository, times(1))
                .findByBirthDateBetween(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getCityPopulation() {
        when(personRepository.getCityPopulation())
                .thenReturn(List.of(
                        new CityPopulationDto("Berlin", 3L),
                        new CityPopulationDto("Munich", 1L)
                ));

        Iterable<CityPopulationDto> it = personService.getCityPopulation();
        assertNotNull(it);
        assertEquals(2, ((List<?>) it).size());
    }

    @Test
    void findEmployeeBySalary() {
        when(personRepository.findEmployeesWithSalaryBetween(4000, 9000))
                .thenReturn(List.of(employee(501, 5000), employee(502, 8000)));

        Iterable<EmployeeDto> it = personService.findEmployeeBySalary(4000, 9000);

        assertNotNull(it);
        List<EmployeeDto> list = (List<EmployeeDto>) it;
        assertEquals(2, list.size());
        assertTrue(list.stream().allMatch(e -> e.getSalary() >= 4000 && e.getSalary() <= 9000));
    }

    @Test
    void findAllChildren() {
        when(personRepository.getAllChildren())
                .thenReturn(List.of(child(601), child(602)));

        Iterable<ChildDto> it = personService.findAllChildren();

        assertNotNull(it);
        List<ChildDto> list = (List<ChildDto>) it;
        assertEquals(2, list.size());
        assertTrue(list.stream().allMatch(c -> c.getKindergarten() != null));
    }

    @Test
    void run() throws Exception {
        // В твоём CommandLineRunner обычно seed-логика: если пусто — засеять.
        when(personRepository.count()).thenReturn(0L);
        when(personRepository.save(any(Person.class))).thenAnswer(inv -> inv.getArgument(0));

        personService.run();

        verify(personRepository, atLeast(1)).save(any(Person.class));
    }
}
