package com.geovannycode.nisum.domain.mapper;

import com.geovannycode.nisum.domain.dto.CreateUserRequest;
import com.geovannycode.nisum.domain.dto.PhoneDTO;
import com.geovannycode.nisum.domain.dto.UserDTO;
import com.geovannycode.nisum.domain.entities.PhoneEntity;
import com.geovannycode.nisum.domain.entities.UserEntity;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserDTO convertToDTO(UserEntity user) {
        if (user == null) {
            return null;
        }

        Set<PhoneDTO> phoneItems = user.getPhones().stream()
                .map(phone -> new PhoneDTO(phone.getNumber(), phone.getCityCode(), phone.getCountryCode()))
                .collect(Collectors.toSet());

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                phoneItems,
                user.getToken(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastLogin(),
                user.isActive());
    }

    public static UserEntity convertToEntity(CreateUserRequest request) {
        UserEntity newUser = new UserEntity();
        newUser.setName(request.name());
        newUser.setEmail(request.email());
        newUser.setPassword(request.password());
        Set<PhoneEntity> phones = new HashSet<>();
        for (PhoneDTO phone : request.phones()) {
            PhoneEntity phoneItem = new PhoneEntity();
            phoneItem.setNumber(phone.number());
            phoneItem.setCityCode(phone.cityCode());
            phoneItem.setCountryCode(phone.countryCode());
            phoneItem.setUser(newUser);
            phones.add(phoneItem);
        }
        newUser.setPhones(phones);
        LocalDateTime now = LocalDateTime.now();
        newUser.setCreatedAt(now);
        newUser.setLastLogin(now);
        newUser.setActive(true);
        newUser.setRole(request.role());
        return newUser;
    }
}
