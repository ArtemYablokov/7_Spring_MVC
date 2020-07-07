package com.yabloko.models;

import com.yabloko.forms.UserForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "apple_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER) // OOOOOO
    private List<Car> cars = new ArrayList<>();

    public static User from(UserForm form) {
        return User
                .builder()
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .password(form.getPassword())
                .cars(new ArrayList<>())
                .build();
    }
}