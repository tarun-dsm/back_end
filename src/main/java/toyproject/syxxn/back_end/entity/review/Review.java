package toyproject.syxxn.back_end.entity.review;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.entity.BaseLastModifiedAtEntity;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.application.Application;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseLastModifiedAtEntity {

    @Column(columnDefinition = "decimal(2,1)", nullable = false)
    private BigDecimal grade;

    @Column(nullable = false)
    private String review;

    private Boolean isUpdated;

    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false)
    private Account writer;

    @ManyToOne
    @JoinColumn(name = "target_id", nullable = false)
    private Account target;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Builder
    public Review(Application application, Account target, Account writer, BigDecimal grade, String review) {
        this.application = application;
        this.target = target;
        this.writer = writer;
        this.grade = grade;
        this.review = review;
        this.isUpdated = false;
    }

    public void update(BigDecimal grade, String review) {
        this.grade = grade;
        this.review = review;
        this.isUpdated = true;
    }

}
