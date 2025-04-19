package com.bubla.model;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ChatRoom")
public class ChatRoom {
    @Id
    private String id;
    private String chatId;
    private String senderid;
    private String recepientId;
}
