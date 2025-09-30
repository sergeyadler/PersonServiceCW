package cohort_65.java.personserviceclasswork.person.dao;

import cohort_65.java.personserviceclasswork.person.dto.CityPopulationDto;
import cohort_65.java.personserviceclasswork.person.model.Child;
import cohort_65.java.personserviceclasswork.person.model.Employee;
import cohort_65.java.personserviceclasswork.person.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;


public interface PersonRepository extends JpaRepository<Person, Integer> {

    List<Person> findByAddressCityIgnoreCase(String city);

    List<Person> findByNameIgnoreCase(String name);

    List<Person> findByBirthDateBetween(LocalDate startAge, LocalDate endAge);

    @Query("SELECT new cohort_65.java.personserviceclasswork.person.dto.CityPopulationDto(p.address.city, count(p))" +
            "FROM Person p  " +
            "GROUP BY p.address.city ")
    List<CityPopulationDto> getCityPopulation();


    @Query("SELECT e FROM Employee e WHERE e.salary BETWEEN :min AND :max")
    List<Employee> findEmployeesWithSalaryBetween(Integer min, Integer max);

    @Query(value = "SELECT c FROM Child c")
    List<Child>getAllChildren();

}
