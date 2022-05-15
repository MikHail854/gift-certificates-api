package ru.clevertec.ecl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.entty.User;


public interface UserRepository extends JpaRepository<User, Integer> {
}
