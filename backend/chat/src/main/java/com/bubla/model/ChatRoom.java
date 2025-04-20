package com.bubla.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chat_room")
public class ChatRoom {
    @Id
    private String id;
    @NonNull
    private String chatId;
    @NonNull
    private String senderId;
    @NonNull
    private String recipientId;
}
