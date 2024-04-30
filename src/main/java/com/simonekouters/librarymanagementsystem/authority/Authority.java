package com.simonekouters.librarymanagementsystem.authority;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity(name = "authorities")
@NoArgsConstructor
@AllArgsConstructor
public class Authority {
    @Id
    private Long memberId;

    private String authority;
}
