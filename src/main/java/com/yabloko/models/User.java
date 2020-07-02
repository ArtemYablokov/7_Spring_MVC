package com.yabloko.models;

import com.yabloko.forms.UserForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 04.04.2018
 * User
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Car> cars;


    public static User from(UserForm form) {
        return User.builder()
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .build();
    }

}
