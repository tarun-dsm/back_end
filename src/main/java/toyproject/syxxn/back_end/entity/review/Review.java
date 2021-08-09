package toyproject.syxxn.back_end.entity.review;

import lombok.AllArgsConstructor;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public void update(BigDecimal grade, String comment) {
        this.grade = grade;
        this.comment = comment;
    }

}
