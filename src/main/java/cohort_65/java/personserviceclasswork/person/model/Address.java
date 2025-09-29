package cohort_65.java.personserviceclasswork.person.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Embeddable

public class Address {
    String city;
    String street;
    Integer building;

}