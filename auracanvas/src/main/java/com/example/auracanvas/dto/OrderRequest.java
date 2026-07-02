package com.example.auracanvas.dto;

import jakarta.validation.constraints.NotBlank;

public class OrderRequest {
    @NotBlank
    private String shippingAddress;
    @NotBlank
    private String phoneNumber;

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
