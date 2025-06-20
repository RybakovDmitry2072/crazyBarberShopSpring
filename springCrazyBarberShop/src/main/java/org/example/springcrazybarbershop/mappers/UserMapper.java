package org.example.springcrazybarbershop.mappers;

import org.example.springcrazybarbershop.dto.UserDto;
import org.example.springcrazybarbershop.models.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {FileMapper.class})
public interface UserMapper {

    @Mapping(target = "image", source = "image")
    UserDto toUserDto(User user);

    @Mapping(target = "image", source = "image")
    User toUser(UserDto userDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserDto dto, @MappingTarget User entity);
}
