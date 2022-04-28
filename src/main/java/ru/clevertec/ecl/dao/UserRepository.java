package ru.clevertec.ecl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.entty.User;


public interface UserRepository extends JpaRepository<User, Integer> {
}
