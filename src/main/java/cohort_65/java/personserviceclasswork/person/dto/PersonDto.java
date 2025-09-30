package cohort_65.java.personserviceclasswork.person.dto;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;




@Getter @Setter
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY, // читаем уже существующее поле
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EmployeeDto.class, name = "EMPLOYEE"),
        @JsonSubTypes.Type(value = ChildDto.class,    name = "CHILD"),
        @JsonSubTypes.Type(value = PersonDto.class,   name = "PERSON")
})
public class PersonDto {
    String type;
    Integer id ;
    String name;
    LocalDate birthDate;
    AddressDto address;
}
