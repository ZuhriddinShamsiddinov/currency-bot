package model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Story {
    String original;
    String target;
    String userName;
    String date;
    double valuefrom;
    double valueto;
}
