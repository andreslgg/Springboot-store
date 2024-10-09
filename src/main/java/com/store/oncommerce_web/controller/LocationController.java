package com.store.oncommerce_web.controller;

import com.store.oncommerce_web.model.Address;
import com.store.oncommerce_web.model.User;
import com.store.oncommerce_web.service.AddressService;
import com.store.oncommerce_web.service.LocationService;
import com.store.oncommerce_web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @GetMapping("/location-form")
    public String showLocationForm() {
        return "location_form";  // Nombre del template Thymeleaf sin la extensión `.html`
    }

    @GetMapping("/autocomplete")
    @ResponseBody
    public List<Object> autocomplete(@RequestParam String query) {
        return locationService.searchLocation(query);
    }


    @PostMapping("/addAddress")
    public String addAddress(
            @RequestParam("postalcode") String postalCode,
            @RequestParam("country") String country,
            @RequestParam("state") String state,
            @RequestParam("city") String city,
            @RequestParam("street") String street,
            @RequestParam("externalNumber") Long externalNumber,
            @RequestParam(value = "internalNumber", required = false) String internalNumber,
            @RequestParam("user_id") Long userId,
            RedirectAttributes redirectAttributes) {

        // Validar campos obligatorios
        if (postalCode.isEmpty() || country.isEmpty() || state.isEmpty() || city.isEmpty() || street.isEmpty() || externalNumber == null) {
            redirectAttributes.addFlashAttribute("error", "All fields must be filled out.");
            return "redirect:/error";  // Redirige a una página de error o muestra un mensaje
        }

        // Buscar el usuario asociado por su ID
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/error";
        }

        try {
            // Crear y guardar la nueva dirección
            Address address = new Address();
            address.setCountry(country);
            address.setZipCode(postalCode);
            address.setCity(city);
            address.setState(state);
            address.setStreet(street);
            address.setUser(user.get());  // Asigna el usuario a la dirección
            address.setCityState(state);
            address.setInternalNumber(internalNumber);
            address.setExternalNumber(externalNumber);

            // Guardar la dirección
            addressService.saveAddress(address);
            redirectAttributes.addFlashAttribute("success", "Address added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving address: " + e.getMessage());
            return "redirect:/error";
        }

        return "redirect:/checkout";
    }

}


