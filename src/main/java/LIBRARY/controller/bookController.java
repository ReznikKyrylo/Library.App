package LIBRARY.controller;

import LIBRARY.entity.Book;
import LIBRARY.entity.Person;
import LIBRARY.service.bookService;
import LIBRARY.service.personService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/books")
public class bookController {

    private final bookService bookService;
    private final personService personService;

    @Autowired
    public bookController(bookService bookService, personService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }
//    @GetMapping()
//    public String all(Model model,
//                      @RequestParam(value = "sort_by_year" , required = false) boolean sort,
//                      @RequestParam(value = "page" , required = false) Integer page,
//                      @RequestParam(value = "books_per_page" , required = false) Integer booksPerPage){
//        if(page==null || booksPerPage==null)
//            model.addAttribute("books",bookService.findAll(sort));
//        else
//        model.addAttribute("books",bookService.findAll(page,booksPerPage,sort));
//            return "books";
//    }

    @GetMapping()
    public String all(Model model,
                      @RequestParam(value = "sort_by_year" , required = false,defaultValue = "false") boolean sort,
                      @RequestParam(value = "page" , required = false ,defaultValue = "0") Integer page,
                      @RequestParam(value = "books_per_page" , required = false,defaultValue = "3") Integer booksPerPage){

        if(page==null || booksPerPage==null)
            model.addAttribute("books",bookService.findAll(sort));
        else{
            Page<Book> pageBook = bookService.findAllByPage(page,booksPerPage,sort);
            model.addAttribute("books",pageBook.getContent());
            model.addAttribute("currentPage",page);
            model.addAttribute("totalPage",pageBook.getTotalPages()-1);
            model.addAttribute("sort",sort);
        }

        return "books";
    }


    @GetMapping("/new")
    public String create(@ModelAttribute("book") Book book) {
        return "book-create";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("book") @Validated Book book,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "book-create";
        }
        bookService.create(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,Model model,@ModelAttribute("person") Person person){
        model.addAttribute("book",bookService.findById(id));
        Person bookOwner = bookService.getBookOwner(id);
        if(bookOwner!=null){
            model.addAttribute("owner",bookOwner);
        } else model.addAttribute("people",personService.findAll(false));
        return "book-show";
    }



    @GetMapping("/{id}/release")
    public String release(@PathVariable("id") Integer id) {
        bookService.release(id);
        return"redirect:/books/"+id;
    }

    @PostMapping("/{id}/assign")
    public String assign(@PathVariable("id") Integer id, @ModelAttribute("person") Person person) {
        bookService.assign(id, person);
        return "redirect:/books/"+id;
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id,
                       @ModelAttribute("book") Book book,
                       Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "book-edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id,
                       @ModelAttribute("book") @Validated Book book,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            return "book-edit";
        }
        bookService.update(id,book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search() {
        return "search-books";
    }

    @PostMapping("/search")
    public String search(@RequestParam(name = "query") String query,Model model) {
        model.addAttribute("books",bookService.findByTitleStartingWith(query)) ;
        return "search-books";
    }
}
