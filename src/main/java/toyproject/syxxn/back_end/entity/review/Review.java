package toyproject.syxxn.back_end.entity.review;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.entity.BaseCreatedAtEntity;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.application.Application;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseCreatedAtEntity {

    @NotNull
    private BigDecimal grade;

    @NotNull
    private String comment;

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
    public Review(Application application, Account target, Account writer, BigDecimal grade, String comment) {
        this.application = application;
        this.target = target;
        this.writer = writer;
        this.grade = grade;
        this.comment = comment;
    }

    public void update(BigDecimal grade, String comment) {
        this.grade = grade;
        this.comment = comment;
    }

}
