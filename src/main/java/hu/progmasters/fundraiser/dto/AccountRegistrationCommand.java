/*
 * Copyright © Progmasters (QTC Kft.), 2018.
 * All rights reserved. No part or the whole of this Teaching Material (TM) may be reproduced, copied, distributed,
 * publicly performed, disseminated to the public, adapted or transmitted in any form or by any means, including
 * photocopying, recording, or other electronic or mechanical methods, without the prior written permission of QTC Kft.
 * This TM may only be used for the purposes of teaching exclusively by QTC Kft. and studying exclusively by QTC Kft.’s
 * students and for no other purposes by any parties other than QTC Kft.
 * This TM shall be kept confidential and shall not be made public or made available or disclosed to any unauthorized person.
 * Any dispute or claim arising out of the breach of these provisions shall be governed by and construed in accordance with the laws of Hungary.
 */

package hu.progmasters.fundraiser.dto;

import javax.validation.constraints.Size;

public class AccountRegistrationCommand {

    @Size(min = 4, max = 30, message = "Username must be between {min} and {max} characters")
    private String username;

    @Size(min = 5, max = 100, message = "Goal must be between {min} and {max} characters")
    private String goal;


    public String getUsername() {
        return username;
    }

    public String getGoal() {
        return goal;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
