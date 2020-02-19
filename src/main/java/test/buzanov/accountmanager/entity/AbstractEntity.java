package test.buzanov.accountmanager.entity;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Абстрактная сущность.
 * @author Aleksey Buzanov
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
    @Id
    @NotNull
    private String id = UUID.randomUUID().toString();

    @NotNull
    @Column(updatable = false)
    private Date creation = new Date(System.currentTimeMillis());
    @NotNull
    private Date update = new Date(System.currentTimeMillis());

    private static final long serialVersionUID = 1L;
}
