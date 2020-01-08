package com.softserve.academy.event.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;




@NamedQuery(
        name = "findToken",
        query = "from VerificationToken v where v.token= :token"
)
@NamedQuery(
        name = "findUser",
        query = "from VerificationToken u where u.user= :user"
)
@Entity
@Getter
@Setter
@NoArgsConstructor
public class VerificationToken implements Serializable {

    private static final int EXPIRATION = 60 *24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expiryDate;

    public VerificationToken(final String token, final User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    private LocalDateTime calculateExpiryDate(int timeInMinutes) {
        LocalDateTime now = LocalDateTime.now();
        return now.plusMinutes(timeInMinutes);
    }

    public void updateToken(final String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }
}
