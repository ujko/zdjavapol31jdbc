package personSample.personDao;

import personSample.model.Person;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class PersonDaoImpl implements PersonDao {
    private static final String URL = "jdbc:mysql://localhost:3306/mysql?useTimezone=true&serverTimezone=UTC";
    private Connection connection;

    private void createConnection() {
        Properties prop = new Properties();
        prop.put("user", "root");
        prop.put("password", "example");
        try {
            connection = DriverManager.getConnection(URL, prop);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void createTable() {
        createConnection();

        String query = "create table person(person_id int, first_name varchar(55), last_name varchar(55), birth_date date)";
        String add1 = "insert into person(person_id, first_name, last_name, birth_date) values(2, 'Adrian', 'Kwiatkowski', '2001-01-15')";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            statement.executeUpdate(add1);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        closeConnection();
    }

    @Override
    public List<Person> getAll() {
        List<Person> persons = new ArrayList<>();
        createConnection();
        try (Statement statement = connection.createStatement()) {
            String query = "select * from person";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                persons.add(new Person(
                        rs.getInt("person_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("birth_date").toLocalDate()));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        closeConnection();
        return persons;
    }

    @Override
    public Person getById(int id) {
        createConnection();
        String query = "select * from person where person_id = ?";
        Person person = new Person(-1, "", "", LocalDate.now());
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                person = new Person(resultSet.getInt("person_id"), resultSet.getString("first_name"),
                        resultSet.getString("last_name"), resultSet.getDate("birth_date").toLocalDate());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        closeConnection();
        return person;
    }

    @Override
    public boolean addPerson(Person person) {
        //TODO: poprawić tą metodę tak żeby nie wymagała person_id
        createConnection();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String add = String.format("insert into person(person_id, first_name, last_name, birth_date) values(%d, '%s', '%s', '%s')"
                , person.getPersonId(), person.getFirstName(), person.getLastName(), person.getBirthDate().format(formatter));
        int result = 0;
        try {
            Statement statement = connection.createStatement();
            result = statement.executeUpdate(add);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        closeConnection();

        return result > 0;
    }

    @Override
    public boolean updatePerson(int personId, Person updatedPerson) {
        return false;
    }

    @Override
    public boolean deletePerson(int personId) {
        return false;
    }

//    @Override
//    public List<Person> getByFirstName(String firstName) {
//        return null;
//    }

    public List<Person> getByFirstName(String firstName) {
        return getAll()
                .stream()
                .filter(x -> x.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Person> getByLastName(String lastName) {
        return null;
    }

    @Override
    public List<Person> getByAgeBetween(int minAge, int maxAge) {
        return null;
    }
}
