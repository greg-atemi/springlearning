package com.magrega.demo.service;

import com.magrega.demo.dto.address.AddressDTO;
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
    private AddressDTO mockAddressDTO;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1);
        mockUser.setAddressList(new ArrayList<>());  // must be mutable list

        mockAddress = new Address();
        mockAddress.setId(1);
        mockAddress.setCountry("Kenya");
        mockAddress.setCounty("Nairobi");
        mockAddress.setLocality("Westlands");
        mockAddress.setMapsPin("-1.2921,36.8219");
        mockAddress.setUser(mockUser);

        mockAddressDTO = new AddressDTO();
        mockAddressDTO.setCountry("Kenya");
        mockAddressDTO.setCounty("Nairobi");
        mockAddressDTO.setLocality("Westlands");
        mockAddressDTO.setMapsPin("-1.2921,36.8219");
    }

    // ─────────────────────────────────────────────
    // getAddresses()
    // ─────────────────────────────────────────────

    @Test
    void getAddresses_ShouldReturnAllAddresses() {
        when(addressRepo.findAll()).thenReturn(List.of(mockAddress));

        List<Address> result = addressService.getAddresses();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCountry()).isEqualTo("Kenya");
        verify(addressRepo, times(1)).findAll();
    }

    @Test
    void getAddresses_ShouldReturnEmptyList_WhenNoAddresses() {
        when(addressRepo.findAll()).thenReturn(List.of());

        List<Address> result = addressService.getAddresses();

        assertThat(result).isEmpty();
        verify(addressRepo, times(1)).findAll();
    }

    // ─────────────────────────────────────────────
    // getAddressById()
    // ─────────────────────────────────────────────

    @Test
    void getAddressById_ShouldReturnAddress_WhenExists() {
        when(addressRepo.findById(1)).thenReturn(Optional.of(mockAddress));

        Address result = addressService.getAddressById(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getLocality()).isEqualTo("Westlands");
        verify(addressRepo, times(1)).findById(1);
    }

    @Test
    void getAddressById_ShouldReturnNull_WhenNotFound() {
        when(addressRepo.findById(99)).thenReturn(Optional.empty());

        Address result = addressService.getAddressById(99);

        assertThat(result).isNull();
        verify(addressRepo, times(1)).findById(99);
    }

    // ─────────────────────────────────────────────
    // addAddress()
    // ─────────────────────────────────────────────

    @Test
    void addAddress_ShouldCallSave_WithGivenAddress() {
        addressService.addAddress(mockAddress);

        verify(addressRepo, times(1)).save(mockAddress);
    }

    @Test
    void addAddress_ShouldNotInteractWithUserRepo() {
        addressService.addAddress(mockAddress);

        verifyNoInteractions(userRepo);
    }

    // ─────────────────────────────────────────────
    // updateAddressById()
    // ─────────────────────────────────────────────

    @Test
    void updateAddressById_ShouldCallSave_WithGivenAddress() {
        mockAddress.setLocality("Kilimani");

        addressService.updateAddressById(mockAddress);

        verify(addressRepo, times(1)).save(mockAddress);
    }

    @Test
    void updateAddressById_ShouldPersistChangedFields() {
        mockAddress.setCounty("Mombasa");
        mockAddress.setLocality("Nyali");

        addressService.updateAddressById(mockAddress);

        verify(addressRepo).save(argThat(a ->
                a.getCounty().equals("Mombasa") &&
                        a.getLocality().equals("Nyali")
        ));
    }

    // ─────────────────────────────────────────────
    // deleteAddressById()
    // ─────────────────────────────────────────────

    @Test
    void deleteAddressById_ShouldDelete_WhenAddressExists() {
        when(addressRepo.existsById(1)).thenReturn(true);

        addressService.deleteAddressById(1);

        verify(addressRepo, times(1)).deleteById(1);
    }

    @Test
    void deleteAddressById_ShouldThrowRuntimeException_WhenAddressNotFound() {
        when(addressRepo.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> addressService.deleteAddressById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Address with id 99 not found");

        verify(addressRepo, never()).deleteById(any());
    }

    @Test
    void deleteAddressById_ShouldNotInteractWithUserRepo() {
        when(addressRepo.existsById(1)).thenReturn(true);

        addressService.deleteAddressById(1);

        verifyNoInteractions(userRepo);
    }

    // ─────────────────────────────────────────────
    // addAddressToUser()
    // ─────────────────────────────────────────────

    @Test
    void addAddressToUser_ShouldSaveAndReturnAddress_WhenUserExists() {
        when(userRepo.findById(1)).thenReturn(Optional.of(mockUser));
        when(addressRepo.save(any(Address.class))).thenReturn(mockAddress);

        Address result = addressService.addAddressToUser(1, mockAddressDTO);

        assertThat(result).isNotNull();
        assertThat(result.getCountry()).isEqualTo("Kenya");
        verify(addressRepo, times(1)).save(any(Address.class));
    }

    @Test
    void addAddressToUser_ShouldMapAllDTOFields_Correctly() {
        when(userRepo.findById(1)).thenReturn(Optional.of(mockUser));
        when(addressRepo.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));

        Address result = addressService.addAddressToUser(1, mockAddressDTO);

        assertThat(result.getCountry()).isEqualTo("Kenya");
        assertThat(result.getCounty()).isEqualTo("Nairobi");
        assertThat(result.getLocality()).isEqualTo("Westlands");
        assertThat(result.getMapsPin()).isEqualTo("-1.2921,36.8219");
    }

    @Test
    void addAddressToUser_ShouldLinkAddressToUser() {
        when(userRepo.findById(1)).thenReturn(Optional.of(mockUser));
        when(addressRepo.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));

        Address result = addressService.addAddressToUser(1, mockAddressDTO);

        // Address should be linked to the user
        assertThat(result.getUser()).isEqualTo(mockUser);

        // User's address list should contain the new address
        assertThat(mockUser.getAddressList()).hasSize(1);
    }

    @Test
    void addAddressToUser_ShouldThrowRuntimeException_WhenUserNotFound() {
        when(userRepo.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addressService.addAddressToUser(99, mockAddressDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");

        verify(addressRepo, never()).save(any());
    }

    @Test
    void addAddressToUser_ShouldNotCallDeleteOrFind_OnAddressRepo() {
        when(userRepo.findById(1)).thenReturn(Optional.of(mockUser));
        when(addressRepo.save(any(Address.class))).thenReturn(mockAddress);

        addressService.addAddressToUser(1, mockAddressDTO);

        verify(addressRepo, never()).findById(any());
        verify(addressRepo, never()).deleteById(any());
    }
}