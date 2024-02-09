package LIBRARY.controller;

import LIBRARY.entity.Person;
import LIBRARY.service.*;
import LIBRARY.validator.personValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class peopleController {

    private final personService personService;
    private final personValidator personValidator;
    @Autowired
    public peopleController(personService personService,
                            personValidator personValidator) {
        this.personService = personService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String all(Model model,
                      @RequestParam(value = "sort_by_name",required = false ,defaultValue = "false") boolean sort,
                      @RequestParam(value = "page",required = false,defaultValue = "0") Integer page,
                      @RequestParam(value = "people_per_page" , required = false ,defaultValue = "3") Integer peoplePerPage) {
        if (page == null || peoplePerPage == null)
            model.addAttribute("people", personService.findAll(sort));
        else {
            Page<Person> pagePerson = personService.findAllByPage(page, peoplePerPage, sort);
            model.addAttribute("people", pagePerson.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPage", pagePerson.getTotalPages() - 1);
            model.addAttribute("sort", sort);
        }
        return "people";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("person",personService.findById(id));
        model.addAttribute("books",personService.getBooksByPersonId(id));
        return "person-show";
    }

    @GetMapping("/new")
    public String create(@ModelAttribute("person")Person person){
        return "person-create";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("person") @Validated Person person, BindingResult bindingResult , Model model) {

        personValidator.validate(person,bindingResult);
        if (bindingResult.hasErrors()) {
            return "person-create";
        }
        personService.create(person);
    return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id,@ModelAttribute("person") Person person,Model model){
        model.addAttribute("person",personService.findById(person.getId()));
        return "person-edit";
    }
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id,
                       @ModelAttribute("person") @Validated Person person,
                       BindingResult bindingResult,
                       Model model){
        if(bindingResult.hasErrors()){
            return "person-edit";
        }
        personService.update(person,id);
        return "redirect:/people";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id){
        personService.delete(id);
        return "redirect:/people";
    }

    @GetMapping("/search")
    public String search() {
        return "people-search";
    }

    @PostMapping("/search")
    public String search(@RequestParam(name = "query") String query,Model model) {
        model.addAttribute("people",personService.findByTitleStartingWith(query)) ;
        return "people-search";
    }
}
