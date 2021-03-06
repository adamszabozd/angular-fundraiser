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

package hu.progmasters.fundraiser.dto.transfer.create;

public class TransferCreationCommand {

    private Long targetFundId;

    private Double senderAmount;

    public static TransferCreationCommand getDummyInstance(Long id) {
        TransferCreationCommand instance = new TransferCreationCommand();
        instance.setSenderAmount(200.00);
        instance.setTargetFundId(id);
        return instance;
    }

    public Long getTargetFundId() {
        return targetFundId;
    }

    public void setTargetFundId(Long targetFundId) {
        this.targetFundId = targetFundId;
    }

    public Double getSenderAmount() {
        return senderAmount;
    }

    public void setSenderAmount(Double senderAmount) {
        this.senderAmount = senderAmount;
    }

}
