package com.magrega.demo.service;

import com.magrega.demo.model.Address;
import com.magrega.demo.model.User;
import com.magrega.demo.repository.AddressRepo;
import com.magrega.demo.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepo addressRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private AddressService addressService;

    private Address mockAddress;
    private User mockUser;

    @BeforeEach
    void setUp() {

        mockUser = new User();
        mockUser.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        mockUser.setAddressList(new ArrayList<>());

        mockAddress = new Address();
        mockAddress.setId(1);
        mockAddress.setCountry("Kenya");
        mockAddress.setCounty("Nairobi");
        mockAddress.setCityTown("Nairobi");
        mockAddress.setLocalityArea("Westlands");
        mockAddress.setMapsPin("-1.2921,36.8219");
        mockAddress.setUser(mockUser);
    }

    // ==========================================================
    // getAddresses()
    // ==========================================================

    @Test
    void getAddresses_ShouldReturnAllAddresses() {

        when(addressRepo.findAll()).thenReturn(List.of(mockAddress));

        List<Address> result = addressService.getAddresses();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(mockAddress);
        assertThat(result.get(0).getCountry()).isEqualTo("Kenya");

        verify(addressRepo).findAll();
    }

    @Test
    void getAddresses_ShouldReturnEmptyList_WhenRepositoryEmpty() {

        when(addressRepo.findAll()).thenReturn(List.of());

        List<Address> result = addressService.getAddresses();

        assertThat(result).isEmpty();

        verify(addressRepo).findAll();
    }

    // ==========================================================
    // getAddressById()
    // ==========================================================

    @Test
    void getAddressById_ShouldReturnAddress_WhenExists() {

        when(addressRepo.findById(1))
                .thenReturn(Optional.of(mockAddress));

        Address result = addressService.getAddressById(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getCountry()).isEqualTo("Kenya");
        assertThat(result.getCounty()).isEqualTo("Nairobi");
        assertThat(result.getCityTown()).isEqualTo("Nairobi");
        assertThat(result.getLocalityArea()).isEqualTo("Westlands");

        verify(addressRepo).findById(1);
    }

    @Test
    void getAddressById_ShouldReturnNull_WhenNotFound() {

        when(addressRepo.findById(99))
                .thenReturn(Optional.empty());

        Address result = addressService.getAddressById(99);

        assertThat(result).isNull();

        verify(addressRepo).findById(99);
    }

    // ==========================================================
    // addAddress()
    // ==========================================================

    @Test
    void addAddress_ShouldSaveAddress() {

        when(addressRepo.save(mockAddress))
                .thenReturn(mockAddress);

        Address result = addressService.addAddress(mockAddress);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(mockAddress);

        verify(addressRepo).save(mockAddress);
    }

    @Test
    void addAddress_ShouldReturnSavedAddress() {

        when(addressRepo.save(any(Address.class)))
                .thenReturn(mockAddress);

        Address result = addressService.addAddress(mockAddress);

        assertThat(result.getCountry()).isEqualTo("Kenya");
        assertThat(result.getCounty()).isEqualTo("Nairobi");
        assertThat(result.getCityTown()).isEqualTo("Nairobi");
        assertThat(result.getLocalityArea()).isEqualTo("Westlands");
        assertThat(result.getMapsPin()).isEqualTo("-1.2921,36.8219");

        verify(addressRepo).save(mockAddress);
    }

    @Test
    void addAddress_ShouldNotInteractWithUserRepo() {

        when(addressRepo.save(any(Address.class)))
                .thenReturn(mockAddress);

        addressService.addAddress(mockAddress);

        verifyNoInteractions(userRepo);
    }

    // ==========================================================
    // updateAddressById()
    // ==========================================================

    @Test
    void updateAddressById_ShouldSaveUpdatedAddress() {

        mockAddress.setCounty("Mombasa");
        mockAddress.setLocalityArea("Nyali");

        addressService.updateAddressById(mockAddress);

        verify(addressRepo).save(mockAddress);
    }


    @Test
    void updateAddressById_ShouldPersistChangedFields() {

        mockAddress.setCountry("Kenya");
        mockAddress.setCounty("Mombasa");
        mockAddress.setCityTown("Mombasa");
        mockAddress.setLocalityArea("Nyali");

        addressService.updateAddressById(mockAddress);

        verify(addressRepo).save(argThat(address ->
                address.getCountry().equals("Kenya") &&
                        address.getCounty().equals("Mombasa") &&
                        address.getCityTown().equals("Mombasa") &&
                        address.getLocalityArea().equals("Nyali")
        ));
    }


    @Test
    void updateAddressById_ShouldOnlyInteractWithAddressRepo() {

        addressService.updateAddressById(mockAddress);

        verify(addressRepo).save(mockAddress);
        verifyNoInteractions(userRepo);
    }


    // ==========================================================
    // deleteAddressById()
    // ==========================================================

    @Test
    void deleteAddressById_ShouldDelete_WhenAddressExists() {

        when(addressRepo.existsById(1))
                .thenReturn(true);

        addressService.deleteAddressById(1);

        verify(addressRepo).existsById(1);
        verify(addressRepo).deleteById(1);
    }


    @Test
    void deleteAddressById_ShouldThrowException_WhenAddressDoesNotExist() {

        when(addressRepo.existsById(99))
                .thenReturn(false);

        assertThatThrownBy(() ->
                addressService.deleteAddressById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Address with id 99 not found");

        verify(addressRepo, never())
                .deleteById(any());
    }


    @Test
    void deleteAddressById_ShouldNotDelete_WhenAddressMissing() {

        when(addressRepo.existsById(100))
                .thenReturn(false);

        assertThatThrownBy(() ->
                addressService.deleteAddressById(100))
                .isInstanceOf(RuntimeException.class);

        verify(addressRepo, never())
                .deleteById(any());
    }


    @Test
    void deleteAddressById_ShouldNotInteractWithUserRepo() {

        when(addressRepo.existsById(1))
                .thenReturn(true);

        addressService.deleteAddressById(1);

        verifyNoInteractions(userRepo);
    }

    // ==========================================================
    // assignAddressToUser()
    // ==========================================================

    @Test
    void assignAddressToUser_ShouldAssignAddressToUser_WhenBothExist() {

        UUID userId = mockUser.getId();

        when(userRepo.findById(userId))
                .thenReturn(Optional.of(mockUser));

        when(addressRepo.findById(1))
                .thenReturn(Optional.of(mockAddress));

        when(addressRepo.save(any(Address.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        Address result = addressService.assignAddressToUser(userId, 1L);


        assertThat(result).isNotNull();
        assertThat(result.getUser()).isEqualTo(mockUser);
        assertThat(result.getCountry()).isEqualTo("Kenya");
        assertThat(mockUser.getAddressList())
                .contains(mockAddress);


        verify(userRepo).findById(userId);
        verify(addressRepo).findById(1);
        verify(addressRepo).save(mockAddress);
    }


    @Test
    void assignAddressToUser_ShouldThrowException_WhenUserNotFound() {

        UUID userId = mockUser.getId();

        when(userRepo.findById(userId))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() ->
                addressService.assignAddressToUser(userId, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found with id: " + userId);


        verify(addressRepo, never())
                .findById(anyInt());

        verify(addressRepo, never())
                .save(any(Address.class));
    }


    @Test
    void assignAddressToUser_ShouldThrowException_WhenAddressNotFound() {

        UUID userId = mockUser.getId();

        when(userRepo.findById(userId))
                .thenReturn(Optional.of(mockUser));

        when(addressRepo.findById(1))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() ->
                addressService.assignAddressToUser(userId, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Address not found with id: 1");


        verify(addressRepo).findById(1);

        verify(addressRepo, never())
                .save(any(Address.class));
    }


    @Test
    void assignAddressToUser_ShouldAddAddressToUsersAddressList() {

        UUID userId = mockUser.getId();

        when(userRepo.findById(userId))
                .thenReturn(Optional.of(mockUser));

        when(addressRepo.findById(1))
                .thenReturn(Optional.of(mockAddress));

        when(addressRepo.save(any(Address.class)))
                .thenReturn(mockAddress);


        Address result =
                addressService.assignAddressToUser(userId, 1L);


        assertThat(result.getUser())
                .isEqualTo(mockUser);

        assertThat(mockUser.getAddressList())
                .contains(mockAddress);
    }


    // ==========================================================
    // getAddressesByUserId()
    // ==========================================================

    @Test
    void getAddressesByUserId_ShouldReturnAddressesForUser() {

        UUID userId = mockUser.getId();

        when(addressRepo.findByUserId(userId))
                .thenReturn(List.of(mockAddress));


        List<Address> result =
                addressService.getAddressesByUserId(userId);


        assertThat(result)
                .hasSize(1);

        assertThat(result.get(0))
                .isEqualTo(mockAddress);


        verify(addressRepo)
                .findByUserId(userId);
    }


    @Test
    void getAddressesByUserId_ShouldReturnEmptyList_WhenNoAddressesExist() {

        UUID userId = mockUser.getId();

        when(addressRepo.findByUserId(userId))
                .thenReturn(List.of());


        List<Address> result =
                addressService.getAddressesByUserId(userId);


        assertThat(result)
                .isEmpty();


        verify(addressRepo)
                .findByUserId(userId);
    }

}