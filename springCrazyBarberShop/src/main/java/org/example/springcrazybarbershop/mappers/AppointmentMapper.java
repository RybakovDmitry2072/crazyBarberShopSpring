package org.example.springcrazybarbershop.mappers;

import org.example.springcrazybarbershop.dto.AppointmentDto;
import org.example.springcrazybarbershop.models.Appointment;
import org.example.springcrazybarbershop.models.AppointmentStatus;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {TimeSlotMapper.class, EmployeeMapper.class})
public interface AppointmentMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "timeSlotDto", source = "timeSlot")
    @Mapping(target = "employeeDto", source = "employee")
    @Mapping(target = "haircutCategory", source = "haircutCategory")
    @Mapping(target = "status", source = "appointmentStatus", qualifiedByName = "appointmentStatusToString")
    AppointmentDto toAppointmentDto(Appointment appointment);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "timeSlot", source = "timeSlotDto")
    @Mapping(target = "employee", source = "employeeDto")
    @Mapping(target = "haircutCategory", source = "haircutCategory")
    @Mapping(target = "appointmentStatus", source = "status", qualifiedByName = "stringToAppointmentStatus")
    @Mapping(target = "client", ignore = true)
    Appointment toAppointment(AppointmentDto appointmentDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "timeSlot", source = "timeSlotDto")
    @Mapping(target = "employee", source = "employeeDto")
    @Mapping(target = "haircutCategory", source = "haircutCategory")
    @Mapping(target = "appointmentStatus", source = "status", qualifiedByName = "stringToAppointmentStatus")
    @Mapping(target = "client", ignore = true)
    void updateAppointmentFromDto(AppointmentDto dto, @MappingTarget Appointment entity);

    @Named("appointmentStatusToString")
    default String appointmentStatusToString(AppointmentStatus status) {
        if (status == null) return "";
        return switch (status) {
            case BOOKED -> "Забронировано";
            case CONFIRMED -> "Подтверждено";
            default -> status.name();
        };
    }

    @Named("stringToAppointmentStatus")
    default AppointmentStatus stringToAppointmentStatus(String status) {
        if (status == null) return null;
        return switch (status) {
            case "Забронировано" -> AppointmentStatus.BOOKED;
            case "Подтверждено" -> AppointmentStatus.CONFIRMED;
            default -> AppointmentStatus.valueOf(status);
        };
    }
}