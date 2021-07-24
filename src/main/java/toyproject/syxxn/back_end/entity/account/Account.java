package toyproject.syxxn.back_end.entity.account;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.application.Application;
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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email","nickname"})})
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(length = 45)
    private String email;

    @NotNull
    @Column(columnDefinition = "char(60)")
    private String password;

    @NotNull
    @Column(length = 10)
    private String nickname;

    @NotNull
    @Column(columnDefinition = "int(3)")
    private Integer age;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @NotNull
    @Column(columnDefinition = "bit(1)")
    private Boolean isExperienceRasingPet;

    @Column(length = 100)
    private String experience;

    @NotNull
    private String address;

    @NotNull
    @Column(columnDefinition = "bit(1)")
    private Boolean isLocationConfirm;

    @OneToMany(mappedBy = "account")
    @JsonManagedReference
    private List<Post> posts;

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Application application;

    @OneToMany(mappedBy = "writer", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Review> writtenReview;

    @OneToMany(mappedBy = "target", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Review> reviews;

}
