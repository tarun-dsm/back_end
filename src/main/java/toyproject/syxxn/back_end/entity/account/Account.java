package toyproject.syxxn.back_end.entity.account;

import lombok.*;
import toyproject.syxxn.back_end.entity.BaseIdEntity;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.review.Review;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email","nickname"})})
public class Account extends BaseIdEntity {

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
    private Integer age;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @NotNull
    @Column(columnDefinition = "bit(1)")
    private Boolean isExperienceRasingPet;

    private String experience;

    @NotNull
    private BigDecimal longitude;

    @NotNull
    private BigDecimal latitude;

    @NotNull
    @Column(columnDefinition = "bit(1)")
    private Boolean isLocationConfirm;

    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE)
    private List<Post> posts;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Application> applications;

    @OneToMany(mappedBy = "writer", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Review> writtenReview;

    @OneToMany(mappedBy = "target", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    public void updateLocation(BigDecimal longitude, BigDecimal latitude) {
        this.isLocationConfirm = true;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
