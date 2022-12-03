package me.whiteship.demoinfleanrestapi.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
    public Optional<Account> findByEmail(String username);
}
