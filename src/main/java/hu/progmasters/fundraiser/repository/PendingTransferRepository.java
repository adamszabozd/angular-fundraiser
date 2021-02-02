package hu.progmasters.fundraiser.repository;

import hu.progmasters.fundraiser.domain.PendingTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingTransferRepository extends JpaRepository<PendingTransfer, Long> {

    boolean existsPendingTransfersByConfirmationCode(String confirmationCode);

    PendingTransfer findPendingTransferByConfirmationCode(String confirmationCode);
}
