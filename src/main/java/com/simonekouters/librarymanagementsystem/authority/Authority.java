package com.simonekouters.librarymanagementsystem.authority;

import com.simonekouters.librarymanagementsystem.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity(name = "authorities")
@NoArgsConstructor
@AllArgsConstructor
public class Authority {
    @Id
    private String email;

    private String authority;
}
