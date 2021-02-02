/*
 * Copyright © Progmasters (QTC Kft.), 2016-2019.
 * All rights reserved. No part or the whole of this Teaching Material (TM) may be reproduced, copied, distributed,
 * publicly performed, disseminated to the public, adapted or transmitted in any form or by any means, including
 * photocopying, recording, or other electronic or mechanical methods, without the prior written permission of QTC Kft.
 * This TM may only be used for the purposes of teaching exclusively by QTC Kft. and studying exclusively by QTC Kft.’s
 * students and for no other purposes by any parties other than QTC Kft.
 * This TM shall be kept confidential and shall not be made public or made available or disclosed to any unauthorized person.
 * Any dispute or claim arising out of the breach of these provisions shall be governed by and construed in accordance with the laws of Hungary.
 */

package hu.progmasters.fundraiser.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

public class AuthenticatedOrcDetails {

    private String name;

    private List<String> roles;

    public AuthenticatedOrcDetails() {
    }

    public AuthenticatedOrcDetails(UserDetails user) {
        this.name = user.getUsername();
        this.roles = mapRoles(user);
    }

    private List<String> mapRoles(UserDetails user) {
        return user.getAuthorities()
                   .stream()
                   //Probably optional in our case, however,
                   .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                   .map(Object::toString)
                   .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public List<String> getRoles() {
        return roles;
    }

}
