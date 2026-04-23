package cl.banking.users.mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import cl.banking.users.api.model.PhoneRequest;
import cl.banking.users.api.model.UserRequest;
import cl.banking.users.api.model.UserResponse;
import cl.banking.users.domain.Phone;
import cl.banking.users.domain.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	/**
     * De Request (DTO) a Entidad
     */
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "name", source = "name")
	@Mapping(target = "email", source = "email")
	@Mapping(target = "password", source = "password")
	@Mapping(target = "phones", source = "phones")
	User toEntity(UserRequest userRequest);
	
	/**
     * De Entidad a Response (DTO)
     */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phones", source = "phones")
    @Mapping(target = "created", source = "createdAt", qualifiedByName = "toOffsetDateTime")
    @Mapping(target = "modified", source = "modifiedAt", qualifiedByName = "toOffsetDateTime")
    @Mapping(target = "lastLogin", source = "lastLogin", qualifiedByName = "toOffsetDateTime")
    @Mapping(target = "token", source = "token")
    @Mapping(target = "isactive", source = "isActive")
    UserResponse toResponse(User user);

	/**
     * Convierte PhoneRequest (DTO) a Phone (Entity)
     * Maneja el typo: "contrycode" en JSON → "countryCode" en Entity
     */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "number", source = "number")
    @Mapping(target = "cityCode", source = "citycode")
	@Mapping(target = "countryCode", source = "contrycode")
	Phone toEntity(PhoneRequest phoneRequest);
	
	/**
     * Convierte Phone (Entity) a PhoneRequest (DTO)
     * Maneja el typo: "countryCode" en Entity → "contrycode" en JSON
     */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "number", source = "number")
    @Mapping(target = "citycode", source = "cityCode")
	@Mapping(target = "contrycode", source = "countryCode")
	PhoneRequest toDto(Phone phone);
	
	/**
     * Convierte lista de PhoneRequest a lista de Phone
     */
    List<Phone> toEntityList(List<PhoneRequest> requests);

    /**
     * Convierte lista de Phone a lista de PhoneRequest
     */
    List<PhoneRequest> toDtoList(List<Phone> phones);
	
	@Named("toOffsetDateTime")
    default OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atOffset(ZoneOffset.UTC);
    }
	
}