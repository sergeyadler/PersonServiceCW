package cohort_65.java.personserviceclasswork.person.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(of ="id")
public class Person {
    @Id
    Integer id;
    @Setter
    String name;
    LocalDate birthDate;
    @Setter
    @Embedded
    Address address;
}
