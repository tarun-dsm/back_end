package toyproject.syxxn.back_end.entity.account;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email","nickname"})
})
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(45)", nullable = false)
    private String email;

    @Column(columnDefinition = "char(60)", nullable = false)
    private String password;

    @Column(columnDefinition = "varchar(10)", nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Sex sex;

    @Column(nullable = false)
    private Boolean isExperienceRasingPet;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String experience;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Boolean isLocationConfirm;

}
