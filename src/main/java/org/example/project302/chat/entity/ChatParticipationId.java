package org.example.project302.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project302.user.entity.User;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatParticipationId implements Serializable {
    private Chatroom chatroom;
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatParticipation) && !(o instanceof ChatParticipationId)) return false;

        if (o instanceof ChatParticipation) {
            ChatParticipation cp = (ChatParticipation) o;
            if (this.getUser().getUserId() == cp.getUser().getUserId() && this.getChatroom().getChatId() == cp.getChatroom().getChatId())
                return true;
        } else if (o instanceof ChatParticipationId) {
            ChatParticipationId cpId = (ChatParticipationId) o;
            if (this.getUser().getUserId() == cpId.getUser().getUserId() && this.getChatroom().getChatId() == cpId.getChatroom().getChatId())
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, chatroom);
    }

}
