package com.bubla.model;





import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    private String id;


    @NonNull
    private String chatId;

    @NonNull
    private String senderId;

    @NonNull
    private String recipientId;
    private String senderName;
    private String getRecipientName;
    private String content;
    private Date timestamp;
    private MessageStatus status;
}
