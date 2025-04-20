package com.example.volunteer.model;

import com.example.volunteer.model.auth.User; 
import jakarta.persistence.*; 
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "volunteers") 
public class Volunteer {

    @Id
    @Column(name = "user_id") 
    private String id; 


    @OneToOne(fetch = FetchType.LAZY) 
    @MapsId 
    @JoinColumn(name = "user_id")
    private User user; 

    private String middleName;

    private Long organizationId;

    private String skills;
    private Double rating;

    public Volunteer(User user) {
        this.id = user.getId();
        this.user = user;
    }
}