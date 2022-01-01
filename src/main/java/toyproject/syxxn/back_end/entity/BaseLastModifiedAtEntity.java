package toyproject.syxxn.back_end.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseLastModifiedAtEntity extends BaseIdEntity{

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd`T`hh:mm:ss")
    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    public String getLastModifiedAtToString() {
        if (this.lastModifiedAt != null)
            return this.lastModifiedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        else
            return null;
    }

}
