package toyproject.syxxn.back_end.entity.account;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.review.Review;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    private Long id;

    @NotNull
    @Column(columnDefinition = "varchar(45)")
    private String email;

    @NotNull
    @Column(columnDefinition = "char(60)")
    private String password;

    @NotNull
    @Column(columnDefinition = "varchar(10)")
    private String nickname;

    @NotNull
    private Integer age;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @NotNull
    private Boolean isExperienceRasingPet;

    @NotNull
    @Column(columnDefinition = "varchar(100)")
    private String experience;

    @NotNull
    private String address;

    @NotNull
    private Boolean isLocationConfirm;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Review> reviews;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Post> posts;

}
