package com.mvpmatch.vendingmachine.util;

import com.mvpmatch.vendingmachine.config.model.MyUserPrincipal;
import com.mvpmatch.vendingmachine.entity.Role;

/**
 * Created by vedadsurkovic on 4/1/22
 **/
public final class Util {

    private Util() {}

    public static boolean checkIfRoleIsValid(final MyUserPrincipal myUserPrincipal, final Role role) {
        return (myUserPrincipal.getUser() != null) &&
            (myUserPrincipal.getUser().getRole() != role);
    }

}
