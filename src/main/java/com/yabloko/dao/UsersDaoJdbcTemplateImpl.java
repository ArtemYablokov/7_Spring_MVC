package com.yabloko.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import com.yabloko.models.Car;
import com.yabloko.models.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.*;

/**
 * 04.04.2018
 * UsersDaoJdbcTemplateImpl
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
@Component
public class UsersDaoJdbcTemplateImpl implements UsersDao {

    private JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private Map<Long, User> usersMap = new HashMap<>();

    //language=SQL
    private final String SQL_SELECT_USER_WITH_CARS =
            "SELECT users.*, cars.id as car_id, cars.model " +
                    "FROM users LEFT JOIN cars ON users.id = cars.owner_id WHERE users.id = ?";

    private final String SQL_SELECT_USERS_WITH_CARS =
            "SELECT users.*, cars.id as car_id, cars.model " +
                    "FROM users LEFT JOIN cars ON users.id = cars.owner_id ";

    //language=SQL
    private final String SQL_INSERT_USER = "" +
            "INSERT INTO users(first_name, last_name) VALUES (:firstName, :lastName)";

    @Autowired
    public UsersDaoJdbcTemplateImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private RowMapper<User> userRowMapperWithoutCars = (resultSet, i) ->
            User.builder()
            .id(resultSet.getLong("id"))
            .firstName(resultSet.getString("first_name"))
            .lastName(resultSet.getString("last_name"))
            .build();


    private RowMapper<User> userRowMapperWithCars = (ResultSet resultSet, int i) -> {
       Long id = resultSet.getLong("id");

       if (!usersMap.containsKey(id)) {
           String firstName = resultSet.getString("first_name");
           String lastName = resultSet.getString("last_name");
           User user = new User(id, firstName, lastName, new ArrayList<>());
           usersMap.put(id, user);
       }

       Car car = new Car(resultSet.getLong("car_id"),
               resultSet.getString("model"), usersMap.get(id));

       usersMap.get(id).getCars().add(car);

       return usersMap.get(id);
    };

    //language=SQL
    private final String SQL_SELECT_ALL_BY_FIRST_NAME = "SELECT * FROM users WHERE first_name = ?";

    private final String SQL_SELECT_ALL_BY_FIRST_NAME_WITH_CARS = "SELECT u.*, c.id AS car_id, c.model " +
            "FROM users AS u JOIN cars AS c ON u.id = c.owner_id WHERE first_name = ?";
    @Override
    public List<User> findAllByFirstName(String firstName) {
        return template.query(SQL_SELECT_ALL_BY_FIRST_NAME_WITH_CARS, userRowMapperWithCars, firstName);
    }


    private final String SQL_SELECT_BY_ID = "SELECT * FROM users WHERE id = :id";
    @Override
    public Optional<User> find(Long id) {

        Map<String, Object> paramMap = new HashMap<>();

        // вместо вопросительного знака передаем ИМЕННОВАННЫЙ ПАРАМЕТР
        paramMap.put("id", id);
        List<User> result = namedParameterJdbcTemplate.query(SQL_SELECT_BY_ID, paramMap, userRowMapperWithoutCars);

        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public void save(User model) {
        Map<String, Object> paramMap = new HashMap<>();

        // вместо вопросительного знака передаем ИМЕННОВАННЫЙ ПАРАМЕТР
        paramMap.put("firstName", model.getFirstName());
        paramMap.put("lastName", model.getLastName());

        // !!! not QUERY !!!
        namedParameterJdbcTemplate.update(SQL_INSERT_USER, paramMap);
    }

    @Override
    public List<User> findAll() {
        List<User> result =  template.query(SQL_SELECT_USERS_WITH_CARS, userRowMapperWithCars);
        usersMap.clear();
        return result;
    }


    @Override
    public void update(User model) {

    }
    @Override
    public void delete(Long id) {

    }
}
