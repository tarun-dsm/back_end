package toyproject.syxxn.back_end.entity.account;

import com.querydsl.core.annotations.QueryEntity;
import lombok.*;
import toyproject.syxxn.back_end.entity.BaseIdEntity;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.review.Review;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@QueryEntity
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
    private Boolean isExperienceRaisingPet;

    @Column(length = 100)
    private String experience;

    @NotNull
    private BigDecimal longitude;

    @NotNull
    private BigDecimal latitude;

    @Column(length = 10)
    private String administrationDivision;

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

    @Builder
    public Account(String email, String password, String nickname, Integer age, Sex sex, Boolean isExperienceRaisingPet, String experience, Boolean isLocationConfirm, BigDecimal latitude, BigDecimal longitude) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
        this.sex = sex;
        this.isExperienceRaisingPet = isExperienceRaisingPet;
        this.experience = experience;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isLocationConfirm = isLocationConfirm;
    }

    public void updateLocation(BigDecimal longitude, BigDecimal latitude, String administrationDivision) {
        this.isLocationConfirm = true;
        this.longitude = longitude;
        this.latitude = latitude;
        this.administrationDivision = administrationDivision;
    }

    public String getRating() {
        Double avgGrade = getAvg().doubleValue();

        if (avgGrade.compareTo(5.0) >= 0) {
            return "굉장히 엄청난";
        } else if (avgGrade.compareTo(4.0) >= 0) {
            return "능숙한";
        } else if (avgGrade.compareTo(3.0) >= 0) {
            return "평범한";
        } else if (avgGrade.compareTo(2.0) >= 0) {
            return "우왕좌왕";
        } else if(avgGrade.compareTo(1.0) >= 0) {
            return "우당탕탕";
        } else {
            return "처음처럼";
        }
    }

    public BigDecimal getAvg() {
        if (this.reviews.size() == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal sumData = BigDecimal.ZERO;
        for (int i = 0; i < this.reviews.size(); i++){
            sumData = sumData.add(reviews.get(i).getGrade());
        }
        return sumData.divide(BigDecimal.valueOf(this.reviews.size()), MathContext.DECIMAL64);
    }

}
