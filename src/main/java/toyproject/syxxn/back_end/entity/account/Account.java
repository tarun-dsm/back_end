package toyproject.syxxn.back_end.entity.account;

import com.querydsl.core.annotations.QueryEntity;
import lombok.*;
import toyproject.syxxn.back_end.entity.BaseIdEntity;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.application.Application;
import toyproject.syxxn.back_end.entity.comment.Comment;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.report.Report;
import toyproject.syxxn.back_end.entity.review.Review;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@QueryEntity
public class Account extends BaseIdEntity {

    @Column(length = 45, nullable = false, unique = true)
    private String email;

    @Column(columnDefinition = "char(60)", nullable = false)
    private String password;

    @Column(length = 10, nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(length = 6, nullable = false)
    private Sex sex;

    @Column(columnDefinition = "bit(1)", nullable = false)
    private Boolean isExperienceRaisingPet;

    @Column(length = 100)
    private String experienceDescription;

    @Column(columnDefinition = "decimal(15,10)", nullable = false)
    private BigDecimal longitude;

    @Column(columnDefinition = "decimal(15,10)", nullable = false)
    private BigDecimal latitude;

    @Column(length = 10)
    private String administrationDivision;

    @Column(nullable = false)
    private Boolean isLocationConfirm;

    @Column(nullable = false)
    private Boolean isBlocked;

    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE)
    private List<Post> posts;

    @OneToMany(mappedBy = "applicant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Application> applications;

    @OneToMany(mappedBy = "writer", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Review> writtenReview;

    @OneToMany(mappedBy = "target", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    @OneToMany(mappedBy = "reporter", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Report> writtenReports;

    @OneToMany(mappedBy = "target", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Report> reports;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @Builder
    public Account(String email, String password, String nickname, Integer age, Sex sex, Boolean isExperienceRaisingPet, String experienceDescription) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
        this.sex = sex;
        this.isExperienceRaisingPet = isExperienceRaisingPet;
        this.experienceDescription = experienceDescription;
        this.longitude = BigDecimal.ZERO;
        this.latitude = BigDecimal.ZERO;
        this.isLocationConfirm = false;
        this.isBlocked = false;
    }

    public Account updateLocation(BigDecimal longitude, BigDecimal latitude, String administrationDivision) {
        this.isLocationConfirm = true;
        this.longitude = longitude;
        this.latitude = latitude;
        this.administrationDivision = administrationDivision;

        return this;
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

    public String getExperienceDescription() {
        return this.experienceDescription == null ? "" : this.getExperienceDescription();
    }

}
