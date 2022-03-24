package model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private String username;
    private String phone;
    private String chatId;
    private String lastName;
    private String firstName;
    private String language;

}