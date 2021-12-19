package toyproject.syxxn.back_end.entity.report;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.entity.BaseCreatedAtEntity;
import toyproject.syxxn.back_end.entity.account.Account;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Report extends BaseCreatedAtEntity {

    private String reason;

    @ManyToOne
    @JoinColumn(name = "target_id", nullable = false)
    private Account target;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private Account reporter;

    @Builder
    public Report(String reason, Account target, Account reporter) {
        this.reason = reason;
        this.target = target;
        this.reporter = reporter;
    }

}
