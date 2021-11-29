package toyproject.syxxn.back_end.entity.application;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import toyproject.syxxn.back_end.entity.account.Account;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static toyproject.syxxn.back_end.entity.application.QApplication.application;
import static toyproject.syxxn.back_end.entity.account.QAccount.account;
import static toyproject.syxxn.back_end.entity.post.QPost.post;

@RequiredArgsConstructor
@Repository
public class ApplicationCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Boolean existsNotEndApplication(Account user) {
        return jpaQueryFactory.selectFrom(application)
                .innerJoin(application.post, post)
                .innerJoin(application.applicant, account)
                .where(account.eq(user))
                .where(post.isApplicationEnd.eq(false)
                    .or(application.isAccepted.eq(true)))
                .where(application.createdAt.before(LocalDateTime.now())
                         .and(post.applicationEndDate.after(LocalDate.now())))
                .fetch().size() > 0;
    }

}
