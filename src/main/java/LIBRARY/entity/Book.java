package LIBRARY.entity;

import org.hibernate.annotations.Type;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Not be empty")
    @Size(min=1,max=100,message = "Title should be between 1 and 100 characters")
    private String title;

    @NotEmpty(message = "Not be empty")
    @Size(min=2,max=100,message = "Author should be between 2 and 100 characters")
    @Pattern(regexp = "[A-ZА-Я][a-zа-я]+ [A-ZА-Я][a-zа-я]+",message = "Invalid Author")
    private String author;

    @Digits(integer=4, fraction=0, message = "No more than 4 digits")
    @NotNull(message = "Not be empty")
    @Min(value = 1900 , message = "Invalid Year")
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "owner_id",referencedColumnName = "id")
    private Person owner;

//    @Temporal(TemporalType.TIMESTAMP)
    @Type(type="org.hibernate.type.LocalDateTimeType")
//    @DateTimeFormat()
    private LocalDateTime time;

    @Transient
    private boolean overdue;

    public Book() {
    }

    public Book(String title, String author, Integer year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }
}
