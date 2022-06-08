package com.nhnacademy.nhn_board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {

    Integer userNo;
    String userId;
    String userPw;
    Boolean checkAdmin;
}
