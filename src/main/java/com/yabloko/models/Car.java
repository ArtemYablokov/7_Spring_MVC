package com.yabloko.models;

import lombok.*;

/**
 * 04.04.2018
 * Car
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */


@Data
@NoArgsConstructor
@AllArgsConstructor

@Builder

@ToString(exclude = "owner")
public class Car {
    private Long id;
    private String model;
    private User owner;


}


