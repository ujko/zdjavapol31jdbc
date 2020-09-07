package personSample;

import personSample.model.Person;
import personSample.personDao.PersonDao;
import personSample.personDao.PersonDaoImpl;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        PersonDao dao = new PersonDaoImpl();
        List<Person> people = dao.getAll();
        people.forEach(System.out::println);
//        new PersonDaoImpl().createTable();
    }
}
