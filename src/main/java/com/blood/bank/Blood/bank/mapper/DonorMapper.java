package com.blood.bank.Blood.bank.mapper;

import com.blood.bank.Blood.bank.dto.DonorRegistrationDto;
import com.blood.bank.Blood.bank.model.Address;
import com.blood.bank.Blood.bank.model.Donor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface DonorMapper {

    

    @Mapping(target = "addresses", source = "donorRegistrationDto", qualifiedByName = "mapAddress")
    Donor toDonor(DonorRegistrationDto donorRegistrationDto);

    @Mapping(target = "country", source = "addresses", qualifiedByName = "mapCountry")
    @Mapping(target = "state", source = "addresses", qualifiedByName = "mapState")
    @Mapping(target = "district", source = "addresses", qualifiedByName = "mapDistrict")
    @Mapping(target = "city", source = "addresses", qualifiedByName = "mapCity")
    @Mapping(target = "streetAddress", source = "addresses", qualifiedByName = "mapStreetAddress")
    @Mapping(target = "zipCode", source = "addresses", qualifiedByName = "mapZipCode")
    @Mapping(target = "landmark", source = "addresses", qualifiedByName = "mapLandmark")
    DonorRegistrationDto toDto(Donor donor);

    @Mapping(target = "id", ignore = true) // Ignore ID for update
    void updateDonorFromDto(DonorRegistrationDto donorRegistrationDto, @MappingTarget Donor donor);

    @Named("mapAddress")
    default Set<Address> mapAddress(DonorRegistrationDto donorRegistrationDto) {
        if (donorRegistrationDto == null) {
            return null;
        }
        Address address = Address.builder()
                .country(donorRegistrationDto.getCountry())
                .state(donorRegistrationDto.getState())
                .district(donorRegistrationDto.getDistrict())
                .city(donorRegistrationDto.getCity())
                .streetAddress(donorRegistrationDto.getStreetAddress())
                .zipCode(donorRegistrationDto.getZipCode())
                .landmark(donorRegistrationDto.getLandmark())
                .build();
        Set<Address> addresses = new HashSet<>();
        addresses.add(address);
        return addresses;
    }

    @Named("mapAddressForUpdate")
    default Set<Address> mapAddressForUpdate(DonorRegistrationDto donorRegistrationDto, @MappingTarget Donor donor) {
        if (donorRegistrationDto == null) {
            return donor.getAddresses(); // Keep existing addresses if DTO is null
        }

        Address addressToUpdate;
        if (donor.getAddresses().isEmpty()) {
            addressToUpdate = new Address();
            addressToUpdate.setDonor(donor);
            donor.getAddresses().add(addressToUpdate);
        } else {
            addressToUpdate = donor.getAddresses().iterator().next();
        }

        addressToUpdate.setCountry(donorRegistrationDto.getCountry());
        addressToUpdate.setState(donorRegistrationDto.getState());
        addressToUpdate.setDistrict(donorRegistrationDto.getDistrict());
        addressToUpdate.setCity(donorRegistrationDto.getCity());
        addressToUpdate.setStreetAddress(donorRegistrationDto.getStreetAddress());
        addressToUpdate.setZipCode(donorRegistrationDto.getZipCode());
        addressToUpdate.setLandmark(donorRegistrationDto.getLandmark());

        return donor.getAddresses();
    }

    @Named("mapCountry")
    default String mapCountry(Set<Address> addresses) {
        return addresses != null && !addresses.isEmpty() ? addresses.iterator().next().getCountry() : null;
    }

    @Named("mapState")
    default String mapState(Set<Address> addresses) {
        return addresses != null && !addresses.isEmpty() ? addresses.iterator().next().getState() : null;
    }

    @Named("mapDistrict")
    default String mapDistrict(Set<Address> addresses) {
        return addresses != null && !addresses.isEmpty() ? addresses.iterator().next().getDistrict() : null;
    }

    @Named("mapCity")
    default String mapCity(Set<Address> addresses) {
        return addresses != null && !addresses.isEmpty() ? addresses.iterator().next().getCity() : null;
    }

    @Named("mapStreetAddress")
    default String mapStreetAddress(Set<Address> addresses) {
        return addresses != null && !addresses.isEmpty() ? addresses.iterator().next().getStreetAddress() : null;
    }

    @Named("mapZipCode")
    default String mapZipCode(Set<Address> addresses) {
        return addresses != null && !addresses.isEmpty() ? addresses.iterator().next().getZipCode() : null;
    }

    @Named("mapLandmark")
    default String mapLandmark(Set<Address> addresses) {
        return addresses != null && !addresses.isEmpty() ? addresses.iterator().next().getLandmark() : null;
    }
}
