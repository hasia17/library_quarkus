package rs.internal.DTOs;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorCreateUpdateDTO {

    @NotNull
    private String name;

    @NotNull
    private String lastName;

    private Integer age;


    public void setAge(Integer age) throws InvalidArgumentException{
        this.validateAge(age);
        this.age = age;
    }

    private void validateAge(Integer age) throws InvalidArgumentException{
        if(age < 1 || age > 120){
            throw new InvalidArgumentException("Age must be between 1 and 120!");
        }
    }

    public class InvalidArgumentException extends Exception {
        private InvalidArgumentException(String message){
            super(message);
        }
    }
}
