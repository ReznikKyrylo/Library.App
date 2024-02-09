package LIBRARY.validator;

import LIBRARY.entity.Person;
import LIBRARY.service.personService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class personValidator implements Validator {

    private final personService personService;

    @Autowired
    public personValidator(personService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person)target;
        if (personService.findByFullName(person.getFullName()).isPresent())
            errors.rejectValue("fullName","","Person with that name already exist");
    }
}
