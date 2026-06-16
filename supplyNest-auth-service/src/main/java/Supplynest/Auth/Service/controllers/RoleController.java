package Supplynest.Auth.Service.controllers;

import Supplynest.Auth.Service.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/supplyNest/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

}
