package org.example.project302.deal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.chat.entity.ChatParticipation;
import org.example.project302.chat.entity.ChatParticipationId;
import org.example.project302.product.entity.Product;
import org.example.project302.user.entity.User;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationId implements Serializable {
    private User user;
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participation) && !(o instanceof ParticipationId)) return false;

        if (o instanceof Participation) {
            Participation part = (Participation) o;
            if (this.getUser().getUserId() == part.getUser().getUserId() && this.getProduct().getPdId() == part.getProduct().getPdId())
                return true;
        } else if (o instanceof Participation) {
            Participation partId = (Participation) o;
            if (this.getUser().getUserId() == partId.getUser().getUserId() && this.getProduct().getPdId() == partId.getProduct().getPdId())
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, product);
    }
}
