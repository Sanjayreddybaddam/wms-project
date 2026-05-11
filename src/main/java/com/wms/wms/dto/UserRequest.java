package com.wms.wms.dto;

import com.wms.wms.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequest {
	
    private String username;
    private String password;
    private Role role;
}
