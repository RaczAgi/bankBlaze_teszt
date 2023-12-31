package hu.bankblaze.bankblaze_teszt.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "queue_number")
public class QueueNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int number;
    private Boolean toRetail = false;
    private Boolean toCorporate = false;
    private Boolean toTeller = false;
    private Boolean toPremium = false;
    private Boolean active = true;
}

