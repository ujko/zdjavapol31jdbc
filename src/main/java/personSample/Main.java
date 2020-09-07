package personSample;

import personSample.model.Person;
import personSample.personDao.PersonDao;
import personSample.personDao.PersonDaoImpl;

import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        PersonDao dao = new PersonDaoImpl();
        Person newPerson = new Person(4, "Krzysztof", "Jarzyna", LocalDate.of(1996, 11, 03));

        dao.addPerson(newPerson);

        List<Person> people = dao.getAll();
        people.forEach(System.out::println);
//        new PersonDaoImpl().createTable();
    }
}
