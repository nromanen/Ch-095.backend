package com.softserve.academy.event.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;



@NamedQueries(
        {
                @NamedQuery(
                        name = "findToken",
                        query = "from VerificationToken v where v.token= :token"
                ),
                @NamedQuery(
                        name = "findUser",
                        query = "from VerificationToken u where u.user= :user"
                )
        }
)

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VerificationToken implements Serializable {
    private static final int EXPIRATION = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    // todo change to local date!
    private Date expiryDate;

    public VerificationToken(final String token) {
        super();
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public VerificationToken(final String token, final User user) {
        super();
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    private Date calculateExpiryDate(int timeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTimeInMillis()));
        calendar.add(Calendar.MINUTE, timeInMinutes);
        return new Date(calendar.getTimeInMillis());
    }

    public void updateToken(final String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }
}
