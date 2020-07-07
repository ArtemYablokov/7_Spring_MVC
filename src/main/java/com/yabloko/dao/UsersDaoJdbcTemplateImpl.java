package com.yabloko.dao;

import com.yabloko.models.Car;
import com.yabloko.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.*;

@Component
public class UsersDaoJdbcTemplateImpl implements UsersDao {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UsersDaoJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    Map<Long, User> usersMap = null;

    private RowMapper<User> userRowMapper = (ResultSet resultSet, int i) -> {

        Long id = resultSet.getLong("id");

        if (!usersMap.containsKey(id)) {
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");

            User user = User.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .cars(new ArrayList<>())
                    .build();

            usersMap.put(id, user);

        }
        Car car = new Car(resultSet.getLong("car_id"),
                resultSet.getString("model"), usersMap.get(id));
        usersMap.get(id).getCars().add(car);
        return usersMap.get(id);
    };

    private RowMapper<User> userRowMapperWithOutCar = (ResultSet resultSet, int i) ->
            User.builder()
                    .firstName(resultSet.getString("first_name"))
                    .lastName(resultSet.getString("last_name"))
                    .build();


    private RowMapper<User> userRowMapperPassword = (ResultSet resultSet, int i) ->
            User.builder()
                    .firstName(resultSet.getString("first_name"))
                    .lastName(resultSet.getString("last_name"))
                    .password(resultSet.getString("password"))
                    .build();




    //language=SQL
    private final String SQL_SELECT_ALL_BY_FIRST_NAME =
            "SELECT u.*, c.id as car_id, c.model FROM apple_user u LEFT JOIN apple_car c " +
                    "ON u.id = c.owner_id WHERE first_name = ?";
    @Override
    public List<User> findAllByFirstName(String firstName) {
        usersMap = new HashMap<>();
        jdbcTemplate.query(SQL_SELECT_ALL_BY_FIRST_NAME, userRowMapper, firstName);

        return new ArrayList<>(usersMap.values());


    }


    private final String SQL_SELECT_BY_ID =
            "SELECT u.*, c.id as car_id, c.model FROM apple_user u LEFT JOIN apple_car c " +
                    "ON u.id = c.owner_id  WHERE u.id = ?";
    @Override
    public Optional<User> find(Long id) {

        usersMap = new HashMap<>();
        jdbcTemplate.query(SQL_SELECT_BY_ID, userRowMapper, id);

        if (usersMap.containsKey(id)) {
            return Optional.of(usersMap.get(id));
        }
        return Optional.empty();
    }

    //language=SQL
    private final String SQL_SELECT_ALL =
            "SELECT u.*, c.id as car_id, c.model FROM apple_user u LEFT JOIN apple_car c " +
                    "ON u.id = c.owner_id";
    @Override
    public List<User> findAll() {
        usersMap = new HashMap<>();

        jdbcTemplate.query(SQL_SELECT_ALL, userRowMapper);

        return new ArrayList<>(usersMap.values());
    }


    //language=SQL
    private final String SQL_SAVE_RAW_USER =
            "INSERT INTO apple_user (first_name, last_name) VALUES (:first_name, :last_name)";
    //language=SQL
    private final String SQL_SAVE_RAW_CAR =
            "INSERT INTO apple_car (owner_id, model) VALUES (:owner_id, :model)";
    //language=SQL
    private final String SQL_MAX_ID_CAR =
            "SELECT u.*, c.id as car_id, c.model FROM apple_user u LEFT JOIN apple_car c " +
            "ON u.id = c.owner_id ORDER BY id DESC LIMIT 1";


    public boolean saveRaw(String firstName, String lastName, String carModel) {

        Map<String, Object> map = new HashMap<>();
        map.put("first_name", firstName); // вместо вопросительного знака передаем ИМЕННОВАННЫЙ ПАРАМЕТР
        map.put("last_name", lastName);
        namedParameterJdbcTemplate.update(SQL_SAVE_RAW_USER, map);
        map.clear();

        List<User> users = jdbcTemplate.query(SQL_MAX_ID_CAR, userRowMapperWithOutCar);

        Long id = users.get(0).getId();

        map.put("owner_id", id); // вместо вопросительного знака передаем ИМЕННОВАННЫЙ ПАРАМЕТР
        map.put("model", carModel);
        namedParameterJdbcTemplate.update(SQL_SAVE_RAW_CAR, map);
        return true;
    }


    //language=SQL
    private final String SQL_SELECT_ALL_WITHOUt_CARS =
            "SELECT * FROM apple_user";
    public boolean isExist(String name, String password) {
        User temp = User.builder()
                .firstName(name)
                .password(password)
                .build();
        List<User> userList = jdbcTemplate.query(SQL_SELECT_ALL_WITHOUt_CARS, userRowMapperPassword);
        for (User user : userList) {
            if (user.equals(temp))
                return true;
        }

        return false;
    }

    @Override
    public void save(User model) {

    }

    @Override
    public void update(User model) {

    }

    @Override
    public void delete(Long id) {

    }
}