package toyproject.syxxn.back_end.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseCreatedAtEntity extends BaseIdEntity{

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd`T`hh:mm:ss")
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public String getCreatedAtToString() {

        return String.valueOf(this.createdAt);
    }

    public String getCreatedAtToLocalDate() {
        return String.valueOf(this.createdAt.plusHours(9));
    }

}
